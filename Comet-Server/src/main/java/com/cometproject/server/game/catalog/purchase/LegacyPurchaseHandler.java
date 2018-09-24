package com.cometproject.server.game.catalog.purchase;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.game.bots.BotMode;
import com.cometproject.api.game.bots.BotType;
import com.cometproject.api.game.bots.IBotData;
import com.cometproject.api.game.catalog.types.CatalogPageType;
import com.cometproject.api.game.catalog.types.ICatalogBundledItem;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.catalog.types.bundles.IRoomBundle;
import com.cometproject.api.game.catalog.types.purchase.CatalogPurchase;
import com.cometproject.api.game.catalog.types.purchase.ICatalogPurchaseHandler;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.IGiftData;
import com.cometproject.api.game.furniture.types.IMusicData;
import com.cometproject.api.game.furniture.types.ItemType;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.composers.catalog.BoughtItemMessageComposer;
import com.cometproject.server.composers.catalog.GiftUserNotFoundMessageComposer;
import com.cometproject.server.composers.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.StaticPetProperties;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.api.game.catalog.types.bundles.RoomBundleItem;
import com.cometproject.server.game.rooms.objects.entities.types.data.PlayerBotData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.*;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.EnforceRoomCategoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.EffectsInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LegacyPurchaseHandler implements ICatalogPurchaseHandler {
    private final Logger log = Logger.getLogger(LegacyPurchaseHandler.class.getName());
    private ExecutorService executorService;

    public LegacyPurchaseHandler() {
    }

    @Override
    public void purchaseItem(ISession client, int pageId, int itemId, String data, int amount, IGiftData giftData) {
        if (CometSettings.asyncCatalogPurchase) {
            if (this.executorService == null) {
                this.executorService = Executors.newFixedThreadPool(2);
            }

            this.executorService.submit(() -> this.handle(client, pageId, itemId, data, amount, giftData));
        } else {
            this.handle(client, pageId, itemId, data, amount, giftData);
        }
    }

    /**
     * Handle the catalog purchase
     *
     * @param client   The session assigned to the player who's purchasing the item
     * @param pageId   The catalog page the purchased item is on
     * @param itemId   The ID of the item that was purchased
     * @param data     The data sent by the client
     * @param amount   The amount of items we're purchasing
     * @param giftData Gift data (if-any)
     */
    @Override
    public void handle(ISession client, int pageId, int itemId, String data, int amount, IGiftData giftData) {
        if (client == null || client.getPlayer() == null) return;

        // TODO: redo all of this, it sucks so bad ;P, maybe add purchase handlers for each item or some crap
        if (amount > 100) {
            client.send(new AlertMessageComposer(Locale.get("catalog.error.toomany")));
            return;
        }

        final int playerIdToDeliver = giftData == null ? -1 : PlayerDao.getIdByUsername(giftData.getReceiver());

        if (giftData != null) {
            if (playerIdToDeliver == 0) {
                client.send(new GiftUserNotFoundMessageComposer());
                return;
            } else {
                client.getPlayer().getAchievements().progressAchievement(AchievementType.GIFT_GIVER, 1);
            }
        }

        Set<PlayerItem> unseenItems = Sets.newHashSet();
        ICatalogPage page = CatalogManager.getInstance().getPage(pageId);

        try {
            ICatalogItem item;

            try {
                if (page == null || page.getType() == CatalogPageType.RECENT_PURCHASES) {
                    page = CatalogManager.getInstance().getCatalogPageByCatalogItemId(itemId);

                    if (page.getMinRank() > client.getPlayer().getData().getRank() || !page.getItems().containsKey(itemId)) {
                        //y u do dis.
                        client.disconnect();
                        return;
                    }
                }

                item = page.getItems().get(itemId);
            } catch (Exception e) {
                return;
            }

            if (item == null || page == null || item.getPageId() != page.getId()) {
                return;
            }

            if (giftData != null) {
                try {
                    final FurnitureDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                    if (itemDefinition == null) {
                        return;
                    }

                    if (itemDefinition != null && !itemDefinition.canGift()) {
                        return;
                    }
                } catch (Exception e) {
                    return;
                }

                if (client.getPlayer().getLastGift() != 0 && !client.getPlayer().getPermissions().getRank().floodBypass()) {
                    if (((int) Comet.getTime() - client.getPlayer().getLastGift()) < CometSettings.playerGiftCooldown) {
                        client.send(new AdvancedAlertMessageComposer(Locale.get("catalog.error.gifttoofast")));
                        client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                        return;
                    }
                }

                client.getPlayer().setLastGift((int) Comet.getTime());
            }

            if (item.isBadgeOnly()) {

                if (item.hasBadge() && client.getPlayer().getInventory().hasBadge(item.getBadgeId())) {
                    client.send(new PurchaseErrorMessageComposer(1));
                    client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                    return;
                }
            }

            if (amount > 1 && !item.allowOffer()) {
                client.send(new AlertMessageComposer(Locale.get("catalog.error.nooffer")));

                return;
            }

            int totalCostCredits;
            int totalCostPoints;
            int totalCostActivityPoints;
            int totalCostSeasonal;

            if (item.getLimitedSells() >= item.getLimitedTotal() && item.getLimitedTotal() != 0) {
//                client.send(new LimitedEditionSoldOutMessageComposer());
                // TODO: Fix this.
                return;
            }

            try {
                if (CatalogManager.getInstance().getPage(item.getPageId()).getMinRank() > client.getPlayer().getData().getRank()) {
                    client.disconnect();
                    return;
                }
            } catch (Exception ignored) {
                // Invalid page id..
                return;
            }

            if (item.allowOffer()) {
                totalCostCredits = applyDiscount(item.getCostCredits(), amount);
                totalCostPoints = applyDiscount(item.getCostDiamonds(), amount);
                totalCostActivityPoints = applyDiscount(item.getCostActivityPoints(), amount);
                totalCostSeasonal = applyDiscount(item.getCostSeasonal(), amount);
            } else {
                totalCostCredits = item.getCostCredits();
                totalCostPoints = item.getCostDiamonds();
                totalCostActivityPoints = item.getCostActivityPoints();
                totalCostSeasonal = item.getCostSeasonal();
            }

            if ((!CometSettings.playerInfiniteBalance && (client.getPlayer().getData().getCredits() < totalCostCredits || client.getPlayer().getData().getActivityPoints() < totalCostActivityPoints)) || client.getPlayer().getData().getVipPoints() < totalCostPoints) {
                client.getLogger().warn("Player with ID: " + client.getPlayer().getId() + " tried to purchase item with ID: " + item.getId() + " with the incorrect amount of credits or points.");
                client.send(new AlertMessageComposer(Locale.get("catalog.error.notenough")));
                return;
            }

            if (item.getLimitedTotal() > 0) {
                item.increaseLimitedSells(amount);
                CatalogDao.updateLimitSellsForItem(item.getId(), amount);
            }

            if (!CometSettings.playerInfiniteBalance) {
                client.getPlayer().getData().decreaseCredits(totalCostCredits);
                client.getPlayer().getData().decreaseActivityPoints(totalCostActivityPoints);
            }

            client.getPlayer().getData().decreaseVipPoints(totalCostPoints);
            client.getPlayer().getData().decreaseSeasonalPoints(totalCostSeasonal);

            client.getPlayer().sendBalance();
            client.getPlayer().getData().save();

            if (page != null) {
                if (page.getType() == CatalogPageType.BUNDLE) {
                    purchaseBundle(page, client);
                    return;
                }
            }

            if (item.isBadgeOnly()) {
                if (item.getPresetData().equals("name_colour")) {
                    client.getPlayer().getData().setNameColour(item.getBadgeId());
                } else {
                    if (item.hasBadge() && !client.getPlayer().getInventory().hasBadge(item.getBadgeId())) {
                        client.getPlayer().getInventory().addBadge(item.getBadgeId(), true);
                    }
                }

                client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                return;
            }

            for (ICatalogBundledItem bundledItem : item.getItems()) {
                FurnitureDefinition def = ItemManager.getInstance().getDefinition(bundledItem.getItemId());

                if (def == null) {
                    continue;
                }

                client.send(new BoughtItemMessageComposer(item, def));

                if (def.getItemName().equals("DEAL_HC_1")) {
                    // TODO: HC buying
                    throw new Exception("HC purchasing is not implemented");
                }

                String extraData = "";

                boolean isTeleport = false;

                if (def.getItemType() == ItemType.EFFECT) {
                    int effectId = def.getSpriteId();

                    // deliver effect

                    if (!client.getPlayer().getInventory().hasEffect(effectId)) {
                        client.getPlayer().getInventory().getEffects().add(effectId);
                        PlayerDao.saveEffect(client.getPlayer().getId(), effectId);

                        client.send(new EffectsInventoryMessageComposer(client.getPlayer().getInventory().getEffects(), client.getPlayer().getInventory().getEquippedEffect()));
                    }

                    return;
                } else if (def.getInteraction().equals("trophy")) {
                    extraData +=
                            client.getPlayer().getData().getUsername() + Character.toChars(9)[0] + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getYear() + Character.toChars(9)[0] + data;
                } else if (def.isTeleporter()) {
                    amount = amount * 2;
                    isTeleport = true;
                } else if (item.getDisplayName().startsWith("a0 pet")) {
                    String petRace = item.getDisplayName().replace("a0 pet", "");
                    String[] petData = data.split("\n"); // [0:name, 1:race, 2:colour]

                    if (petData.length != 3) {
                        throw new Exception("Invalid pet data length: " + petData.length);
                    }

                    int petId = PetDao.createPet(client.getPlayer().getId(), petData[0], Integer.parseInt(petRace), Integer.parseInt(petData[1]), petData[2]);

                    client.getPlayer().getAchievements().progressAchievement(AchievementType.PET_LOVER, 1);
                    client.getPlayer().getPets().addPet(new PetData(petId, petData[0], 0, StaticPetProperties.DEFAULT_LEVEL, StaticPetProperties.DEFAULT_HAPPINESS, StaticPetProperties.DEFAULT_EXPERIENCE, StaticPetProperties.DEFAULT_ENERGY, StaticPetProperties.DEFAULT_HUNGER, client.getPlayer().getId(), client.getPlayer().getData().getUsername(), petData[2], Integer.parseInt(petData[1]), Integer.parseInt(petRace)));
                    client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));

                    client.send(new UnseenItemsMessageComposer(new HashMap<Integer, List<Integer>>() {{
                        put(3, Lists.newArrayList(petId));
                    }}));
                    return;
                } else if (def.getInteraction().equals("postit")) {
                    amount = 20; // we want 20 stickies

                    extraData = "";
                } else if (def.isRoomDecor()) {
                    if (data.isEmpty()) {
                        extraData += "0";
                    } else {
                        extraData += data.replace(",", ".");
                    }
                } else if (def.getInteraction().equals("group_item") || def.getInteraction().equals("group_gate")) {
                    if (data.isEmpty() || !StringUtils.isNumeric(data)) return;

                    if (!client.getPlayer().getGroups().contains(new Integer(data))) {
                        return;
                    }

                    extraData = data;
                } else if (def.getType().equals("r")) {
                    // It's a bot!
                    String botName = "New Bot";
                    String botFigure = item.getPresetData();
                    String botGender = "m";
                    String botMotto = "Beeb beeb boop beep!";
                    BotType type = BotType.GENERIC;
                    BotMode mode = BotMode.DEFAULT;

                    switch (item.getDisplayName()) {
                        case "bot_bartender":
                            type = BotType.WAITER;
                            break;
                        case "bot_spy":
                            type = BotType.SPY;
                            break;
                    }

                    final int botId = PlayerBotDao.createBot(client.getPlayer().getId(), botName, botFigure, botGender, botMotto, type);
                    //int id, String username, String motto, String figure, String gender, String ownerName, int ownerId, String messages,
                    // boolean automaticChat, int chatDelay, BotType botType, BotMode mode, String data

                    final IBotData botData = new PlayerBotData(
                            botId,
                            botName,
                            botMotto,
                            botFigure,
                            botGender,
                            client.getPlayer().getData().getUsername(),
                            client.getPlayer().getId(),
                            "",
                            true,
                            7,
                            type,
                            mode,
                            ""
                    );

                    client.getPlayer().getBots().addBot(botData);
                    client.send(new BotInventoryMessageComposer(client.getPlayer().getBots().getBots()));

                    client.send(new UnseenItemsMessageComposer(new HashMap<Integer, List<Integer>>() {{
                        put(5, Lists.newArrayList(botId));
                    }}));

                    return;
                } else if (def.getInteraction().equals("badge_display")) {
                    if (client.getPlayer().getInventory().getBadges().get(data) == null) {
                        return;
                    }

                    extraData = data + "~" + client.getPlayer().getData().getUsername() + "~" + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getYear();
                } else if (def.getInteraction().equals("group_forum")) {
                    if (data.isEmpty() || !StringUtils.isNumeric(data))
                        return;

                    if (!client.getPlayer().getGroups().contains(new Integer(data))) {
                        return;
                    }

                    int groupId = Integer.parseInt(data);
                    IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

                    if (!group.getData().hasForum() && group.getData().getOwnerId() == client.getPlayer().getId()) {
                        GameContext.getCurrent().getGroupService().addForum(group);

                        Map<String, String> notificationParams = Maps.newHashMap();

                        notificationParams.put("groupId", groupId + "");
                        notificationParams.put("groupName", group.getData().getTitle());

                        client.send(new NotificationMessageComposer("forums.delivered", notificationParams));

                    }

                    extraData = "" + groupId;
                } else if (def.isSong()) {
                    final String songName = item.getPresetData();

                    if (songName != null && !songName.isEmpty()) {
                        IMusicData musicData = ItemManager.getInstance().getMusicDataByName(songName);

                        if (musicData != null) {
                            extraData = String.format("%s\n%s\n%s\n%s\n%s\n%s",
                                    client.getPlayer().getData().getUsername(),
                                    Calendar.getInstance().get(Calendar.YEAR),
                                    Calendar.getInstance().get(Calendar.MONTH),
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                    musicData.getLengthSeconds(),
                                    musicData.getTitle());
                        }
                    }
                }

                long[] teleportIds = null;

                if (isTeleport) {
                    teleportIds = new long[amount];
                }

                List<CatalogPurchase> purchases = new ArrayList<>();

                if (giftData != null) {
                    giftData.setExtraData(extraData);

                    FurnitureDefinition itemDefinition = ItemManager.getInstance().getBySpriteId(giftData.getSpriteId());

                    purchases.add(new CatalogPurchase(playerIdToDeliver, itemDefinition == null ? CatalogManager.getInstance().getGiftBoxesOld().get(0) : itemDefinition.getId(), "GIFT::##" + JsonUtil.getInstance().toJson(giftData)));
                } else {
                    for (int purchaseCount = 0; purchaseCount < amount; purchaseCount++) {
                        for (int itemCount = 0; itemCount != bundledItem.getAmount(); itemCount++) {
                            purchases.add(new CatalogPurchase(client.getPlayer().getId(), bundledItem.getItemId(), extraData));
                        }
                    }
                }

                final List<Long> newItems;
                final Data<List<Long>> idsData = Data.createEmpty();
                StorageContext.getCurrentContext().getRoomItemRepository().purchaseItems(purchases, idsData::set, client.getPlayer().getInventory().viewingInventoryUserId());

                if (!idsData.has()) {
                    throw new Exception("Failed to insert items");
                }

                newItems = idsData.get();

                for (long newItem : newItems) {
                    if (item.getLimitedTotal() > 0) {
                        LimitedEditionDao.save(new LimitedEditionItemData(newItem, item.getLimitedSells(), item.getLimitedTotal()));
                    }

                    if (giftData == null)
                        unseenItems.add(client.getPlayer().getInventory().add(newItem, bundledItem.getItemId(), extraData, giftData, item.getLimitedTotal() > 0 ? new LimitedEditionItemData(bundledItem.getItemId(), item.getLimitedSells(), item.getLimitedTotal()) : null));

                    if (isTeleport)
                        teleportIds[newItems.indexOf(newItem)] = newItem;
                }

                if (isTeleport) {
                    long lastId = 0;

                    for (int i = 0; i < teleportIds.length; i++) {
                        if (lastId == 0) {
                            lastId = teleportIds[i];
                        }

                        if (i % 2 == 0 && lastId != 0) {
                            lastId = teleportIds[i];
                            continue;
                        }

                        TeleporterDao.savePair(teleportIds[i], lastId);
                    }
                }

                if (giftData != null) {
                    this.deliverGift(playerIdToDeliver, giftData, newItems, client.getPlayer().getData().getUsername());
                } else {
                    if (item.hasBadge()) {
                        client.getPlayer().getInventory().addBadge(item.getBadgeId(), true);
                    }

                    client.send(new UnseenItemsMessageComposer(unseenItems, ItemManager.getInstance()));
                    client.send(new UpdateInventoryMessageComposer());

                    if (CometSettings.logCatalogPurchases) {
                        CatalogDao.saveRecentPurchase(client.getPlayer().getId(), item.getId(), amount, extraData);
                    }

                    client.getPlayer().getRecentPurchases().add(item.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error while buying catalog item", e);
        } finally {
            // Clean up the purchase - even if there was an exception!!
            unseenItems.clear();
        }
    }

    /**
     * Deliver the gift
     *
     * @param playerId The ID of the player to deliver the item to
     * @param giftData The data of the gift
     * @param newItems List of items to deliver
     */
    @Override
    public void deliverGift(int playerId, IGiftData giftData, List<Long> newItems, String senderUsername) {
        Session client = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if (client != null) {
            Set<PlayerItem> unseenItems = Sets.newHashSet();

            if (client.getPlayer() != null) {
                if (client.getPlayer().getInventory() != null) {
                    for (long newItem : newItems) {
                        unseenItems.add(client.getPlayer().getInventory().add(newItem, ItemManager.getInstance().getBySpriteId(giftData.getSpriteId()).getId(), "GIFT::##" + JsonUtil.getInstance().toJson(giftData), giftData, null));
                    }
                }

                if (client.getPlayer().getAchievements() != null) {
                    client.getPlayer().getAchievements().progressAchievement(AchievementType.GIFT_RECEIVER, 1);
                }
            }

            client.send(new UnseenItemsMessageComposer(unseenItems, ItemManager.getInstance()));
            client.send(new UpdateInventoryMessageComposer());
            client.send(new NotificationMessageComposer("gift_received", Locale.getOrDefault("notification.gift_received", "You have just received a gift from %username%!").replace("%username%", senderUsername)));

        }
    }

    @Override
    public void purchaseBundle(IRoomBundle roomBundle, ISession client) {
        try {
            int roomId = RoomManager.getInstance().createRoom(roomBundle.getConfig().getRoomName().replace("%username%", client.getPlayer().getData().getUsername()), "", roomBundle.getRoomModelData(), 0, 20, 0, client, roomBundle.getConfig().getThicknessWall(), roomBundle.getConfig().getThicknessFloor(), roomBundle.getConfig().getDecorations(), roomBundle.getConfig().isHideWalls());
            final Set<IRoomItemData> roomItemData = Sets.newHashSet();

            for (RoomBundleItem roomBundleItem : roomBundle.getRoomBundleData()) {
                final Position position = roomBundleItem.getWallPosition() == null ? new Position(roomBundleItem.getX(), roomBundleItem.getY(), roomBundleItem.getZ()) : null;

                roomItemData.add(new RoomItemData(-1, roomBundleItem.getItemId(), client.getPlayer().getId(), "", position, roomBundleItem.getRotation(), roomBundleItem.getExtraData(), roomBundleItem.getWallPosition(), null));
            }

            StorageContext.getCurrentContext().getRoomItemRepository().placeBundle(roomId, roomItemData);

            client.send(new RoomForwardMessageComposer(roomId));
            client.send(new EnforceRoomCategoryMessageComposer());
            client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));

            client.getPlayer().setLastRoomCreated((int) Comet.getTime());
        } catch (Exception e) {
            client.send(new MotdNotificationMessageComposer("Invalid room bundle data, please contact an administrator."));
            client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
        }
    }

    @Override
    public void purchaseBundle(ICatalogPage page, ISession client) {
        RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(page.getExtraData());
        purchaseBundle(roomBundle, client);
    }

    @Override
    public int applyDiscount(int cost, int quantity) {
        int uCost = cost;
        cost = cost * quantity;

        if (quantity <= 5) return cost;
        else if (quantity <= 10) return cost - uCost;
        else if (quantity <= 11) return cost - uCost * 2;
        else if (quantity <= 16) return cost - uCost * 3;
        else if (quantity <= 17) return cost - uCost * 4;
        else if (quantity <= 22) return cost - uCost * 5;
        else if (quantity <= 23) return cost - uCost * 6;
        else if (quantity <= 28) return cost - uCost * 7;
        else if (quantity <= 29) return cost - uCost * 8;
        else if (quantity <= 34) return cost - uCost * 9;
        else if (quantity <= 35) return cost - uCost * 10;
        else if (quantity <= 39) return cost - uCost * 11;
        else if (quantity <= 40) return cost - uCost * 12;
        else if (quantity <= 41) return cost - uCost * 13;
        else if (quantity <= 46) return cost - uCost * 14;
        else if (quantity <= 47) return cost - uCost * 15;
        else if (quantity <= 52) return cost - uCost * 16;
        else if (quantity <= 53) return cost - uCost * 17;
        else if (quantity <= 54) return cost - uCost * 18;
        else if (quantity <= 59) return cost - uCost * 19;
        else if (quantity <= 64) return cost - uCost * 20;
        else if (quantity <= 65) return cost - uCost * 21;
        else if (quantity <= 70) return cost - uCost * 22;
        else if (quantity <= 71) return cost - uCost * 23;
        else if (quantity <= 76) return cost - uCost * 24;
        else if (quantity <= 77) return cost - uCost * 25;
        else if (quantity <= 78) return cost - uCost * 26;
        else if (quantity <= 82) return cost - uCost * 27;
        else if (quantity <= 88) return cost - uCost * 28;
        else if (quantity <= 89) return cost - uCost * 29;
        else if (quantity <= 94) return cost - uCost * 30;
        else if (quantity <= 95) return cost - uCost * 31;
        else if (quantity <= 98) return cost - uCost * 32;
        else if (quantity <= 100) return cost - uCost * 33;

        return cost;
    }
}

package com.cometproject.server.game.catalog.purchase;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.StaticPetProperties;
import com.cometproject.server.game.players.components.types.inventory.InventoryBot;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.GiftUserNotFoundMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.LimitedEditionSoldOutMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.RoomNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.utilities.JsonFactory;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public class CatalogPurchaseHandler {
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
    public void handle(Session client, int pageId, int itemId, String data, int amount, GiftData giftData) {
        // TODO: redo all of this, it sucks so bad ;P, maybe add purchase handlers for each item or some crap
        if (amount > 100) {
            client.send(AlertMessageComposer.compose(Locale.get("catalog.error.toomany")));
            return;
        }

        final int playerIdToDeliver = giftData == null ? -1 : PlayerDao.getIdByUsername(giftData.getReceiver());

        if (giftData != null) {
            if (playerIdToDeliver == 0) {
                client.send(GiftUserNotFoundMessageComposer.compose());
                return;
            }
        }

        List<InventoryItem> unseenItems = new ArrayList<>();

        try {
            CatalogItem item;

            if (pageId > 0) {
                item = CatalogManager.getInstance().getPage(pageId).getItems().get(itemId);
            } else {
                item = CatalogManager.getInstance().getCatalogItemByItemId(itemId);
            }

            if (item == null) {
                return;
            }

            if(giftData != null) {
                try {
                    final ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0));

                    if(itemDefinition != null && !itemDefinition.canGift()) {
                        return;
                    }
                } catch(Exception e) {
                    return;
                }

                if(client.getPlayer().getLastGift() != 0) {
                    if(((int) Comet.getTime() - client.getPlayer().getLastGift()) < 5000) {
                        client.send(AdvancedAlertMessageComposer.compose(Locale.get("catalog.error.gifttoofast")));

                        client.send(BoughtItemMessageComposer.badge());
                        return;
                    }
                }

                client.getPlayer().setLastGift((int) Comet.getTime());
            }

            if (amount > 1 && !item.allowOffer()) {
                client.send(AlertMessageComposer.compose(Locale.get("catalog.error.nooffer")));

                return;
            }

            int totalCostCredits;
            int totalCostPoints;
            int totalCostActivityPoints;

            if (item.getLimitedSells() >= item.getLimitedTotal() && item.getLimitedTotal() != 0) {
                client.send(LimitedEditionSoldOutMessageComposer.compose());
                return;
            }

            if (item.allowOffer()) {
                totalCostCredits = amount > 1 ? ((item.getCostCredits() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostCredits())) : item.getCostCredits();
                totalCostPoints = amount > 1 ? ((item.getCostOther() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostOther())) : item.getCostOther();
                totalCostActivityPoints = amount > 1 ? ((item.getCostActivityPoints() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostActivityPoints())) : item.getCostActivityPoints();
            } else {
                totalCostCredits = item.getCostCredits();
                totalCostPoints = item.getCostOther();
                totalCostActivityPoints = item.getCostActivityPoints();
            }

            if ((!CometSettings.infiniteBalance && (client.getPlayer().getData().getCredits() < totalCostCredits || client.getPlayer().getData().getActivityPoints() < totalCostActivityPoints)) || client.getPlayer().getData().getVipPoints() < totalCostPoints) {
                CometManager.getLogger().warn("Player with ID: " + client.getPlayer().getId() + " tried to purchase item with ID: " + item.getId() + " with the incorrect amount of credits or points.");
                client.send(AlertMessageComposer.compose(Locale.get("catalog.error.notenough")));
                return;
            }

            if(!CometSettings.infiniteBalance) {
                client.getPlayer().getData().decreaseCredits(totalCostCredits);
                client.getPlayer().getData().decreaseActivityPoints(totalCostActivityPoints);
            }

            client.getPlayer().getData().decreasePoints(totalCostPoints);

            client.getPlayer().sendBalance();
            client.getPlayer().getData().save();

            if(item.isBadgeOnly()) {
                if (item.hasBadge()) {
                    client.getPlayer().getInventory().addBadge(item.getBadgeId(), true);
                }

                client.send(BoughtItemMessageComposer.badge());
                return;
            }

            for (int newItemId : item.getItems()) {
                ItemDefinition def = ItemManager.getInstance().getDefinition(newItemId);
                if (def == null) {
                    continue;
                }

                client.send(BoughtItemMessageComposer.compose(item, def));

                if (def.getItemName().equals("DEAL_HC_1")) {
                    // TODO: HC buying
                    throw new Exception("HC purchasing is not implemented");
                }

                String extraData = "";

                boolean isTeleport = false;

                if (def.getInteraction().equals("trophy")) {
                    extraData +=
                            client.getPlayer().getData().getUsername() + Character.toChars(9)[0] + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getYear() + Character.toChars(9)[0] + data;
                } else if (def.getInteraction().equals("teleport") || def.getInteraction().equals("teleport_door")) {
                    amount = amount * 2;
                    isTeleport = true;
                } else if (item.getDisplayName().startsWith("a0 pet")) {
                    String petRace = item.getDisplayName().replace("a0 pet", "");
                    String[] petData = data.split("\n"); // [0:name, 1:race, 2:colour]

                    if (petData.length != 3) {
                        throw new Exception("Invalid pet data length: " + petData.length);
                    }

                    int petId = PetDao.createPet(client.getPlayer().getId(), petData[0], Integer.parseInt(petRace), Integer.parseInt(petData[1]), petData[2]);

                    client.getPlayer().getPets().addPet(new PetData(petId, petData[0], StaticPetProperties.DEFAULT_LEVEL, StaticPetProperties.DEFAULT_HAPPINESS, StaticPetProperties.DEFAULT_EXPERIENCE, StaticPetProperties.DEFAULT_ENERGY, client.getPlayer().getId(), petData[2], Integer.parseInt(petData[1]), Integer.parseInt(petRace)));
                    client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
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
                    String type = "generic";

                    switch(item.getDisplayName()) {
                        case "bot_bartender":
                            type = "waiter";
                            break;
                    }

                    int botId = PlayerBotDao.createBot(client.getPlayer().getId(), botName, botFigure, botGender, botMotto, type);
                    client.getPlayer().getBots().addBot(new InventoryBot(botId, client.getPlayer().getId(), client.getPlayer().getData().getUsername(), botName, botFigure, botGender, botMotto, type));
                    client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));
                    return;
                } else if (def.getInteraction().equals("badge_display")) {
                    if (client.getPlayer().getInventory().getBadges().get(data) == null) {
                        // Fuck off.
                        return;
                    }

                    extraData = data;
                }

                int[] teleportIds = null;

                if (isTeleport) {
                    teleportIds = new int[amount];
                }

                List<CatalogPurchase> purchases = new ArrayList<>();

                if (giftData != null) {
                    giftData.setExtraData(extraData);

                    purchases.add(new CatalogPurchase(playerIdToDeliver, ItemManager.getInstance().getBySpriteId(giftData.getSpriteId()).getId(), "GIFT::##" + JsonFactory.getInstance().toJson(giftData)));
                } else {
                    for (int purchaseCount = 0; purchaseCount < amount; purchaseCount++) {
                        for (int itemCount = 0; itemCount != item.getAmount(); itemCount++) {
                            purchases.add(new CatalogPurchase(client.getPlayer().getId(), newItemId, extraData));
                        }
                    }
                }

                List<Integer> newItems = ItemDao.createItems(purchases);

                for (Integer newItem : newItems) {
                    if (item.getLimitedTotal() > 0) {
                        item.increaseLimitedSells(1);
                        CatalogDao.updateLimitSellsForItem(item.getId());

                        LimitedEditionDao.save(new LimitedEditionItem(newItem, item.getLimitedSells(), item.getLimitedTotal()));
                    }

                    if (giftData == null)
                        unseenItems.add(client.getPlayer().getInventory().add(newItem, newItemId, extraData, giftData));

                    if (isTeleport)
                        teleportIds[newItems.indexOf(newItem)] = newItem;
                }

                if (isTeleport) {
                    int lastId = 0;

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

                    client.send(UnseenItemsMessageComposer.compose(unseenItems));
                    client.send(UpdateInventoryMessageComposer.compose());
                }

            }
        } catch (Exception e) {
            CometManager.getLogger().error("Error while buying catalog item", e);
        } finally {
            // Clean up the purchase - even if there was an exception!!
            unseenItems.clear();
        }
    }

    /**
     * Deliver the gift
     *
     * @param playerId     The ID of the player to deliver the item to
     * @param giftData     The data of the gift
     * @param newItems     List of items to deliver
     */
    private void deliverGift(int playerId, GiftData giftData, List<Integer> newItems, String senderUsername) {
        Session client = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if (client != null) {
            List<InventoryItem> unseenItems = new ArrayList<>();

            for (int newItem : newItems) {
                unseenItems.add(client.getPlayer().getInventory().add(newItem, ItemManager.getInstance().getBySpriteId(giftData.getSpriteId()).getId(), "GIFT::##" + JsonFactory.getInstance().toJson(giftData), giftData));
            }

            client.send(UnseenItemsMessageComposer.compose(unseenItems));
            client.send(UpdateInventoryMessageComposer.compose());
            client.send(RoomNotificationMessageComposer.bubble("gift_received", Locale.getOrDefault("notification.gift_received", "You have just received a gift from %username%!").replace("%username%", senderUsername)));
        }
    }

    /**
     * Catalog purchase object used for batching multiple purchases together
     */
    public class CatalogPurchase {
        /**
         * The ID of the player who purchased the item
         */
        private int playerId;

        /**
         * The item definition ID of the item
         */
        private int itemBaseId;

        /**
         * The data generated for items such as trophies etc
         */
        private String data;

        /**
         * Initialize the catalog purchase object
         *
         * @param playerId   The ID of the player who purchased the item
         * @param itemBaseId The item definition ID of the item
         * @param data       The data generated for items such as trophies etc
         */
        public CatalogPurchase(int playerId, int itemBaseId, String data) {
            this.playerId = playerId;
            this.itemBaseId = itemBaseId;
            this.data = data;
        }

        /**
         * Get the player ID of the player who purchased the item
         *
         * @return The ID of the player who purchased the item
         */
        public int getPlayerId() {
            return playerId;
        }

        /**
         * Get the item definition ID
         *
         * @return The item definition ID
         */
        public int getItemBaseId() {
            return itemBaseId;
        }

        /**
         * Get the data generated for items such as trophies etc
         *
         * @return The data generated for items such as trophies etc
         */
        public String getData() {
            return data;
        }
    }
}

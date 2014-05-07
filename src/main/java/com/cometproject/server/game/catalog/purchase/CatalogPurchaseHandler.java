package com.cometproject.server.game.catalog.purchase;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.StaticPetProperties;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.SendPurchaseAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.items.TeleporterDao;
import com.cometproject.server.storage.queries.pets.PetDao;
import javolution.util.FastMap;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogPurchaseHandler {
    private CatalogManager catalogManager;

    public CatalogPurchaseHandler(CatalogManager catalogManager) {
        this.catalogManager = catalogManager;
    }

    public void handle(Session client, int pageId, int itemId, String data, int amount, GiftData giftData) {
        if (amount > 100) {
            client.send(AlertMessageComposer.compose(Locale.get("catalog.error.toomany")));
            return;
        }

        try {
            CatalogItem item = this.catalogManager.getPage(pageId).getItems().get(itemId);

            if (amount > 1 && !item.allowOffer()) {
                client.send(AlertMessageComposer.compose(Locale.get("catalog.error.nooffer")));

                return;
            }

            if (item == null)
                return;

            int totalCostCredits;
            int totalCostPoints;

            if (item.allowOffer()) {
                totalCostCredits = amount > 1 ? ((item.getCostCredits() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostCredits())) : item.getCostCredits();
                totalCostPoints = amount > 1 ? ((item.getCostOther() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostOther())) : item.getCostOther();
            } else {
                totalCostCredits = item.getCostCredits();
                totalCostPoints = item.getCostOther();
            }

            if (client.getPlayer().getData().getCredits() < totalCostCredits || client.getPlayer().getData().getPoints() < totalCostPoints) {
                GameEngine.getLogger().warn("Player with ID: " + client.getPlayer().getId() + " tried to purchase item with ID: " + item.getId() + " with the incorrect amount of credits or points.");
                client.send(AlertMessageComposer.compose(Locale.get("catalog.error.notenough")));
                return;
            }

            client.getPlayer().getData().decreaseCredits(totalCostCredits);
            client.getPlayer().getData().decreasePoints(totalCostPoints);

            client.getPlayer().sendBalance();
            client.getPlayer().getData().save();

            for (int newItemId : item.getItems()) {
                ItemDefinition def = GameEngine.getItems().getDefintion(newItemId);
                client.send(BoughtItemMessageComposer.compose(item, def));

                if (def.getItemName().equals("DEAL_HC_1")) {
                    // TODO: HC buying
                    throw new Exception("HC purchasing is not implemented");
                }

                Map<Integer, Integer> unseenItems = new FastMap<>();

                String extraData = "";

                boolean isTeleport = false;

                if (def.getInteraction().equals("trophy")) {
                    extraData +=
                            client.getPlayer().getData().getUsername() + Character.toChars(9)[0] + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getYear() + Character.toChars(9)[0] + data;
                } else if (def.getInteraction().equals("teleport")) {
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
                } else if(def.getType().equals("r")) {
                    // It's a bot!
                    String botName = "New Bot";
                    String botFigure = client.getPlayer().getData().getFigure();
                    String botGender = client.getPlayer().getData().getGender();
                    String botMotto = "Beeb beeb boop beep!";

                    int botId = PlayerBotDao.createBot(client.getPlayer().getId(), botName, botFigure, botGender, botMotto);
                    client.getPlayer().getBots().addBot(new InventoryBot(botId, client.getPlayer().getId(), client.getPlayer().getData().getUsername(), botName, botFigure, botGender, botMotto));
                    client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));
                    return;
                }

                int[] teleportIds = null;

                if (isTeleport) {
                    teleportIds = new int[amount];
                }

                List<CatalogPurchase> purchases = new ArrayList<>();

                /*for (int e = 0; e < amount; e++) {
                    for (int i = 0; i != item.getAmount(); i++) {
                        int insertedId = ItemDao.createItem(client.getPlayer().getId(), newItemId, data);

                        if (isTeleport) {
                            teleportIds[e] = insertedId;
                        }

                        unseenItems.put(insertedId, def.getType().equalsIgnoreCase("s") ? 1 : 2);
                        // DELIVER GIFT HERE
                        client.getPlayer().getInventory().add(insertedId, newItemId, extraData, giftData);
                    }

                    if (item.getLimitedTotal() > 0) {
                        item.increaseLimitedSells(1);

                        CatalogDao.updateLimitSellsForItem(item.getId());
                    }
                }*/

                for (int purchaseCount = 0; purchaseCount < amount; purchaseCount++) {
                    for(int itemCount = 0; itemCount != item.getAmount(); itemCount++) {
                        purchases.add(new CatalogPurchase(client.getPlayer().getId(), newItemId, extraData));
                    }

                    if (item.getLimitedTotal() > 0) {
                        item.increaseLimitedSells(1);

                        CatalogDao.updateLimitSellsForItem(item.getId());
                    }
                }

                List<Integer> newItems = ItemDao.createItems(purchases);

                for(Integer newItem : newItems) {
                    unseenItems.put(newItem, def.getType().equalsIgnoreCase("s") ? 1 : 2);
                    client.getPlayer().getInventory().add(newItem, newItemId, extraData, giftData);
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

                if (item.hasBadge()) {
                    client.getPlayer().getInventory().addBadge(item.getBadgeId(), true);
                }

                client.send(UpdateInventoryMessageComposer.compose());
                client.send(SendPurchaseAlertMessageComposer.compose(unseenItems));
            }
        } catch (Exception e) {
            GameEngine.getLogger().error("Error while buying catalog item", e);
        }
    }

    private void deliverGift() {
        // TODO: this
    }

    public class CatalogPurchase {
        private int playerId;
        private int itemBaseId;
        private String data;

        public CatalogPurchase(int playerId, int itemBaseId, String data) {
            this.playerId = playerId;
            this.itemBaseId = itemBaseId;
            this.data = data;
        }

        public int getPlayerId() {
            return playerId;
        }

        public int getItemBaseId() {
            return itemBaseId;
        }

        public String getData() {
            return data;
        }
    }
}

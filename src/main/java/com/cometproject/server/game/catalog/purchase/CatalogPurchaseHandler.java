package com.cometproject.server.game.catalog.purchase;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.StaticPetProperties;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.SendPurchaseAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;
import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class CatalogPurchaseHandler {
    private CatalogManager catalogManager;

    public CatalogPurchaseHandler(CatalogManager catalogManager) {
        this.catalogManager = catalogManager;
    }

    public void handle(Session client, int pageId, int itemId, String data, int amount, GiftData giftData) {
        if (amount > 50) {
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

                    PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT INTO `pet_data` (`owner_id`, `pet_name`, `type`, `race_id`, `colour`, `scratches`, `level`, `happiness`, `experience`, `energy`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", true);

                    statement.setInt(1, client.getPlayer().getId());
                    statement.setString(2, petData[0]);
                    statement.setInt(3, Integer.parseInt(petRace));
                    statement.setInt(4, Integer.parseInt(petData[1]));
                    statement.setString(5, petData[2]);
                    statement.setInt(6, 0);
                    statement.setInt(7, StaticPetProperties.DEFAULT_LEVEL);
                    statement.setInt(8, StaticPetProperties.DEFAULT_HAPPINESS);
                    statement.setInt(9, StaticPetProperties.DEFAULT_EXPERIENCE);
                    statement.setInt(10, StaticPetProperties.DEFAULT_ENERGY);

                    statement.execute();

                    ResultSet keys = statement.getGeneratedKeys();

                    if (keys.next()) {
                        int insertedId = keys.getInt(1);
                        client.getPlayer().getPets().addPet(new PetData(insertedId, petData[0], StaticPetProperties.DEFAULT_LEVEL, StaticPetProperties.DEFAULT_HAPPINESS, StaticPetProperties.DEFAULT_EXPERIENCE, StaticPetProperties.DEFAULT_ENERGY, client.getPlayer().getId(), petData[2], Integer.parseInt(petData[1]), Integer.parseInt(petRace)));
                        client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
                    }

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
                }

                int[] teleportIds = null;

                if (isTeleport) {
                    teleportIds = new int[amount];
                }

                for (int e = 0; e < amount; e++) {
                    for (int i = 0; i != item.getAmount(); i++) {
                        PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into items (`user_id`, `base_item`, `extra_data`, `wall_pos`) VALUES(?, ?, ?, ?);", true);

                        statement.setInt(1, client.getPlayer().getId());
                        statement.setInt(2, newItemId);
                        statement.setString(3, extraData);
                        statement.setString(4, "");
                        statement.execute();

                        ResultSet keys = statement.getGeneratedKeys();

                        if (keys.next()) {
                            int insertedId = keys.getInt(1);

                            if (isTeleport) {
                                teleportIds[e] = insertedId;
                            }

                            unseenItems.put(insertedId, def.getType().equalsIgnoreCase("s") ? 1 : 2);
                            // DELIVER GIFT HERE
                            client.getPlayer().getInventory().add(insertedId, newItemId, extraData, giftData);
                        }
                    }

                    if (item.getLimitedTotal() > 0) {
                        item.increaseLimitedSells(1);

                        PreparedStatement s = Comet.getServer().getStorage().prepare("UPDATE catalog_items SET limited_sells = limited_sells + 1 WHERE id = ?");
                        s.setInt(1, item.getId());

                        s.execute();
                    }
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

                        Comet.getServer().getStorage().execute("INSERT into items_teles (id_one, id_two) VALUES(" + lastId + ", " + teleportIds[i] + ");");
                        Comet.getServer().getStorage().execute("INSERT into items_teles (id_one, id_two) VALUES(" + teleportIds[i] + ", " + lastId + ");");
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
}

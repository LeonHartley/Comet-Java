package com.cometsrv.network.messages.incoming.catalog;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.Locale;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.catalog.types.CatalogItem;
import com.cometsrv.game.items.types.ItemDefinition;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometsrv.network.messages.outgoing.catalog.SendPurchaseAlertMessageComposer;
import com.cometsrv.network.messages.outgoing.misc.AlertMessageComposer;
import com.cometsrv.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PurchaseItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();
        int itemId = msg.readInt();
        String data = msg.readString();
        int amount = msg.readInt();

        if(amount > 50) {
            client.send(AlertMessageComposer.compose(Locale.get("catalog.error.toomany")));
            return;
        }

        try {
            CatalogItem item = GameEngine.getCatalog().getPage(pageId).getItems().get(itemId);

            if(amount > 1 && !item.allowOffer()) {
                client.send(AlertMessageComposer.compose(Locale.get("catalog.error.nooffer")));
                return;
            }

            if(item == null)
                return;

            int totalCostCredits;
            int totalCostPoints;

            if(item.allowOffer()) {
                totalCostCredits = amount > 1 ? ((item.getCostCredits() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostCredits())) : item.getCostCredits();
                totalCostPoints = amount > 1 ? ((item.getCostOther() * amount) - ((int) Math.floor((double) amount / 6) * item.getCostOther())) : item.getCostOther();
            } else {
                totalCostCredits = item.getCostCredits();
                totalCostPoints = item.getCostOther();
            }

            if(client.getPlayer().getData().getCredits() < totalCostCredits || client.getPlayer().getData().getPoints() < totalCostPoints) {
                GameEngine.getLogger().warn("Player with ID: " + client.getPlayer().getId() + " tried to purchase item with ID: " + item.getId() + " with the incorrect amount of credits or points.");
                return;
            }

            client.getPlayer().getData().decreaseCredits(totalCostCredits);
            client.getPlayer().getData().decreasePoints(totalCostPoints);

            client.getPlayer().sendBalance();
            client.getPlayer().getData().save();

            for(int newItemId : item.getItems()) {
                ItemDefinition def = GameEngine.getItems().getDefintion(newItemId);
                client.send(BoughtItemMessageComposer.compose(item, def));

                if(def.getItemName().equals("DEAL_HC_1")) {
                    // TODO: HC buying
                    return;
                }

                Map<Integer, Integer> unseenItems = new FastMap<>();

                String extraData = "";

                boolean isTeleport = false;

                if(def.getInteraction().equals("trophy")) {
                    extraData +=
                            client.getPlayer().getData().getUsername() + Character.toChars(9)[0] + DateTime.now().getDayOfMonth() + "-" + DateTime.now().getMonthOfYear() + "-" + DateTime.now().getYear() + Character.toChars(9)[0] + data;
                } else if(def.getInteraction().equals("teleport")) {
                    amount = amount * 2;
                    isTeleport = true;
                }

                int[] teleportIds = null;

                if(isTeleport) {
                    teleportIds = new int[amount];
                }

                try {
                    for(int e = 0; e < amount; e++) {
                        for(int i = 0; i != item.getAmount(); i++) {
                            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into items (`user_id`, `base_item`, `extra_data`, `wall_pos`) VALUES(?, ?, ?, ?);");

                            statement.setInt(1, client.getPlayer().getId());
                            statement.setInt(2, newItemId);
                            statement.setString(3, extraData);
                            statement.setString(4, "");
                            statement.execute();

                            ResultSet keys = statement.getGeneratedKeys();

                            if(keys.next()) {
                                int insertedId = keys.getInt(1);

                                if(isTeleport) {
                                    teleportIds[e] = insertedId;
                                }

                                unseenItems.put(insertedId, def.getType().equalsIgnoreCase("s") ? 1 : 2);
                                client.getPlayer().getInventory().add(insertedId, newItemId, extraData);
                            }
                        }

                        if(item.getLimitedTotal() > 0) {
                            item.increaseLimitedSells(1);

                            PreparedStatement s = Comet.getServer().getStorage().prepare("UPDATE catalog_items SET limited_sells = limited_sells + 1 WHERE id = ?");
                            s.setInt(1, item.getId());

                            s.execute();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                if(isTeleport) {
                    int lastId = 0;

                    for(int i = 0; i < teleportIds.length; i++) {
                        if(lastId == 0) {
                            lastId = teleportIds[i];
                        }

                        if(i % 2 == 0 && lastId != 0) {
                            lastId = teleportIds[i];
                            continue;
                        }

                        Comet.getServer().getStorage().execute("INSERT into items_teles (id_one, id_two) VALUES(" + lastId + ", " + teleportIds[i] + ");");
                        Comet.getServer().getStorage().execute("INSERT into items_teles (id_one, id_two) VALUES(" + teleportIds[i] + ", " + lastId + ");");
                    }
                }

                if(item.hasBadge()) {
                    client.getPlayer().getInventory().addBadge(item.getBadgeId(), true);
                }

                client.send(UpdateInventoryMessageComposer.compose());
                client.send(SendPurchaseAlertMessageComposer.compose(unseenItems));
            }
        } catch(Exception e) {
            GameEngine.getLogger().error("Error while buying catalog item", e);
        }
    }
}

package com.cometproject.server.game.rooms.types.components.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.components.TradeComponent;
import com.cometproject.server.network.messages.outgoing.catalog.SendPurchaseAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.trading.*;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.util.List;

public class Trade {
    private PlayerEntity user1, user2;
    private int stage = 1;
    private List<InventoryItem> user1Items, user2Items;

    private boolean user1Accepted = false, user2Accepted = false;

    public Trade(PlayerEntity user1, PlayerEntity user2) {
        this.user1 = user1;
        this.user2 = user2;

        user1Items = new ArrayList<>();
        user2Items = new ArrayList<>();

        if (!user1.hasStatus("trd")) {
            user1.addStatus("trd", "");
            user1.markNeedsUpdate();
        }

        if (!user2.getPlayer().getEntity().hasStatus("trd")) {
            user2.addStatus("trd", "");
            user2.markNeedsUpdate();
        }

        sendToUsers(TradeStartMessageComposer.compose(user1.getPlayer().getId(), user2.getPlayer().getId()));
    }

    public void dispose() {
        user1Items.clear();
        user2Items.clear();
        stage = 0;
        user1 = null;
        user2 = null;
        user1Items = null;
        user2Items = null;
    }

    public void cancel(int userId) {
        this.cancel(userId, true);
    }

    public void cancel(int userId, boolean isLeave) {
        this.user1Items.clear();
        this.user2Items.clear();

        boolean sendToUser1 = true;
        boolean sendToUser2 = true;

        if (isLeave) {
            if (user1.getPlayer() == null || userId == user1.getPlayer().getId()) {
                sendToUser1 = false;
            } else {
                sendToUser2 = false;
            }
        }

        if (user1 != null && user1.getPlayer() != null && sendToUser1) {
            user1.removeStatus("trd");
            user1.markNeedsUpdate();
        }

        if (user2 != null && user2.getPlayer() != null && sendToUser2) {
            user2.removeStatus("trd");
            user2.markNeedsUpdate();
        }

        sendToUsers(TradeCloseMessageComposer.compose(userId));
    }

    public void addItem(int user, InventoryItem item) {
        if (user == 1) {
            if (!this.user1Items.contains(item)) {
                this.user1Items.add(item);
            }
        } else {
            if (!this.user2Items.contains(item)) {
                this.user2Items.add(item);
            }
        }

        this.updateWindow();
    }

    public int getUserNumber(PlayerEntity user) {
        return (user1 == user) ? 1 : 0;
    }

    public void removeItem(int user, InventoryItem item) {
        if (user == 1) {
            if (this.user1Items.contains(item)) {
                this.user1Items.remove(item);
            }
        } else {
            if (this.user2Items.contains(item)) {
                this.user2Items.remove(item);
            }
        }

        this.updateWindow();
    }

    public void accept(int user) {
        if (user == 1)
            this.user1Accepted = true;
        else
            this.user2Accepted = true;

        sendToUsers(TradeAcceptUpdateMessageComposer.compose(((user == 1) ? user1 : user2).getPlayer().getId()));

        if (user1Accepted && user2Accepted) {
            this.stage++;
            sendToUsers(TradeCompleteMessageComposer.compose());
            user1Accepted = false;
            user2Accepted = false;
        }
    }

    public void confirm(int user, TradeComponent tradeComponent) {
        if (stage != 2) {
            return;
        }

        if (user == 1)
            this.user1Accepted = true;
        else
            this.user2Accepted = true;

        sendToUsers(TradeAcceptUpdateMessageComposer.compose(((user == 1) ? user1 : user2).getPlayer().getId()));

        if (user1Accepted && user2Accepted) {
            complete();

            this.user1Items.clear();
            this.user2Items.clear();

            tradeComponent.remove(this);

            if (user1.getPlayer().getEntity().hasStatus("trd")) {
                user1.getPlayer().getEntity().removeStatus("trd");
                user1.getPlayer().getEntity().markNeedsUpdate();
            }

            if (user2.getPlayer().getEntity().hasStatus("trd")) {
                user2.getPlayer().getEntity().removeStatus("trd");
                user2.getPlayer().getEntity().markNeedsUpdate();
            }

        }
    }

    public void complete() {
        for (InventoryItem item : this.user1Items) {
            if (user1.getPlayer().getInventory().getItem(item.getId()) == null) {
                sendToUsers(AlertMessageComposer.compose(Locale.get("game.trade.error")));
                return;
            } else {
                user1.getPlayer().getInventory().removeItem(item);
                user2.getPlayer().getInventory().addItem(item);

                try {
                    PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE items SET user_id = ? AND room_id = 0 WHERE id = ?");

                    statement.setInt(1, user2.getPlayer().getId());
                    statement.setInt(2, item.getId());

                    statement.execute();
                } catch (Exception e) {
                    GameEngine.getLogger().error("There was an error during trade between " + user1.getPlayer().getId() + " and " + user2.getPlayer().getId(), e);
                }
            }
        }

        for (InventoryItem item : this.user2Items) {
            if (user2.getPlayer().getInventory().getItem(item.getId()) == null) {
                sendToUsers(AlertMessageComposer.compose(Locale.get("game.trade.error")));
                return;
            } else {
                user2.getPlayer().getInventory().removeItem(item);
                user1.getPlayer().getInventory().addItem(item);

                try {
                    PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE items SET user_id = ? AND room_id = 0 WHERE id = ?");

                    statement.setInt(1, user1.getPlayer().getId());
                    statement.setInt(2, item.getId());

                    statement.execute();
                } catch (Exception e) {
                    GameEngine.getLogger().error("There was an error during trade between " + user1.getPlayer().getId() + " and " + user2.getPlayer().getId(), e);
                }
            }
        }

        user1.getPlayer().getSession().send(SendPurchaseAlertMessageComposer.compose(user2Items));
        user2.getPlayer().getSession().send(SendPurchaseAlertMessageComposer.compose(user1Items));

        sendToUsers(UpdateInventoryMessageComposer.compose());
        sendToUsers(TradeCloseCleanMessageComposer.compose());
    }

    public void updateWindow() {
        this.sendToUsers(TradeUpdateMessageComposer.compose(
                this.user1.getPlayer().getId(),
                this.user2.getPlayer().getId(),
                this.user1Items,
                this.user2Items
        ));
    }

    public void sendToUsers(Composer msg) {
        if (user1 != null && user1.getPlayer() != null && user1.getPlayer().getSession() != null)
            user1.getPlayer().getSession().getChannel().writeAndFlush(msg.get().duplicate().retain());

        if (user2 != null && user2.getPlayer() != null && user2.getPlayer().getSession() != null)
            user2.getPlayer().getSession().getChannel().writeAndFlush(msg.get().duplicate().retain());
    }

    public PlayerEntity getUser1() {
        return this.user1;
    }

    public PlayerEntity getUser2() {
        return this.user2;
    }
}

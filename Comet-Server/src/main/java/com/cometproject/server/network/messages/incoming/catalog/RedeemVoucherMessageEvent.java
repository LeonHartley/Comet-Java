package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.types.Voucher;
import com.cometproject.server.game.catalog.types.VoucherStatus;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.EnforceRoomCategoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.catalog.VoucherDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

public class RedeemVoucherMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String voucherCode = msg.readString();

        if (client.getPlayer().getVoucherRedeemAttempts() >= 20 &&
                (client.getPlayer().getLastVoucherRedeemAttempt() + 120) > (System.currentTimeMillis() / 1000)) {
            return;
        }

        client.getPlayer().setVoucherRedeemAttempts(client.getPlayer().getLastVoucherRedeemAttempt() + 1);
        client.getPlayer().setLastVoucherRedeemAttempt((int) System.currentTimeMillis() / 1000);

        final Voucher voucher = VoucherDao.findVoucherByCode(voucherCode);

        if (voucher == null) {
            client.getPlayer().sendMotd(Locale.getOrDefault("voucher.error.doesnt_exist", "The voucher you entered doesn't exist!"));
            return;
        }

        if (voucher.getStatus() == VoucherStatus.CLAIMED) {
            client.getPlayer().sendMotd(Locale.getOrDefault("voucher.error.claimed", "The voucher you entered has already been claimed!"));
            return;
        }

        boolean failure = false;

        // redeem the voucher
        switch (voucher.getType()) {
            case COINS: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int coinAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseCredits(coinAmount);
                client.send(new AdvancedAlertMessageComposer(Locale.get("command.coins.title"), Locale.get("command.coins.received").replace("%amount%", String.valueOf(coinAmount))));
                break;
            }

            case ROOM_BUNDLE: {
                RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(voucher.getData());

                try {
                    int roomId = RoomManager.getInstance().createRoom(roomBundle.getConfig().getRoomName().replace("%username%", client.getPlayer().getData().getUsername()), "", roomBundle.getRoomModelData(), 0, 20, 0, client, roomBundle.getConfig().getThicknessWall(), roomBundle.getConfig().getThicknessFloor(), roomBundle.getConfig().getDecorations(), roomBundle.getConfig().isHideWalls());

                    for (RoomBundleItem roomBundleItem : roomBundle.getRoomBundleData()) {
                        long newItemId = ItemDao.createItem(client.getPlayer().getId(), roomBundleItem.getItemId(), roomBundleItem.getExtraData());

                        if (roomBundleItem.getWallPosition() == null) {
                            RoomItemDao.placeFloorItem(roomId, roomBundleItem.getX(), roomBundleItem.getY(), roomBundleItem.getZ(), roomBundleItem.getRotation(), roomBundleItem.getExtraData(), newItemId);
                        } else {

                            RoomItemDao.placeWallItem(roomId, roomBundleItem.getWallPosition(), roomBundleItem.getExtraData(), newItemId);
                        }
                    }

                    client.send(new RoomForwardMessageComposer(roomId));
                    client.send(new EnforceRoomCategoryMessageComposer());
                    client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                    client.getPlayer().setLastRoomCreated((int) Comet.getTime());

                } catch (Exception e) {
                    client.send(new MotdNotificationMessageComposer("Invalid room bundle data, please contact an administrator."));
                    client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                }
                break;
            }
        }

        if(failure) {
            client.getPlayer().sendMotd(Locale.getOrDefault("voucher.error", "The voucher was redeemed unsuccessfully"));

            client.getPlayer().getData().save();
            client.getPlayer().sendBalance();
        } else {
            client.getPlayer().sendMotd(Locale.getOrDefault("voucher.success", "The voucher was redeemed successfully"));
        }

        VoucherDao.claimVoucher(voucher.getId(), client.getPlayer().getId());
        client.getPlayer().setVoucherRedeemAttempts(0);
    }
}

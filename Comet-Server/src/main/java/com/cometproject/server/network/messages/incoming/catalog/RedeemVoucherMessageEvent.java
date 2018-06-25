package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.api.game.catalog.types.vouchers.VoucherStatus;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.composers.catalog.BoughtItemMessageComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.Voucher;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.api.game.catalog.types.bundles.RoomBundleItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.EnforceRoomCategoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.catalog.VoucherDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.storage.api.StorageContext;
import org.apache.commons.lang.StringUtils;

public class RedeemVoucherMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String voucherCode = msg.readString();

        if (client.getPlayer().getVoucherRedeemAttempts() >= 20 &&
                (client.getPlayer().getLastVoucherRedeemAttempt() + 120) > (System.currentTimeMillis() / 1000)) {
            return;
        }

        client.getPlayer().setVoucherRedeemAttempts(client.getPlayer().getVoucherRedeemAttempts() + 1);
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
                // fixed for both options :))
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int coinAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseCredits(coinAmount);
                client.getPlayer().getData().save();
                client.send(client.getPlayer().composeCreditBalance());
                client.send(new AdvancedAlertMessageComposer(Locale.get("command.coins.title"), Locale.get("command.coins.received").replace("%amount%", String.valueOf(coinAmount))));
                break;
            }

            case DUCKETS: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int ducketAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseActivityPoints(ducketAmount);
                client.getPlayer().getData().save();
                client.send(client.getPlayer().composeCurrenciesBalance());
                client.send(new AdvancedAlertMessageComposer(Locale.get("command.duckets.successtitle"),
                        Locale.get("command.duckets.successmessage").replace("%amount%", String.valueOf(ducketAmount))
                ));
                break;
            }

            case VIP_POINTS: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int vipPointAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseVipPoints(vipPointAmount);
                client.getPlayer().getData().save();
                client.send(client.getPlayer().composeCurrenciesBalance());
                client.send(new AdvancedAlertMessageComposer(
                        Locale.get("command.points.successtitle"),
                        Locale.get("command.points.successmessage").replace("%amount%", String.valueOf(vipPointAmount))
                ));
                break;
            }

            case ROOM_BUNDLE: {
                RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(voucher.getData());

                CatalogManager.getInstance().getPurchaseHandler().purchaseBundle(roomBundle, client);
                break;
            }
        }

        if (failure) {
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

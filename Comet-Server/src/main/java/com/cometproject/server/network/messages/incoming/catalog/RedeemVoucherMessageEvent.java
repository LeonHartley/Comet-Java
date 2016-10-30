package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.types.Voucher;
import com.cometproject.server.game.catalog.types.VoucherStatus;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.catalog.VoucherDao;
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
            }
            break;
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

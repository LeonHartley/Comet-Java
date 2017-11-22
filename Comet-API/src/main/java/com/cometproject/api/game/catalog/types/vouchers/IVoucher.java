package com.cometproject.api.game.catalog.types.vouchers;

public interface IVoucher {
    int getId();

    VoucherType getType();

    String getData();

    int getCreatedBy();

    int getCreatedAt();

    int getClaimedBy();

    int getClaimedAt();

    VoucherStatus getStatus();

    String getCode();
}

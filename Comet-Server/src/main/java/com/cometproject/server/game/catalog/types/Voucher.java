package com.cometproject.server.game.catalog.types;

import com.cometproject.api.game.catalog.types.vouchers.IVoucher;
import com.cometproject.api.game.catalog.types.vouchers.VoucherStatus;
import com.cometproject.api.game.catalog.types.vouchers.VoucherType;

public class Voucher implements IVoucher {
    private final int id;
    private final VoucherType type;
    private final String data;
    private final int createdBy;
    private final int createdAt;
    private final int claimedBy;
    private final int claimedAt;
    private final VoucherStatus status;
    private final String code;

    public Voucher(int id, VoucherType type, String data, int createdBy, int createdAt, int claimedBy, int claimedAt, VoucherStatus status, String code) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.claimedBy = claimedBy;
        this.claimedAt = claimedAt;
        this.status = status;
        this.code = code;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public VoucherType getType() {
        return type;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public int getCreatedBy() {
        return createdBy;
    }

    @Override
    public int getCreatedAt() {
        return createdAt;
    }

    @Override
    public int getClaimedBy() {
        return claimedBy;
    }

    @Override
    public int getClaimedAt() {
        return claimedAt;
    }

    @Override
    public VoucherStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }
}

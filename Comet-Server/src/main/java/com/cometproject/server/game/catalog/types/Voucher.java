package com.cometproject.server.game.catalog.types;

public class Voucher {
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

    public int getId() {
        return id;
    }

    public VoucherType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public int getClaimedBy() {
        return claimedBy;
    }

    public int getClaimedAt() {
        return claimedAt;
    }

    public VoucherStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}

package com.cometproject.server.game.groups.types;

public enum GroupType {
    REGULAR(0), EXCLUSIVE(1), PRIVATE(2);

    private int typeId;

    GroupType(int type) {
        this.typeId = type;
    }

    public int getTypeId() {
        return this.typeId;
    }
}

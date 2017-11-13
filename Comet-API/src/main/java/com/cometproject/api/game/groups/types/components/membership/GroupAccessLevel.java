package com.cometproject.api.game.groups.types.components.membership;

public enum GroupAccessLevel {
    MEMBER, ADMIN, OWNER;

    public boolean isAdmin() {
        return this.equals(ADMIN) || this.equals(OWNER);
    }
}

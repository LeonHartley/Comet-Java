package com.cometproject.website.website.access;

public enum AccessLevel {
    GUEST(-1),
    ALL(0),
    PLAYER(1),
    VIP(2),
    MODERATOR(5),
    ADMIN(7);

    private int accessLevel;

    AccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getLevel() {
        return accessLevel;
    }
}

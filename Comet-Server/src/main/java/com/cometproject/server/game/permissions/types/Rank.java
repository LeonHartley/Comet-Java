package com.cometproject.server.game.permissions.types;

import com.cometproject.api.game.players.data.components.permissions.IRank;

public class Rank implements IRank {
    private final int id;
    private final String name;

    private final boolean floodBypass;
    private final int floodTime;

    private final boolean disconnectable;
    private final boolean modTool;
    private final boolean bannable;

    private final boolean roomKickable;
    private final boolean roomFullControl;
    private final boolean roomMuteBypass;
    private final boolean roomFilterBypass;
    private final boolean roomIgnorable;
    private final boolean roomEnterFull;
    private final boolean roomEnterLocked;
    private final boolean roomStaffPick;
    private final boolean roomSeeWhispers;

    private final boolean messengerStaffChat;
    private final int messengerMaxFriends;

    private boolean aboutDetailed;
    private boolean aboutStats;

    public Rank(int id, String name, boolean floodBypass, int floodTime, boolean disconnectable, boolean modTool, boolean bannable, boolean roomKickable, boolean roomFullControl, boolean roomMuteBypass, boolean roomFilterBypass, boolean roomIgnorable, boolean roomEnterFull, boolean roomEnterLocked, boolean roomStaffPick, boolean roomSeeWhispers, boolean messengerStaffChat, int messengerMaxFriends, boolean aboutDetailed, boolean aboutStats) {
        this.id = id;
        this.name = name;
        this.floodBypass = floodBypass;
        this.floodTime = floodTime;
        this.disconnectable = disconnectable;
        this.modTool = modTool;
        this.bannable = bannable;
        this.roomKickable = roomKickable;
        this.roomFullControl = roomFullControl;
        this.roomMuteBypass = roomMuteBypass;
        this.roomFilterBypass = roomFilterBypass;
        this.roomIgnorable = roomIgnorable;
        this.roomEnterFull = roomEnterFull;
        this.roomEnterLocked = roomEnterLocked;
        this.roomStaffPick = roomStaffPick;
        this.roomSeeWhispers = roomSeeWhispers;
        this.messengerStaffChat = messengerStaffChat;
        this.messengerMaxFriends = messengerMaxFriends;
        this.aboutDetailed = aboutDetailed;
        this.aboutStats = aboutStats;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean floodBypass() {
        return this.floodBypass;
    }

    @Override
    public int floodTime() {
        return this.floodTime;
    }

    @Override
    public boolean disconnectable() {
        return this.disconnectable;
    }

    @Override
    public boolean bannable() {
        return this.bannable;
    }

    @Override
    public boolean modTool() {
        return this.modTool;
    }

    @Override
    public boolean roomKickable() {
        return this.roomKickable;
    }

    @Override
    public boolean roomFullControl() {
        return this.roomFullControl;
    }

    @Override
    public boolean roomMuteBypass() {
        return this.roomMuteBypass;
    }

    @Override
    public boolean roomFilterBypass() {
        return this.roomFilterBypass;
    }

    @Override
    public boolean roomIgnorable() {
        return this.roomIgnorable;
    }

    @Override
    public boolean roomEnterFull() {
        return roomEnterFull;
    }

    @Override
    public boolean messengerStaffChat() {
        return this.messengerStaffChat;
    }

    @Override
    public boolean roomEnterLocked() {
        return roomEnterLocked;
    }

    @Override
    public boolean roomStaffPick() {
        return this.roomStaffPick;
    }

    @Override
    public int messengerMaxFriends() {
        return this.messengerMaxFriends;
    }

    @Override
    public boolean aboutDetailed() {
        return this.aboutDetailed;
    }

    @Override
    public boolean aboutStats() {
        return aboutStats;
    }

    public boolean roomSeeWhispers() {
        return this.roomSeeWhispers;
    }
}

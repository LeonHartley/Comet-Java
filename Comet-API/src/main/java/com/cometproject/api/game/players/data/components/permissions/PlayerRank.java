package com.cometproject.api.game.players.data.components.permissions;

public interface PlayerRank {
    int getId();

    String getName();

    boolean floodBypass();

    int floodTime();

    boolean disconnectable();

    boolean bannable();

    boolean modTool();

    boolean roomKickable();

    boolean roomFullControl();

    boolean roomMuteBypass();

    boolean roomFilterBypass();

    boolean roomIgnorable();

    boolean roomEnterFull();

    boolean roomEnterLocked();

    boolean roomStaffPick();

    boolean messengerStaffChat();

    int messengerMaxFriends();

    boolean aboutDetailed();

    boolean aboutStats();
}

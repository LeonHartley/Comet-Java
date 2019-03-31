package com.cometproject.server.network.ws.request;

import com.google.gson.annotations.SerializedName;

public enum WsRequestType {
    @SerializedName("AUTH")
    AUTH,

    @SerializedName("PIANO_PLAY_NOTE")
    PIANO_PLAY_NOTE,

    @SerializedName("OPEN_ROOM")
    OPEN_ROOM,

    @SerializedName("ROOM_VOTE")
    ROOM_VOTE,

    @SerializedName("OPEN_LINK")
    OPEN_LINK
}

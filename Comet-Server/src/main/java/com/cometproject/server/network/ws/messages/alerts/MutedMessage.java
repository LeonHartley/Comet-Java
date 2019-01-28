package com.cometproject.server.network.ws.messages.alerts;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class MutedMessage extends WsMessage {
    private final MuteType muteType;
    private final boolean isMute;
    private final String msg;

    public MutedMessage(MuteType muteType, boolean isMute, String msg) {
        super(WsMessageType.MUTED);

        this.muteType = muteType;
        this.isMute = isMute;
        this.msg = msg;
    }

    public MuteType getMuteType() {
        return muteType;
    }

    public boolean isMute() {
        return isMute;
    }

    public String getMsg() {
        return msg;
    }

    public enum MuteType {
        ROOM_MUTE,
        MODERATOR_MUTE,
        USER_MUTE
    }
}

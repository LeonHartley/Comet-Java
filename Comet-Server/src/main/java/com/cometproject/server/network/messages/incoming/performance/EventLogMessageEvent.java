package com.cometproject.server.network.messages.incoming.performance;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class EventLogMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
    //
        //(_arg1:int, _arg2:String, _arg3:String, _arg4:String, _arg5:String, _arg6:Boolean, _arg7:int, _arg8:int, _arg9:int, _arg10:int, _arg11:int
        String unk1 = msg.readString();
        String unk2 = msg.readString();
        String unk3 = msg.readString();
        String unk4 = msg.readString();
        int unk5 = msg.readInt();

    }
}

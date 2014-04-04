package com.cometproject.tools.packets.instances;

import com.cometproject.tools.packets.Packet;
import javolution.util.FastList;

import java.util.List;

public class MessageEvent extends Packet {
    private List<String> structure;

    public MessageEvent(short id, String className) {
        super(id, className, PacketType.EVENT);
    }
}

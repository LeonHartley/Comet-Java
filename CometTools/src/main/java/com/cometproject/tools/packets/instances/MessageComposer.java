package com.cometproject.tools.packets.instances;

import com.cometproject.tools.packets.Packet;
import javolution.util.FastList;

import java.util.List;

public class MessageComposer extends Packet {
    private List<String> structure;

    public MessageComposer(short id, String className) {
        super(id, className, PacketType.COMPOSER);

        this.structure = new FastList<>();
    }

    public void appendStructure(String type) {
        this.structure.add(type);
    }
}

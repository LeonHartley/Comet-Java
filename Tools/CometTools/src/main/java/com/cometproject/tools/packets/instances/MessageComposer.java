package com.cometproject.tools.packets.instances;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.tools.packets.Packet;
import javolution.util.FastList;

import java.util.List;


public class MessageComposer extends Packet {
    private List<String> structure;
    private String parserName;

    public MessageComposer(short id, String className, String parserName) {
        super(id, className, PacketType.COMPOSER);

        this.structure = new FastList<>();
        this.parserName = parserName;
    }

    public void appendStructure(String type) {
        this.structure.add(type);
    }

    public void setStructure(List<String> structure) {
        this.structure = structure;
    }

    public List<String> getStructure() {
        return this.structure;
    }
}

package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class HotelViewItemMessageComposer extends MessageComposer {

    private final String campaignString;
    private final String campaignName;

    public HotelViewItemMessageComposer(String campaignString, String campaignName) {
        this.campaignString = campaignString;
        this.campaignName = campaignName;
    }

    @Override
    public short getId() {
        return Composers.CampaignMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.campaignString);
        msg.writeString(this.campaignName);
    }
}

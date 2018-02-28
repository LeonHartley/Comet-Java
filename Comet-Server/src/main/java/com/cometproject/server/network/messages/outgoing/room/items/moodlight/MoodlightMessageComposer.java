package com.cometproject.server.network.messages.outgoing.room.items.moodlight;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.data.MoodlightPresetData;
import com.cometproject.server.game.rooms.objects.items.types.wall.MoodlightWallItem;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class MoodlightMessageComposer extends MessageComposer {
    private final MoodlightWallItem moodlightWallItem;

    public MoodlightMessageComposer(final MoodlightWallItem moodlightWallItem) {
        this.moodlightWallItem = moodlightWallItem;
    }

    @Override
    public short getId() {
        return Composers.MoodlightConfigMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.moodlightWallItem.getMoodlightData().getPresets().size());
        msg.writeInt(this.moodlightWallItem.getMoodlightData().getActivePreset());

        int id = 1;

        for (MoodlightPresetData data : this.moodlightWallItem.getMoodlightData().getPresets()) {
            msg.writeInt(id++);
            msg.writeInt(data.backgroundOnly ? 2 : 1);
            msg.writeString(data.colour);
            msg.writeInt(data.intensity);
        }
    }
}

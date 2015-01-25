package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.game.players.components.types.settings.WardrobeItem;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.utilities.JsonFactory;

import java.util.List;


public class SaveWardrobeMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int slot = msg.readInt();
        String figure = msg.readString();
        String gender = msg.readString();

        List<WardrobeItem> wardrobe = client.getPlayer().getSettings().getWardrobe();

        boolean wardrobeUpdated = false;

        for (WardrobeItem item : wardrobe) {
            if (item.getSlot() == slot) {
                item.setFigure(figure);
                item.setGender(gender);

                wardrobeUpdated = true;
            }
        }

        if (!wardrobeUpdated) {
            wardrobe.add(new WardrobeItem(slot, gender, figure));
        }

        client.getPlayer().getSettings().setWardrobe(wardrobe);
        PlayerDao.saveWardrobe(JsonFactory.getInstance().toJson(wardrobe), client.getPlayer().getId());
    }
}

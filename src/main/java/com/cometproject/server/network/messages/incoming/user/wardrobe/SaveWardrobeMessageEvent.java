package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.components.types.WardrobeItem;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.google.gson.Gson;

import java.sql.PreparedStatement;
import java.util.List;

public class SaveWardrobeMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int slot = msg.readInt();
        String figure = msg.readString();
        String gender = msg.readString();

        List<WardrobeItem> wardrobe = client.getPlayer().getSettings().getWardrobe();

        boolean wardrobeUpdated = false;

        for(WardrobeItem item : wardrobe) {
            if(item.getSlot() == slot) {
                item.setFigure(figure);
                item.setGender(gender);

                wardrobeUpdated = true;
            }
        }

        if(!wardrobeUpdated) {
            wardrobe.add(new WardrobeItem(slot, gender, figure));
        }

        client.getPlayer().getSettings().setWardrobe(wardrobe);

        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE player_settings SET wardrobe = ? WHERE player_id = ?");

            statement.setString(1, new Gson().toJson(wardrobe));
            statement.setInt(2, client.getPlayer().getId());

            statement.executeUpdate();
        } catch(Exception e) {
            GameEngine.getLogger().error("Error while saving player wardrobe", e);
        }
    }
}

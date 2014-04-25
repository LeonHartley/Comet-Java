package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.WallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class DeleteRoomMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        PlayerEntity entity = client.getPlayer().getEntity();

        if(entity == null)
            return;

        Room room = entity.getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        for (FloorItem item : room.getItems().getFloorItems()) {
            room.getItems().removeItem(item, client);
        }

        for (WallItem item : room.getItems().getWallItems()) {
            room.getItems().removeItem(item, client);
        }

        for(BotEntity bot : room.getEntities().getBotEntities()) {
            InventoryBot inventoryBot = new InventoryBot(bot.getBotId(), bot.getData().getOwnerId(), bot.getData().getOwnerName(), bot.getUsername(), bot.getFigure(), bot.getGender(), bot.getMotto());
            client.getPlayer().getBots().addBot(inventoryBot);

            bot.leaveRoom();
            Comet.getServer().getStorage().execute("UPDATE bots SET room_id = 0 WHERE id = " + inventoryBot.getId());
        }

        for(PetEntity pet : room.getEntities().getPetEntities()) {
            client.getPlayer().getPets().addPet(pet.getData());

            pet.leaveRoom(false, false, false);
            Comet.getServer().getStorage().execute("UPDATE pet_data SET room_id = 0, x = 0, y = 0 WHERE id = " + pet.getData().getId());
        }

        room.dispose();

        GameEngine.getRooms().getRooms().remove(room.getId());

        if(Comet.getServer().getNetwork().getSessions().isPlayerLogged(room.getData().getOwnerId())) {
            Session owner = Comet.getServer().getNetwork().getSessions().getByPlayerId(room.getData().getOwnerId());

            if(owner.getPlayer() != null && owner.getPlayer().getRooms() != null) {
                if(owner.getPlayer().getRooms().containsKey(room.getId())) {
                    owner.getPlayer().getRooms().remove(room.getId());
                }
            }
        }

        GameEngine.getLogger().info("Room deleted: " + room.getId() + " by " + client.getPlayer().getId() + " / " + client.getPlayer().getData().getUsername());
        Comet.getServer().getStorage().execute("DELETE FROM rooms WHERE id = " + room.getId());

        client.send(UpdateInventoryMessageComposer.compose());
        client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
        client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));
    }
}

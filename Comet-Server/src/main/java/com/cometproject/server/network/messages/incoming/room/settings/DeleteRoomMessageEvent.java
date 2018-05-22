package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.handshake.HomeRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.bots.RoomBotDao;
import com.cometproject.server.storage.queries.pets.RoomPetDao;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.rooms.RoomDao;

import java.util.ArrayList;
import java.util.List;


public class DeleteRoomMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        PlayerEntity entity = client.getPlayer().getEntity();

        if (entity == null)
            return;

        Room room = entity.getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().getRank().roomFullControl())) {
            return;
        }

        final int roomId = room.getId();

        if (room.getGroup() != null) {
            client.send(new AlertMessageComposer(Locale.getOrDefault("room.delete.error.group", "You cannot delete a room with a group, please delete the group first!")));
            return;
        }

        List<RoomItem> itemsToRemove = new ArrayList<>();
        itemsToRemove.addAll(room.getItems().getFloorItems().values());
        itemsToRemove.addAll(room.getItems().getWallItems().values());

        for (RoomItem item : itemsToRemove) {
            if (item instanceof RoomItemFloor) {
                room.getItems().removeItem((RoomItemFloor) item, client);
            } else if (item instanceof RoomItemWall) {
                room.getItems().removeItem((RoomItemWall) item, client, true);
            }
        }

        for (BotEntity bot : room.getEntities().getBotEntities()) {
            client.getPlayer().getBots().addBot(bot.getData());

            RoomBotDao.setRoomId(0, bot.getData().getId());
        }

        for (PetEntity pet : room.getEntities().getPetEntities()) {
            client.getPlayer().getPets().addPet(pet.getData());

            RoomPetDao.updatePet(0, 0, 0, pet.getData().getId());
        }

        RoomManager.getInstance().forceUnload(room.getId());
        RoomManager.getInstance().removeData(room.getId());

        if (client.getPlayer().getSettings().getHomeRoom() == roomId) {
            client.send(new HomeRoomMessageComposer(client.getPlayer().getSettings().getHomeRoom(), 0));
            client.getPlayer().getSettings().setHomeRoom(0);
        }

        PlayerDao.resetHomeRoom(roomId);

        client.getLogger().debug("Room deleted: " + room.getId() + " by " + client.getPlayer().getId() + " / " + client.getPlayer().getData().getUsername());
        RoomDao.deleteRoom(room.getId());

        client.getPlayer().getRooms().remove((Integer) roomId);

        client.send(new UpdateInventoryMessageComposer());
        client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));
        client.send(new BotInventoryMessageComposer(client.getPlayer().getBots().getBots()));

        itemsToRemove.clear();
    }
}

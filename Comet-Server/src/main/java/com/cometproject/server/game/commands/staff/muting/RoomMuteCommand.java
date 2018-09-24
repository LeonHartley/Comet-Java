package com.cometproject.server.game.commands.staff.muting;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class RoomMuteCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getEntity().getRoom().hasRoomMute()) {
            for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                playerEntity.setRoomMuted(false);
            }

            for (RoomEntity entity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                if (entity.getEntityType() == RoomEntityType.PLAYER) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;
                    playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), Locale.getOrDefault("command.room.unmute", "You are now able to chat again :-)")));
                }
            }

            client.getPlayer().getEntity().getRoom().setRoomMute(false);
        } else {
            for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                playerEntity.setRoomMuted(true);
            }

            client.getPlayer().getEntity().getRoom().setRoomMute(true);

            for (RoomEntity entity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                if (entity.getEntityType() == RoomEntityType.PLAYER) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;
                    playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), Locale.getOrDefault("command.room.muted", "A staff member has muted the room.")));
                }
            }
        }

        this.logDesc = "El staff %s ha hecho roommute en la sala '%b'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName());

    }

    @Override
    public String getPermission() {
        return "roommute_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roommute.description");
    }

    @Override
    public String getLoggableDescription(){
        return this.logDesc;
    }

    @Override
    public boolean Loggable(){
        return true;
    }
}

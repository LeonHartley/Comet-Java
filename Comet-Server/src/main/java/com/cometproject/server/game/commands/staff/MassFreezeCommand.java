package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;

public class MassFreezeCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        for (RoomEntity roomEntity : client.getPlayer().getEntity().getRoom().getEntities().getAllEntities().values()) {
            if (roomEntity != client.getPlayer().getEntity() && !(roomEntity instanceof PetEntity)) {
                roomEntity.cancelWalk();
                roomEntity.setCanWalk(!roomEntity.canWalk());

                if (!roomEntity.canWalk()) {
                    if (roomEntity instanceof PlayerEntity) {
                        sendNotif(Locale.getOrDefault("command.massfreeze.frozen", "You've been frozen!"),
                                ((PlayerEntity) roomEntity).getPlayer().getSession());
                    }

                    roomEntity.applyEffect(new PlayerEffect(12));
                } else {
                    if (roomEntity instanceof PlayerEntity) {
                        sendNotif(Locale.getOrDefault("command.massfreeze.unfrozen", "You've been frozen!"),
                                ((PlayerEntity) roomEntity).getPlayer().getSession());
                    }

                    roomEntity.applyEffect(roomEntity.getLastEffect());
                }
            }
        }

        this.logDesc = "%s executed massfreeze"
                .replace("%s", client.getPlayer().getData().getUsername());
    }

    @Override
    public String getPermission() {
        return "massfreeze_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.massfreeze.description");
    }

    @Override
    public String getLoggableDescription(){
        return this.logDesc;
    }

    @Override
    public boolean isLoggable(){
        return true;
    }
}

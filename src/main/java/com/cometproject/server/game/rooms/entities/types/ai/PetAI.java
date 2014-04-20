package com.cometproject.server.game.rooms.entities.types.ai;

import com.cometproject.server.game.pets.PetCommands;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;

public class PetAI implements BotAI {
    private PetEntity entity;

    public PetAI(GenericEntity entity) {
        this.entity = (PetEntity) entity;
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        return false;
        /*String command = message.split(" ")[1];

        if(command.equals(PetCommands.FREE)) {

        } else if(command.equals(PetCommands.COME)) {

        } else if(command.equals(PetCommands.DEAD)) {

        } else if(command.equals(PetCommands.JUMP)) {

        } else if(command.equals(PetCommands.SIT)) {

        } else if(command.equals(PetCommands.LAY)) {

        } else if(command.equals(PetCommands.SLEEP)) {

        }
        return false;*/
    }

    @Override
    public void onPlayerEntityInRange(PlayerEntity entity) {

    }

    @Override
    public void onProcess() {

    }
}

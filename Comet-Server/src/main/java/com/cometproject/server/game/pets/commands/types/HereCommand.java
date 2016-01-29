package com.cometproject.server.game.pets.commands.types;

import com.cometproject.server.game.pets.commands.PetCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;

public class HereCommand extends PetCommand {
    @Override
    public boolean execute(PlayerEntity executor, PetEntity entity) {
        Position position = executor.getPosition().squareInFront(executor.getBodyRotation());

        entity.moveTo(position);

        executor.getRoom().getMapping().getTile(position.getX(), position.getY()).scheduleEvent(entity.getId(), (e) -> {
            if (e instanceof PetEntity) {
                ((PetEntity) e).getPetAI().sit();
                ((PetEntity) e).getPetAI().applyGesture("sml");

                ((PetEntity) e).getPetAI().increaseExperience(this.experienceGain());
            }


            e.lookTo(executor.getPosition().getX(), executor.getPosition().getY());
        });

        return true;
    }

    @Override
    public int getRequiredLevel() {
        return 0;
    }

    @Override
    public boolean requiresOwner() {
        return true;
    }

    @Override
    public int experienceGain() {
        return 10;
    }
}

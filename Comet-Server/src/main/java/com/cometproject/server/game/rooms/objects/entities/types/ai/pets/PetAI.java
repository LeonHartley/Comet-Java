package com.cometproject.server.game.rooms.objects.entities.types.ai.pets;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.AbstractBotAI;
import com.cometproject.server.utilities.RandomInteger;


public class PetAI extends AbstractBotAI {
    private static final PetAction[] possibleActions = {
            PetAction.LAY, PetAction.SIT, PetAction.TALK
    };

    public PetAI(GenericEntity entity) {
        super(entity);

        this.setTicksUntilCompleteInSeconds(25);
    }

    @Override
    public boolean onAddedToRoom() {
        return false;
    }

    @Override
    public void onTickComplete() {
        PetAction petAction = possibleActions[RandomInteger.getRandom(0, possibleActions.length - 1)];

        switch (petAction) {
            case TALK:
                this.say(this.getRandomMessage());
                break;

            case LAY:
                this.lay();
                break;

            case SIT:
                this.sit();
                break;

        }

        this.setTicksUntilCompleteInSeconds(25);
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        return false;
    }

    private String getRandomMessage() {
        if (this.getPetEntity().getData().getSpeech().length == 0)
            return null;

        int messageKey = RandomInteger.getRandom(0, this.getPetEntity().getData().getSpeech().length - 1);
        return this.getPetEntity().getData().getSpeech()[messageKey];
    }
}

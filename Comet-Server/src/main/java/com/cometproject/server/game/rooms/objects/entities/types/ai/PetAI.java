package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.utilities.RandomInteger;


public class PetAI extends AbstractBotAI {

    public PetAI(GenericEntity entity) {
        super(entity);
    }

    @Override
    public boolean onAddedToRoom() {

        return false;
    }

    @Override
    public void onTickComplete() {

    }

    @Override
    public void onTick() {
        super.onTick();

        // It's a pet.
        PetEntity petEntity = this.getPetEntity();

        if (petEntity.getCycleCount() == 50 && petEntity.getData().getSpeech().length > 0) { // 25 seconds
            int messageKey = RandomInteger.getRandom(0, petEntity.getData().getSpeech().length - 1);
            String message = petEntity.getData().getSpeech()[messageKey];

            if (message != null && !message.isEmpty()) {
                if (petEntity.getPosition().getX() < petEntity.getRoom().getModel().getSquareHeight().length && petEntity.getPosition().getY() < petEntity.getRoom().getModel().getSquareHeight()[petEntity.getPosition().getX()].length) {
                    final String status = "" + this.getPetEntity().getRoom().getModel().getSquareHeight()[petEntity.getPosition().getX()][petEntity.getPosition().getY()];

                    switch (message) {
                        case "sit":
                            petEntity.addStatus(RoomEntityStatus.SIT, status);
                            petEntity.markNeedsUpdate();
                            break;

                        case "lay":
                            petEntity.addStatus(RoomEntityStatus.LAY, status);
                            petEntity.markNeedsUpdate();
                            break;

                        default:
                            petEntity.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(petEntity.getId(), message, 0, 0));
                            break;
                    }
                }
            }

            petEntity.resetCycleCount();
        }

        petEntity.incrementCycleCount();
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

}

package com.cometproject.server.game.rooms.objects.entities.types.ai.pets;

import com.cometproject.server.game.pets.data.PetMessageType;
import com.cometproject.server.game.pets.data.PetSpeech;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.AbstractBotAI;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.utilities.RandomInteger;


public class PetAI extends AbstractBotAI {
    private static final PetAction[] possibleActions = {
            PetAction.LAY, PetAction.SIT, PetAction.TALK,
    };

    private String ownerName = "";

    public PetAI(GenericEntity entity) {
        super(entity);

        this.setTicksUntilCompleteInSeconds(25);
    }

    @Override
    public boolean onAddedToRoom() {
        this.say(this.getMessage(PetMessageType.WELCOME_HOME));

        int playerId = this.getPetEntity().getData().getOwnerId();
        PlayerEntity playerEntity = this.getEntity().getRoom().getEntities().getEntityByPlayerId(playerId);

        if (playerEntity != null) {
            Position position = playerEntity.getPosition().squareInFront(playerEntity.getBodyRotation());

            RoomTile tile = this.getPetEntity().getRoom().getMapping().getTile(position.getX(), position.getY());

            if (tile != null) {
                this.moveTo(position);

                tile.scheduleEvent(this.getPetEntity().getId(), (entity) -> {
                    entity.lookTo(playerEntity.getPosition().getX(), playerEntity.getPosition().getY());
                });
            }
        }

        return false;
    }

    @Override
    public void onTickComplete() {
        PetAction petAction = possibleActions[RandomInteger.getRandom(0, possibleActions.length - 1)];

        switch (petAction) {
            case TALK:
                this.say(this.getMessage(PetMessageType.GENERIC));
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

    private PetSpeech getPetSpeech() {
        return this.getPetEntity().getData().getSpeech();
    }

    private String getMessage(PetMessageType type) {
        String message = this.getPetSpeech().getMessageByType(type);

        if (message.contains("%ownerName%")) {
            if (this.ownerName.isEmpty()) {
                PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(this.getPetEntity().getData().getOwnerId(), PlayerAvatar.USERNAME_FIGURE);

                if (playerAvatar != null) {
                    this.ownerName = playerAvatar.getUsername();
                }
            }

            message = message.replace("%ownerName%", this.ownerName);
        }

        return message;
    }
}

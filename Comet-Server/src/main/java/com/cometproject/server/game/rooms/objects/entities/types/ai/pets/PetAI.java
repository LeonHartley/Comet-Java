package com.cometproject.server.game.rooms.objects.entities.types.ai.pets;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.pets.commands.PetCommandManager;
import com.cometproject.server.game.pets.data.PetMessageType;
import com.cometproject.server.game.pets.data.PetSpeech;
import com.cometproject.server.game.pets.races.PetType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.AbstractBotAI;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetFoodFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetNestFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetToyFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding.BreedingBoxFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding.types.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.pets.AddExperiencePointsMessageComposer;
import com.cometproject.server.utilities.RandomUtil;

import java.util.Arrays;
import java.util.List;


public class PetAI extends AbstractBotAI {
    public static final List<Integer> levelBoundaries = Arrays.asList(0, 100, 200, 400, 600, 900, 1300,
            1800, 2400, 3200, 4300, 5700, 7600, 10100, 13300, 17500, 23000, 30200, 39600, 51900);
    private static final PetAction[] possibleActions = {
            PetAction.LAY, PetAction.SIT, PetAction.TALK, PetAction.PLAY
    };
    private String ownerName = "";

    private int playTimer = 0;
    private int gestureTimer = 0;
    private int interactionTimer = 0;
    private int waitTimer = 0;
    private boolean nesting = false;

    private PetToyFloorItem toyItem;

    private PetFoodFloorItem foodItem;

    public PetAI(RoomEntity entity) {
        super(entity);

        this.setTicksUntilCompleteInSeconds(RandomUtil.getRandomInt(0, 50));
    }

    @Override
    public boolean onPlayerEnter(PlayerEntity entity) {
        if (this.getPetEntity().getData() == null) {
            return false;
        }

        if (entity.getPlayerId() == this.getPetEntity().getData().getOwnerId()) {
            this.onAddedToRoom();
        }

        return false;
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
        if (this.playTimer != 0 || this.getPetEntity().hasMount()) {
            return;
        }

        // check if we have enough energy, if not, send a hungry message & apply hunger gesture
        if (this.getPetEntity().getData().getHunger() >= 65) {
            // try find some food!
            this.applyGesture("hng");

            this.say(this.getMessage(PetMessageType.HUNGRY), ChatEmotion.SAD);

            // attempt to eat food.
            this.tryEat();
        } else if (this.getPetEntity().getData().getEnergy() == 0) {
            // nest!
            if (this.nesting) {
                this.say(this.getMessage(PetMessageType.SLEEPING));
            } else {
                this.tryNest();
            }
        } else {
            PetAction petAction = possibleActions[RandomUtil.getRandomInt(0, possibleActions.length - 1)];

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

                case PLAY:
                    this.play();
                    break;
            }
        }

        this.getPetEntity().getData().saveStats();

        if (RandomUtil.getRandomBool(0.5)) {
            this.getPetEntity().getData().decreaseEnergy(5);
        }

        this.getPetEntity().getData().increaseHunger(1);
        this.setTicksUntilCompleteInSeconds(25);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (this.playTimer != 0) {
            this.playTimer--;

            if (this.playTimer == 0) {
                if (this.toyItem != null) {
                    this.toyItem.onEntityStepOff(this.getPetEntity());
                }

                this.getPetEntity().removeStatus(RoomEntityStatus.PLAY);
                this.getPetEntity().markNeedsUpdate();
            }
        }

        if (this.gestureTimer != 0) {
            this.gestureTimer--;

            if (this.gestureTimer == 0) {
                this.getPetEntity().removeStatus(RoomEntityStatus.GESTURE);
                this.getPetEntity().markNeedsUpdate();
            }
        }

        if (this.interactionTimer != 0) {
            this.interactionTimer--;

            if (this.interactionTimer == 0) {
                if (this.getPetEntity().hasStatus(RoomEntityStatus.PLAY_DEAD)) {
                    this.getPetEntity().removeStatus(RoomEntityStatus.PLAY_DEAD);
                    this.getPetEntity().markNeedsUpdate();
                }
            }
        }

        if (this.waitTimer != 0) {
            this.waitTimer--;
        }
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        if (message.startsWith(this.getPetEntity().getData().getName())) {
            String commandKey = message.replace(this.getPetEntity().getData().getName() + " ", "");

            // check if we have enough energy, if not, send a hungry message & apply hunger gesture
            if (this.getPetEntity().getData().getHunger() == 100) {
                // we dont have enough energy, tell them we're hungry!
                this.applyGesture("hng");
                this.say(this.getMessage(PetMessageType.HUNGRY), ChatEmotion.SAD);

                // attempt to eat food.
                this.tryEat();
            } else if (this.getPetEntity().getData().getEnergy() < 10) {
                this.tryNest();
            } else {
                if (PetCommandManager.getInstance().executeCommand(commandKey.toLowerCase(), entity, this.getPetEntity())) {
                    final boolean decreaseEnergy = RandomUtil.getRandomInt(0, 2) == 1;// 1 in 3 chance of decreasing energy
                    final boolean increaseHunger = RandomUtil.getRandomInt(0, 2) == 1;// 1 in 3 chance of increasing hunger

                    if (decreaseEnergy) {
                        // drain energy.
                        this.getPetEntity().getData().decreaseEnergy(10);
                    }

                    if (increaseHunger) {
                        this.getPetEntity().getData().increaseHunger(10);
                    }

                    this.interactionTimer += 25;
                }
            }
        }

        return false;
    }

    public void tryNest() {
        this.waitTimer = 10;
        this.nesting = true;

        final PetNestFloorItem petNest = WiredUtil.getRandomElement(this.getPetEntity().getRoom().getItems().getByClass(PetNestFloorItem.class));

        if (petNest != null) {
            if (petNest.getTile().getEntities().size() == 0) {
                this.getPetEntity().moveTo(petNest.getPosition());
            }
        }
    }

    public void tryEat() {
        this.waitTimer = 10;

        final PetFoodFloorItem petFood = WiredUtil.getRandomElement(this.getPetEntity().getRoom().getItems().getByClass(PetFoodFloorItem.class));

        boolean decreaseHappiness = false;

        if (petFood != null) {
            if (petFood.getTile().getEntities().size() == 0) {
                this.foodItem = petFood;

                this.getPetEntity().moveTo(petFood.getPosition());
            } else {
                decreaseHappiness = true;
            }
        } else {
            decreaseHappiness = true;
        }

        if (decreaseHappiness && RandomUtil.getRandomBool(0.5)) {
            this.applyGesture("sad");
            this.getPetEntity().getData().increaseHappiness(-10);
        }
    }

    public void applyGesture(String gestureType) {
        if (!this.canGesture()) {
            return;
        }

        this.gestureTimer = 15;

        this.say(String.format("applying gesture status %s", gestureType));
        this.getPetEntity().addStatus(RoomEntityStatus.GESTURE, gestureType);

//        getPetEntity().sendUpdateMessage()

//        if (!this.getPetEntity().isWalking())
        this.getPetEntity().markNeedsUpdate();
    }

    private boolean canGesture() {
        switch (this.getPetEntity().getData().getTypeId()) {
            case PetType.DRAGON:
            case PetType.HORSE:
            case PetType.SPIDER:
            case PetType.FROG:
            case PetType.CHICKEN:
            case PetType.TURTLE:
                return true;
        }

        return false;
    }

    public void waitForInteraction() {
        this.waitTimer = 20;
    }

    public void stay() {
        this.interactionTimer = RandomUtil.getRandomInt(40, 80);
    }

    public void onScratched() {
        this.waitTimer = 0;

        PetEntity petEntity = this.getPetEntity();
        this.say(this.getMessage(PetMessageType.SCRATCHED), ChatEmotion.SMILE);

        this.getPetEntity().cancelWalk();
        this.applyGesture("sml");

        this.getPetEntity().getData().increaseHappiness(10);
        this.increaseExperience(10);

        petEntity.getData().incrementScratches();
        petEntity.getData().saveStats();
    }

    public void increaseExperience(int amount) {
        this.getPetEntity().getData().increaseExperience(amount);
        this.getEntity().getRoom().getEntities().broadcastMessage(new AddExperiencePointsMessageComposer(this.getPetEntity().getData().getId(), this.getPetEntity().getId(), amount));

        int level = 0;
        boolean levelled = false;

        for (Integer levelBoundary : levelBoundaries) {
            level++;

            if (this.getPetEntity().getData().getLevel() < level &&
                    this.getPetEntity().getData().getExperience() >= levelBoundary) {
                this.getPetEntity().getData().setLevel(level);
                levelled = true;
            }
        }

        if (levelled) {
            this.applyGesture("lvl");
        }
    }

    public void free() {
        this.interactionTimer = 0;
        this.playTimer = 0;

        this.clearPetStatuses();
        this.walkNow();

        this.getPetEntity().markNeedsUpdate();

        if (this.followingPlayer != null) {
            this.followingPlayer.getFollowingEntities().remove(this.getPetEntity());
            this.followingPlayer = null;
        }

        if (this.toyItem != null) {
            this.toyItem.onEntityStepOff(this.getPetEntity());
        }
    }

    private void clearPetStatuses() {
        if (this.getPetEntity().hasStatus(RoomEntityStatus.LAY)) {
            this.getPetEntity().removeStatus(RoomEntityStatus.LAY);
        }

        if (this.getPetEntity().hasStatus(RoomEntityStatus.SIT)) {
            this.getPetEntity().removeStatus(RoomEntityStatus.SIT);
        }

        if (this.getPetEntity().hasStatus(RoomEntityStatus.PLAY_DEAD)) {
            this.getPetEntity().removeStatus(RoomEntityStatus.PLAY_DEAD);
        }

        if (this.getPetEntity().hasStatus(RoomEntityStatus.PLAY)) {
            this.getPetEntity().removeStatus(RoomEntityStatus.PLAY);
        }

        if (this.getPetEntity().hasStatus(RoomEntityStatus.EAT)) {
            this.getPetEntity().removeStatus(RoomEntityStatus.EAT);
        }
    }

    public void play() {
        // Find a random toy item
        final PetToyFloorItem floorItem = WiredUtil.getRandomElement(this.getPetEntity().getRoom().getItems().getByClass(PetToyFloorItem.class));

        if (floorItem != null) {
            this.toyItem = floorItem;

            // 1 min play timer.
            this.playTimer = RandomUtil.getRandomInt(10, 50);

            this.moveTo(floorItem.getPosition());
        }
    }

    public void playDead() {
        this.getPetEntity().cancelWalk();

        this.clearPetStatuses();

        this.getPetEntity().addStatus(RoomEntityStatus.PLAY_DEAD, "");
        this.getPetEntity().markNeedsUpdate();
    }

    private PetSpeech getPetSpeech() {
        final PetSpeech petSpeech = PetManager.getInstance().getSpeech(this.getPetEntity().getData().getTypeId());

        if (petSpeech == null) {
            return PetManager.getInstance().getSpeech(-1);
        }

        return petSpeech;
    }

    private String getMessage(PetMessageType type) {
        if (this.getPetSpeech() == null) {
            return null;
        }

        String message = this.getPetSpeech().getMessageByType(type);

        if (message == null) {
            return null;
        }

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

    @Override
    public boolean canMove() {
        return this.waitTimer == 0 && this.playTimer == 0 && this.interactionTimer == 0 & this.getPetEntity().getMountedEntity() == null;
    }

    public void setFollowingPlayer(PlayerEntity followingPlayer) {
        if (followingPlayer == null && this.followingPlayer != null) {
            this.followingPlayer.getFollowingEntities().remove(this.getPetEntity());
        }

        this.followingPlayer = followingPlayer;
    }

    public void beginBreeding() {
        final BreedingBoxFloorItem breedingBox = this.findBreedingBox();

        if (breedingBox == null) {
            return;
        }

        this.waitTimer = 120;
        // if we've got one, move to it.
        this.getPetEntity().moveTo(breedingBox.getPosition());
    }

    public void beginNesting() {
        this.waitTimer = 62;
        this.lay();
    }

    public void beginEating() {
        this.getPetEntity().addStatus(RoomEntityStatus.EAT, "1");
        this.getPetEntity().markNeedsUpdate();

        this.waitTimer = 240;
    }

    public void breedComplete() {
        this.waitTimer = 0;

        this.applyGesture("sml");

        this.getPetEntity().getData().decreaseEnergy(40);
        this.getPetEntity().getData().increaseHunger(40);

        this.free();
        this.increaseExperience(10);
    }

    private BreedingBoxFloorItem findBreedingBox() {
        List<? extends BreedingBoxFloorItem> availableBoxes = null;

        switch (this.getPetEntity().getData().getTypeId()) {
            case PetType.DOG: {
                availableBoxes = this.getEntity().getRoom().getItems().getByClass(DogBreedingBoxFloorItem.class);
                break;
            }

            case PetType.CAT: {
                availableBoxes = this.getEntity().getRoom().getItems().getByClass(CatBreedingBoxFloorItem.class);
                break;
            }

            case PetType.BEAR: {
                availableBoxes = this.getEntity().getRoom().getItems().getByClass(BearBreedingBoxFloorItem.class);
                break;
            }

            case PetType.TERRIER: {
                availableBoxes = this.getEntity().getRoom().getItems().getByClass(TerrierBreedingBoxFloorItem.class);
                break;
            }

            case PetType.PIG: {
                availableBoxes = this.getEntity().getRoom().getItems().getByClass(PigBreedingBoxFloorItem.class);
                break;
            }
        }

        if (availableBoxes == null) {
            return null;
        }

        for (BreedingBoxFloorItem box : availableBoxes) {
            if (box.getTile().getEntities().size() <= 1) { // make sure there's either 1 or 0
                return box;
            }
        }

        return null;
    }

    public void eatingComplete(boolean awardHappiness) {
        this.waitTimer = 0;

        this.getPetEntity().getData().increaseHappiness(10);
        this.applyGesture("sml");

        this.free();

        this.getPetEntity().getData().saveStats();
        this.getPetEntity().removeStatus(RoomEntityStatus.EAT);
        this.getPetEntity().markNeedsUpdate();
    }

    public void eatingComplete() {
        this.eatingComplete(true);
    }

    public void nestingComplete() {
        this.nesting = false;
        this.waitTimer = 0;

        this.getPetEntity().getData().increaseEnergy(100);
        this.getPetEntity().getData().increaseHappiness(100);
    }
}

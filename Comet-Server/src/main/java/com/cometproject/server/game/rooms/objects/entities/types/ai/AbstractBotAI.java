package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.utilities.RandomInteger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractBotAI implements BotAI {
    private static final ExecutorService botPathCalculator = Executors.newFixedThreadPool(2);

    private RoomEntity entity;

    private long ticksUntilComplete = 0;
    private boolean walkNow = false;

    protected PlayerEntity followingPlayer;

    public AbstractBotAI(RoomEntity entity) {
        this.entity = entity;
    }

    @Override
    public void onTick() {
        if (this.ticksUntilComplete != 0) {
            this.ticksUntilComplete--;

            if (this.ticksUntilComplete == 0) {
                this.onTickComplete();
            }
        }

        int chance = RandomInteger.getRandom(1, (this.getEntity().hasStatus(RoomEntityStatus.SIT) || this.getEntity().hasStatus(RoomEntityStatus.LAY)) ? 50 : 20);

        if (!this.getEntity().hasMount()) {
            boolean newStep = true;

            if (this.getEntity() instanceof BotEntity) {
                BotEntity botEntity = ((BotEntity) this.getEntity());

                if(botEntity.getData() == null) {
                    return;
                }

                if (botEntity.getData().getMode().equals("relaxed")) {
                    newStep = false;
                }
            }

            if ((chance < 3 || this.walkNow) && newStep) {
                if(this.walkNow) {
                    this.walkNow = false;
                }

                if (!this.getEntity().isWalking() && this.canMove() && this.followingPlayer == null) {
                    botPathCalculator.submit(() -> {
                        RoomTile reachableTile = this.getEntity().getRoom().getMapping().getRandomReachableTile(this.getEntity());

                        if (reachableTile != null) {
                            this.getEntity().moveTo(reachableTile.getPosition().getX(), reachableTile.getPosition().getY());
                        }
                    });
                }
            }
        }

        if (this.getEntity() instanceof BotEntity) {
            try {
                if (((BotEntity) this.getEntity()).getCycleCount() == ((BotEntity) this.getEntity()).getData().getChatDelay() * 2) {
                    String message = ((BotEntity) this.getEntity()).getData().getRandomMessage();

                    if (message != null && !message.isEmpty()) {
                        ((BotEntity) this.getEntity()).say(message);
                    }

                    ((BotEntity) this.getEntity()).resetCycleCount();
                }

                ((BotEntity) this.getEntity()).incrementCycleCount();
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void onTickComplete() {

    }

    public void walkNow() {
        this.walkNow = true;
    }

    @Override
    public void setTicksUntilCompleteInSeconds(double seconds) {
        long realTime = Math.round(seconds * 1000 / 500);

        if (realTime < 1) {
            realTime = 1; //0.5s
        }

        this.ticksUntilComplete = realTime;
    }

    @Override
    public void say(String message) {
        this.say(message, ChatEmotion.NONE);
    }

    @Override
    public void say(String message, ChatEmotion emotion) {
        if (message == null) {
            return;
        }

        this.getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.getEntity().getId(), message, emotion, 0));
    }

    protected void moveTo(Position position) {
        this.getEntity().moveTo(position.getX(), position.getY());
    }

    public void sit() {
        this.getEntity().addStatus(RoomEntityStatus.SIT, "" + this.getPetEntity().getRoom().getModel().getSquareHeight()[this.getEntity().getPosition().getX()][this.getEntity().getPosition().getY()]);
        this.getEntity().markNeedsUpdate();
    }

    public void lay() {
        this.getEntity().addStatus(RoomEntityStatus.LAY, "" + this.getPetEntity().getRoom().getModel().getSquareHeight()[this.getEntity().getPosition().getX()][this.getEntity().getPosition().getY()]);
        this.getEntity().markNeedsUpdate();
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        return false;
    }

    @Override
    public boolean onPlayerLeave(PlayerEntity entity) {
        return false;
    }

    @Override
    public boolean onPlayerEnter(PlayerEntity entity) {
        return false;
    }

    @Override
    public boolean onAddedToRoom() {
        return false;
    }

    @Override
    public boolean onRemovedFromRoom() {
        if(this.followingPlayer != null) {
            this.followingPlayer.getFollowingEntities().remove(this);
            this.followingPlayer = null;
        }

        return false;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    public RoomEntity getEntity() {
        return entity;
    }

    public BotEntity getBotEntity() {
        return (BotEntity) entity;
    }

    public PetEntity getPetEntity() {
        return (PetEntity) entity;
    }
}

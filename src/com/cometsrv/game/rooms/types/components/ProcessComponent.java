package com.cometsrv.game.rooms.types.components;

import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.avatars.misc.Position;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometsrv.utilities.TimeSpan;
import javolution.util.FastList;
import org.apache.log4j.Logger;

public class ProcessComponent implements Runnable {
    private Room room;

    private Logger log;
    private Thread processThread;
    private boolean active = false;

    public ProcessComponent(Room room) {
        this.room = room;
        this.log = Logger.getLogger("Room Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if(this.processThread != null && this.active) {
            stop();
        }

        this.active = true;
        this.processThread = new Thread(this);
        processThread.start();

        log.debug("Processing started");
    }

    public void stop() {
        if(this.processThread != null) {
            this.processThread.interrupt();
            this.active = false;

            log.debug("Processing stopped");
        }
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void run() {
        while(this.active) {
            try {
                if(!this.active) {
                    break;
                }

                if(this.processThread.isInterrupted()) {
                    return;
                }

                if(this.getRoom().getAvatars().getAvatars().size() == 0) {
                    this.getRoom().dispose();
                }

                long timeStart = System.currentTimeMillis();

                FastList<Avatar> usersToUpdate = new FastList<>();

                synchronized (this.getRoom().getAvatars().getAvatars().values()) {
                    for(Avatar avatar : this.getRoom().getAvatars().getAvatars().values()) {
                        if(avatar.getPlayer() == null) {
                            this.getRoom().getAvatars().remove(avatar);
                            continue;
                        }

                        if(!avatar.inRoom || avatar.getRoom() == null) {
                            this.getRoom().getAvatars().remove(avatar);
                        }

                        if(avatar.getPlayer().floodTime >= 0.5) {
                            avatar.getPlayer().floodTime -= 0.5;
                        }

                        if(avatar.isMoving && avatar.getPath().size() > 0) {
                            handleWalk(avatar);
                        } else if(avatar.isMoving) {
                            if(avatar.hasStatus("mv")) {
                                avatar.removeStatus("mv");
                            }

                            avatar.needsUpdate = true;
                            avatar.isMoving = false;
                        }

                        if(avatar.hasStatus("sign")) {
                            if(avatar.signTime != 0) {
                                avatar.signTime--;
                            } else {
                                avatar.removeStatus("sign");
                                avatar.needsUpdate = true;
                            }
                        }

                        avatar.idleTime++;

                        if(!avatar.isIdle) {
                            if(avatar.idleTime >= 600) {
                                avatar.isIdle = true;

                                this.getRoom().getAvatars().broadcast(IdleStatusMessageComposer.compose(avatar.getPlayer().getId(), true));
                            }
                        }

                        if(avatar.idleTime >= 2400) {
                            avatar.dispose(false, true, true);
                            continue;
                        }

                        if(avatar.needsUpdate) {
                            if(avatar.hasStatus("mv") && !avatar.isMoving) {
                                avatar.removeStatus("mv");
                            }

                            for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(avatar.getPosition().getX(), avatar.getPosition().getY())) {
                                if(item.getDefinition().canSit) {
                                    avatar.isSitting = true;
                                    avatar.setBodyRotation(item.getRotation());
                                    avatar.setHeadRotation(item.getRotation());
                                    avatar.getStatuses().put("sit", Double.toString(1.0));
                                }
                            }

                            usersToUpdate.add(avatar);
                            avatar.needsUpdate = false;
                        }
                    }
                }

                if(usersToUpdate.size() != 0) {
                    this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(usersToUpdate));
                    usersToUpdate.clear();
                }

                TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

                if(span.toMilliseconds() > 100) {
                    log.debug("Process took: " + span.toMilliseconds() + "ms to execute.");
                }

                Thread.sleep(500);
            } catch(Exception e) {
                if(e instanceof InterruptedException) {
                    return;
                }

                log.error("Error while processing room", e);
                this.getRoom().dispose();
            }
        }
    }

    private void handleWalk(Avatar avatar) {
        Square next = (avatar.getIsTeleporting()) ? new Square(avatar.getGoalX(), avatar.getGoalY()) : avatar.getPath().poll();

        if(avatar.getIsTeleporting()) {
            avatar.getPath().clear();
        }

        if(next.x == room.getModel().getDoorX() && next.y == room.getModel().getDoorY()) {
            avatar.dispose(false, false, true);
            this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(avatar));
            return;
        }

        double height = avatar.getRoom().getModel().getSquareHeight()[avatar.getPosition().getX()][avatar.getPosition().getY()];

        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(avatar.getPosition().getX(), avatar.getPosition().getY())) {
            item.setNeedsUpdate(true, InteractionAction.ON_WALK, avatar, 0);
            //height += item.getHeight();
        }

        if(avatar.hasStatus("mv")) {
            avatar.removeStatus("mv");
        }

        if(avatar.hasStatus("sit")) {
            avatar.removeStatus("sit");
        }

        if(avatar.hasStatus("lay")) {
            avatar.removeStatus("lay");
        }

        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(next.x, next.y)) {
            if(item.getDefinition().getInteraction().equals("gate") && item.getExtraData().equals("0")) {
                avatar.getPath().clear();

                avatar.needsUpdate = true;
                avatar.isMoving = false;
                return;
            }
        }

        int rotation = Position.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), next.x, next.y, avatar.isMoonwalking);
        avatar.setBodyRotation(rotation);
        avatar.setHeadRotation(rotation);

        avatar.getStatuses().put("mv", String.valueOf(next.x).concat(",").concat(String.valueOf(next.y)).concat(",").concat(String.valueOf(height)));

        this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(avatar));

        avatar.getPosition().setX(next.x);
        avatar.getPosition().setY(next.y);

        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(avatar.getPosition().getX(), avatar.getPosition().getY())) {
            item.setNeedsUpdate(true, InteractionAction.ON_WALK, avatar, 1);
            height += Position.calculateHeight(item);
        }

        avatar.getPosition().setZ(height);

    }

    public void dispose() {
        this.processThread = null;
    }

    public Room getRoom() {
        return this.room;
    }
}

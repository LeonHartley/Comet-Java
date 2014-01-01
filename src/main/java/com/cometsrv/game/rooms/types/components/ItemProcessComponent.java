package com.cometsrv.game.rooms.types.components;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.items.interactions.InteractionState;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.tasks.CometTask;
import com.cometsrv.tasks.CometThreadManagement;
import com.cometsrv.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ItemProcessComponent implements CometTask {
    private Room room;

    private Logger log;
    private ScheduledFuture myFuture;
    private CometThreadManagement mgr;
    private boolean active = false;
    private int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.interval"));
    private int flag = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.flag"));

    public ItemProcessComponent(CometThreadManagement mgr, Room room) {
        this.mgr = mgr;
        this.room = room;

        this.myFuture = this.mgr.executePeriodic(this, interval, interval, TimeUnit.MILLISECONDS);

        log = Logger.getLogger("Item Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if(this.myFuture != null && this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = this.mgr.executePeriodic(this, interval, interval, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if(this.myFuture != null) {
            this.active = false;
            this.myFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void run() {
        try {
            if(!this.active) {
                return;
            }

            if(this.getRoom().getAvatars().getAvatars().size() == 0) {
                this.stop();
            }

            long timeStart = System.currentTimeMillis();

            for(FloorItem item : this.getRoom().getItems().getFloorItems()) {
                if(item.needsUpdate()) {
                    InteractionState state = InteractionState.FINISHED;

                    if(item.getUpdateType() == InteractionAction.ON_WALK) {
                        state = GameEngine.getItems().getInteractions().onWalk(item.getUpdateState() == 1, item, item.getUpdateAvatar());
                    } else if(item.getUpdateType() == InteractionAction.ON_USE) {
                        state = GameEngine.getItems().getInteractions().onInteract(item.getUpdateState(), item, item.getUpdateAvatar());
                    } else if (item.getUpdateType() == InteractionAction.ON_PLACED) {
                        state = GameEngine.getItems().getInteractions().onPlace(item, item.getUpdateAvatar());
                    } else if (item.getUpdateType() == InteractionAction.ON_PICKUP) {
                        state = GameEngine.getItems().getInteractions().onPickup(item, item.getUpdateAvatar());
                    } else if (item.getUpdateType() == InteractionAction.ON_TICK) {
                        state = GameEngine.getItems().getInteractions().onTick(item, item.getUpdateAvatar());
                    }

                    if (state == InteractionState.FINISHED) {
                        item.setNeedsUpdate(false);
                    }
                }

                if(GameEngine.getWired().isWiredTrigger(item)) {
                    if(!this.getRoom().getWired().isWiredSquare(item.getX(), item.getY())) {
                        this.getRoom().getWired().add(item.getX(), item.getY());
                    }
                }
            }

            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if(span.toMilliseconds() > flag) {
                log.warn("Item process took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch(Exception e) {
            if(e instanceof InterruptedException) {
                return;
            }

            log.error("Error while processing items", e);
        }
    }

    public void dispose() {
        if (this.myFuture != null) { this.myFuture.cancel(false); }
        this.myFuture = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
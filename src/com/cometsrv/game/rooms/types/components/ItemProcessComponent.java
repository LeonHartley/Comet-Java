package com.cometsrv.game.rooms.types.components;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.utilities.TimeSpan;
import org.apache.log4j.Logger;

public class ItemProcessComponent implements Runnable{
    private Room room;

    private Logger log;
    private Thread processThread;
    private boolean active = false;
    private int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.interval"));
    private int flag = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.flag"));

    public ItemProcessComponent(Room room) {
        this.room = room;

        log = Logger.getLogger("Item Process [" + room.getData().getName() + "]");
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
                    this.stop();
                }

                long timeStart = System.currentTimeMillis();

                for(FloorItem item : this.getRoom().getItems().getFloorItems()) {
                    if(item.needsUpdate()) {
                        if(item.getUpdateType() == InteractionAction.ON_WALK) {
                            GameEngine.getItems().getInteractions().onWalk(item.getUpdateState() == 1, item, item.getUpdateAvatar());

                        } else if(item.getUpdateType() == InteractionAction.ON_USE) {
                            GameEngine.getItems().getInteractions().onInteract(item.getUpdateState(), item, item.getUpdateAvatar());
                        }

                        item.setNeedsUpdate(false);
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

                Thread.sleep(this.interval);
            } catch(Exception e) {
                if(e instanceof InterruptedException) {
                    return;
                }

                log.error("Error while processing items", e);
            }
        }
    }

    public void dispose() {
        this.processThread = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
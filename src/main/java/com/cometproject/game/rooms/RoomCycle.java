package com.cometproject.game.rooms;

import com.cometproject.game.GameEngine;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.tasks.CometTask;
import com.cometproject.tasks.CometThreadManagement;
import com.cometproject.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomCycle implements CometTask {
    private ScheduledFuture myFuture;
    //private int cycleCount;
    private boolean active;
    private Logger log = Logger.getLogger(RoomCycle.class.getName());

    public RoomCycle(CometThreadManagement mgr) {
        this.myFuture = mgr.executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
        active = true;
    }

    @Override
    public void run() {
        int flag = 100;

        try {
            if(!this.isActive()) {
                return;
            }

            long start = System.currentTimeMillis();
            if(GameEngine.getRooms() == null) {
                // we've tried to cycle through the rooms but we haven't finished setting the environment up yet!
                return;
            }


            synchronized(GameEngine.getRooms().getActiveRooms()) {
                for(Room room : GameEngine.getRooms().getActiveRooms()) {
                    if(room == null) continue;

                    room.tick();
                }
            }

            TimeSpan span = new TimeSpan(start, System.currentTimeMillis());

            if(span.toMilliseconds() > flag) {
                log.warn("Global room processing took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch(Exception e) {
            log.error("Error while cycling rooms", e);
        }
    }

    public void stop() {
        this.setActive(false);
        this.myFuture.cancel(false);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

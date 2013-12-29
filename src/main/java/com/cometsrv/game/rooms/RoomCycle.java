package com.cometsrv.game.rooms;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.utilities.TimeSpan;
import org.apache.log4j.Logger;

public class RoomCycle implements Runnable {
    private Thread cycleThread;
    private int cycleCount;
    private boolean active;
    private Logger log = Logger.getLogger(RoomCycle.class.getName());

    public RoomCycle() {
        cycleThread = new Thread(this);
        active = true;
        cycleThread.start();
    }

    @Override
    public void run() {
        int flag = 100;
        int interval = 500;

        while(this.active) {
            try {
                if(this.cycleThread.isInterrupted()) {
                    break;
                }

                long start = System.currentTimeMillis();
                if(GameEngine.getRooms() == null) {
                    // we've tried to cycle through the rooms but we haven't finished setting the environment up yet!
                    continue;
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

                Thread.sleep(interval);
            } catch(Exception e) {
                log.error("Error while cycling rooms", e);
            }
        }
    }

    public void stop() {
        this.setActive(false);
        this.cycleThread.interrupt();
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

package com.cometproject.server.game.rooms;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomCycle implements CometTask {
    private Logger log = Logger.getLogger(RoomCycle.class.getName());

    private final static int PERIOD = 500;

    private final CometThreadManagement threadManagement;
    private ScheduledFuture myFuture;

    public RoomCycle(CometThreadManagement mgr) {
        this.threadManagement = mgr;
    }

    public void start() {
        this.myFuture = this.threadManagement.executePeriodic(this, PERIOD, PERIOD, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        this.myFuture.cancel(false);
    }

    public boolean isActive() {
        return (!this.myFuture.isCancelled());
    }

    @Override
    public void run() {
        int flag = 450;

        try {
            long start = System.currentTimeMillis();

            // run this before ticking
            CometManager.getRooms().unloadIdleRooms();

            for (Room room : CometManager.getRooms().getRoomInstances().values()) {
                try {
                    room.tick();
                } catch (Exception e) {
                    log.error("Error while cycling room: " + room.getData().getId() + ", " + room.getData().getName(), e);
                }
            }

            TimeSpan span = new TimeSpan(start, System.currentTimeMillis());

            if (span.toMilliseconds() > flag) {
                log.warn("Global room processing took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch (Exception e) {
            log.error("Error while cycling rooms", e);
        }
    }
}

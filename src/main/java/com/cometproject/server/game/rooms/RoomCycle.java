package com.cometproject.server.game.rooms;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomCycle implements CometTask {
    private ScheduledFuture myFuture;
    private boolean active;
    private Logger log = Logger.getLogger(RoomCycle.class.getName());

    private List<Integer> roomsToUnload;

    public RoomCycle(CometThreadManagement mgr) {
        this.myFuture = mgr.executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
        active = true;

        this.roomsToUnload = new ArrayList<>();
    }

    @Override
    public void run() {
        int flag = 450;

        try {
            if (!this.isActive()) {
                return;
            }

            long start = System.currentTimeMillis();

            if (CometManager.getRooms() == null) {
                // we've tried to cycle through the rooms but we haven't finished setting the environment up yet!
                return;
            }

            for (Room room : CometManager.getRooms().getActiveRooms()) {
                if (room == null) continue;

                try {
                    if (this.roomsToUnload.contains(room.getId())) {
                        room.dispose();
                        continue;
                    }

                    room.tick();
                } catch (Exception e) {
                    log.error("Error while cycling room: " + room.getData().getId() + ", " + room.getData().getName(), e);
                }
            }

            this.roomsToUnload.clear();

            TimeSpan span = new TimeSpan(start, System.currentTimeMillis());

            if (span.toMilliseconds() > flag) {
                log.warn("Global room processing took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch (Exception e) {
            log.error("Error while cycling rooms", e);
        }
    }

    public void stop() {
        this.setActive(false);
        this.myFuture.cancel(false);
    }

    public void requestUnload(int roomId) {
        if (this.roomsToUnload.contains(roomId))
            return;

        this.roomsToUnload.add(roomId);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

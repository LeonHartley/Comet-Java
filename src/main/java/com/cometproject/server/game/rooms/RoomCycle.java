package com.cometproject.server.game.rooms;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.TimeSpan;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class RoomCycle implements CometTask {
    private Logger log = Logger.getLogger(RoomCycle.class.getName());

    private final static int PERIOD = 500;

    private ScheduledFuture myFuture;

    public RoomCycle() {
    }

    public void start() {
        this.myFuture = CometThreadManager.getInstance().executePeriodic(this, PERIOD, PERIOD, TimeUnit.MILLISECONDS);
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
            RoomManager.getInstance().unloadIdleRooms();

            for (Room room : RoomManager.getInstance().getRoomInstances().values()) {
                try {
                    room.tick();
                } catch (Exception e) {
                    log.error("Error while cycling room: " + room.getData().getId() + ", " + room.getData().getName(), e);
                }
            }

            List<Integer> expiredPromotedRooms = Lists.newArrayList();

            for(RoomPromotion roomPromotion : RoomManager.getInstance().getRoomPromotions().values()) {
                if(roomPromotion.isExpired()) {
                    expiredPromotedRooms.add(roomPromotion.getRoomId());
                }
            }

            if(expiredPromotedRooms.size() != 0) {
                for (int roomId : expiredPromotedRooms) {
                    RoomManager.getInstance().getRoomPromotions().remove(roomId);
                }

                expiredPromotedRooms.clear();
            }

            TimeSpan span = new TimeSpan(start, System.currentTimeMillis());

            if (span.toMilliseconds() > flag) {
                log.warn("Global room processing (" + RoomManager.getInstance().getRoomInstances().size() + " rooms) took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch (Exception e) {
            log.error("Error while cycling rooms", e);
        }
    }
}

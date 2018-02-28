package com.cometproject.server.game.items.types;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class LowPriorityItemProcessor implements CometTask {
    private static final int processTime = 25;
    private static LowPriorityItemProcessor instance;
    private final Logger log = Logger.getLogger(LowPriorityItemProcessor.class);
    private List<RoomItemFloor> itemsToProcess;

    public LowPriorityItemProcessor() {
        this.itemsToProcess = new CopyOnWriteArrayList<>();

        CometThreadManager.getInstance().executePeriodic(this, processTime, processTime, TimeUnit.MILLISECONDS);
    }

    public static LowPriorityItemProcessor getInstance() {
        if (instance == null) {
            instance = new LowPriorityItemProcessor();
        }

        return instance;
    }

    public static int getProcessTime(double time) {
        long realTime = Math.round(time * 1000 / processTime);

        if (realTime < 1) {
            realTime = 1;
        }

        return (int) realTime;
    }

    @Override
    public void run() {
        List<RoomItemFloor> itemsToRemove = new ArrayList<>();

        for (RoomItemFloor roomItemFloor : itemsToProcess) {
            try {
                if (roomItemFloor.requiresTick()) {
                    roomItemFloor.tick();
                } else {
                    itemsToRemove.add(roomItemFloor);
                }
            } catch (Exception e) {
                log.error("Error while processing item " + roomItemFloor.getId() + " / " + roomItemFloor.getClass().getSimpleName(), e);
            }
        }

        this.itemsToProcess.removeAll(itemsToRemove);

        itemsToRemove.clear();
    }

    public void submit(RoomItemFloor floorItem) {
        this.itemsToProcess.add(floorItem);
    }
}

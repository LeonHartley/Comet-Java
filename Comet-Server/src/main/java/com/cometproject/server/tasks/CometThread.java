package com.cometproject.server.tasks;

public class CometThread extends Thread {

    public CometThread(CometTask task) {
        super(task, "Comet Task");
    }

    public CometThread(CometTask task, String identifier) {
        super(task, "Comet Task [" + identifier + "]");
    }

    @Override
    public void start() {
        if (this.isRunning()) {
            return;
        }

        super.start();
    }

    public boolean isRunning() {
        return super.isAlive();
    }
}

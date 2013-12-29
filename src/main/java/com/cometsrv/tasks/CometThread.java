package com.cometsrv.tasks;

public class CometThread extends Thread {

    public CometThread(CometTask task) {
        super(task, "Comet Task");
    }

    public CometThread(CometTask task, String identifier) {
        super(task, "Comet Task [" + name + "]");
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

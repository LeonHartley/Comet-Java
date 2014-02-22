package com.cometproject.server.tasks;

public interface CometTask extends Runnable {

    @Override
    public abstract void run();
}

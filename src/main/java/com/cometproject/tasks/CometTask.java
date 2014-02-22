package com.cometproject.tasks;

public interface CometTask extends Runnable {

    @Override
    public abstract void run();
}

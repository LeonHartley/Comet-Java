package com.cometproject.logger.tasks;

public interface CometTask extends Runnable {

    @Override
    public abstract void run();
}

package com.cometsrv.tasks;

public interface CometTask extends Runnable {

    @Override
    public abstract void run();
}

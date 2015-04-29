package com.cometproject.logger.tasks;

import com.cometproject.api.networking.messages.IMessageComposer;
public interface CometTask extends Runnable {

    @Override
    public abstract void run();
}

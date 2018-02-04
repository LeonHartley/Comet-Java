package com.cometproject.networking.api.messages;

import com.cometproject.api.networking.messages.IMessageEvent;

public abstract class MessageParser {
    private boolean hasError = false;

    public boolean hasError() {
        return this.hasError;
    }

    public void setHasError(boolean error) {
        this.hasError = error;
    }

    public abstract void parse(IMessageEvent event);

    public void flush() {
        // Override if we need to dispose anything we previously allocated
    }
}

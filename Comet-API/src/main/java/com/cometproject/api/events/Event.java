package com.cometproject.api.events;

public abstract class Event {
    private boolean async;
    private boolean cancelled;

    public Event() {
        this.async = true;
        this.cancelled = false;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean isAsynchronous) {
        this.async = isAsynchronous;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

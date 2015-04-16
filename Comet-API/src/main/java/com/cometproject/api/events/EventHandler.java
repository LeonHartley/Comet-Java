package com.cometproject.api.events;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHandler {
    private final ExecutorService asyncEventExecutor;

    private final Map<Class<?>, List<EventListener>> listeners;

    public EventHandler() {
        this.asyncEventExecutor = Executors.newCachedThreadPool();
        this.listeners = Maps.newConcurrentMap();
    }

    public boolean addListener(EventListener listener) {
        for(Method method : listener.getClass().getMethods()) {
            System.out.println(method.getName());
        }

        return true;
    }

    public void handleEvent(Event event) {
        if(this.listeners.containsKey(event.getClass())) {
            if(event.isAsync()) {
                this.asyncEventExecutor.submit(() -> System.out.format("Async event handled: %s\n", event.getClass().getSimpleName()));
            } else {
                System.out.format("Event handled: %s\n", event.getClass().getSimpleName());
            }
        } else {
            System.out.format("Unhandled event: %s\n", event.getClass().getSimpleName());
        }
    }
}

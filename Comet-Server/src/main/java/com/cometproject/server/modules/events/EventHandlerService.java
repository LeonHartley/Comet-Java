package com.cometproject.server.modules.events;

import com.cometproject.api.events.Event;
import com.cometproject.api.events.EventHandler;
import com.cometproject.api.events.EventListener;
import com.cometproject.api.events.EventListenerContainer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHandlerService implements EventHandler {
    private final ExecutorService asyncEventExecutor;
    private final Logger log = Logger.getLogger(EventHandlerService.class);

    private final Map<Class<?>, List<Method>> listeners;

    public EventHandlerService() {
        this.asyncEventExecutor = Executors.newCachedThreadPool();
        this.listeners = Maps.newConcurrentMap();
    }

    public boolean addListenerContainer(EventListenerContainer listener) {
        for (Method method : listener.getClass().getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof EventListener) {
                    EventListener eventListener = (((EventListener) annotation));

                    if (this.listeners.containsKey(eventListener.event())) {
                        this.listeners.get(eventListener.event()).add(method);
                    } else {
                        List<Method> methods = Lists.newArrayList(method);

                        this.listeners.put(eventListener.event(), methods);
                    }

                    log.debug(String.format("Registered event listener for %s: %s", eventListener.event().getSimpleName(), listener.getClass().getName()));
                }
            }
        }

        return true;
    }

    public void handleEvent(Event event) {
        if (this.listeners.containsKey(event.getClass())) {
            if (event.isAsync()) {
                this.asyncEventExecutor.submit(() -> System.out.format("Async event handled: %s\n", event.getClass().getSimpleName()));
            } else {
                System.out.format("Event handled: %s\n", event.getClass().getSimpleName());
            }
        } else {
            System.out.format("Unhandled event: %s\n", event.getClass().getSimpleName());
        }
    }
}

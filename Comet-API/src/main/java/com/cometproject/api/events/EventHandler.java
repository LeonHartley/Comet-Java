package com.cometproject.api.events;

public interface EventHandler {
   boolean addListenerContainer(EventListenerContainer listener);

   void handleEvent(Event event);
}

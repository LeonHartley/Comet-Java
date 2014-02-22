package com.cometproject.network.messages.incoming;

import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public interface IEvent {
	public void handle(Session client, Event msg);
}

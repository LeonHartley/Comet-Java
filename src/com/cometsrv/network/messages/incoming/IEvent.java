package com.cometsrv.network.messages.incoming;

import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public interface IEvent {
	public void handle(Session client, Event msg);
}

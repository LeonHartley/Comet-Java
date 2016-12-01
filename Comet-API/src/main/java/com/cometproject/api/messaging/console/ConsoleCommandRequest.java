package com.cometproject.api.messaging.console;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.UUID;

public class ConsoleCommandRequest extends MessageRequest<ConsoleCommandResponse> {
    private String command;

    public ConsoleCommandRequest(String command) {
        super(UUID.randomUUID(), ConsoleCommandResponse.class);

        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}

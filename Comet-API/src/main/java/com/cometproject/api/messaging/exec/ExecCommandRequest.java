package com.cometproject.api.messaging.exec;

import com.cometproject.api.messaging.console.ConsoleCommandResponse;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.UUID;

public class ExecCommandRequest extends MessageRequest<ExecCommandResponse> {
    private String command;

    public ExecCommandRequest(String command) {
        super(UUID.randomUUID(), ExecCommandResponse.class);

        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}

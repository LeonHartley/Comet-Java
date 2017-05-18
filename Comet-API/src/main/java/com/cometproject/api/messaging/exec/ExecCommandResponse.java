package com.cometproject.api.messaging.exec;

import io.coerce.services.messaging.client.messages.response.MessageResponse;

public class ExecCommandResponse implements MessageResponse {

    private final String output;

    public ExecCommandResponse(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }
}

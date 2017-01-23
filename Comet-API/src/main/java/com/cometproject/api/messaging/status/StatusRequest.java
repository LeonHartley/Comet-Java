package com.cometproject.api.messaging.status;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.UUID;

public class StatusRequest extends MessageRequest<StatusResponse> {
    public StatusRequest() {
        super(UUID.randomUUID(), StatusResponse.class);
    }
}

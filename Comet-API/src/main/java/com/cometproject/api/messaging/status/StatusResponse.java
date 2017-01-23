package com.cometproject.api.messaging.status;

import com.cometproject.api.stats.CometStats;
import io.coerce.services.messaging.client.messages.response.MessageResponse;

public class StatusResponse implements MessageResponse {
    private final CometStats status;
    private final String version;

    public StatusResponse(final CometStats status, final String version) {
        this.status = status;
        this.version = version;
    }

    public CometStats getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }
}

package com.cometproject.api.messaging.performance;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.UUID;

public class QueryRequest extends MessageRequest<QueryResponse> {
    private final long timeTakenMs;
    private final String query;

    public QueryRequest(String query, long timeTakenMs) {
        super(UUID.randomUUID(), QueryResponse.class);

        this.query = query;
        this.timeTakenMs = timeTakenMs;
    }

    @Override
    protected void onResponseReceived(QueryResponse queryResponse) {

    }

    public long getTimeTakenMs() {
        return timeTakenMs;
    }

    public String getQuery() {
        return query;
    }
}

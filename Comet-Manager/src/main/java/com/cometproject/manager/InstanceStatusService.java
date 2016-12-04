package com.cometproject.manager;

import com.cometproject.manager.repositories.InstanceRepository;
import io.coerce.services.messaging.client.MessageFuture;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.types.GetServersByServiceNameRequest;
import io.coerce.services.messaging.client.messages.response.types.GetServersByServiceNameResponse;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InstanceStatusService {

    private static InstanceStatusService instance;

    private Map<String, String> instances;

    public InstanceStatusService() {

    }

    public void processUpdates(InstanceRepository instanceRepository, MessagingClient client) throws ExecutionException, InterruptedException {
        final MessageFuture<GetServersByServiceNameResponse> instances = client.submitRequest("master", new GetServersByServiceNameRequest("com.cometproject:instance/*"));

        for(String instanceName : instances.get().getServices()) {
            final String instanceId = instanceName.split("/")[1];
        }
    }

    public static InstanceStatusService getInstance() {
        if(instance == null) {
            instance = new InstanceStatusService();
        }

        return instance;
    }
}

package com.cometproject.manager;

import com.cometproject.api.messaging.performance.QueryRequest;
import com.cometproject.manager.controllers.websocket.QueryLogHandler;
import com.cometproject.manager.repositories.InstanceRepository;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.services.messaging.client.MessageFuture;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.types.GetServersByServiceNameRequest;
import io.coerce.services.messaging.client.messages.response.types.GetServersByServiceNameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootApplication
@EnableScheduling
public class CometManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CometManagerApplication.class, args);
    }

    private MessagingClient client;

    @Autowired
    private InstanceRepository instanceRepository;

    public CometManagerApplication() {
        try {
            this.client = MessagingClient.create("com.cometproject:manager", new CoerceConfiguration());

            this.client.observe(QueryRequest.class, (queryRequest) -> {
                for (final WebSocketSession listener : QueryLogHandler.listeners) {
                    try {
                        listener.sendMessage(new TextMessage("[QUERY][" + instanceRepository.findOne(queryRequest.getSender().split("/")[1]).getName() + "] " + queryRequest.getQuery() + " took " + queryRequest.getTimeTakenMs() + "ms"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.client.connect("master.cometproject.com", 6500, (client) -> {

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay=1000)
    public void processUpdates() throws ExecutionException, InterruptedException {
        InstanceStatusService.getInstance().processUpdates(this.instanceRepository, this.client);
    }
}

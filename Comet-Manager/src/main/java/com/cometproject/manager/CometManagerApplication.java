package com.cometproject.manager;

import com.cometproject.api.messaging.performance.QueryRequest;
import com.cometproject.manager.controllers.websocket.QueryLogHandler;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.services.messaging.client.MessagingClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class CometManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CometManagerApplication.class, args);
    }

    private final MessagingClient client = MessagingClient.create("com.cometproject:manager", new CoerceConfiguration());

    public CometManagerApplication() {
        client.observe(QueryRequest.class, (queryRequest) -> {
            for(final WebSocketSession listener : QueryLogHandler.listeners) {
                try {
                    listener.sendMessage(new TextMessage("[QUERY] " + queryRequest.getQuery() + " took " + queryRequest.getTimeTakenMs() + "ms"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        client.connect("178.33.171.199", 6500, (client) -> {
            System.out.println("we connected lol");
        });
    }

    @Scheduled(fixedDelay=1000)
    public void processUpdates() {
        InstanceStatusService.getInstance().processStatus();
    }
}

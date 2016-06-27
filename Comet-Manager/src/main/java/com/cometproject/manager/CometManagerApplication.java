package com.cometproject.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class CometManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CometManagerApplication.class, args);
    }

    public CometManagerApplication() {

    }

    @Scheduled(fixedDelay=1000)
    public void processUpdates() {
        InstanceStatusService.getInstance().processStatus();
    }
}

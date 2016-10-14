package com.cometproject.manager;

import java.util.Map;

public class InstanceStatusService {

    private static InstanceStatusService instance;

    private static Map<String, String> instances;

    public InstanceStatusService() {

    }

    public void processStatus() {

    }

    public static InstanceStatusService getInstance() {
        if(instance == null) {
            instance = new InstanceStatusService();
        }

        return instance;
    }
}

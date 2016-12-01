package com.cometproject.manager.repositories.hosts;

import java.util.Map;

public class InstanceStatus {
    private String status;
    private Map<String, Object> statusObject;

    public InstanceStatus() {

    }

    public String getStatusStyle() {
        if(this.status.equals("DOWN")) {
            return "label-danger";
        } else if(this.status.equals("STOPPING")){
            return "label-warning";
        } else {
            return "label-success";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getStatusObject() {
        return statusObject;
    }

    public void setStatusObject(Map<String, Object> statusObject) {
        this.statusObject = statusObject;
    }


}

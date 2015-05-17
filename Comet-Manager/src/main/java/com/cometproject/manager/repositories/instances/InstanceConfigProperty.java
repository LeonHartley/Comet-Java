package com.cometproject.manager.repositories.instances;

public class InstanceConfigProperty {
    private String key;
    private String value;

    public InstanceConfigProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("ConfigProperty [%s, %s]", this.key, this.value);
    }
}

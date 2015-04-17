package com.cometproject.logger;

import java.util.LinkedHashMap;
import java.util.Map;


public class ResponseBuilder {
    private final Map<String, Object> map = new LinkedHashMap<>();

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public ResponseBuilder add(String key, Object val) {
        this.map.put(key, val);
        return this;
    }

    public Map<String, Object> get() {
        return this.map;
    }
}

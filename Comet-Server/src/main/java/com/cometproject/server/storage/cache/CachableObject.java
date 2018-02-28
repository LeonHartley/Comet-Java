package com.cometproject.server.storage.cache;

import com.cometproject.api.utilities.JsonUtil;
import com.google.gson.JsonObject;

import java.io.Serializable;

public abstract class CachableObject implements Serializable {
    @Override
    public String toString() {
        final JsonObject jsonObject = this.toJson();

        if (jsonObject != null) {
            return JsonUtil.getInstance().toJson(jsonObject);
        }

        return JsonUtil.getInstance().toJson(this);
    }

    public JsonObject toJson() {
        return null;
    }
}

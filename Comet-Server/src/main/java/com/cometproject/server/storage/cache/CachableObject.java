package com.cometproject.server.storage.cache;

import com.cometproject.server.utilities.JsonUtil;

import java.io.Serializable;

public abstract class CachableObject implements Serializable {
    @Override
    public String toString() {
        return JsonUtil.getInstance().toJson(this);
    }
}

package com.cometsrv.game.wired.data;

import javolution.util.FastMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

public class WiredDataFactory {
    private static Map<Integer, WiredDataInstance> instances;

    public static void init() {
        instances = new FastMap<>();
    }

    public static WiredDataInstance get(Integer id) {
        if(instances.containsKey(id)) {
            return instances.get(id);
        }

        // get from db and cache instance
        return null;
    }

    public static void save(WiredDataInstance data) {
        throw new NotImplementedException();
    }
}

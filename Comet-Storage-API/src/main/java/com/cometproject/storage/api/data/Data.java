package com.cometproject.storage.api.data;

public class Data<T> {

    private T value;

    public Data() {

    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public boolean has() {
        return this.value != null;
    }

    public static <T> Data<T> createEmpty() {
        return new Data<T>();
    }
}

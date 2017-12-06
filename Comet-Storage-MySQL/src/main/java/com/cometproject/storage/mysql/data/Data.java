package com.cometproject.storage.mysql.data;

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
}

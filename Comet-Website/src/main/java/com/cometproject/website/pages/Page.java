package com.cometproject.website.pages;

public class Page {
    private String id;
    private String name;
    private String tab;

    public Page(String id, String name, String tab) {
        this.id = id;
        this.name = name;
        this.tab = tab;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTab() {
        return tab;
    }
}

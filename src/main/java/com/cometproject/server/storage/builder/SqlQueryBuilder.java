package com.cometproject.server.storage.builder;

/**
 * Created by Matty on 02/05/2014.
 */
public class SqlQueryBuilder {
    private final StringBuilder builder = new StringBuilder();

    public SqlQueryBuilder select() {
        this.builder.append("SELECT");
        return this;
    }

    public SqlQueryBuilder all() {
        this.builder.append("*");
        return this;
    }
}

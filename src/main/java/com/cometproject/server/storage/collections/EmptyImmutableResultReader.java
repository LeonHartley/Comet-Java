package com.cometproject.server.storage.collections;

import java.sql.ResultSet;

/**
 * Created by Matty on 01/05/2014.
 */
public class EmptyImmutableResultReader extends ImmutableResultReader {
    public EmptyImmutableResultReader() {
        super(null, false);
    }
}

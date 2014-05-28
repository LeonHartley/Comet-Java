package com.cometproject.server.network.messages.types;

import org.apache.log4j.Logger;

public class DuplicatedComposer extends Composer {
    private static Logger log = Logger.getLogger(DuplicatedComposer.class);

    public DuplicatedComposer(Composer c) {
        super(c.id, c.body.copy());

        log.trace("Composer [ " + c.id + " ] was duplicated");
    }
}

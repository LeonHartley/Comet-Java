package com.cometproject.server.network.messages.types;

import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;

public class UnpooledComposer extends Composer {
    private static Logger log = Logger.getLogger(UnpooledComposer.class);

    public UnpooledComposer(Composer c) {
        super(c.id, Unpooled.buffer().writeBytes(c.body));
        c.body.release(); // release the old

        log.trace("Composer [ " + c.id + " ] was converted to unpooled bytebuf");
    }
}

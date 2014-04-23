package com.cometproject.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Created by Matty on 23/04/2014.
 */
public class ByteBufAllocator {
    private static PooledByteBufAllocator allocator;

    static {
        allocator = new PooledByteBufAllocator();
    }

    /*
     * Netty automatically releases the buffer because it is so kind!
     */
    public static ByteBuf retrieve() {
        return allocator.buffer();
    }
}

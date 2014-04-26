package com.cometproject.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Created by Matty on 26/04/2014.
 */
public class SharedByteBufAllocator {
    private static ByteBufAllocator allocator;

    static {
        allocator = new PooledByteBufAllocator();
    }

    public static ByteBufAllocator getAllocator() {
        return allocator;
    }

    public static ByteBuf allocate() {
        return allocator.buffer();
    }
}

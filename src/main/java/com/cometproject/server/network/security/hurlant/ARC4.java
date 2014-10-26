package com.cometproject.server.network.security.hurlant;

public class ARC4 {
    private static final int POOL_SIZE = 256;

    private int i = 0;
    private int j = 0;
    private byte[] bytes;

    public ARC4(byte[] sharedKey) {
        this.bytes = new byte[POOL_SIZE];
        this.init(sharedKey);
    }

    public void init(byte[] key) {
        for (this.i = 0; this.i < POOL_SIZE; ++this.i) {
            this.bytes[this.i] = (byte) this.i;
        }

        for (this.i = 0; this.i < POOL_SIZE; ++this.i) {
            this.j = (this.j + this.bytes[this.i] + key[this.i % key.length]) & (POOL_SIZE - 1);
            this.swap(this.i, this.j);
        }
    }

    public void encrypt(byte[] src) {
        for (int k = 0; k < src.length; k++) {
            src[k] ^= this.next();
        }
    }

    public byte next() {
        this.i = ++this.i & (POOL_SIZE - 1);
        this.j = (this.j + this.bytes[this.i]) & (POOL_SIZE - 1);
        this.swap(this.i, this.j);
        return this.bytes[(this.bytes[this.i] + this.bytes[this.j]) & 255];
    }

    private void swap(int a, int b) {
        byte t = this.bytes[a];
        this.bytes[a] = this.bytes[b];
        this.bytes[b] = t;
    }
}

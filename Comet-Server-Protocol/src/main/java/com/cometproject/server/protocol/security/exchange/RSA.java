package com.cometproject.server.protocol.security.exchange;

import java.math.BigInteger;

public class RSA {
    public BigInteger exponent; // public exponent
    public BigInteger n; // modulus
    // private key
    public BigInteger privateKey;
    // bools (can encrypt/decrypt?)
    public boolean decryptable;

    public boolean encry;

    private BigInteger zero = new BigInteger("0");

    public RSA() {

    }

    public void init() {
        this.n = new BigInteger("0c4d6715d5b398582297fbdadf23c0838d5d2e824fd75c2ba0bf9ef37eb30c30b060edf60a592c9a031d51d49efd6246edc1b67ad4aca535fc0e9af4f76f94aa9429f4fc7f681a25a332c4962511c09d53748dd4247f2298737217cb06a88dfa43eed57f8adb5896f49d574e0b647f5370b4c1dfd749d197810c355a51ea2cab7", 16);
        this.exponent = new BigInteger("10001", 16);
        this.privateKey = new BigInteger("5f8979d287117f3472f1f65d742221177ce8f615e6137f250fe50a6fc8ae8d2c4f8f6e6a19530a9bbf3422181dd9812b7197b7958ba4721d1aa4119bef32064310eec40fbfdc7a662ae2365a6f8a3fdf3e4a8169e7a44ba3de335b6336b32b45d080410b7d2ac8541fb2d5bfc011c740c990fa0acfc0a6864ed55c3f108c2239", 16);

        encry = (n != null && n != zero && exponent != zero);
        decryptable = (encry && privateKey != zero && privateKey != null);
    }

    public int getBlockSize() {
        return (n.bitLength() + 7) / 8;
    }

    public BigInteger doPublic(BigInteger x) {
        if (this.encry) {
            return x.modPow(new BigInteger(this.exponent + ""), this.n);
        }

        return zero;
    }

    public String encrypt(String text) {
        BigInteger m = new BigInteger(this.pkcs1pad2(text.getBytes(), this.getBlockSize()));

        if (m.equals(zero)) {
            return null;
        }

        BigInteger c = this.doPublic(m);

        if (c.equals(zero)) {
            return null;
        }

        String result = c.toString(16);

        if ((result.length() & 1) == 0) {
            return result;
        }

        return "0" + result;
    }

    public String sign(String text) {
        BigInteger m = new BigInteger(this.pkcs1pad2(text.getBytes(), this.getBlockSize()));

        if (m.equals(zero)) {
            return null;
        }

        BigInteger c = this.doPrivate(m);

        if (c.equals(zero)) {
            return null;
        }

        String result = c.toString(16);

        if ((result.length() & 1) == 0) {
            return result;
        }

        return result;
    }

    private byte[] pkcs1pad2(byte[] data, int n) {
        byte[] bytes = new byte[n];

        int i = data.length - 1;

        while (i >= 0 && n > 11) {
            bytes[--n] = data[i--];
        }

        bytes[--n] = 0;

        while (n > 2) {
            bytes[--n] = (byte) 0xFF;
        }

        bytes[--n] = (byte) 1;
        bytes[--n] = 0;

        return bytes;
    }

    public BigInteger doPrivate(BigInteger x) {
        if (this.decryptable) {
            return x.modPow(this.privateKey, this.n);
        }

        return zero;
    }

    public String decrypt(String ctext) {
        BigInteger c = new BigInteger(ctext, 16);
        BigInteger m = this.doPrivate(c);

        if (m.equals(zero)) {
            return null;
        }

        byte[] bytes = this.pkcs1unpad2(m, this.getBlockSize());

        if (bytes == null) {
            return null;
        }

        return new String(bytes);
    }

    private byte[] pkcs1unpad2(BigInteger src, int n) {
        byte[] bytes = src.toByteArray();
        byte[] out;
        int i = 0;

        while (i < bytes.length && bytes[i] == 0) {
            ++i;
        }

        if (bytes.length - i != n - 1 || bytes[i] > 2) {
            return null;
        }

        ++i;

        while (bytes[i] != 0) {
            if (++i >= bytes.length) {
                return null;
            }
        }

        out = new byte[(bytes.length - i) + 1];
        int p = 0;

        while (++i < bytes.length) {
            out[p++] = (bytes[i]);
        }

        return out;
    }
}
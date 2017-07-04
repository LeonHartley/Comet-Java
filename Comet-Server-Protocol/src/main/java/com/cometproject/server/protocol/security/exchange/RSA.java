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
        this.n = new BigInteger("0d04427757ea95b0e11dfa17e90fc84db7774abf2f67dcef0636d25fbc9dda8434a4e8bf75e12431d023f3a0579d40b2b85f474ec71f8259c8358066731af295b6a187b4709feff6f7788e95d4f838d4c1c338dcb7fe9eda7ed8edeef4f24c681c005eaa793d5645298ea4e5b6fe30e21011c5f7ff6086e7bff1a9cc847bca869", 16);
        this.exponent = new BigInteger("10001", 16);
        this.privateKey = new BigInteger("2a2911dfdc33468693a1b4dc9d573142086daafd755ec48caf640223759b149c02a7e1f7c2a869c3ae97c41425b4824a5e44bd82bc13a2f18b393f9a96594242ac7b67d8e5f61f170cc68b4d88287b1ab19dfd5c5b9245d150432aea7ea84522eeb4f234f7deb3eb60f5aa79f13d0f9f6069023b120fa5afe2ca7ef5240230bd", 16);

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
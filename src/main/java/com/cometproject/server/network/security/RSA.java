package com.cometproject.server.network.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class RSA {
    public BigInteger n;
    public BigInteger e;
    public BigInteger d;
    public BigInteger p;
    public BigInteger q;
    public BigInteger dmp1;
    public BigInteger dmq1;
    public BigInteger coeff;

    protected Boolean canDecrypt;
    protected Boolean canEncrypt;

    public RSA(BigInteger n, BigInteger e, BigInteger d, BigInteger p, BigInteger q, BigInteger dmp1, BigInteger dmq1, BigInteger coeff) {
        this.n = n;
        this.e = e;
        this.d = d;
        this.p = p;
        this.q = q;
        this.dmp1 = dmp1;
        this.dmq1 = dmq1;
        this.coeff = coeff;

        this.canEncrypt = !this.n.equals(BigInteger.ZERO) && !this.e.equals(BigInteger.ZERO);
        this.canDecrypt = this.canEncrypt && !this.d.equals(BigInteger.ZERO);
    }

    private int getBlockSize() {
        return (this.n.bitCount() + 7) / 8;
    }

    public BigInteger doPublic(BigInteger x) {
        if (this.canEncrypt) {
            return x.modPow(this.e, this.n);
        }

        return BigInteger.ZERO;
    }

    public String encrypt(String text) {
        if (text.length() > this.getBlockSize() - 11) {
            // ERROR!
        }

        BigInteger m = new BigInteger(this.pkcs1pad2(text.getBytes(Charset.forName("iso-8859-1")), this.getBlockSize()));
        if (m == BigInteger.ZERO) {
            return null;
        }

        BigInteger c = this.doPublic(m);
        if (c == BigInteger.ZERO) {
            return null;
        }

        String result = c.toString(16);
        if ((result.length() & 1) == 0) {
            return result;
        }

        return "0" + result;
    }

    private byte[] pkcs1pad2(byte[] data, int n) {
        byte[] bytes = new byte[n];
        int i = data.length - 1;
        while (i >= 0 && n > 11) {
            bytes[--n] = data[i--];
        }
        bytes[--n] = 0;

        while (n > 2) {
            bytes[--n] = 0x01;
        }

        bytes[--n] = 0x2;
        bytes[--n] = 0;

        return bytes;
    }

    public BigInteger doPrivate(BigInteger x) {
        if (this.canDecrypt) {
            return x.modPow(this.d, this.n);
        }

        return BigInteger.ZERO;
    }

    public String decrypt(String ctext) throws UnsupportedEncodingException {
        BigInteger c = new BigInteger(ctext, 16);
        BigInteger m = this.doPrivate(c);
        if (m.equals(BigInteger.ZERO)) {
            return null;
        }

        byte[] bytes = this.pkcs1unpad2(m, this.getBlockSize());

        if (bytes == null) {
            return null;
        }

        return new String(bytes, "iso-8859-1");
    }

    private byte[] pkcs1unpad2(BigInteger m, int b) {
        byte[] bytes = m.toByteArray();

        int i = 0;
        while (i < bytes.length && bytes[i] == 0) ++i;

        if (bytes.length - i != (b - 1) || bytes[i] != 2) {
            return null;
        }

        while (bytes[i] != 0) {
            if (++i >= bytes.length) {
                return null;
            }
        }

        byte[] result = new byte[bytes.length - i + 1];
        int p = 0;
        while (++i < bytes.length) {
            result[p++] = bytes[i];
        }

        return result;
    }
}
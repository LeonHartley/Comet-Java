package com.cometproject.server.network.security.hurlant;

import com.cometproject.server.network.security.SecurityUtil;
import com.cometproject.server.utilities.RandomInteger;

import java.math.BigInteger;


public class RSAKey {
    private final String e;
    private final BigInteger n, d, p, q, dmp1, dmq1, coeff;
    private final boolean canDecrypt, canEncrypt;

    public RSAKey(BigInteger n, String e, BigInteger d, BigInteger p, BigInteger q, BigInteger dmp1, BigInteger dmq1, BigInteger coeff) {
        this.e = e;
        this.n = n;
        this.d = d;
        this.p = p;
        this.q = q;
        this.dmp1 = dmp1;
        this.dmq1 = dmq1;
        this.coeff = coeff;

        this.canEncrypt = !this.n.equals(BigInteger.ZERO) && !this.e.equals("0");
        this.canDecrypt = this.canEncrypt && !this.d.equals(BigInteger.ZERO);
    }

    public static RSAKey parsePublicKey(String n, String e) {
        return new RSAKey(new BigInteger(n, 16), e, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public static RSAKey parsePrivateKey(String n, String e, String d) {
        return new RSAKey(new BigInteger(n, 16), e, new BigInteger(d, 16), BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public static RSAKey parsePrivateKey(String n, String e, String d, String p, String q, String dmp1, String dmq1, String coeff) {
        return new RSAKey(new BigInteger(n, 16), e, new BigInteger(d, 16), new BigInteger(p, 16), new BigInteger(q, 16), new BigInteger(dmp1, 16), new BigInteger(dmq1, 16), new BigInteger(coeff, 16));
    }

    public int getBlockSize() {
        return (this.n.bitCount() + 7) / 8;
    }

    public byte[] sign(byte[] source) {
        return this.encrypt(source, false, Pkcs1PadType.FULL_BYTE);
    }

    public byte[] verify(byte[] source) {
        return this.decrypt(source, true, Pkcs1PadType.FULL_BYTE);
    }

    public byte[] encrypt(byte[] source) {
        return this.encrypt(source, false, Pkcs1PadType.RANDOM_BYTE);
    }

    public byte[] decrypt(byte[] source) {
        return this.decrypt(source, true, Pkcs1PadType.RANDOM_BYTE);
    }

    public byte[] encrypt(byte[] source, boolean isPublic, Pkcs1PadType type) {
        try {
            final int blockSize = this.getBlockSize();
            final byte[] paddedBytes = this.pkcs1pad(source, blockSize, type);
            final BigInteger m = new BigInteger(paddedBytes);

            if (m.equals(BigInteger.ZERO)) {
                return null;
            }

            final BigInteger c;

            if (isPublic) {
                c = this.doPublic(m);
            } else {
                c = this.doPrivate(m);
            }

            return c.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(byte[] source, boolean isPublic, Pkcs1PadType type) {
        try {
            final BigInteger c = new BigInteger(SecurityUtil.bytesToHex(source), 16);
            final BigInteger m;
            if (isPublic) {
                m = this.doPublic(c);
            } else {
                m = this.doPrivate(c);
            }

            final int blockSize = this.getBlockSize();
            final byte[] unpaddedBytes = this.pkcs1unpad(m.toByteArray(), blockSize, type);
            return unpaddedBytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] pkcs1pad(byte[] src, int n, Pkcs1PadType type) {
        byte[] bytes = new byte[n];

        int i = src.length - 1;

        while (i >= 0 && n > 0) {
            bytes[--n] = src[i--];
        }

        bytes[--n] = 0;

        while (n > 2) {
            byte x = 0;

            switch (type) {
                case FULL_BYTE:
                    x = (byte) 255;
                    break;
                case RANDOM_BYTE:
                    x = (byte) RandomInteger.getRandom(1, 255);
                    break;
            }

            bytes[--n] = x;
        }

        bytes[--n] = 1;
        bytes[--n] = 0;

        return bytes;
    }

    private byte[] pkcs1unpad(byte[] source, int n, Pkcs1PadType type) {
        int i = 0;

        while (i < source.length && source[i] == 0) {
            ++i;
        }

        if (source.length - i != n - 1 || source[i] > 2) {
            return null;
        }

        ++i;

        while (source[i] != 0) {
            if (++i >= source.length) {
                return null;
            }
        }

        byte[] bytes = new byte[source.length - i - 1];

        for (int p = 0; ++i < source.length; p++) {
            bytes[p] = source[i];
        }

        return bytes;
    }

    private BigInteger doPrivate(BigInteger m) {
        if (this.p.equals(BigInteger.ZERO) && this.q.equals(BigInteger.ZERO)) {
            return m.modPow(this.n, this.d);
        } else {
            return BigInteger.ZERO;
        }
    }

    private BigInteger doPublic(BigInteger m) {
        return m.modPow(new BigInteger(this.e, 16), this.n);
    }

    private enum Pkcs1PadType {
        FULL_BYTE(1), RANDOM_BYTE(2);

        private int type;

        Pkcs1PadType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
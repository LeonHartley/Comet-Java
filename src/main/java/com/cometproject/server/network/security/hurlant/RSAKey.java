package com.cometproject.server.network.security.hurlant;

import java.math.BigInteger;

public class RSAKey {
    private final int e;
    private final BigInteger n, d, p, q, dmp1, dmq1, coeff;
    private final boolean canDecrypt, canEncrypt;

    public RSAKey(BigInteger n, int e, BigInteger d, BigInteger p, BigInteger q, BigInteger dmp1, BigInteger dmq1, BigInteger coeff) {
        this.e = e;
        this.n = n;
        this.d = d;
        this.p = p;
        this.q = q;
        this.dmp1 = dmp1;
        this.dmq1 = dmq1;
        this.coeff = coeff;

        this.canEncrypt = !this.n.equals(BigInteger.ZERO) && this.e != 0;
        this.canDecrypt = this.canEncrypt && !this.d.equals(BigInteger.ZERO);
    }

    public static RSAKey parsePublicKey(String n, String e) {
        return new RSAKey(new BigInteger(n, 16), Integer.parseInt(e), BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public static RSAKey parsePrivateKey(String n, String e, String d) {
        return new RSAKey(new BigInteger(n, 16), Integer.parseInt(e), new BigInteger(d, 16), BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public static RSAKey parsePrivateKey(String n, String e, String d, String p, String q, String dmp1, String dmq1, String coeff) {
        return new RSAKey(new BigInteger(n, 16), Integer.parseInt(e), new BigInteger(d, 16), new BigInteger(p, 16), new BigInteger(q, 16), new BigInteger(dmp1, 16), new BigInteger(dmq1, 16), new BigInteger(coeff, 16));
    }

    public int getBlockSize() {
        return (this.n.bitCount() + 7) / 8;
    }

    public byte[] encrypt(byte[] source) {
        return this.encrypt(source, true);
    }

    public byte[] decrypt(byte[] source) {
        return this.decrypt(source, true);
    }

    public byte[] encrypt(byte[] source, boolean isPublic) {
        try {
            final int blockSize = this.getBlockSize();
            final byte[] paddedBytes = this.pkcs1pad(source, blockSize, Pkcs1PadType.FULL_BYTE);
            final BigInteger m = new BigInteger(paddedBytes);

            if(m.equals(BigInteger.ZERO)) {
                return null;
            }

            final BigInteger c;

            if(isPublic) {
                c = this.doPublic(m);
            } else {
                c = this.doPrivate(m);
            }

            return c.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(byte[] source, boolean isPublic) {
        try {
            final int blockSize = this.getBlockSize();
            final byte[] paddedBytes = this.pkcs1pad(source, blockSize, Pkcs1PadType.FULL_BYTE);
            final BigInteger m = new BigInteger(paddedBytes);

            if(m.equals(BigInteger.ZERO)) {
                return null;
            }

            final BigInteger c;

            if(isPublic) {
                c = this.doPublic(m);
            } else {
                c = this.doPrivate(m);
            }

            return c.toByteArray();
        } catch(Exception e) {
            return null;
        }
    }

    public byte[] sign(byte[] source) {
        return this.encrypt(source, false);
    }

    public byte[] verify(byte[] source) {
        return this.decrypt(source, false);
    }

    private byte[] pkcs1pad(byte[] source, int n, Pkcs1PadType type) {
        byte[] bytes = new byte[n];

        int i = source.length - 1;

        while(i >= 0 && n > 11) {
            bytes[--n] = source[i--];
        }

        bytes[--n] = 0;

        while(n > 2) {
            byte x = (byte) 0xFF;

//            switch(type) {
//                case FULL_BYTE: x = (byte) 0xFF; break;
//            }

            bytes[--n] = x;
        }

        bytes[--n] = type.getType();
        bytes[--n] = 0;

        return bytes;
    }

    private byte[] pkcs1unpad(byte[] source, int n, Pkcs1PadType type) {
        int i = 0;

        while(i < source.length && source[i] == 0) {
            ++i;
        }

        if(source.length - i != n - 1 || source[i] > 2) {
            return null;
        }

        ++i;

        while(source[i] != 0) {
            if(++i >= source.length) {
                // no idea.
            }
        }

        byte[] bytes = new byte[source.length - i - 1];

        for(int p = 0; ++i < source.length; p++) {
            bytes[p] = source[i];
        }

        return bytes;
    }

    private BigInteger doPrivate(BigInteger m) {
        if(this.p.equals(BigInteger.ZERO) && this.q.equals(BigInteger.ZERO)) {
            return m.modPow(this.d, this.n);
        } else {
            return BigInteger.ZERO;
        }
    }

    private BigInteger doPublic(BigInteger m) {
        return m.modPow(BigInteger.valueOf(this.e), this.n);
    }

    private enum Pkcs1PadType {
        FULL_BYTE((byte) 1), RANDOM_BYTE((byte) 2);

        private byte type;

        Pkcs1PadType(byte type) {
            this.type = type;
        }

        public byte getType() {
            return type;
        }
    }
}

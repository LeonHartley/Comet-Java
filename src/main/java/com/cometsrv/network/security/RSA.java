package com.cometsrv.network.security;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.Random;

public class RSA
{
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

    private Logger log = Logger.getLogger(RSA.class.getName());

    public RSA(BigInteger n, BigInteger e, BigInteger d, BigInteger p, BigInteger q, BigInteger dmp1, BigInteger dmq1, BigInteger coeff)
    {
        this.n = n;
        this.e = e;
        this.d = d;
        this.p = p;
        this.q = q;
        this.dmp1 = dmp1;
        this.dmq1 = dmq1;
        this.coeff = coeff;

        this.canEncrypt = this.n.intValue() != 0 && this.e.intValue() != 0;
        this.canDecrypt = this.canEncrypt && this.d.intValue() != 0;
    }

    public RSA(int b, BigInteger e)
    {
        this.e = e;

        int qs = b >> 1;

        while (true)
        {
            while (true)
            {
                this.p = BigInteger.probablePrime(b - qs, new Random());

                if (BigInteger.valueOf(this.p.intValue() - 1).gcd(this.e).intValue() == 1 && this.p.isProbablePrime(10))
                {
                    break;
                }
            }

            while (true)
            {
                this.q = BigInteger.probablePrime(qs, new Random());

                if (BigInteger.valueOf(this.q.intValue() - 1).gcd(this.e).intValue() == 1 && this.p.isProbablePrime(10))
                {
                    break;
                }
            }

            if (this.p.intValue() < this.q.intValue())
            {
                BigInteger t = this.p;
                this.p = this.q;
                this.q = t;
            }

            BigInteger phi = BigInteger.valueOf((this.p.intValue() - 1) * (this.q.intValue() - 1));
            if (phi.gcd(this.e).intValue() == 1)
            {
                this.n = BigInteger.valueOf(this.p.intValue() * this.q.intValue());
                this.d = this.e.modInverse(phi);
                this.dmp1 = BigInteger.valueOf(this.d.intValue() % (this.p.intValue() - 1));
                this.dmq1 = BigInteger.valueOf(this.d.intValue() % (this.q.intValue() - 1));
                this.coeff = this.q.modInverse(this.p);
                break;
            }
        }

        this.canEncrypt = this.n.intValue() != 0 && this.e.intValue() != 0;
        this.canDecrypt = this.canEncrypt && this.d.intValue() != 0;
    }

    public RSA(BigInteger e, BigInteger p, BigInteger q)
    {
        this.e = e;
        this.p = p;
        this.q = q;

        BigInteger phi = BigInteger.valueOf((this.p.intValue() - 1) * (this.q.intValue() - 1));
        if (phi.gcd(this.e).intValue() == 1)
        {
            this.n = BigInteger.valueOf(this.p.intValue() * this.q.intValue());
            this.d = this.e.modInverse(phi);
            this.dmp1 = BigInteger.valueOf(this.d.intValue() % (this.p.intValue() - 1));
            this.dmq1 = BigInteger.valueOf(this.d.intValue() % (this.q.intValue() - 1));
            this.coeff = this.q.modInverse(this.p);
        }

        this.canEncrypt = this.n.intValue() != 0 && this.e.intValue() != 0;
        this.canDecrypt = this.canEncrypt && this.d.intValue() != 0;
    }

    private int getBlockSize()
    {
        return (this.n.bitCount() + 7) / 8;
    }

    public BigInteger doPublic(BigInteger x)
    {
        if (this.canEncrypt)
        {
            return x.modPow(this.e, this.n);
        }

        return BigInteger.valueOf(0);
    }

    public String encrypt(String text)
    {
        if (text.length() > this.getBlockSize() - 11)
        {
            log.warn("Message is too long! [Length: " + text.length() + "]");
        }

        BigInteger m = new BigInteger(this.pkcs1pad2(text.getBytes(), this.getBlockSize()));
        if (m.intValue() == 0)
        {
            return null;
        }

        BigInteger c = this.doPublic(m);
        if (c.intValue() == 0)
        {
            return null;
        }

        String result = c.toString(16);
        if ((result.length() & 1) == 0)
        {
            return result;
        }

        return "0" + result;
    }

    private byte[] pkcs1pad2(byte[] data, int n)
    {
        byte[] bytes = new byte[n];
        int i = data.length - 1;
        while (i >= 0 && n > 11)
        {
            bytes[--n] = data[i--];
        }
        bytes[--n] = 0;

        while (n > 2)
        {
            bytes[--n] = 0x01;
        }

        bytes[--n] = 0x2;
        bytes[--n] = 0;

        return bytes;
    }

    public BigInteger doPrivate(BigInteger x)
    {
        if (this.canDecrypt)
        {
            return x.modPow(this.d, this.n);
        }
        else
        {
            System.out.println("fail");
            return BigInteger.valueOf(0);
        }
    }

    public String decrypt(String ctext)
    {
        BigInteger c = new BigInteger(ctext, 16);
        BigInteger m = this.doPrivate(c);


        if (m.intValue() == 0)
        {
            return null;
        }

        byte[] bytes = this.pkcs1unpad2(m, this.getBlockSize());

        if (bytes == null)
        {
            return null;
        }

        return new String(bytes);
    }

    private byte[] pkcs1unpad2(BigInteger m, int b)
    {
        byte[] bytes = m.toByteArray();

        int i = 0;
        while (i < bytes.length && bytes[i] == 0) ++i;

        if (bytes.length - i != (b - 1) || bytes[i] != 0x2)
        {
            return null;
        }

        while (bytes[i] != 0)
        {
            if (++i >= bytes.length)
            {
                return null;
            }
        }

        byte[] result = new byte[bytes.length - i + 1];
        int p = 0;
        while (++i < bytes.length)
        {
            result[p++] = bytes[i];
        }

        return result;
    }
}
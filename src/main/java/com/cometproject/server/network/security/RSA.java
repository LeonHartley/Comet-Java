package com.cometproject.server.network.security;

import sun.security.util.BigInt;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Random;

public class RSA
{

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
        this.canEncrypt = (!this.n.equals(BigInteger.ZERO)) && (!this.e.equals(BigInteger.ZERO));
        this.canDecrypt = this.canEncrypt && (!this.d.equals(BigInteger.ZERO));
    }

    public String Decrypt(String ctext) throws UnsupportedEncodingException {
        BigInteger x = new BigInteger(ctext, 0x10);
        BigInteger m = this.DoPrivate(x);
        if (m.equals(BigInteger.ZERO))
        {
            return null;
        }
        byte[] bytes = this.pkcs1unpad2(m, this.GetBlockSize());
        if (bytes == null)
        {
            return null;
        }
        return new String(bytes, "iso-8859-1");
    }

    public BigInteger DoPrivate(BigInteger x)
    {
        if (this.canDecrypt)
        {
            return x.modPow(this.d, this.n);
        }
        return BigInteger.ZERO;
    }

    public BigInteger DoPublic(BigInteger x)
    {
        if (this.canEncrypt)
        {
            return x.modPow(this.e, this.n);
        }
        return BigInteger.ZERO;
    }

    public String Encrypt(String text)
    {
        if (text.length() > (this.GetBlockSize() - 11))
        {
        }

        BigInteger x = new BigInteger(this.pkcs1pad2(text.getBytes(Charset.forName("iso-8859-1")), this.GetBlockSize()));
        if (x.equals(BigInteger.ZERO))
        {
            return null;
        }
        BigInteger integer2 = this.DoPublic(x);
        if (integer2.equals(BigInteger.ZERO))
        {
            return null;
        }

        String str = integer2.toString(0x10);
        if ((str.length() & 1) == 0)
        {
            return str;
        }
        return ("0" + str);
    }

    private int GetBlockSize()
    {
        return ((this.n.bitCount() + 7) / 8);
    }

    private byte[] pkcs1pad2(byte[] data, int n)
    {
        byte[] buffer = new byte[n];
        int num = data.length - 1;
        while ((num >= 0) && (n > 11))
        {
            buffer[--n] = data[num--];
        }
        buffer[--n] = 0;
        while (n > 2)
        {
            buffer[--n] = 1;
        }
        buffer[--n] = 2;
        buffer[--n] = 0;
        return buffer;
    }

    private byte[] pkcs1unpad2(BigInteger m, int b)
    {
        byte[] buffer = m.toByteArray();
        int index = 0;
        while ((index < buffer.length) && (buffer[index] == 0))
        {
            index++;
        }
        if (((buffer.length - index) != (b - 1)) || (buffer[index] != 2))
        {
            return null;
        }
        while (buffer[index] != 0)
        {
            if (++index >= buffer.length)
            {
                return null;
            }
        }
        byte[] buffer2 = new byte[(buffer.length - index) + 1];
        int num2 = 0;
        while (++index < buffer.length)
        {
            buffer2[num2++] = buffer[index];
        }
        return buffer2;
    }

    public boolean canEncrypt;
    public boolean canDecrypt;

    public BigInteger coeff;

    public BigInteger d;

    public BigInteger dmp1;

    public BigInteger dmq1;

    public BigInteger e;

    public BigInteger n;

    public BigInteger p;

    public BigInteger q;
}
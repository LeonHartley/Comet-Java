package com.cometproject.server.network.security;
/*
public class RC4 {

    private int i = 0;
    private int j = 0;
    private int[] Table = new int[256];

    public RC4(byte[] key) {
        int k = key.length;
        while (this.i < 256) {
            Table[this.i] = this.i;
            this.i++;
        }

        this.i = 0;
        this.j = 0;
        while (this.i < 256) {
            this.j = (((this.j + Table[this.i]) + key[(this.i % k)]) % 256);
            this.swap(this.i, this.j);
            this.i++;
        }

        this.i = 0;
        this.j = 0;
    }

    public static RC4 init(byte[] key) {
        return new RC4(key);
    }

    public void swap(int a, int b) {
        int k = this.Table[a];
        this.Table[a] = this.Table[b];
        this.Table[b] = k;
    }

    public byte[] parse(byte[] bytes) {
        int k = 0;
        byte[] result = new byte[bytes.length];
        int pos = 0;

        for (byte aByte : bytes) {
            this.i = ((this.i + 1) % 256);
            this.j = ((this.j + this.Table[this.i]) % 256);
            this.swap(this.i, this.j);
            k = ((this.Table[this.i] + this.Table[this.j]) % 256);
            result[pos++] = (byte) (aByte ^ this.Table[k]);
        }

        return result;
    }
}*/
public class RC4
{
    private int i = 0;
    private int j = 0;
    private int[] Table = new int[0x100];

    public RC4(byte[] key)
    {
        int length = key.length;
        while (this.i < 0x100)
        {
            this.Table[this.i] = this.i;
            this.i++;
        }
        this.i = 0;
        this.j = 0;
        while (this.i < 0x100)
        {
            this.j = ((this.j + this.Table[this.i]) + key[this.i % length]) % 0x100;
            this.Swap(this.i, this.j);
            this.i++;
        }
        this.i = 0;
        this.j = 0;
    }

    public byte[] Parse(byte[] bytes)
    {
        int index = 0;
        byte[] buffer = new byte[bytes.length];
        int num2 = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            this.i = (this.i + 1) % 0x100;
            this.j = (this.j + this.Table[this.i]) % 0x100;
            this.Swap(this.i, this.j);
            index = (this.Table[this.i] + this.Table[this.j]) % 0x100;
            buffer[num2++] = (byte) (bytes[i] ^ this.Table[index]);
        }
        return buffer;
    }

    public void Swap(int a, int b)
    {
        int num = this.Table[a];
        this.Table[a] = this.Table[b];
        this.Table[b] = num;
    }
}
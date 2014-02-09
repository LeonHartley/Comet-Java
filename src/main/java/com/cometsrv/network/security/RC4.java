package com.cometsrv.network.security;


public class RC4
{
    private int i = 0;
    private int j = 0;
    private int[] table;

    public RC4()
    {
        this.table = new int[256];
    }

    public RC4(byte[] key)
    {
        this.table = new int[256];

        this.init(key);
    }

    public void init(byte[] key)
    {
        int k = key.length;
        this.i = 0;
        while (this.i < 256)
        {
            this.table[this.i] = this.i;
            this.i++;
        }

        this.i = 0;
        this.j = 0;
        while (this.i < 0x0100)
        {
            this.j = (((this.j + this.table[this.i]) + key[(this.i % k)]) % 256);
            this.swap(this.i, this.j);
            this.i++;
        }

        this.i = 0;
        this.j = 0;
    }

    public void swap(int a, int b)
    {
        int k = this.table[a];
        this.table[a] = this.table[b];
        this.table[b] = k;
    }

    public byte[] decipher(byte[] bytes)
    {
        int k = 0;
        byte[] result = new byte[bytes.length];
        int pos = 0;

        for (byte aByte : bytes) {
            this.i = ((this.i + 1) % 256);
            this.j = ((this.j + this.table[this.i]) % 256);
            this.swap(this.i, this.j);
            k = ((this.table[this.i] + this.table[this.j]) % 256);
            result[pos++] = (byte) (aByte ^ this.table[k]);
        }

        return result;
    }

}

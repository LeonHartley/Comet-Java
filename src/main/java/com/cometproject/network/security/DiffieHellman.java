package com.cometproject.network.security;

import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman
{
    public int BITLENGTH = 30;

    public BigInteger prime;
    public BigInteger generator;

    public BigInteger privateKey;
    public BigInteger publicKey;

    public BigInteger publicClientKey;

    public BigInteger sharedKey;

    public DiffieHellman()
    {
        this.init();
    }

    public DiffieHellman(int b)
    {
        this.BITLENGTH = b;

        this.init();
    }

    private void init()
    {
        this.publicKey = BigInteger.valueOf(0);
        while (this.publicKey.intValue() == 0)
        {
            this.prime = BigInteger.probablePrime(BITLENGTH, new Random());
            this.generator = BigInteger.probablePrime(BITLENGTH, new Random());

            this.privateKey = new BigInteger(generateRandomHex(BITLENGTH), 16);

            if (this.generator.intValue() > this.prime.intValue())
            {
                BigInteger temp = this.prime;
                this.prime = this.generator;
                this.generator = temp;
            }

            this.publicKey = this.generator.modPow(this.privateKey, this.prime);
        }
    }

    public DiffieHellman(BigInteger prime, BigInteger generator)
    {
        this.prime = prime;
        this.generator = generator;

        this.privateKey = new BigInteger(generateRandomHex(BITLENGTH), 16);

        if (this.generator.intValue() > this.prime.intValue())
        {
            BigInteger temp = this.prime;
            this.prime = this.generator;
            this.generator = temp;
        }

        this.publicKey = this.generator.modPow(this.privateKey, this.prime);
    }

    public void generateSharedKey(String ckey)
    {
        this.publicClientKey = new BigInteger(ckey, 10);

        this.sharedKey = this.publicClientKey.modPow(this.privateKey, this.prime);
    }

    public static String generateRandomHex(int len)
    {
        int rand = 0;
        String result = "";

        Random rnd = new Random();

        for (int i = 0; i < len; i++)
        {
            rand =  1 + (int)(rnd.nextDouble() * 254); // 1 - 255
            result += Integer.toString(rand, 16);
        }
        return result;
    }
}

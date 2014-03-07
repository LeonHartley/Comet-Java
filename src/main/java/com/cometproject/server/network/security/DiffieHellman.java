package com.cometproject.server.network.security;

import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman {
    public int BITLENGTH = 30;

    public BigInteger prime;
    public BigInteger generator;

    public BigInteger privateKey;
    public BigInteger publicKey;
    public BigInteger sharedKey;

    public BigInteger publicClientKey;

    /*public DiffieHellman()
    {
        this.InitDH();
    }

    public DiffieHellman(int b)
    {
        this.BITLENGTH = b;

        this.InitDH();
    }

    private void InitDH()
    {
        this.publicKey = BigInteger.ZERO;

        while (this.publicKey.equals(BigInteger.ZERO))
        {
            this.prime = BigInteger.genPseudoPrime(BITLENGTH, 10, new Random());
            this.generator = BigInteger.genPseudoPrime(BITLENGTH, 10, new Random());

            this.privateKey = new BigInteger(getRandomHexString(BITLENGTH), 16);

            if (this.generator.intValue() > this.prime.intValue())
            {
                BigInteger temp = this.prime;
                this.prime = this.generator;
                this.generator = temp;
            }

            this.publicKey = this.generator.modPow(this.privateKey, this.prime);
        }
    }*/

    public DiffieHellman(BigInteger prime, BigInteger generator) {
        this.prime = prime;
        this.generator = generator;

        this.privateKey = new BigInteger(getRandomHexString(BITLENGTH), 16);

        if (this.generator.intValue() > this.prime.intValue()) {
            BigInteger temp = this.prime;
            this.prime = this.generator;
            this.generator = temp;
        }

        this.publicKey = this.generator.modPow(this.privateKey, this.prime);

    }

    public void generateSharedKey(String ckey) {
        this.publicClientKey = new BigInteger(ckey, 10);

        this.sharedKey = this.publicClientKey.modPow(this.privateKey, this.prime);
    }

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

}

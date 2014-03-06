package com.cometproject.server.network.security;

import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman {
    public int BITLENGTH = 30;
    public BigInteger Prime;
    public BigInteger Generator;

    public BigInteger PrivateKey;
    public BigInteger PublicKey;

    public BigInteger PublicClientKey;
    public BigInteger SharedKey;
    private BigInteger zero = (new BigInteger("0"));

    public DiffieHellman() {
        this.init();
    }

    public DiffieHellman(int b) {
        this.BITLENGTH = b;

        this.init();
    }

    private void init() {
        this.PublicKey = new BigInteger("0");
        while (this.PublicKey.equals(zero)) {
            //this.Prime = BigInteger.probablePrime(BITLENGTH, new Random());
            //this.Generator = BigInteger.probablePrime(BITLENGTH, new Random());

            this.Prime = HabboEncryption.PRIME;
            this.Generator = HabboEncryption.GENERATOR;

            this.PrivateKey = new BigInteger(GenerateRandomHexString(30), 16);

            if (this.Generator.intValue() > this.Prime.intValue()) {
                BigInteger temp = this.Prime;
                this.Prime = this.Generator;
                this.Generator = temp;
            }

            this.PublicKey = this.Generator.modPow(this.PrivateKey, this.Prime);
        }
    }

    public DiffieHellman(BigInteger prime, BigInteger generator) {
        this.Prime = prime;
        this.Generator = generator;

        this.PrivateKey = new BigInteger(GenerateRandomHexString(BITLENGTH), 16);

        if (this.Generator.intValue() > this.Prime.intValue()) {
            BigInteger temp = this.Prime;
            this.Prime = this.Generator;
            this.Generator = temp;
        }

        this.PublicKey = this.Generator.modPow(this.PrivateKey, this.Prime);
    }

    public void GenerateSharedKey(String ckey) {
        this.PublicClientKey = new BigInteger(ckey, 10);

        this.SharedKey = this.PublicClientKey.modPow(this.PrivateKey, this.Prime);
    }

    public static String GenerateRandomHexString(int len) {
        int rand = 0;
        String result = "";

        Random rnd = new Random();

        for (int i = 0; i < len; i++) {
            rand = 1 + (int) (rnd.nextDouble() * 254); // 1 - 255
            result += Integer.toString(rand, 16);
        }
        return result;
    }
}
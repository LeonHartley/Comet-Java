package com.cometproject.server.network.security.exchange;

import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman {
    private final int BIT_LENGTH = 32;
    private final Random random = new Random();

    private BigInteger prime;
    private BigInteger generator;
    private BigInteger publicKey;
    private BigInteger privateKey;

    public DiffieHellman() {
        this.init();
    }

    private void init() {
        this.publicKey = null;

        this.prime = new BigInteger(BIT_LENGTH, random);
        this.generator = new BigInteger(BIT_LENGTH, random);

        byte[] bytes = new byte[BIT_LENGTH / 8];
        random.nextBytes(bytes);

        this.privateKey = new BigInteger(bytes);

        if(this.generator.compareTo(this.prime) == 1) {
            BigInteger temp = this.prime;
            this.prime = this.generator;
            this.generator = temp;
        }

//        this.publicKey = this.generator.modPow(this.privateKey, this.prime);
    }

    public BigInteger calculateSharedKey(BigInteger m) {
        return m.modPow(this.privateKey, this.prime);
    }

    public BigInteger getPrime() {
        return prime;
    }

    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }

    public BigInteger getGenerator() {
        return generator;
    }

    public void setGenerator(BigInteger generator) {
        this.generator = generator;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }
}
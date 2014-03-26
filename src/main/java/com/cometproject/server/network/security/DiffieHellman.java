package com.cometproject.server.network.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Matty on 10/03/2014.
 */
public class DiffieHellman {
    public final int BITLENGTH = 30;

    private BigInteger prime;
    private BigInteger generator;
    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger publicClientKey;
    private BigInteger sharedKey;

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

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public BigInteger getPublicClientKey() {
        return publicClientKey;
    }

    public void setPublicClientKey(BigInteger publicClientKey) {
        this.publicClientKey = publicClientKey;
    }

    public BigInteger getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(BigInteger sharedKey) {
        this.sharedKey = sharedKey;
    }

    public DiffieHellman(BigInteger prime, BigInteger generator) {
        this.prime = prime;
        this.generator = generator;

        this.privateKey = new BigInteger(generateRandomHexString(BITLENGTH), 16);

        if (this.generator.compareTo(this.prime) == 1) {
            BigInteger tmp = this.prime;
            this.prime = this.generator;
            this.generator = tmp;
        }

        this.publicKey = this.generator.modPow(this.privateKey, this.prime);
    }

    public void generateSharedKey(String key) {
        this.publicClientKey = new BigInteger(key, 10);
        this.sharedKey = this.publicClientKey.modPow(this.privateKey, this.prime);
    }

    public static String generateRandomHexString(int length) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < length){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, length);
    }
}

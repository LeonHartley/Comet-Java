package com.cometproject.server.network.security;

import org.apache.log4j.Logger;

import java.math.BigInteger;

public class Encryption {
    private RSA rsa;
    private RC4 rc4;
    private DiffieHellman diffieHellman;

    private boolean initialized = false;
    private Logger log = Logger.getLogger(Encryption.class.getName());

    public Encryption(BigInteger n, BigInteger e, BigInteger d) {
        this.rsa = new RSA(n, e, d, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
        this.rc4 = new RC4();
        this.diffieHellman = new DiffieHellman(200);
    }

    public boolean initialize(String text) {
        try {
            String publicKey = this.rsa.decrypt(text);

            this.diffieHellman.generateSharedKey(publicKey.replace(Character.toString((char) 0), ""));
            this.rc4.init(this.diffieHellman.sharedKey.toString().getBytes());

            this.initialized = true;
        } catch(Exception e) {
            log.error("Failed to initialize encryption", e);

            this.initialized = false;
        }

        return this.initialized;
    }

    public String getSharedKey() {
        return diffieHellman.sharedKey.toString();
    }

    public String getPublicKey() {
        return diffieHellman.publicKey.toString();
    }

    public String getPrivateKey() {
        return diffieHellman.privateKey.toString();
    }
}

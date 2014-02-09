package com.cometsrv.network.security;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSA {
    private static Logger logger = Logger.getLogger(RSA.class);
    private KeyPair keyPair;
    private Cipher cipher;
    private int blockSize;

    public RSA(String privateKey, String publicKey, String modulus, String primP, String primQ, String dmodp1, String dmodq1, String qinvmodp) {
        this.blockSize = (modulus.length() / 2);
        this.keyPair = this.getKeyPair(privateKey, publicKey, modulus, primP, primQ, dmodp1, dmodq1, qinvmodp);
    }

    public static void generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(new RSAKeyGenParameterSpec(keySize, new BigInteger("10001", 16)));
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            logger.info("priv_exp: " + privateKey.getPrivateExponent().toString(16));
            logger.info("pub_exp: " + publicKey.getPublicExponent().toString(16));
            logger.info("modulus: " + privateKey.getModulus().toString(16));
            logger.info("p_prim: " + privateKey.getPrimeP().toString(16));
            logger.info("q_prim: " + privateKey.getPrimeQ().toString(16));
            logger.info("dmodp1: " + privateKey.getPrimeExponentP().toString(16));
            logger.info("dmodq1: " + privateKey.getPrimeExponentQ().toString(16));
            logger.info("invpmodq: " + privateKey.getCrtCoefficient().toString(16));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean init() {
        String data = "testing123";
        try {
            this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // if this doesn't work change ECB to None
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return this.decrypt(this.encrypt(data)).equals(data);
    }

    public int getBlockSize() {
        return blockSize;
    }

    private KeyPair getKeyPair(String privateKey, String publicKey, String modulus, String primP, String primQ, String dmodp1, String dmodq1, String qinvmodp) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey rsaPublicKey = keyFactory.generatePublic(
                    new RSAPublicKeySpec(
                            new BigInteger(modulus, 16),
                            new BigInteger(publicKey, 16)
                    )
            );

            PrivateKey rsaPrivateKey = keyFactory.generatePrivate(
                    new RSAPrivateCrtKeySpec(
                            new BigInteger(modulus, 16),
                            new BigInteger(publicKey, 16),
                            new BigInteger(privateKey, 16),
                            new BigInteger(primP, 16),
                            new BigInteger(primQ, 16),
                            new BigInteger(dmodp1, 16),
                            new BigInteger(dmodq1, 16),
                            new BigInteger(qinvmodp, 16)
                    )
            );

            return new KeyPair(rsaPublicKey, rsaPrivateKey);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(byte[] data) {
        try {
            return new String(this.decryptRaw(data), "UTF8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    public byte[] decryptRaw(byte[] data) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.keyPair.getPrivate());

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                int length;
                for (int i = 0; i < data.length; i += this.getBlockSize()) {
                    length = Math.min(this.getBlockSize(), data.length - i);
                    outputStream.write(this.cipher.doFinal(data, i, length));
                }

                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public byte[] encrypt(String data) {
        try {
            return this.encryptRaw(data.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public byte[] encryptRaw(byte[] data) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.keyPair.getPublic());

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                int length;
                for (int i = 0; i < data.length; i += this.getBlockSize()) {
                    length = Math.min(this.getBlockSize(), data.length - i);
                    outputStream.write(this.cipher.doFinal(data, i, length));
                }

                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
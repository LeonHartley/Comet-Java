package com.cometproject.server.network.security;

import com.cometproject.server.network.sessions.Session;
import org.apache.log4j.Logger;

import java.math.BigInteger;

public class HabboEncryption extends DiffieHellman {
    public static final BigInteger N = new BigInteger("86851DD364D5C5CECE3C883171CC6DDC5760779B992482BD1E20DD296888DF91B33B936A7B93F06D29E8870F703A216257DEC7C81DE0058FEA4CC5116F75E6EFC4E9113513E45357DC3FD43D4EFAB5963EF178B78BD61E81A14C603B24C8BCCE0A12230B320045498EDC29282FF0603BC7B7DAE8FC1B05B52B2F301A9DC783B7", 16);
    public static final BigInteger E = new BigInteger("3");
    public static final BigInteger D = new BigInteger("59AE13E243392E89DED305764BDD9E92E4EAFA67BB6DAC7E1415E8C645B0950BCCD26246FD0D4AF37145AF5FA026C0EC3A94853013EAAE5FF1888360F4F9449EE023762EC195DFF3F30CA0B08B8C947E3859877B5D7DCED5C8715C58B53740B84E11FBC71349A27C31745FCEFEEEA57CFF291099205E230E0C7C27E8E1C0512B", 16);

    public static BigInteger PRIME = new BigInteger("114670925920269957593299136150366957983142588366300079186349531", 10);
    public static BigInteger GENERATOR = new BigInteger("1589935137502239924254699078669119674538324391752663931735947", 10);

    private RSA rsa;
    private Logger log = Logger.getLogger(HabboEncryption.class.getName());

    public HabboEncryption(BigInteger n, BigInteger e, BigInteger d) {
        super(PRIME, GENERATOR);

        this.rsa = new RSA(n, e, d, new BigInteger("0"), new BigInteger("0"), new BigInteger("0"), new BigInteger("0"), new BigInteger("0"));
    }

    public boolean initialize(Session client, String text) {
        try {
            String publicKey = this.rsa.decrypt(text);

            System.out.println("Pubkey: " + publicKey);

            this.generateSharedKey(publicKey.replace(Character.toString((char) 0), ""));

            return true;
        } catch(Exception e) {
            log.error("Failed to initialize HabboEncryption", e);
        }

        return false;
    }
}

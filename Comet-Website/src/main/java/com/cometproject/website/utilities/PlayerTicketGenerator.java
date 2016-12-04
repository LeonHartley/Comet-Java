package com.cometproject.website.utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PlayerTicketGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextTicket() {
        return new BigInteger(130, random).toString(21);
    }
}

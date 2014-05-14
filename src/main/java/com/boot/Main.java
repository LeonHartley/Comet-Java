package com.boot;

import com.cometproject.server.boot.Comet;

public class Main {
    /*
     *  Use this method for the boot so we can obfuscate the packages com.cometproject.*
     */
    public static void main(String[] args) {
        Comet.run(args);
    }
}

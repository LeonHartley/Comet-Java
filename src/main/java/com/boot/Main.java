package com.boot;

import com.cometproject.server.boot.Comet;


public class Main {
    /**
     * Boot the server from a separate package so if one day we decide to obfuscate the source,
     * we can do so without exposing Comet's true package structure.
     *
     * @param args Arguments passed to the instance
     */
    public static void main(String[] args) {
        Comet.run(args);
    }
}

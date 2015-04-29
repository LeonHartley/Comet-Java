package com.cometproject.tools.utils;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;


public class ScriptsLoader {
    public static String load(String fileName) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        File file = new File(fileName);

        try {
            String line;
            StringBuilder product = new StringBuilder();
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsolutePath()));

            while((line = in.readLine()) != null) {
                product.append(line)
                       .append("\n");
            }

            return product.toString();
        } catch(Exception e) {
            System.out.println("Failed to load scripts: " + fileName);
            e.printStackTrace();
        } finally {
            System.out.println("Load scripts (" + fileName + ") process took: " + (((double) stopwatch.elapsed(TimeUnit.MILLISECONDS)) / 1000));
        }

        return null;
    }
}

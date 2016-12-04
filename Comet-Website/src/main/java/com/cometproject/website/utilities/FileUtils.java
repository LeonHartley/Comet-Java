package com.cometproject.website.utilities;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtil;

public class FileUtils {
    private static Logger log = Logger.getLogger(FileUtils.class.getName());

    public static String getContents(String file) {
        FileInputStream fileInputStream = null;
        StringWriter stringWriter = new StringWriter();

        try {
            fileInputStream = new FileInputStream(file);

            IOUtil.copy(fileInputStream, stringWriter, "UTF-8");
        } catch(Exception e) {
            log.error("Error while loading file", e);
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch(Exception e) {
                log.warn("Failed to close stream", e);
            }
        }

        return stringWriter.toString();
    }
}

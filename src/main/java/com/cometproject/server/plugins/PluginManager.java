package com.cometproject.server.plugins;

import com.cometproject.server.plugins.types.PluginPlayer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;

public class PluginManager {
    public static final String ENGINE_NAME = "JavaScript";

    private ScriptEngineManager scriptEngineManager;
    private ScriptEngine scriptEngine;

    private static Logger log = Logger.getLogger(PluginManager.class.getName());

    public PluginManager() {
        scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByName(PluginManager.ENGINE_NAME);

        try {
            File folder = new File("./scripts/");
            int scriptCount = 0;

            for (int i = 0; i < folder.listFiles().length; i++) {
                File file = folder.listFiles()[i];
                if (file.isFile() && file.getName().endsWith(".js")) {
                    String content = FileUtils.fileRead(file.getAbsolutePath());

                    try {
                        scriptEngine.eval(content);

                    } catch (Exception ex) {
                        log.error("Error while parsing scripts", ex);
                    } finally {
                        scriptCount++;
                    }
                }
            }

            log.debug("Loaded " + scriptCount + " scripts");
        } catch (Exception e) {
            log.error("Error while loading script files", e);
        }
    }

    public void invokePlayerCommand(String command, PluginPlayer invokingPlayer) {
        try {
            ((Invocable) scriptEngine).invokeFunction(command, invokingPlayer);
        } catch (Exception e) {
            log.error("Error invoking Player command", e);
        }
    }
}

package com.cometproject.server.modules;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.events.EventHandler;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.server.IGameService;
import com.cometproject.server.modules.events.EventHandlerService;
import com.cometproject.server.utilities.Initializable;
import com.cometproject.server.utilities.JsonFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements Initializable {
    private static ModuleManager moduleManagerInstance;
    private static final Logger log = Logger.getLogger(ModuleManager.class.getName());

    private EventHandler eventHandler;
    private CometGameService gameService;

    private List<CometModule> modules;

    public ModuleManager() {
        this.eventHandler = new EventHandlerService();
        this.gameService = new CometGameService(this.eventHandler);
    }

    public static ModuleManager getInstance() {
        if (moduleManagerInstance == null) {
            moduleManagerInstance = new ModuleManager();
        }

        return moduleManagerInstance;
    }

    @Override
    public void initialize() {
        this.modules = new ArrayList<>();

        for (String moduleName : this.findModules()) {
            try {
                this.loadModule(moduleName);
            } catch (Exception e) {
                log.warn("Error while loading module: " + moduleName, e);
            }
        }
    }

    private List<String> findModules() {
        List<String> results = new ArrayList<>();

        File[] files = new File("./modules").listFiles();

        if (files == null) return results;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                results.add(file.getName());
            }
        }

        return results;
    }

    private void loadModule(String name) throws Exception {
        ClassLoader loader = URLClassLoader.newInstance(
                new URL[]{ new URL("jar:file:modules/" + name + "!/")},
                getClass().getClassLoader()
        );

        URL configJsonLocation = loader.getResource("plugin.json");

        if(configJsonLocation == null) throw new Exception("plugin.json does not exist");

        final ModuleConfig moduleConfig = JsonFactory.getInstance().fromJson(Resources.toString(configJsonLocation, Charsets.UTF_8), ModuleConfig.class);

        log.info("Loaded module: " + moduleConfig.getName());

        Class<?> clazz = Class.forName(moduleConfig.getEntryPoint(), true, loader);
        Class<? extends CometModule> runClass = clazz.asSubclass(CometModule.class);
        Constructor<? extends CometModule> ctor = runClass.getConstructor(IGameService.class);

        CometModule cometModule = ctor.newInstance(this.gameService);

        // test load event
        cometModule.loadModule();

        // test unload event
        cometModule.unloadModule();
    }
}

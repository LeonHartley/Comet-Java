package com.cometproject.server.modules;

import com.cometproject.api.events.EventHandler;
import com.cometproject.api.server.CometGameService;
import com.cometproject.server.modules.events.EventHandlerService;
import com.cometproject.server.modules.modules.TestModule;
import com.cometproject.server.utilities.Initializable;

public class ModuleManager implements Initializable {
    private static ModuleManager moduleManagerInstance;
    private EventHandler eventHandler;

    public ModuleManager() {
        this.eventHandler = new EventHandlerService();

        TestModule testModule = new TestModule(new CometGameService(this.eventHandler));
    }

    public static ModuleManager getInstance() {
        if(moduleManagerInstance == null) {
            moduleManagerInstance = new ModuleManager();
        }

        return moduleManagerInstance;
    }

    @Override
    public void initialize() {

    }
}

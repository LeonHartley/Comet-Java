package com.cometproject.server.modules.modules;

import com.cometproject.api.events.listeners.modules.ModuleEventListener;
import com.cometproject.api.events.modules.OnModuleLoadEvent;
import com.cometproject.api.events.modules.OnModuleUnloadEvent;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.server.CometGameService;

public class TestModule extends CometModule implements ModuleEventListener {
    public TestModule(CometGameService gameService) {
        super(gameService);
    }

    @Override
    public void onModuleLoad(OnModuleLoadEvent event) {

    }

    @Override
    public void onModuleUnload(OnModuleUnloadEvent event) {

    }
}

package com.cometsrv.game.items;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.items.interactions.InteractionManager;
import com.cometsrv.game.items.types.ItemDefinition;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;

public class ItemManager {
    private FastMap<Integer, ItemDefinition> itemDefinitions;
    private InteractionManager interactions;

    private Logger log = Logger.getLogger(ItemManager.class.getName());

    public ItemManager() {
        this.itemDefinitions = new FastMap<>();
        this.interactions = new InteractionManager();

        this.loadItemDefinitions();
    }

    public void loadItemDefinitions() {
        if(this.getItemDefinitions().size() >= 1) {
            this.getItemDefinitions().clear();
        }

        try {
            ResultSet items = Comet.getServer().getStorage().getTable("SELECT * FROM furniture");

            while(items.next()) {
                this.getItemDefinitions().put(items.getInt("id"), new ItemDefinition(items));
            }
        } catch(Exception e) {
            log.error("Error while loading item definitions", e);
        }

        log.info("Loaded " + this.getItemDefinitions().size() + " item definitions");
    }

    public ItemDefinition getDefintion(int itemId) {
        if(this.getItemDefinitions().containsKey(itemId)) {
            return this.getItemDefinitions().get(itemId);
        }

        return null;
    }

    public InteractionManager getInteractions() {
        return this.interactions;
    }

    public FastMap<Integer, ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }
}

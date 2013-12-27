package com.cometsrv.game.navigator;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.navigator.types.Category;
import com.cometsrv.game.navigator.types.FeaturedRoom;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.sql.ResultSet;

public class NavigatorManager {
    public FastList<Category> categories;
    public FastList<FeaturedRoom> featuredRooms;

    Logger log = Logger.getLogger(NavigatorManager.class.getName());

    public NavigatorManager() {
        this.categories = new FastList<>();
        this.featuredRooms = new FastList<>();

        this.loadCategories();
    }

    public void loadCategories() {
        try {
            if(this.getCategories().size() != 0) {
                this.getCategories().clear();
            }

            ResultSet result = Comet.getServer().getStorage().getRow("SELECT * FROM navigator_categories WHERE enabled = '1'");

            while(result.next()) {
                this.getCategories().add(new Category(result));
            }
        } catch(Exception e) {
            log.error("Error while loading navigator categories", e);
        }

        log.info("Loaded " + this.getCategories().size() + " room categories");
    }

    public Category getCategory(int id) {
        for(Category c : this.getCategories()) {
            if(c.getId() == id) {
                return c;
            }
        }

        return null;
    }

    public FastList<Category> getCategories() {
        return this.categories;
    }
}

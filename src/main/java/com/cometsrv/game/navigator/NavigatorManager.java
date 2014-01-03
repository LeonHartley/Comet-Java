package com.cometsrv.game.navigator;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.navigator.types.Category;
import com.cometsrv.game.navigator.types.featured.FeaturedRoom;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class NavigatorManager {
    public List<Category> categories;
    public List<FeaturedRoom> featuredRooms;

    Logger log = Logger.getLogger(NavigatorManager.class.getName());

    public NavigatorManager() {
        this.categories = new FastList<>();
        this.featuredRooms = new FastList<>();

        this.loadCategories();
        this.loadFeaturedRooms();
    }

    public void loadFeaturedRooms() {
        try {
            if(this.featuredRooms.size() != 0) {
                this.featuredRooms.clear();
            }

            ResultSet result = Comet.getServer().getStorage().getTable("SELECT * FROM navigator_featured_rooms WHERE enabled = '1'");

            if(result == null) {
                return;
            }

            while(result.next()) {
                this.featuredRooms.add(new FeaturedRoom(result));
            }
        } catch(Exception e) {
            log.error("Error while loading featured rooms", e);
        }

        log.info("Loaded " + this.featuredRooms.size() + " featured rooms");
    }

    public void loadCategories() {
        try {
            if(this.getCategories().size() != 0) {
                this.getCategories().clear();
            }

            ResultSet result = Comet.getServer().getStorage().getTable("SELECT * FROM navigator_categories WHERE enabled = '1'");

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

    public List<Category> getCategories() {
        return this.categories;
    }

    public List<FeaturedRoom> getFeaturedRooms() {
        return this.featuredRooms;
    }
}

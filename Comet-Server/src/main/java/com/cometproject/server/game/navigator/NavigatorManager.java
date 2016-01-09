package com.cometproject.server.game.navigator;

import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.navigator.types.categories.NavigatorCategoryType;
import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.storage.queries.navigator.NavigatorDao;
import com.cometproject.server.utilities.Initializable;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavigatorManager implements Initializable {
    private static NavigatorManager navigatorManagerInstance;

    private Map<Integer, Category> categories;
    private List<Category> userCategories;

    private List<FeaturedRoom> featuredRooms;

    Logger log = Logger.getLogger(NavigatorManager.class.getName());

    public NavigatorManager() {
    }

    @Override
    public void initialize() {
        this.loadCategories();
        this.loadFeaturedRooms();

        log.info("NavigatorManager initialized");
    }

    public static NavigatorManager getInstance() {
        if (navigatorManagerInstance == null)
            navigatorManagerInstance = new NavigatorManager();

        return navigatorManagerInstance;
    }

    public void loadFeaturedRooms() {
        try {
            if (this.featuredRooms != null && this.featuredRooms.size() != 0) {
                this.featuredRooms.clear();
            }

            this.featuredRooms = NavigatorDao.getFeaturedRooms();

        } catch (Exception e) {
            log.error("Error while loading featured rooms", e);
        }

        log.info("Loaded " + this.featuredRooms.size() + " featured rooms");
    }

    public void loadCategories() {
        try {
            if (this.categories != null && this.categories.size() != 0) {
                this.categories.clear();
            }

            if(this.userCategories == null) {
                this.userCategories = Lists.newArrayList();
            } else {
                this.userCategories.clear();
            }

            this.categories = NavigatorDao.getCategories();

            for(Category category : this.categories.values()) {
                if(category.getCategoryType() == NavigatorCategoryType.CATEGORY) {
                    this.userCategories.add(category);
                }
            }
        } catch (Exception e) {
            log.error("Error while loading navigator categories", e);
        }

        log.info("Loaded " + (this.getCategories() == null ? 0 : this.getCategories().size()) + " room categories");
    }

    public Category getCategory(int id) {
        for (Category c : this.getCategories().values()) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }

    public boolean isFeatured(int roomId) {
        for (FeaturedRoom room : featuredRooms) {
            if (room.getRoomId() == roomId)
                return true;
        }

        return false;
    }

    public FeaturedRoom getFeaturedRoomById(int roomId) {
        for (FeaturedRoom room : featuredRooms) {
            if (room.getRoomId() == roomId)
                return room;
        }

        return null;
    }

    public Map<Integer, Category> getCategories() {
        return this.categories;
    }

    public List<FeaturedRoom> getFeaturedRooms() {
        return featuredRooms;
    }

    public List<Category> getUserCategories() {
        return userCategories;
    }
}

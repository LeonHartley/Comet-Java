package com.cometproject.api.game;

import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.api.game.furniture.IFurnitureService;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.players.IPlayerService;
import com.cometproject.api.game.rooms.IRoomService;
import com.cometproject.api.game.rooms.models.IRoomModelService;
import org.apache.log4j.Logger;

public class GameContext {
    private static GameContext gameContext;

    private ICatalogService catalogService;
    private IFurnitureService furnitureService;
    private IGroupService groupService;
    private IPlayerService playerService;
    private IRoomService roomService;
    private IRoomModelService roomModelService;

    private final Logger logger = Logger.getLogger(GameContext.class);

    public ICatalogService getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(ICatalogService catalogService) {
        logger.info("CatalogService initialised, " + catalogService.getClass().getName());

        this.catalogService = catalogService;
    }

    public IFurnitureService getFurnitureService() {
        return this.furnitureService;
    }

    public void setFurnitureService(IFurnitureService furnitureService) {
        logger.info("FurnitureService initialised, " + furnitureService.getClass().getName());

        this.furnitureService = furnitureService;
    }

    public IGroupService getGroupService() {
         return this.groupService;
    }

    public void setGroupService(IGroupService groupService) {
        logger.info("GroupService initialised, " + groupService.getClass().getName());

        this.groupService = groupService;
    }

    public IPlayerService getPlayerService() {
        return this.playerService;
    }

    public void setPlayerService(IPlayerService playerService) {
        this.playerService = playerService;
    }

    public static GameContext getCurrent() {
        if(gameContext == null) {
            System.out.println("GameContext not configured");
            System.exit(0);
        }

        return gameContext;
    }

    public static void setCurrent(GameContext instance) {
        GameContext.gameContext = instance;
    }

    public IRoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(IRoomService roomService) {
        logger.info("RoomService initialised, " + roomService.getClass().getName());

        this.roomService = roomService;
    }

    public IRoomModelService getRoomModelService() {
        return roomModelService;
    }

    public void setRoomModelService(IRoomModelService roomModelService) {
        logger.info("RoomModelService initialised, " + roomModelService.getClass().getName());

        this.roomModelService = roomModelService;
    }
}

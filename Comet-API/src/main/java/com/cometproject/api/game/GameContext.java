package com.cometproject.api.game;

import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.api.game.furniture.IFurnitureService;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.players.IPlayerService;
import com.cometproject.api.game.rooms.IRoomService;

public class GameContext {
    private static GameContext gameContext;

    private ICatalogService catalogService;
    private IFurnitureService furnitureService;
    private IGroupService groupService;
    private IPlayerService playerService;
    private IRoomService roomService;

    public ICatalogService getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(ICatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public IFurnitureService getFurnitureService() {
        return this.furnitureService;
    }

    public void setFurnitureService(IFurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    public IGroupService getGroupService() {
         return this.groupService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public IPlayerService getPlayerService() {
        return this.playerService;
    }

    public void setPlayerService(IPlayerService playerService) {
        this.playerService = playerService;
    }

    public static GameContext current() {
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
        this.roomService = roomService;
    }
}

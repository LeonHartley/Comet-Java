package com.cometsrv.game.rooms.avatars;

/*
public class Avatar {
    private Player player;
    private Room room;

    private int roomId;
    private boolean isFullyLoaded;
    public boolean inRoom;

    public boolean authOK;

    private FastMap<String, String> statuses;
    private Position3D position;

    private int bodyRotation;
    private int headRotation;

    public boolean needsUpdate;
    public boolean needsRemove;
    public boolean isIdle;

    public boolean isMoving;
    public boolean isSitting;
    public boolean isMoonwalking;
    private boolean isTeleporting;
    public boolean isWarping;
    private boolean isInvisible;
    private int danceId;
    private int effectId;

    private Position3D positionToSet;
    private LinkedList<Square> path;
    private Pathfinder pathfinder;

    private int goalX;
    private int goalY;

    public int idleTime;
    public int signTime;

    private GameTeam gameTeam;

    public Avatar(Player player) {
        this.player = player;
        this.inRoom = true;
        this.authOK = false;
        this.isFullyLoaded = false;

        this.roomId = 0;
        this.room = null;

        this.needsRemove = false;
        this.needsUpdate = false;

        this.isMoving = false;
        this.isMoonwalking = false;
        this.isSitting = false;
        this.isTeleporting = false;
        this.isWarping = false;
        this.isInvisible = false;
        this.isIdle = false;

        this.signTime = 0;
        this.idleTime = 0;
        this.danceId = 0;
        this.effectId = 0;

        this.statuses = new FastMap<>();
        this.position = new Position3D(0, 0, 0);
        this.gameTeam = GameTeam.NONE;
    }

    public void prepareRoom(Room room, String password) {
        if(this.getRoom() != null) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        this.room = room;
        this.roomId = room.getId();

        if(!room.isActive) {
            room.load();
        }

        if(room.getAvatars().count() >= room.getData().getMaxUsers() && !this.getPlayer().getPermissions().hasPermission("room_enter_full")) {
            this.getPlayer().getSession().send(RoomFullMessageComposer.compose());
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        if(room.getRights().hasBan(this.getPlayer().getData().getId()) && !this.getPlayer().getPermissions().hasPermission("room_unkickable")) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
            return;
        }
        boolean isOwner = room.getData().getOwnerId() == this.getPlayer().getId();

        // TODO: Door bell + password parsing
        if(!isOwner && !this.getPlayer().getPermissions().hasPermission("room_enter_locked")) {
            if(room.getData().getAccess().equals("password") && !room.getData().getPassword().equals(password)) {
                // TODO: Send invalid password message
                this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
                return;
            }
            else if(room.getData().getAccess().equals("doorbell")) {
                //TODO: doorbell
            }
        }

        this.authOK = true;

        if(!this.enterRoom()) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
        }
    }

    private boolean enterRoom() {
        this.getPlayer().getSession().send(ModelAndIdMessageComposer.compose(this.getRoom().getModel().getId(), this.getPlayer().getId()));

        for(String decoration : this.room.getData().getDecorations()) {
            String[] deco = decoration.split("=");

            if(deco[0].equals("wallpaper") || deco[0].equals("floor")) {
                if(deco[1].equals("0.0")) {
                    continue;
                }
            }

            this.getPlayer().getSession().send(PapersMessageComposer.compose(deco[0], deco[1]));
        }

        int accessLevel = 0;

        if(this.getRoom().getData().getOwnerId() == this.getPlayer().getId() || this.getPlayer().getPermissions().hasPermission("full_room_access")) {
            this.statuses.put("flatctrl 4", "useradmin");
            accessLevel = 4;
        }

        else if(this.getRoom().getRights().hasRights(this.getPlayer().getId())) {
            this.statuses.put("flatctrl 1", "");
            accessLevel = 1;
        }

        this.getPlayer().getSession().send(AccessLevelMessageComposer.compose(accessLevel));

        if(this.getRoom().getData().getOwnerId() == this.getPlayer().getId()) {
            this.getPlayer().getSession().send(OwnerRightsMessageComposer.compose());
        }

        return true;
    }

    public void moveTo(int x, int y) {
        this.setGoal(x, y);

        LinkedList<Square> path = this.getPathfinder().makePath();

        if(path == null) {
            return;
        }

        this.unidle();
        this.setPath(path);
        this.isMoving = true;
    }

    public void moveToAndInteract(int x, int y) {

    }

    public void warpTo(int x, int y) {
        this.setGoal(x, y);
        this.unidle();

        this.isWarping = true;
    }

    public boolean onChat(String message) {
        try {
            long time = System.currentTimeMillis();

            if(time - player.lastMessage < 500) {
                player.floodFlag++;

                if(player.floodFlag >= 4) {
                    player.floodTime = 30;
                    player.floodFlag = 0;

                    player.getSession().send(FloodFilterMessageComposer.compose(player.floodTime));
                }
            }

            if(player.floodTime >= 1) {
                return false;
            }

            player.lastMessage = time;

            if(message.startsWith(":")) {
                if(GameEngine.getCommands().isCommand(message.substring(1))) {
                    GameEngine.getCommands().parse(message.substring(1), this.getPlayer().getSession());
                    return false;
                }
            }

            if(this.getRoom().getWired().trigger(TriggerType.ON_SAY, message, this))
                return false;

            if(CometSettings.logChatToConsole) {
                this.getRoom().log.info(this.getPlayer().getData().getUsername() + ": " + message);
            }

            this.getRoom().getChatlog().add(message, this.getPlayer().getId());

            this.unidle();
            return true;

        } catch(Exception e) {
            this.getRoom().log.error("Error wile parsing chat message", e);
            return false;
        }
    }

    public void unidle() {
        this.idleTime = 0;

        if(isIdle) {
            this.getRoom().getAvatars().broadcast(IdleStatusMessageComposer.compose(this.getPlayer().getId(), false));
        }

        isIdle = false;
    }

    public void idle() {
        this.getRoom().getAvatars().broadcast(IdleStatusMessageComposer.compose(this.getPlayer().getId(), true));
        this.isIdle = true;
    }

    public void dispose(boolean isOffline, boolean isKick, boolean toHotelView) {
        this.clearStatuses();
        this.getRoom().getAvatars().broadcast(LeaveRoomMessageComposer.compose(this.getPlayer().getId()));

        if(!isOffline && toHotelView) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
        }

        Trade trade = getRoom().getTrade().get(this.getPlayer().getSession());

        if(trade != null) {
            trade.cancel(getPlayer().getId(), true);
        }

        this.getRoom().getAvatars().remove(this);

        if(this.getRoom().getAvatars().count() == 0) {
            this.getRoom().dispose();
        }

        if(pathfinder != null) {
            this.pathfinder.dispose();
            this.pathfinder = null;
            this.path.clear();
        }

        this.position = null;
        this.room = null;

        this.getPlayer().setAvatar(null);
        this.player = null;

    }

    public void applyEffect(int effect) {
        this.effectId = effect;
        this.room.getAvatars().broadcast(ApplyEffectMessageComposer.compose(this.getPlayer().getId(), effectId));
    }

    public void dispose() {
        this.dispose(true, false, false);
    }

    public Position3D getPositionToSet() { return this.positionToSet; };

    public LinkedList<Square> getPath() {
        return this.path;
    }

    public void setPath(LinkedList<Square> path) {
        this.path = path;
    }

    public Pathfinder getPathfinder() {
        return this.pathfinder;
    }

    public void setPathfinder() {
        this.pathfinder = new Pathfinder(this);
    }

    public void setGoal(int x, int y) {
        this.goalX = x;
        this.goalY = y;
    }

    public void setPosition(Position3D pos) {
        this.position = pos;
    }

    public int getGoalX() {
        return this.goalX;
    }

    public int getGoalY() {
        return this.goalY;
    }

    public Map<String, String> getStatuses() {
        return this.statuses;
    }

    public Position3D getPosition() {
        return this.position;
    }

    public int getBodyRotation() {
        return this.bodyRotation;
    }

    public int getHeadRotation() {
        return this.headRotation;
    }

    public void setBodyRotation(int r) {
        this.bodyRotation = r;
    }

    public void setHeadRotation(int r) {
        this.headRotation = r;
    }

    public boolean getIsInvisible() {
        return this.isInvisible;
    }

    public void setIsInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public int getDanceId() {
        return this.danceId;
    }

    public void setDanceId(int danceId) {
        this.danceId = danceId;
    }

    public int getEffectId() {
        return this.effectId;
    }

    public boolean hasStatus(String status) {
        return this.getStatuses().containsKey(status);
    }

    public void clearStatuses() {
        this.getStatuses().clear();
    }

    public void removeStatus(String status) {
        if(this.hasStatus(status)) {
            this.getStatuses().remove(status);
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public Room getRoom() {
        return this.room;
    }

    public int getRoomId() {
        return this.roomId;
    }

    public boolean isLoaded() {
        return this.isFullyLoaded;
    }

    public boolean getIsTeleporting() {
        return this.isTeleporting;
    }

    public void setIsTeleporting(boolean isTeleporting) {
        this.isTeleporting = isTeleporting;
    }

    public void setNeedsUpdate(boolean b) {
        this.needsUpdate = b;
    }

    public boolean getNeedsUpdate() {
        return this.needsUpdate;
    }

    public void setGameTeam(GameTeam team) {
        this.gameTeam = team;
    }

    public GameTeam getTeam() {
        return this.gameTeam;
    }

    public boolean isTeamed() {
        return (this.gameTeam != GameTeam.NONE);
    }
}
*/
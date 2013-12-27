package com.cometsrv.game.rooms.types.components;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.network.messages.types.Composer;
import com.cometsrv.network.sessions.Session;
import javolution.util.FastMap;

public class AvatarComponent {
    private FastMap<Integer, Avatar> avatars;
    private int virtualCounter = 0;
    private Room room;

    public AvatarComponent(Room room) {
        this.room = room;
        this.avatars = new FastMap<>();
    }

    public void dispose() {
        this.avatars.clear();
        this.avatars = null;

        this.room = null;
    }

    public Avatar get(int id) {
        if(this.avatars.containsKey(id)) {
            return this.avatars.get(id);
        }

        return null;
    }

    public void remove(Avatar avatar) {
        if(this.avatars.containsKey(avatar.getPlayer().getId())) {
            this.avatars.remove(avatar.getPlayer().getId());
        }
    }

    public boolean isSquareAvailable(int x, int y) {
        for(Avatar avatar : this.getAvatars().values()) {
            if(avatar.getPosition().getX() == x && avatar.getPosition().getY() == y) {
                return false;
            }
        }

        return true;
    }

    public Avatar getAvatarAt(int x, int y) {
        for(Avatar avatar : this.getAvatars().values()) {
            if(avatar.getPosition().getX() == x && avatar.getPosition().getY() == y) {
                return avatar;
            }
        }

        return null;
    }

    public int count() {
        return this.avatars.size();
    }

    public boolean addAvatar(Avatar avatar) {
        if(avatar == null) {
            return false;
        }

        if(avatar.getRoom() == null || avatar.getRoomId() != this.room.getId()) {
            return false;
        }

        RoomModel model = this.room.getModel();
        avatar.getPosition().setX(model.getDoorX());
        avatar.getPosition().setY(model.getDoorY());
        avatar.getPosition().setZ(model.getDoorZ());

        avatar.setBodyRotation(model.getDoorRotation());
        avatar.setHeadRotation(model.getDoorRotation());

        this.getAvatars().put(avatar.getPlayer().getId(), avatar);
        avatar.setNeedsUpdate(true);
        return true;
    }

    public void broadcast(Composer msg) {
        Session client;

        for(Avatar avatar : this.getAvatars().values()) {
            client = avatar.getPlayer().getSession();

            if(client != null) {
                client.send(msg);
            }
        }
    }

    public synchronized FastMap<Integer, Avatar> getAvatars() {
        return this.avatars;
    }

    public void clear() {
        this.avatars.clear();
    }
}

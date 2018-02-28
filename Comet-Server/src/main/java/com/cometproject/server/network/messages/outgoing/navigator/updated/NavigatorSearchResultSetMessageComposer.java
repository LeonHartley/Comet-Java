package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.settings.RoomAccessType;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.navigator.types.categories.NavigatorSearchAllowance;
import com.cometproject.server.game.navigator.types.categories.NavigatorViewMode;
import com.cometproject.server.game.navigator.types.search.NavigatorSearchService;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

public class NavigatorSearchResultSetMessageComposer extends MessageComposer {

    private final String category;
    private final String data;
    private final List<Category> categories;
    private final Player player;
    private int invisibleRooms;

    public NavigatorSearchResultSetMessageComposer(String category, String data, List<Category> categories, Player player) {
        this.category = category;
        this.data = data;
        this.categories = categories;
        this.player = player;
        this.invisibleRooms = 0;
    }

    @Override
    public short getId() {
        return Composers.NavigatorSearchResultSetMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.category);
        msg.writeString(this.data);

        if (this.categories == null) {
            msg.writeInt(1);
            msg.writeString("query");
            msg.writeString("");

            msg.writeInt(NavigatorSearchAllowance.getIntValue(NavigatorSearchAllowance.NOTHING));
            msg.writeBoolean(false);
            msg.writeInt(0);

            List<IRoomData> rooms = NavigatorSearchService.order(RoomManager.getInstance().getRoomsByQuery(this.data), 50);

            for (IRoomData roomData : rooms) {
                if (roomData.getAccess() == RoomAccessType.INVISIBLE && player.getData().getRank() < 3) {
                    Room room = RoomManager.getInstance().get(roomData.getId());

                    if (room.getGroup() != null) {
                        if (!player.getGroups().contains(room.getGroup().getId())) {
                            invisibleRooms++;
                        }
                    } else {
                        if (!room.getRights().hasRights(player.getId())) {
                            invisibleRooms++;
                        }
                    }
                }
            }

            msg.writeInt(rooms.size() - invisibleRooms);

            for (IRoomData roomData : rooms) {
                if (roomData.getAccess() == RoomAccessType.INVISIBLE && player.getData().getRank() < 3) {
                    Room room = RoomManager.getInstance().get(roomData.getId());
                    if (room.getGroup() != null) {
                        if (!player.getGroups().contains(room.getGroup().getId())) {
                            continue;
                        }
                    } else {
                        if (!room.getRights().hasRights(player.getId())) {
                            continue;
                        }
                    }
                }

                RoomWriter.write(roomData, msg);
            }

            rooms.clear();
        } else {
            msg.writeInt(this.categories.size());

            for (Category category : this.categories) {
                msg.writeString(category.getCategoryId());
                msg.writeString(category.getPublicName());

                msg.writeInt(NavigatorSearchAllowance.getIntValue(category.getSearchAllowance()));
                msg.writeBoolean(false);//is minimised
                msg.writeInt(this.player.getNavigator().getViewModes().containsKey(category.getCategoryId()) ?
                        this.player.getNavigator().getViewModes().get(category.getCategoryId()) :
                        category.getViewMode() == NavigatorViewMode.REGULAR ? 0 :
                                category.getViewMode() == NavigatorViewMode.THUMBNAIL ? 1 : 0);

                List<IRoomData> rooms = NavigatorSearchService.getInstance().search(category, this.player, this.categories.size() == 1);

                msg.writeInt(rooms.size());// size of rooms found.

                for (IRoomData roomData : rooms) {
                    RoomWriter.write(roomData, msg);
                }

                rooms.clear();
            }
        }
    }

    @Override
    public void dispose() {
        if (this.categories != null)
            this.categories.clear();
    }
}

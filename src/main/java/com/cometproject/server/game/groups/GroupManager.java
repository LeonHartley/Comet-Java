package com.cometproject.server.game.groups;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.items.*;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class GroupManager {
    private Map<Integer, Group> groups;
    private List<GroupBase> bases;
    private List<GroupSymbol> symbols;
    private List<GroupBaseColour> baseColours;
    private Map<Integer, GroupSymbolColour> symbolColours;
    private Map<Integer, GroupBackgroundColour> backgroundColours;

    Logger log = Logger.getLogger(GroupManager.class.getName());

    public GroupManager() {
        groups = new FastMap<>();

        //loadItems();
    }

    /*public void loadItems() {
        bases = new ArrayList<>();
        symbols = new ArrayList<>();
        baseColours = new ArrayList<>();
        symbolColours = new FastMap<>();
        backgroundColours = new FastMap<>();

        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM group_items WHERE enabled = '1'");

            while (data.next()) {
                switch (data.getString("type")) {
                    case "base":
                        bases.add(new GroupBase(data));
                        break;

                    case "symbol":
                        symbols.add(new GroupSymbol(data));
                        break;

                    case "color":
                        baseColours.add(new GroupBaseColour(data));
                        break;

                    case "color2":
                        symbolColours.put(data.getInt("id"), new GroupSymbolColour(data));
                        break;

                    case "color3":
                        backgroundColours.put(data.getInt("id"), new GroupBackgroundColour(data));
                        break;
                }
            }

        } catch (Exception e) {
            log.error("Error while loading group items", e);
        }
    }

    public Group getGroup(int id) throws SQLException {
        Group instance = null;
        if (groups.containsKey(id)) {
            instance = groups.get(id);
        }

        if (instance != null)
            return instance;

        PreparedStatement statement = Comet.getServer().getStorage().prepare("SELECT * FROM groups WHERE id = ?");
        statement.setInt(1, id);

        ResultSet data = statement.executeQuery();

        while (data.next()) {
            GroupData group = new GroupData(data);
            List<GroupMember> members = new ArrayList<>();

            PreparedStatement memberStd = Comet.getServer().getStorage().prepare("SELECT * FROM group_memberships WHERE group_id = ?");
            memberStd.setInt(1, id);

            ResultSet memberSet = memberStd.executeQuery();

            while (memberSet.next()) {
                members.add(new GroupMember(memberSet));
            }

            instance = new Group(group, members);
            this.groups.put(instance.getId(), instance);
        }

        return instance;
    }*/

    public Group createGroup(String name, String description, int roomId, String badge, Session client, int colour1, int colour2) {
       /* try {
            PreparedStatement std = Comet.getServer().getStorage().prepare("INSERT into groups (`name`, `desc`, `badge`, `owner_id`, `created`, `room_id`, `colour1`, `colour2`) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");

            std.setString(1, name);
            std.setString(2, description);
            std.setString(3, badge);
            std.setInt(4, client.getPlayer().getId());
            std.setLong(5, Comet.getTime());
            std.setInt(6, roomId);
            std.setInt(7, colour1);
            std.setInt(8, colour2);

            std.execute();
        } catch (Exception e) {
            log.error("Error while creating group", e);
        }*/
        return null;
    }/*

    // author: Carlos D.
    public String generateBadge(int groupBase, int groupBaseColor, List<Integer> list) {
        StringBuilder image = new StringBuilder("b" + groupBase + groupBaseColor);

        for (int i = 0; i < 3 * 4; i += 3) {
            if (i >= list.size())
                image.append("s");
            else
                image.append(list.get(i) - 20 + "" + list.get(i + 1) + "" + list.get(i + 2));
        }

        return image.toString();
    }

    public String checkSymbol(String symbol) {
        if (symbol.equals("s000") || symbol.equals("s00000")) {
            return "";
        }

        return symbol;
    }

    public synchronized Map<Integer, Group> getGroups() {
        return this.groups;
    }

    public List<GroupBase> getBases() {
        return this.bases;
    }

    public List<GroupSymbol> getSymbols() {
        return this.symbols;
    }

    public List<GroupBaseColour> getBaseColours() {
        return this.baseColours;
    }

    public Map<Integer, GroupSymbolColour> getSymbolColours() {
        return this.symbolColours;
    }

    public Map<Integer, GroupBackgroundColour> getBackgroundColours() {
        return this.backgroundColours;
    }*/
}

package com.cometproject.server.game.groups;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.groups.types.items.*;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        loadItems();
    }

    public void loadItems() {
        bases = new FastList<>();
        symbols = new FastList<>();
        baseColours = new FastList<>();
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
            List<GroupMember> members = new FastList<>();

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
    }

    public Group createGroup(String name, String description, int roomId, String badge, Session client, int colour1, int colour2) {
        try {
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
        }
        return null;
    }

    // author: Carlos D.
    public String generateBadge(int groupBase, int groupBaseColor, List<Integer> list) {
        /*String str;
        int num;

        String str2 = "b";
        if (String.valueOf(groupBase).length() >= 2) {
            str2 = str2 + groupBase;
        }
        else {
            str2 = str2 + "0" + groupBase;
        }
        str = String.valueOf(groupBaseColor);
        if (str.length() >= 2) {
            str2 = str2 + str;
        }
        else if (str.length() <= 1) {
            str2 = str2 + "0" + str;
        }
        int num2 = 0;
        if (list.get(9) != 0) {
            num2 = 4;
        }
        else if (list.get(6) != 0)
        {
            num2 = 3;
        }
        else if (list.get(3) != 0) {
            num2 = 2;
        }
        else if (list.get(0) != 0) {
            num2 = 1;
        }

        int num3 = 0;

        for (int i = 0; i < num2; i++) {
            str2 = str2 + "s";
            num = list.get(num3) - 20;

            if (String.valueOf(num).length() >= 2) {
                str2 = str2 + num;
            }
            else {
                str2 = str2 + "0" + num;
            }

            int num5 = list.get(1 + num3);
            str = String.valueOf(num5);

            if (str.length() >= 2) {
                str2 = str2 + str;
            }
            else if (str.length() <= 1) {
                str2 = str2 + "0" + str;
            }
            str2 = str2 + list.get(2 + num3).toString();
            switch (num3) {
                case 0:
                    num3 = 3;
                    break;

                case 3:
                    num3 = 6;
                    break;

                case 6:
                    num3 = 9;
                    break;
            }
        }
        return str2;*/

        /*
        internal string GenerateGuildImage(int GuildBase, int GuildBaseColor, List<int> states)
        {
            StringBuilder image = new StringBuilder(String.Format("b{0:00}{1:00}", GuildBase, GuildBaseColor));

            for (int i = 0; i < 3 * 4; i += 3)
            {
                if (i >= states.Count)
                    image.Append("s");
                else
                    image.Append(String.Format("s{0:00}{1:00}{2}", states[i] - 20, states[i + 1], states[i + 2]));
            }

            return image.ToString();
        }
*/
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
    }
}

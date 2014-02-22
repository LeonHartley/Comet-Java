package com.cometproject.server.game.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.types.Ban;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class BanManager {
    private Map<String, Ban> bans;

    Logger logger = Logger.getLogger(BanManager.class.getName());

    public BanManager() {
        loadBans();
    }

    public void loadBans() {
        if(bans != null)
            bans.clear();
        else
            bans = new FastMap<>();

        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM bans WHERE expire = 0 OR expire > " + Comet.getTime());

            while(data.next()) {
                this.bans.put(data.getString("data"), new Ban(data));
            }

            logger.info("Loaded " + this.bans.size() + " bans");
        } catch(Exception e) {
            logger.error("Error while loading bans", e);
        }
    }

    public void add(Ban ban) {
        this.bans.put(ban.getData(), ban);
    }

    public boolean hasBan(String data) {
        return this.bans.containsKey(data);
    }

    public Ban get(String data) {
        return this.bans.get(data);
    }
}

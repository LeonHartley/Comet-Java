package com.cometproject.server.game.landing;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.landing.types.PromoArticle;
import com.cometproject.server.storage.queries.landing.LandingDao;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LandingManager implements Initialisable {
    private static final Logger log = Logger.getLogger(LandingManager.class.getName());
    private static LandingManager landingManagerInstance;
    private Map<Integer, PromoArticle> articles;

    private Map<PlayerAvatar, Integer> hallOfFame;

    public LandingManager() {
    }

    public static LandingManager getInstance() {
        if (landingManagerInstance == null) {
            landingManagerInstance = new LandingManager();
        }

        return landingManagerInstance;
    }

    @Override
    public void initialize() {
        this.loadArticles();
        this.loadHallOfFame();

        log.info("LandingManager initialized");
    }

    public void loadArticles() {
        if (this.articles != null) {
            this.articles.clear();
        }

        this.articles = LandingDao.getArticles();
    }

    private void loadHallOfFame() {
        if (this.hallOfFame != null) {
            this.hallOfFame.clear();
        }

        if (CometSettings.hallOfFameEnabled) {
            this.hallOfFame = LandingDao.getHallOfFame(CometSettings.hallOfFameCurrency, 10);

            // Queue it to be refreshed again in X minutes
            CometThreadManager.getInstance().executeSchedule(
                    this::loadHallOfFame,
                    CometSettings.hallOfFameRefreshMinutes,
                    TimeUnit.MINUTES);
        }
    }

    public Map<Integer, PromoArticle> getArticles() {
        return articles;
    }

    public Map<PlayerAvatar, Integer> getHallOfFame() {
        return this.hallOfFame;
    }
}

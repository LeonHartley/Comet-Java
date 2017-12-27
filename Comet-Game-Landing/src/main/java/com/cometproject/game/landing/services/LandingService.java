package com.cometproject.game.landing.services;

import com.cometproject.api.game.landing.ILandingService;
import com.cometproject.api.game.landing.types.IHallOfFame;
import com.cometproject.api.game.landing.types.IPromoArticle;
import com.cometproject.storage.api.data.Data;
import com.cometproject.storage.api.repositories.landing.IHallOfFameRepository;
import com.cometproject.storage.api.repositories.landing.IPromoArticleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class LandingService implements ILandingService {

    private final List<IPromoArticle> promoArticles;
    private final List<IHallOfFame> hallOfFames;

    private final IPromoArticleRepository promoArticleRepository;
    private final IHallOfFameRepository hallOfFameRepository;

    private ScheduledExecutorService executorService;

    public LandingService(ScheduledExecutorService executorService, IHallOfFameRepository hallOfFameRepository, IPromoArticleRepository promoArticleRepository) {
        this.executorService = executorService;
        this.hallOfFameRepository = hallOfFameRepository;
        this.promoArticleRepository = promoArticleRepository;
        this.promoArticles = new ArrayList<>();
        this.hallOfFames = new ArrayList<>();
        this.loadArticles();
        this.loadHallOfFame();
    }

    @Override
    public void loadHallOfFame() {
        if (this.hallOfFames.size() > 0) {
            this.hallOfFames.clear();
        }

        final Data<List<IHallOfFame>> data = new Data<>();

        this.hallOfFameRepository.getHallOfFamers(data::set, "vip_points", 10);

        if (data.has()) {
            this.hallOfFames.addAll(data.get());
        }

        this.executorService.schedule(this::loadHallOfFame, 1, TimeUnit.MINUTES);
    }

    @Override
    public void loadArticles() {
        if (this.promoArticles.size() > 0) {
            this.promoArticles.clear();
        }

        final Data<List<IPromoArticle>> data = new Data<>();

        this.promoArticleRepository.getPromoArticles(data::set);

        if (data.has()) {
            this.promoArticles.addAll(data.get());
        }
    }

    @Override
    public List<IPromoArticle> getArticles() {
        return this.promoArticles;
    }

    @Override
    public List<IHallOfFame> getHallOfFames() {
        return this.hallOfFames;
    }
}

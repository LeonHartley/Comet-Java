package com.cometproject.storage.mysql.models.landing;

import com.cometproject.api.game.landing.types.IPromoArticle;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class PromoArticle implements IPromoArticle {

    private int id;
    private String title;
    private String message;
    private String buttonText;
    private String buttonLink;
    private String imagePath;

    public PromoArticle(int id, String title, String message, String buttonText, String buttonLink, String imagePath) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
        this.buttonLink = buttonLink;
        this.imagePath = imagePath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public String getButtonLink() {
        return buttonLink;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

}

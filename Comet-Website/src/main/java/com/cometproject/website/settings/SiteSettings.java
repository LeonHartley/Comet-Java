package com.cometproject.website.settings;

import com.cometproject.website.storage.dao.settings.SiteSettingsDao;
import org.apache.velocity.anakia.Escape;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SiteSettings {
    private String hotelName = "Comet";
    private String hotelSlogan = "Default hotel slogan - change in housekeeping!";
    private String hotelDescription = "Default hotel description - change in housekeeping!";

    private boolean useFacebook = false;
    private String facebookUsername = "";

    private boolean useTwitter = false;
    private String twitterUsername = "";
    private String twitterWidgetId = "0";

    private String playerDefaultFigure = "";
    private String playerDefaultMotto = "";
    private int playerDefaultCredits = 0;
    private int playerDefaultActivityPoints = 0;
    private int playerDefaultVipPoints = 0;
    private int playerDefaultHomeRoom = 0;

    private String gameHost = "";
    private int gamePort = 0;
    private String gameClientSwf = "";
    private String gameClientVariables = "";
    private String gameClientTexts = "";
    private String gameClientProductData = "";
    private String gameClientFurniData = "";
    private String gameClientBanner = "";
    private String gameClientBase = "";

    public SiteSettings(ResultSet data) throws SQLException {
        this.hotelName = data.getString("hotel_name");
        this.hotelSlogan = data.getString("hotel_slogan");
        this.hotelDescription = data.getString("hotel_description");

        this.facebookUsername = data.getString("facebook_username");
        this.useFacebook = !this.facebookUsername.isEmpty();

        this.twitterUsername = data.getString("twitter_username");
        this.twitterWidgetId = data.getString("twitter_widget_id");
        this.useTwitter = !this.twitterUsername.isEmpty();

        this.playerDefaultFigure = data.getString("player_default_figure");
        this.playerDefaultMotto = data.getString("player_default_motto");
        this.playerDefaultCredits = data.getInt("player_default_credits");
        this.playerDefaultActivityPoints = data.getInt("player_default_activity_points");
        this.playerDefaultVipPoints = data.getInt("player_default_vip_points");
        this.playerDefaultHomeRoom = data.getInt("player_default_homeroom");

        this.gameHost = data.getString("game_host");
        this.gamePort = data.getInt("game_port");
        this.gameClientSwf = data.getString("game_client_swf");
        this.gameClientVariables = data.getString("game_client_variables");
        this.gameClientTexts = data.getString("game_client_texts");
        this.gameClientProductData = data.getString("game_client_productdata");
        this.gameClientFurniData = data.getString("game_client_furnidata");
        this.gameClientBanner = data.getString("game_client_banner");
        this.gameClientBase = data.getString("game_client_base");
    }

    public SiteSettings() {}

    public String getHotelName() {
        return Escape.getText(hotelName);
    }

    public String getHotelSlogan() {
        return Escape.getText(hotelSlogan);
    }

    public String getHotelDescription() {
        return hotelDescription.replace("\"", "\\\"");
    }

    public boolean useFacebook() {
        return useFacebook;
    }

    public boolean useTwitter() {
        return useTwitter;
    }

    public String getFacebookUsername() {
        return this.facebookUsername;
    }

    public String getTwitterUsername() {
        return this.twitterUsername;
    }

    public String getTwitterWidgetId() {
        return twitterWidgetId;
    }

    public String getPlayerDefaultMotto() {
        return playerDefaultMotto;
    }

    public int getPlayerDefaultCredits() {
        return playerDefaultCredits;
    }

    public int getPlayerDefaultActivityPoints() {
        return playerDefaultActivityPoints;
    }

    public int getPlayerDefaultVipPoints() {
        return playerDefaultVipPoints;
    }

    public String getPlayerDefaultFigure() {
        return playerDefaultFigure;
    }

    public int getPlayerDefaultHomeRoom() {
        return playerDefaultHomeRoom;
    }

    public String getGameHost() {
        return gameHost;
    }

    public int getGamePort() {
        return gamePort;
    }

    public String getGameClientSwf() {
        return gameClientSwf;
    }

    public String getGameClientVariables() {
        return gameClientVariables;
    }

    public String getGameClientTexts() {
        return gameClientTexts;
    }

    public String getGameClientProductData() {
        return gameClientProductData;
    }

    public String getGameClientFurniData() {
        return gameClientFurniData;
    }

    public String getGameClientBanner() {
        return gameClientBanner;
    }

    public String getGameClientBase() {
        return gameClientBase;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setHotelSlogan(String hotelSlogan) {
        this.hotelSlogan = hotelSlogan;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public void setUseFacebook(boolean useFacebook) {
        this.useFacebook = useFacebook;
    }

    public void setFacebookUsername(String facebookUsername) {
        if(facebookUsername.isEmpty()) {
            this.useFacebook = false;
        }

        this.facebookUsername = facebookUsername;
    }

    public void setUseTwitter(boolean useTwitter) {
        this.useTwitter = useTwitter;
    }

    public void setTwitterUsername(String twitterUsername) {
        if(twitterUsername.isEmpty()) {
            this.useTwitter = false;
        }

        this.twitterUsername = twitterUsername;
    }

    public void setTwitterWidgetId(String twitterWidgetId) {
        this.twitterWidgetId = twitterWidgetId;
    }

    public void setPlayerDefaultFigure(String playerDefaultFigure) {
        this.playerDefaultFigure = playerDefaultFigure;
    }

    public void setPlayerDefaultMotto(String playerDefaultMotto) {
        this.playerDefaultMotto = playerDefaultMotto;
    }

    public void setPlayerDefaultCredits(int playerDefaultCredits) {
        this.playerDefaultCredits = playerDefaultCredits;
    }

    public void setPlayerDefaultActivityPoints(int playerDefaultActivityPoints) {
        this.playerDefaultActivityPoints = playerDefaultActivityPoints;
    }

    public void setPlayerDefaultVipPoints(int playerDefaultVipPoints) {
        this.playerDefaultVipPoints = playerDefaultVipPoints;
    }

    public void setPlayerDefaultHomeRoom(int playerDefaultHomeRoom) {
        this.playerDefaultHomeRoom = playerDefaultHomeRoom;
    }

    public void setGameHost(String gameHost) {
        this.gameHost = gameHost;
    }

    public void setGamePort(int gamePort) {
        this.gamePort = gamePort;
    }

    public void setGameClientSwf(String gameClientSwf) {
        this.gameClientSwf = gameClientSwf;
    }

    public void setGameClientVariables(String gameClientVariables) {
        this.gameClientVariables = gameClientVariables;
    }

    public void setGameClientTexts(String gameClientTexts) {
        this.gameClientTexts = gameClientTexts;
    }

    public void setGameClientProductData(String gameClientProductData) {
        this.gameClientProductData = gameClientProductData;
    }

    public void setGameClientFurniData(String gameClientFurniData) {
        this.gameClientFurniData = gameClientFurniData;
    }

    public void setGameClientBanner(String gameClientBanner) {
        this.gameClientBanner = gameClientBanner;
    }

    public void setGameClientBase(String gameClientBase) {
        this.gameClientBase = gameClientBase;
    }

    public void save() {
        SiteSettingsDao.save(this);
    }

    private static SiteSettings siteSettingsInstance;

    public static SiteSettings getInstance() {
        if(siteSettingsInstance == null)
            siteSettingsInstance = SiteSettingsDao.get();

        return siteSettingsInstance;
    }

}

package com.cometproject.website.players;

import com.cometproject.website.players.playlist.PlaylistItem;
import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.utilities.DateUtil;
import com.cometproject.website.utilities.PlayerTicketGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.velocity.anakia.Escape;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private static final PlayerTicketGenerator playerTicketGenerator = new PlayerTicketGenerator();
    public static final int DEFAULT_RANK = 1;
    private static final Gson gsonInstance = new Gson();

    private int id;
    private String username;
    private String email;

    private String figure;
    private String gender;
    private String motto;

    private int credits;
    private int activityPoints;
    private int vipPoints;

    private int rank;
    private int lastVisit;

    private String password;

    private PlayerPreferences playerPreferences;
    private List<PlaylistItem> youtubePlaylist;

    public Player(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.username = data.getString("username");
        this.email = data.getString("email");

        this.figure = data.getString("figure");
        this.gender = data.getString("gender");
        this.motto = data.getString("motto");

        this.credits = data.getInt("credits");
        this.activityPoints = data.getInt("activity_points");

        this.lastVisit = data.getInt("last_online");

        this.rank = data.getInt("rank");
        this.password = data.getString("password");
    }

    public Player(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.figure = SiteSettings.getInstance().getPlayerDefaultFigure();
        this.motto = SiteSettings.getInstance().getPlayerDefaultMotto();
        this.credits = SiteSettings.getInstance().getPlayerDefaultCredits();
        this.activityPoints = SiteSettings.getInstance().getPlayerDefaultActivityPoints();
        this.vipPoints = SiteSettings.getInstance().getPlayerDefaultVipPoints();
        this.rank = Player.DEFAULT_RANK;
    }

    public void save() {
        PlayerDao.save(this);
    }

    public List<PlaylistItem> getPlaylist() {
        if(this.youtubePlaylist == null) {
            String playlistData = PlayerDao.getPlaylist(this.id);
            this.youtubePlaylist = gsonInstance.fromJson(playlistData, new TypeToken<List<PlaylistItem>>() {}.getType());
        }

        return this.youtubePlaylist;
    }

    public PlayerPreferences getPreferences() {
        if(this.playerPreferences == null)
            this.playerPreferences = PlayerDao.getPreferences(this.id);

        return this.playerPreferences;
    }

    public String generateTicket() {
        String ticket = "COMET-" + playerTicketGenerator.nextTicket();
        PlayerDao.updateTicket(this.id, ticket);

        return ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getActivityPoints() {
        return activityPoints;
    }

    public void setActivityPoints(int activityPoints) {
        this.activityPoints = activityPoints;
    }

    public int getVipPoints() {
        return vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }

    public String getEscapedMotto() {
        return Escape.getText(motto);
    }

    public String getMotto() {
        return this.motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(int lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getFormattedLastVisit() {
        return DateUtil.format(this.lastVisit);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.cometproject.gamecenter.fastfood.net;

import com.cometproject.gamecenter.fastfood.FastFoodGame;
import com.cometproject.gamecenter.fastfood.objects.FoodPlate;

public class FastFoodGameSession {

    private int playerId = -1;
    private String username;
    private String figure;
    private String gender;
    private String credits;
    private int gamesPlayed;
    private int parachutes = -1;
    private int missiles = 10;
    private int shields = 10;
    private int points;
    private FastFoodGame currentGame;
    private FoodPlate currentPlate;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getParachutes() {
        return parachutes;
    }

    public void setParachutes(int parachutes) {
        this.parachutes = parachutes;
    }

    public int getMissiles() {
        return missiles;
    }

    public void setMissiles(int missiles) {
        this.missiles = missiles;
    }

    public int getShields() {
        return shields;
    }

    public void setShields(int shields) {
        this.shields = shields;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public FastFoodGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(FastFoodGame currentGame) {
        this.currentGame = currentGame;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public FoodPlate getCurrentPlate() {
        return currentPlate;
    }

    public void setCurrentPlate(FoodPlate currentPlate) {
        this.currentPlate = currentPlate;
    }
}

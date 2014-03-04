package com.cometproject.server.game.rooms.items.data;

public class MannequinData {
    private String name;
    private String figure;
    private String gender;

    public MannequinData(String extradata) {
        String[] data = extradata.split(",");

        this.name = data[0];
        this.figure = data[1];
        this.gender = data[2];
    }

    public String getName() {
        return this.name;
    }

    public String getFigure() {
        return this.figure;
    }

    public String getGender() {
        return this.gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static MannequinData get(String extraData) {
        return new MannequinData(extraData);
    }

    public static String get(MannequinData data) {
        return data.getName() + "," + data.getFigure() + "," + data.getGender();
    }
}

package com.cometproject.server.game.rooms.items.data;

public class MannequinData {
    private String name;
    private String figure;
    private String gender;

    public MannequinData(String name, String figure, String gender) {
        this.name = name;
        this.figure = figure;
        this.gender = gender;
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
        if(!extraData.contains(";#;")) {
            return null;
        }

        String[] data = extraData.split(";#;");

        if(data.length < 3) {
            return null;
        }

        return new MannequinData(data[0], data[1], data[2]);
    }

    public static String get(MannequinData data) {
        return data.getName() + ";#;" + data.getFigure() + ";#;" + data.getGender();
    }
}

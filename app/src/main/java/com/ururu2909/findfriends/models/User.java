package com.ururu2909.findfriends.models;

public class User {
    private String login;
    private int avatar;
    private double latitude;
    private double longitude;

    public User(String login, int avatar, double latitude, double longitude) {
        this.login = login;
        this.avatar = avatar;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLogin() {
        return login;
    }

    public int getAvatar() {
        return avatar;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

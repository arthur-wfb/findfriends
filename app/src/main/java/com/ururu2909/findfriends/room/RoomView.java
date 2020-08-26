package com.ururu2909.findfriends.room;

import com.ururu2909.findfriends.models.User;

import java.util.ArrayList;

public interface RoomView {
    void showProgress();

    void hideProgress();

    void setError();

    void closeRoomActivity();

    void setCameraView(double latitude, double longitude);

    void addMapMarkers(ArrayList<User> users);

    void updateMapMarkers(ArrayList<User> users);
}

package com.ururu2909.findfriends.room;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.ururu2909.findfriends.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomPresenter implements RoomInteractor.OnRoomLeaveListener, RoomInteractor.OnRoomUsersRetrievedListener {
    private RoomView roomView;
    private RoomInteractor roomInteractor;
    private SharedPreferences mSettings;
    String userLogin;
    JSONObject user;
    ArrayList<User> users = new ArrayList<>();
    boolean firstCall = true;

    RoomPresenter(RoomView roomView, RoomInteractor roomInteractor, SharedPreferences mSettings) {
        this.roomView = roomView;
        this.roomInteractor = roomInteractor;
        this.mSettings = mSettings;
        userLogin = mSettings.getString("login", "");

        startUserLocationsRunnable();
    }

    void leaveRoom(){
        if (roomView != null){
            roomView.showProgress();
        }
        String token = mSettings.getString("token", "");
        if (!token.equals("")){
            roomInteractor.leaveRoom(token, this);
        }
    }

    @Override
    public void onRoomLeaveError() {
        roomView.hideProgress();
        roomView.setError();
    }

    @Override
    public void onRoomLeaveSuccess() {
        roomView.hideProgress();
        stopLocationUpdates();
        roomView.closeRoomActivity();
    }

    @Override
    public void onRoomUsersRetrieveError() {
        roomView.hideProgress();
        roomView.setError();
    }

    @Override
    public void onRoomUsersRetrieveSuccess(JSONArray jsonUsers) {
        roomView.hideProgress();
        users.clear();
        users = jsonArrayToArrayList(jsonUsers);
        if (firstCall){
            roomView.addMapMarkers(users);
            firstCall = false;
        }
        roomView.updateMapMarkers(users);
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int LOCATION_UPDATE_INTERVAL = 3000;

    private void startUserLocationsRunnable(){
        Log.d("xxx", "startUserLocationsRunnable: starting runnable for retrieving updated locations.");
        mHandler.postDelayed(mRunnable = () -> {
            retrieveRoomUsers();
            mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates(){
        mHandler.removeCallbacks(mRunnable);
    }

    private void retrieveRoomUsers(){
        Log.d("xxx", "retrieveUserLocations: retrieving location of all users in the chatroom.");
        String token = mSettings.getString("token", "");
        if (!token.equals("")){
            roomInteractor.retrieveRoomUsers(token, this);
        }
    }

    private ArrayList<User> jsonArrayToArrayList(JSONArray jsonUsers){
        try {
            for (int i=0; i<jsonUsers.length(); i++){
                users.add(new User(
                        jsonUsers.getJSONObject(i).getString("login"),
                        jsonUsers.getJSONObject(i).getInt("avatar"),
                        jsonUsers.getJSONObject(i).getDouble("latitude"),
                        jsonUsers.getJSONObject(i).getDouble("longitude")
                ));
            }
        } catch (Exception e){
            Log.d("xxx", "ошибочка");
        }
        return users;
    }

}

package com.ururu2909.findfriends.home;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class HomePresenter implements HomeInteractor.OnLogoutListener,
                                        HomeInteractor.OnRoomEnteredListener,
                                            HomeInteractor.OnRoomInfoListener,
                                                HomeInteractor.OnRoomDeleteListener,
                                                    HomeInteractor.OnAvatarChangedListener{
    private HomeView homeView;
    private HomeInteractor homeInteractor;
    private SharedPreferences mSettings;

    HomePresenter(HomeView homeView, HomeInteractor homeInteractor, SharedPreferences mSettings) {
        this.homeView = homeView;
        this.homeInteractor = homeInteractor;
        this.mSettings = mSettings;
    }

    void getRoomInfo(){
        if (homeView != null){
            homeView.showProgress();
        }
        String token = mSettings.getString("token", "");
        if (!token.equals("")){
            homeInteractor.getRoomInfo(token, this);
        }
    }

    @Override
    public void onRoomInfoRetrievingError() {
        homeView.hideProgress();
        homeView.setRoomInfoRetrievingError();
    }

    @Override
    public void onRoomInfoRetrieved(JSONObject room) {
        homeView.hideProgress();
        try {
            String roomName = room.getString("roomName");
            String roomLink = room.getString("roomLink");
            homeView.setRoomInfo(roomName, roomLink);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomNotExists() {
        homeView.hideProgress();
        homeView.showCreateRoomButon();
    }

    void deleteRoom(){
        if (homeView != null){
            homeView.showProgress();
        }
        String token = mSettings.getString("token", "");
        if (!token.equals("")){
            homeInteractor.deleteRoom(token, this);
        }
    }


    @Override
    public void onRoomDeletingError() {
        homeView.hideProgress();
        homeView.setRoomDeletingError();
    }

    @Override
    public void onRoomDeleted() {
        homeView.hideProgress();
        getRoomInfo();
    }

    void logout(){
        if (homeView != null){
            homeView.showProgress();
        }
        String login = mSettings.getString("login", "");
        String token = mSettings.getString("token", "");
        if (!(login.equals("") || token.equals(""))){
            homeInteractor.logout(login, token, this);
        }
    }

    @Override
    public void onLogoutError() {
        homeView.hideProgress();
        homeView.setLogoutError();
    }


    @Override
    public void onLogoutSuccess() {
        homeView.hideProgress();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.remove("login");
        editor.remove("avatar");
        editor.remove("token");
        editor.apply();
        homeView.navigateToLogin();
    }

    void enterRoom(String link){
        if (homeView != null){
            homeView.showProgress();
        }
        String token = mSettings.getString("token", "");
        if (!token.equals("")){
            homeInteractor.enterRoom(token, link, this);
        }
    }

    @Override
    public void onRoomEnteringError() {
        homeView.hideProgress();
        homeView.setRoomEnteringError();
    }

    @Override
    public void onRoomEntered() {
        homeView.hideProgress();
        homeView.navigateToRoom();
    }

    void updateAvatar(int avatar){
        if (homeView != null){
            homeView.showProgress();
        }
        String token = mSettings.getString("token", "");
        if (!token.equals("")){
            homeInteractor.updateAvatar(token, avatar, this);
        }
    }

    @Override
    public void onAvatarChangingError() {
        homeView.hideProgress();
        homeView.setAvatarChangingError();
    }

    @Override
    public void onAvatarChanged(int avatar) {
        homeView.hideProgress();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("avatar", avatar);
        editor.apply();
        homeView.updateAvatar(avatar);
    }
}

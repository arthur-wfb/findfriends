package com.ururu2909.findfriends.home;

public interface HomeView {
    void showProgress();

    void hideProgress();

    void setRoomEnteringError();

    void setRoomInfoRetrievingError();

    void setRoomInfo(String roomName, String roomLink);

    void setLogoutError();

    void setRoomDeletingError();

    void setAvatarChangingError();

    void showCreateRoomButon();

    void showLinkRequestDialog();

    void showChooseAvatarDialog();

    void navigateToCreateRoom();

    void navigateToRoom();

    void navigateToLogin();

    void updateAvatar(int avatar);
}

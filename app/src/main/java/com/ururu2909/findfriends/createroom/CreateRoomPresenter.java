package com.ururu2909.findfriends.createroom;

public class CreateRoomPresenter implements CreateRoomInteractor.OnRoomCreatedListener {

    private CreateRoomView createRoomView;
    private CreateRoomInteractor createRoomInteractor;

    CreateRoomPresenter(CreateRoomView createRoomView, CreateRoomInteractor createRoomInteractor) {
        this.createRoomView = createRoomView;
        this.createRoomInteractor = createRoomInteractor;
    }

    void createRoom(String token, String roomName){
        if (createRoomView != null){
            createRoomView.showProgress();
        }
        createRoomInteractor.createRoom(token, roomName, this);
    }

    @Override
    public void onError() {
        createRoomView.hideProgress();
        createRoomView.setError();
    }

    @Override
    public void onSuccess() {
        createRoomView.hideProgress();
        createRoomView.navigateToRoom();
    }
}

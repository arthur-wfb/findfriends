package com.ururu2909.findfriends.room;

import android.os.Handler;
import android.os.Looper;

import com.ururu2909.findfriends.util.Constants;
import com.ururu2909.findfriends.util.Interactor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Response;

class RoomInteractor {
    interface OnRoomLeaveListener {
        void onRoomLeaveError();

        void onRoomLeaveSuccess();
    }

    interface OnRoomUsersRetrievedListener {
        void onRoomUsersRetrieveError();

        void onRoomUsersRetrieveSuccess(JSONArray users);
    }

    void leaveRoom(final String token, final RoomInteractor.OnRoomLeaveListener listener) {
        Interactor.makeRequest(
            new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "leave_room")
                    .addFormDataPart("token", token)
                    .build(),
            new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onRoomLeaveError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onRoomLeaveError();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onRoomLeaveSuccess();
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        );
    }

    void retrieveRoomUsers(final String token, final RoomInteractor.OnRoomUsersRetrievedListener listener) {
        Interactor.makeRequest(
            new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "retrieve_room_users")
                    .addFormDataPart("token", token)
                    .build(),
            new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onRoomUsersRetrieveError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onRoomUsersRetrieveError();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onRoomUsersRetrieveSuccess(json.getJSONArray("data"));
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        );
    }
}

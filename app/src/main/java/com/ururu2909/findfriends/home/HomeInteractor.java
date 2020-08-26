package com.ururu2909.findfriends.home;

import android.os.Handler;
import android.os.Looper;

import com.ururu2909.findfriends.Config;
import com.ururu2909.findfriends.util.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class HomeInteractor {
    interface OnRoomInfoListener {
        void onRoomInfoRetrievingError();

        void onRoomInfoRetrieved(JSONObject room);

        void onRoomNotExists();
    }

    interface OnRoomDeleteListener {
        void onRoomDeletingError();

        void onRoomDeleted();
    }

    interface OnLogoutListener {
        void onLogoutError();

        void onLogoutSuccess();
    }

    interface OnRoomEnteredListener {
        void onRoomEnteringError();

        void onRoomEntered();
    }

    interface OnAvatarChangedListener {
        void onAvatarChangingError();

        void onAvatarChanged(int avatar);
    }

    void getRoomInfo(final String token, final HomeInteractor.OnRoomInfoListener listener) {
        OkHttpClient client = new OkHttpClient();
        URL url;
        try {
            url = new URL(Config.URL);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "retrieve_users_room_info")
                    .addFormDataPart("token", token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onRoomInfoRetrievingError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onRoomInfoRetrievingError();
                                    break;
                                case Constants.ROOM_NOT_EXISTS:
                                    listener.onRoomNotExists();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onRoomInfoRetrieved(json.getJSONObject("data"));
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteRoom(String token, OnRoomDeleteListener listener){
        OkHttpClient client = new OkHttpClient();
        URL url;
        try {
            url = new URL(Config.URL);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "delete_room")
                    .addFormDataPart("token", token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onRoomDeletingError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onRoomDeletingError();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onRoomDeleted();
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void logout(final String login, final String token, final HomeInteractor.OnLogoutListener listener) {
        OkHttpClient client = new OkHttpClient();
        URL url;
        try {
            url = new URL(Config.URL);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "logout")
                    .addFormDataPart("login", login)
                    .addFormDataPart("token", token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onLogoutError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onLogoutError();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onLogoutSuccess();
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void enterRoom(String token, String link, OnRoomEnteredListener listener){
        OkHttpClient client = new OkHttpClient();
        URL url;
        try {
            url = new URL(Config.URL);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "enter_room")
                    .addFormDataPart("token", token)
                    .addFormDataPart("link", link)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onRoomEnteringError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onRoomEnteringError();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onRoomEntered();
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateAvatar(String token, int avatar, OnAvatarChangedListener listener){
        OkHttpClient client = new OkHttpClient();
        URL url;
        try {
            url = new URL(Config.URL);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "update_avatar")
                    .addFormDataPart("token", token)
                    .addFormDataPart("avatar", String.valueOf(avatar))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onAvatarChangingError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onAvatarChangingError();
                                    break;
                                case Constants.SUCCESS:
                                    listener.onAvatarChanged(avatar);
                                    break;
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

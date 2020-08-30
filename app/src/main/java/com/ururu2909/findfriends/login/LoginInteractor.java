package com.ururu2909.findfriends.login;

import android.os.Handler;
import android.os.Looper;

import com.ururu2909.findfriends.util.Constants;
import com.ururu2909.findfriends.util.Interactor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Response;

class LoginInteractor {
    interface OnLoginFinishedListener {
        void onAuthError();

        void onError();

        void onSuccess(String login, int avatar, String token);
    }

    void login(final String login, final String password, final LoginInteractor.OnLoginFinishedListener listener) {
        Interactor.makeRequest(
            new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("method", "login")
                    .addFormDataPart("login", login)
                    .addFormDataPart("password", password)
                    .build(),
            new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(listener::onError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    handler.post(() -> {
                        try {
                            JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                            switch (json.getString("result")){
                                case Constants.ERROR:
                                    listener.onError();
                                    break;
                                case Constants.INCORRECT_DATA:
                                    listener.onAuthError();
                                    break;
                                case Constants.SUCCESS:
                                    int avatar = json.getJSONObject("data").getInt("avatar");
                                    String token = json.getJSONObject("data").getString("token");
                                    listener.onSuccess(login, avatar, token);
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

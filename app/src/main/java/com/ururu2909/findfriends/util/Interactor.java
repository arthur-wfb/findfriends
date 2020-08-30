package com.ururu2909.findfriends.util;

import com.ururu2909.findfriends.Config;

import java.io.IOException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Interactor {
    public static void makeRequest(RequestBody requestBody, Callback callback){
        OkHttpClient client = new OkHttpClient();
        URL url;
        try {
            url = new URL(Config.URL);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

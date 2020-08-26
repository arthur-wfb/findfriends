package com.ururu2909.findfriends;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ururu2909.findfriends.home.HomeActivity;
import com.ururu2909.findfriends.login.LoginActivity;
import com.ururu2909.findfriends.util.Constants;

public class Dispatcher extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(Constants.APP_PREFERENCES, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Intent intent;
        if (token.equals("")){
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        startActivity(intent);
    }
}

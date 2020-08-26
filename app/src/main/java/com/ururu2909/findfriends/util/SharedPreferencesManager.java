package com.ururu2909.findfriends.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesManager {
    private static SharedPreferences instance;

    public static synchronized SharedPreferences getInstance(Context context){
        if(instance == null)
            instance = context.getApplicationContext().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);

        return instance;
    }
}
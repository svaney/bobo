package com.bobo.gmargiani.bobo.utils;

import android.content.SharedPreferences;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class PreferencesApiManager {
    public static final String PREF_DEFAULT_NAME = "BOBO_PREFS";

    private static final String USERNAME = "USERNAME";
    private static final String AUTH_TOKEN = "AUTH_TOKEN";

    private static PreferencesApiManager instance;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public PreferencesApiManager(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mEditor = mSharedPreferences.edit();
        instance = this;
    }

    public static PreferencesApiManager getInstance() {
        return instance;
    }

    public void saveToken(String token) {
        mEditor.putString(AUTH_TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(AUTH_TOKEN, "");
    }

}

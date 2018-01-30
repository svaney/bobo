package com.bobo.gmargiani.bobo.utils;

import android.content.SharedPreferences;

import com.bobo.gmargiani.bobo.utils.consts.Consts;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class PreferencesApiManager {
    public static final String PREF_DEFAULT_NAME = "BOBO_PREFS";


    private static final String SELECTED_LOCALE = "SELECTED_LOCALE";


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

    public String getCurrentLanguage() {
        String locale = getLocale();
        String language = locale.equalsIgnoreCase(Consts.KA) ? Consts.KA : Consts.EN;
        return language;
    }

    public String getLocale() {
        String locale = mSharedPreferences.getString(SELECTED_LOCALE, Consts.KA);
        return locale;
    }
}

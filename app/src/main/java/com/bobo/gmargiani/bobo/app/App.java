package com.bobo.gmargiani.bobo.app;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.model.datamodels.UserInfo;
import com.bobo.gmargiani.bobo.rest.ApiManager;
import com.bobo.gmargiani.bobo.rest.NetApi;
import com.bobo.gmargiani.bobo.rest.RetrofitApi;
import com.bobo.gmargiani.bobo.rest.RetrofitClient;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.EventBus;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class App extends Application {
    private static App instance;
    private EventBus eventBus;
    private UserInfo userInfo;
    private RetrofitClient retrofitClient;
    private ApiManager apiManager;
    private NetApi netApi;
    private PreferencesApiManager preferencesApiManager;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        buildComponents();
    }

    public static App getInstance() {
        return instance;
    }

    private void buildComponents() {

        eventBus = EventBus.getDefault();

        preferencesApiManager = new PreferencesApiManager(getSharedPreferences(PreferencesApiManager.PREF_DEFAULT_NAME, MODE_PRIVATE));

        retrofitClient = new RetrofitClient();
        try {
            retrofitClient.setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (Exception ignored) {
        }

        netApi = new RetrofitApi(retrofitClient);

        userInfo = new UserInfo(eventBus);

        apiManager = new ApiManager(netApi, userInfo);

        userInfo.setApiManager(apiManager);

    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void logOut(boolean sessionExpired) {
    }

    public void postActivityResultEvent(final int requestCode, final int resultCode, final Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityResultEvent activityResultEvent = new ActivityResultEvent(requestCode, resultCode, data);
                        getEventBus().post(activityResultEvent);
                    }
                }, 400);
                break;
        }
    }
}

package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;

/**
 * Created by gmarg on 2/22/2018.
 */

public class LauncherActivity extends RootActivity {

    private AppVersionEvent appVersionEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestAppVersion(false);
        userInfo.requestTokenAuthorizationEvent();
    }

    @Subscribe
    public void onAppVersionEvent(AppVersionEvent event) {
        if (event != appVersionEvent) {
            appVersionEvent = event;
            switch (appVersionEvent.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_SUCCESS:
                case RootEvent.STATE_ERROR:
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }
}

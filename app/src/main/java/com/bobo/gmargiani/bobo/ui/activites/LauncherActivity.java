package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;

/**
 * Created by gmarg on 2/22/2018.
 */

public class LauncherActivity extends RootActivity {

    private AppVersionEvent appVersionEvent;
    private TokenAuthorizationEvent tokenEvent;

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
            checkEvents();
        }
    }

    @Subscribe
    public void onTokenEvent(TokenAuthorizationEvent event) {
        if (event != tokenEvent) {
            tokenEvent = event;
            checkEvents();
        }
    }

    private void checkEvents() {
        if (tokenEvent != null && appVersionEvent != null
                && tokenEvent.getState() != RootEvent.STATE_LOADING
                && appVersionEvent.getState() != RootEvent.STATE_LOADING) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            showFullLoading();
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

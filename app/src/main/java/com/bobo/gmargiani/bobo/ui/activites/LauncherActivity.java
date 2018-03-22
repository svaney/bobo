package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;

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
        findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestAppVersion(false, false, "ANDROID", "MOBILE");
    }

    @Subscribe
    public void onAppVersionEvent(AppVersionEvent event) {
        if (event != appVersionEvent) {
            appVersionEvent = event;
            switch (appVersionEvent.getState()) {
                case RootEvent.STATE_LOADING:
                case RootEvent.STATE_UPDATING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_SUCCESS:
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
                case RootEvent.STATE_DATA_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_NETWORK_ERROR:
                    showFullError();
                    break;
            }
        }

    }

    @OnClick(R.id.full_retry)
    public void onRetry() {
        userInfo.requestAppVersion(false, false, "ANDROID", "MOBILE-EXT");
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

package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;

/**
 * Created by gmarg on 2/22/2018.
 */

public class LauncherActivity extends RootActivity {

    private AppVersionEvent appVersionEvent;
    private TokenAuthorizationEvent tokenEvent;
    private CategoriesEvent categoriesEvent;
    private LocationsEvent locationsEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestAppVersion(false);
        userInfo.requestTokenAuthorizationEvent();
        userInfo.requestCategories();
        userInfo.requestLocations();
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

    @Subscribe
    public void onLocationsEvent(LocationsEvent event) {
        if (event != locationsEvent) {
            locationsEvent = event;
            checkEvents();
        }
    }

    @Subscribe
    public void onCategoriesEvent(CategoriesEvent event) {
        if (event != categoriesEvent) {
            categoriesEvent = event;
            checkEvents();
        }
    }

    private void checkEvents() {
        if (locationsEvent == null || locationsEvent.getState() == RootEvent.STATE_LOADING
                || categoriesEvent == null || categoriesEvent.getState() == RootEvent.STATE_LOADING) {
            showFullLoading();
        } else if (locationsEvent.getState() != RootEvent.STATE_SUCCESS || categoriesEvent.getState() != RootEvent.STATE_SUCCESS) {
            showFullError();
        } else if (tokenEvent != null && appVersionEvent != null
                && tokenEvent.getState() != RootEvent.STATE_LOADING
                && appVersionEvent.getState() != RootEvent.STATE_LOADING) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            showFullLoading();
        }
    }

    @OnClick(R.id.full_retry_button)
    public void onRetryClick() {
        userInfo.requestCategories();
        userInfo.requestLocations();
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

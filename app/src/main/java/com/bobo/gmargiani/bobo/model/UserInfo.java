package com.bobo.gmargiani.bobo.model;

import android.os.Handler;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.rest.ApiManager;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;
import com.bobo.gmargiani.bobo.utils.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class UserInfo implements NetDataListener {
    private Handler handler = new Handler();
    private ApiManager apiManager;
    private EventBus eventBus;

    private AppVersionEvent appVersionEvent;
    private TokenAuthorizationEvent tokenAuthorizationEvent;

    public UserInfo(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public void requestTokenAuthorizationEvent() {
        if (shouldNotRefresh(tokenAuthorizationEvent)) {
            eventBus.post(tokenAuthorizationEvent);
        } else {
            requestTokenAuthorizationEvent(PreferencesApiManager.getInstance().getToken());
        }
    }

    private void requestTokenAuthorizationEvent(final String token) {
        if (shouldNotRefresh(tokenAuthorizationEvent) && Utils.equals(tokenAuthorizationEvent.getToken(), token)) {
            eventBus.post(tokenAuthorizationEvent);
        } else {
            tokenAuthorizationEvent = new TokenAuthorizationEvent();
            tokenAuthorizationEvent.setToken(token);
            tokenAuthorizationEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(tokenAuthorizationEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.authorizeByToken(token);
                }
            }, 2000);

        }
    }

    @Override
    public void onAuthorizeByTokenEvent(ApiResponse<Boolean> response, String token) {
        if (tokenAuthorizationEvent != null && Utils.equals(tokenAuthorizationEvent.getToken(), token)) {
            tokenAuthorizationEvent = (TokenAuthorizationEvent) tokenAuthorizationEvent.copyData();

            if (response.isSuccess()) {
                tokenAuthorizationEvent.setState(RootEvent.STATE_SUCCESS);
                tokenAuthorizationEvent.setAuthorized(response.getResult());
            } else {
                tokenAuthorizationEvent.setState(RootEvent.STATE_ERROR);
            }
            eventBus.post(tokenAuthorizationEvent);
        }
    }

    public void requestAppVersion(boolean update) {

        if (shouldNotRefresh(appVersionEvent, update)) {
            eventBus.post(appVersionEvent);
        } else {

            appVersionEvent = appVersionEvent == null ? new AppVersionEvent() : (AppVersionEvent) appVersionEvent.copyData();

            appVersionEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(appVersionEvent);

            apiManager.getAppVersion("ANDROID", "MOBILE-EXT");

        }
    }

    @Override
    public void onAppVersionEvent(ApiResponse<AppVersion> response) {

        if (appVersionEvent != null) {
            appVersionEvent = (AppVersionEvent) appVersionEvent.copyData();
            if (response.isSuccess()) {
                appVersionEvent.setState(RootEvent.STATE_SUCCESS);
                appVersionEvent.setAppVersion(response.getResult());
            } else {
                appVersionEvent.setState(RootEvent.STATE_ERROR);
            }
            eventBus.post(appVersionEvent);
        }
    }

    public boolean shouldNotRefresh(RootEvent event) {
        return shouldNotRefresh(event, false);
    }

    public boolean isAuthorized() {
        return tokenAuthorizationEvent == null ? false : tokenAuthorizationEvent.isAuthorized();
    }

    public boolean shouldNotRefresh(RootEvent event, boolean update) {
        if (event == null) {
            return false;
        }

        if (event.isError()) {
            return false;
        }

        if (event.isLoading()) {
            return true;
        }

        if (event.isSuccess() && !update) {
            return true;
        }

        return false;
    }


}

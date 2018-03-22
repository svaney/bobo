package com.bobo.gmargiani.bobo.model;

import android.os.Handler;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.model.datamodels.AppVersion;
import com.bobo.gmargiani.bobo.model.datamodels.Order;
import com.bobo.gmargiani.bobo.rest.ApiManager;
import com.bobo.gmargiani.bobo.rest.ApiResponse;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class UserInfo implements NetDataListener {
    private Handler handler = new Handler();
    private Order currentOrder;
    private ApiManager apiManager;
    private EventBus eventBus;

    private AuthorizedEvent authorizedEvent;

    private AppVersionEvent appVersionEvent;

    public UserInfo(EventBus eventBus) {
        this.eventBus = eventBus;

    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void requestAuthorizedEvent() {
        if (authorizedEvent == null) {
            authorizedEvent = new AuthorizedEvent();
            authorizedEvent.setState(RootEvent.STATE_SUCCESS);
        }

        eventBus.post(authorizedEvent);
    }

    public void requestAppVersion(boolean forceRefresh, boolean update, final String deviceType, final String channel) {

        if (!forceRefresh && shouldNotRefresh(appVersionEvent, update)) {
            eventBus.post(appVersionEvent);
        } else {

            boolean newEventNeeded = appVersionEvent == null || forceRefresh;

            appVersionEvent = newEventNeeded ? new AppVersionEvent() : (AppVersionEvent) appVersionEvent.copyData();

            appVersionEvent.setState(newEventNeeded ? RootEvent.STATE_LOADING : RootEvent.STATE_UPDATING);

            eventBus.post(appVersionEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getAppVersion(deviceType, channel);
                }
            }, 1000);

        }
    }


    @Override
    public void onAppVersionEvent(ApiResponse<AppVersion> response) {

        if (appVersionEvent != null) {

            appVersionEvent = (AppVersionEvent) appVersionEvent.copyData();

            if (appVersionEvent.isUpdating()) {
                appVersionEvent.setState(RootEvent.STATE_SUCCESS);
                if (!response.isNetworkFailure() && response.isSuccess()) {
                    appVersionEvent.setAppVersion(response.getResult());
                }
            } else if (appVersionEvent.isLoading()) {
                if (!response.isNetworkFailure()) {
                    if (response.isSuccess()) {
                        appVersionEvent.setState(RootEvent.STATE_SUCCESS);
                        appVersionEvent.setAppVersion(response.getResult());
                    } else {
                        appVersionEvent.setState(RootEvent.STATE_DATA_ERROR);
                        appVersionEvent.setErrorText(response.getError());
                        appVersionEvent.setErrorCode(response.getCode());
                    }

                } else {
                    appVersionEvent.setState(RootEvent.STATE_NETWORK_ERROR);
                }
            }

            eventBus.post(appVersionEvent);
        }
    }

    public boolean shouldNotRefresh(RootEvent event, boolean update) {
        if (event == null) {
            return false;
        }

        if (event.isError()) {
            return false;
        }

        if (event.isLoadingOrUpdating()) {
            return true;
        }

        if (event.hasData() && !update) {
            return true;
        }

        return false;
    }
}

package com.bobo.gmargiani.bobo.model;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TestDataEvent;
import com.bobo.gmargiani.bobo.model.datamodels.AppVersion;
import com.bobo.gmargiani.bobo.rest.ApiManager;
import com.bobo.gmargiani.bobo.rest.ApiResponse;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class UserInfo implements NetDataListener {
    private ApiManager apiManager;
    private EventBus eventBus;

    private AuthorizedEvent authorizedEvent;

    private TestDataEvent testDataEvent;

    public UserInfo(EventBus eventBus) {
        this.eventBus = eventBus;

    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public void requestAuthorizedEvent() {
        if (authorizedEvent == null) {
            authorizedEvent = new AuthorizedEvent();
            authorizedEvent.setState(RootEvent.STATE_SUCCESS);
        }

        eventBus.post(authorizedEvent);
    }

    public void requestTestData(boolean forceRefresh, boolean update, String deviceType, String channel) {

        if (!forceRefresh && shouldNotRefresh(testDataEvent, update)) {
            eventBus.post(testDataEvent);
        } else {

            boolean newEventNeeded = testDataEvent == null || forceRefresh;

            testDataEvent = newEventNeeded ? new TestDataEvent() : (TestDataEvent) testDataEvent.copyData();

            testDataEvent.setState(newEventNeeded ? RootEvent.STATE_LOADING : RootEvent.STATE_UPDATING);

            eventBus.post(testDataEvent);

            apiManager.getTestData(deviceType, channel);
        }
    }


    @Override
    public void onTestData(ApiResponse<AppVersion> response) {

        if (testDataEvent != null) {

            testDataEvent = (TestDataEvent) testDataEvent.copyData();

            if (testDataEvent.isUpdating()) {
                testDataEvent.setState(RootEvent.STATE_SUCCESS);
                if (!response.isNetworkFailure() && response.isSuccess()) {
                    testDataEvent.setAppVersion(response.getResult());
                }
            } else if (testDataEvent.isLoading()) {
                if (!response.isNetworkFailure()) {
                    if (response.isSuccess()) {
                        testDataEvent.setState(RootEvent.STATE_SUCCESS);
                        testDataEvent.setAppVersion(response.getResult());
                    } else {
                        testDataEvent.setState(RootEvent.STATE_DATA_ERROR);
                        testDataEvent.setErrorText(response.getError());
                        testDataEvent.setErrorCode(response.getCode());
                    }

                } else {
                    testDataEvent.setState(RootEvent.STATE_NETWORK_ERROR);
                }
            }

            eventBus.post(testDataEvent);
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

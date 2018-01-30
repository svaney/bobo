package com.bobo.gmargiani.bobo.evenbuts;

import com.bobo.gmargiani.bobo.evenbuts.events.TestDataEvent;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class RootEvent {
    public static final int STATE_LOADING = 10;
    public static final int STATE_SUCCESS = 20;
    public static final int STATE_UPDATING = 30;
    public static final int STATE_DATA_ERROR = -10;
    public static final int STATE_NETWORK_ERROR = -20;

    private int currState;
    private String errorCode;
    private String errorText;

    public boolean isSuccess() {
        return currState == STATE_SUCCESS;
    }

    public boolean isUpdating() {
        return currState == STATE_UPDATING;
    }

    public boolean isLoading() {
        return currState == STATE_LOADING;
    }

    public boolean isDataError() {
        return currState == STATE_DATA_ERROR;
    }

    public boolean isNetworkError() {
        return currState == STATE_NETWORK_ERROR;
    }

    public boolean isError() {
        return currState == STATE_NETWORK_ERROR || currState == STATE_DATA_ERROR;
    }

    public boolean isLoadingOrLoaded() {
        return currState == STATE_LOADING || currState == STATE_SUCCESS;
    }

    public boolean hasData() {
        return currState == STATE_UPDATING || currState == STATE_SUCCESS;
    }

    public boolean isLoadingOrUpdating(){
        return currState == STATE_LOADING || currState == STATE_UPDATING;
    }

    public int getState() {
        return currState;
    }

    public void setState(int currState) {
        this.currState = currState;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public abstract TestDataEvent copyData();
}

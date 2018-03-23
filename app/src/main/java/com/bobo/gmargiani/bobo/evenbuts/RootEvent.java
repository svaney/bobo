package com.bobo.gmargiani.bobo.evenbuts;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class RootEvent {
    public static final int STATE_LOADING = 10;
    public static final int STATE_SUCCESS = 20;
    public static final int STATE_ERROR = 30;

    private int currState;

    public int getState() {
        return currState;
    }

    public void setState(int state) {
        currState = state;
    }

    public boolean isLoading() {
        return currState == STATE_LOADING;
    }

    public boolean isSuccess() {
        return currState == STATE_SUCCESS;
    }

    public boolean isError() {
        return currState == STATE_ERROR;
    }

    public abstract Object copyData();
}

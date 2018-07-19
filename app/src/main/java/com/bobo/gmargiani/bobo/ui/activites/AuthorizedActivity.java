package com.bobo.gmargiani.bobo.ui.activites;

import android.content.DialogInterface;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;

import org.greenrobot.eventbus.Subscribe;

public abstract class AuthorizedActivity extends RootDetailedActivity implements DialogInterface.OnCancelListener {
    private TokenAuthorizationEvent tokenAuthorizationEvent;

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestTokenAuthorizationEvent();
    }

    @Subscribe
    public void onTokenAuthorizationEvent(TokenAuthorizationEvent event) {
        if (tokenAuthorizationEvent != event) {
            tokenAuthorizationEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    if (event.isAuthorized()) {
                        userIsAuthorized();
                    } else {
                        showAuthorizationDialog(null);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onAuthorizationDialogCancel();
    }

    public void onAuthorizationDialogCancel() {
        finish();
    }

    public abstract void userIsAuthorized();


}

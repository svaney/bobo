package com.bobo.gmargiani.bobo.ui.activites;

import android.content.DialogInterface;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LogInEvent;

import org.greenrobot.eventbus.Subscribe;

public abstract class AuthorizedActivity extends RootDetailedActivity implements DialogInterface.OnCancelListener {
    protected LogInEvent logInEvent;

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestLogInEvent();
    }

    @Subscribe
    public void onLogInEvent(LogInEvent event) {
        if (logInEvent != event) {
            logInEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    closeAuthorizeDialog();
                    if (event.isLoggedIn()) {
                        userIsLoggedIn();
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

    public abstract void userIsLoggedIn();


}

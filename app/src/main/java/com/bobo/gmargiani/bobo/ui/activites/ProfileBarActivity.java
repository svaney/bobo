package com.bobo.gmargiani.bobo.ui.activites;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gmargiani on 1/31/2018.
 */

public abstract class ProfileBarActivity extends RootActivity {
    @BindView(R.id.profile_bar)
    ViewGroup profileBar;

    @BindView(R.id.balance_tv)
    TextView balanceTv;

    @BindView(R.id.profile_btn)
    View profileBtn;

    @BindView(R.id.blocked_balance_tv)
    TextView blockedBalanceTv;

    @BindView(R.id.login_register_tv)
    View loginRegisterBtn;

    @BindView(R.id.back_button)
    View backButton;

    private AuthorizedEvent authorizedEvent;


    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestAuthorizedEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorizedEvent(AuthorizedEvent event) {
        if (event != authorizedEvent) {
            authorizedEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpProfileBar();
                    onAuthorizedSuccessEvent(event);
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

    protected void setUpProfileBar() {
        balanceTv.setVisibility(authorizedEvent.isAuthorized() ? View.VISIBLE : View.GONE);
        blockedBalanceTv.setVisibility(authorizedEvent.isAuthorized() ? View.VISIBLE : View.GONE);
        profileBtn.setVisibility(authorizedEvent.isAuthorized() ? View.VISIBLE : View.GONE);
        loginRegisterBtn.setVisibility(authorizedEvent.isAuthorized() ? View.GONE : View.VISIBLE);

    }

    protected abstract void onAuthorizedSuccessEvent(AuthorizedEvent event);


    @OnClick(R.id.setting_btn)
    protected void onSettingsClick() {

    }

    @OnClick(R.id.profile_btn)
    protected void onProfileClick() {

    }

    @OnClick(R.id.back_button)
    protected void onBackButtonPressed() {
        onBackPressed();
    }

    @OnClick(R.id.login_register_tv)
    protected void onLoginRegisterClick() {

    }
}

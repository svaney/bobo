package com.bobo.gmargiani.bobo.ui.activites;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TestDataEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends RootActivity {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

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
            refreshLayout.setRefreshing(false);
            refreshLayout.setRefreshing(event.isUpdating());
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpAuthorizedEvent();
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


    private void setUpAuthorizedEvent() {
        balanceTv.setVisibility(authorizedEvent.isAuthorized() ? View.VISIBLE : View.GONE);
        blockedBalanceTv.setVisibility(authorizedEvent.isAuthorized() ? View.VISIBLE : View.GONE);
        profileBtn.setVisibility(authorizedEvent.isAuthorized() ? View.VISIBLE : View.GONE);
        loginRegisterBtn.setVisibility(authorizedEvent.isAuthorized() ? View.GONE : View.VISIBLE);

        if (authorizedEvent.isAuthorized()) {

        } else {

        }
    }

    @OnClick(R.id.setting_btn)
    protected void onSettingsClick() {

    }

    @OnClick(R.id.profile_btn)
    protected void onProfileClick() {

    }

    @OnClick(R.id.login_register_tv)
    protected void onLoginRegisterClick() {

    }

    @OnClick(R.id.new_order)
    protected void onNewOrderClick() {

    }

    public int getLayoutId() {
        return R.layout.activity_home;
    }
}


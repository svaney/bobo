package com.bobo.gmargiani.bobo.ui.activites;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.utils.AppUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends RootActivity {
    @BindView(R.id.profile_bar)
    ViewGroup profileBar;

    @BindView(R.id.balance_tv)
    TextView balanceTv;

    @BindView(R.id.profile_btn)
    View profileBtn;

    @BindView(R.id.blocked_balance_tv)
    TextView blockedBalanceTv;

    @BindView(R.id.login_register_btn)
    Button loginRegisterBtn;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.new_order)
    View newOrderButton;

    @BindView(R.id.toolbar_text)
    View toolbarText;

    private AuthorizedEvent authorizedEvent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestAuthorizedEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (profileBar.getVisibility() == View.GONE) {
            profileBar.setVisibility(View.VISIBLE);
            animateScreenStart(300, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorizedEvent(AuthorizedEvent event) {
        if (event != authorizedEvent) {
            authorizedEvent = event;
            refreshLayout.setRefreshing(event.isUpdating());
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpProfileBar();
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

        animateScreenStart(600, true);
    }

    private void animateScreenStart(long duration, boolean animateFab) {
        Animation profBarAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.profile_bar_animation);
        profBarAnim.setDuration(duration);

        Animation fabAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_slide_up_animation);
        fabAnim.setDuration(duration);

        profileBar.startAnimation(profBarAnim);
        if (animateFab) {
            newOrderButton.startAnimation(fabAnim);
        }
    }


    @OnClick(R.id.setting_btn)
    protected void onSettingsClick() {

    }

    @OnClick(R.id.profile_btn)
    protected void onProfileClick() {

    }


    @OnClick(R.id.login_register_btn)
    protected void onLoginRegisterClick() {

    }

    @SuppressLint("NewApi")
    @OnClick(R.id.new_order)
    protected void onNewOrderClick() {


        if (AppUtils.atLeastLollipop()) {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.profile_bar_hide_animation);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (profileBar != null) {
                        profileBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            profileBar.startAnimation(anim);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(toolbarText, "toolbar_text"),
                    Pair.create(newOrderButton, "fab_button"));

            startActivity(new Intent(HomeActivity.this, NewOrderActivity.class), options.toBundle());

        } else {
            startActivity(new Intent(this, NewOrderActivity.class));
        }


    }


    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

}


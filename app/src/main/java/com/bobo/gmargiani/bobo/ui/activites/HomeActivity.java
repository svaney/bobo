package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;
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

public class HomeActivity extends ProfileBarActivity {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backButton.setVisibility(View.GONE);
    }

    @Override
    public void onAuthorizedEvent(AuthorizedEvent event) {
        super.onAuthorizedEvent(event);
        refreshLayout.setRefreshing(event.isUpdating());
    }

    @Override
    protected void onAuthorizedSuccessEvent(AuthorizedEvent event) {

    }

    @OnClick(R.id.new_order)
    protected void onNewOrderClick() {
        startActivity(new Intent(this, NewOrderActivity.class));
    }


    public int getLayoutId() {
        return R.layout.activity_home;
    }
}


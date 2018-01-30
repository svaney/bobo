package com.bobo.gmargiani.bobo.ui.activites;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TestDataEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends RootActivity {
    @BindView(R.id.text_view)
    TextView tv;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    private TestDataEvent appVersionEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userInfo.requestTestData(true, false, "ANDROID", "MOBILE-EXT");
    }

    @Subscribe
    public void onTestData(TestDataEvent event) {
        if (event != appVersionEvent) {

            refreshLayout.setRefreshing(false);
            refreshLayout.setRefreshing(event.isUpdating());
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    showData();
                    break;
                case RootEvent.STATE_UPDATING:
                case RootEvent.STATE_SUCCESS:
                    showContent();
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

    private void showData() {
        tv.setText("must update client: " + appVersionEvent.getAppVersion().isMustUpdateClient());
    }

    @OnClick(R.id.full_retry)
    public void onRetryClick() {
        userInfo.requestTestData(true, false, "ANDROID", "MOBILE-EXT");
    }

    @OnClick(R.id.force_refresh)
    public void onForceRefreshClick() {
        userInfo.requestTestData(true, false, "ANDROID", "MOBILE-EXT");
    }

    @OnClick(R.id.update)
    public void onUpdateClick() {
        userInfo.requestTestData(false, true, "ANDROID", "MOBILE-EXT");
    }

    @OnClick(R.id.get_data_with_error)
    public void onGetErrorClick() {
        userInfo.requestTestData(false, false, "ANDROID", "MOBILE");
    }

    @OnClick(R.id.get_data_with_error_update)
    public void onGetErrorUpdateClick() {
        userInfo.requestTestData(false, false, "ANDROID", "MOBILE");
    }
}


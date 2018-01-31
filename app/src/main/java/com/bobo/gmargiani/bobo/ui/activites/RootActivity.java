package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.model.datamodels.UserInfo;
import com.bobo.gmargiani.bobo.ui.dialogs.NewItemDetailsDialogFragment;

import butterknife.ButterKnife;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class RootActivity extends AppCompatActivity {
    protected UserInfo userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getInstance().getEventBus().register(this);
        this.userInfo = App.getInstance().getUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.getInstance().getEventBus().unregister(this);
    }

    protected void showFullLoading() {
        showLoader(true);
        showError(false);
        showContent(false);
    }

    protected void showFullError() {
        showLoader(false);
        showError(true);
        showContent(false);
    }

    protected void showContent() {
        showLoader(false);
        showError(false);
        showContent(true);
    }

    private void showLoader(boolean show) {
        View loader = findViewById(R.id.full_loader);
        if (loader != null) {
            loader.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showError(boolean show) {
        View error = findViewById(R.id.full_retry);
        if (error != null) {
            error.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showContent(boolean show) {
        View content = findViewById(R.id.main_content);
        if (content != null) {
            content.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void showConditionalDialog() {



        final NewItemDetailsDialogFragment dialog = new NewItemDetailsDialogFragment();

        dialog.show(this.getSupportFragmentManager(), "NEW_ITEM");
        dialog.setCancelable(false);
    }

    public abstract int getLayoutId();
}

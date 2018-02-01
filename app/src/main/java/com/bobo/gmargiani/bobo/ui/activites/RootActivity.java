package com.bobo.gmargiani.bobo.ui.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.DeniedPermissionsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.model.datamodels.UserInfo;
import com.bobo.gmargiani.bobo.ui.adapters.BasicRecyclerItemClickListener;
import com.bobo.gmargiani.bobo.utils.ImageUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class RootActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected TextView toolbarTV;

    protected UserInfo userInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setSupportActionBar();
    }

    protected void setSupportActionBar() {
        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setContentInsetsAbsolute(0, 0);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTV = findViewById(R.id.toolbar_text);

        if (toolbarTV != null) {
            toolbarTV.setText(getHeaderText());
        }
    }

    private int getHeaderText() {
        return R.string.app_name;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (needEventBus()) {
            App.getInstance().getEventBus().register(this);
        }
        this.userInfo = App.getInstance().getUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (needEventBus()) {
            App.getInstance().getEventBus().unregister(this);
        }
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

    public void showTextListDialog(String title, ArrayList<String> texts, final int listType, final BasicRecyclerItemClickListener listener) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(title);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        arrayAdapter.addAll(texts);

        builderSingle.setNegativeButton(getString(R.string.button_text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onItemClick(which, listType);
                }
            }
        });

        builderSingle.show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityResultEvent activityResultEvent = new ActivityResultEvent(requestCode, resultCode, data);
                        App.getInstance().getEventBus().post(activityResultEvent);
                    }
                }, 400);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final ArrayList<String> grantedPermissions = new ArrayList<>();
        final ArrayList<String> deniedPermissions = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i]);
            } else {
                deniedPermissions.add(permissions[i]);
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getEventBus().post(new GrantedPermissionsEvent(grantedPermissions, requestCode));
                App.getInstance().getEventBus().post(new DeniedPermissionsEvent(deniedPermissions, requestCode));
            }
        }, 400);


    }

    public abstract int getLayoutId();

    public abstract boolean needEventBus();
}

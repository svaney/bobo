package com.bobo.gmargiani.bobo.ui.activites;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.model.UserInfo;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.NetApi;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.dialogs.AuthorizationDialog;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.LocaleHelper;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.ButterKnife;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class RootActivity extends AppCompatActivity {
    protected UserInfo userInfo;
    protected NetApi netApi;

    protected Handler handler = new Handler();

    private AuthorizationDialog authorizationDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        this.userInfo = App.getInstance().getUserInfo();
        this.netApi = App.getNetApi();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected String getHeaderText() {
        return getString(R.string.app_name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (needEventBus()) {
            App.getInstance().getEventBus().register(this);
        }
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
        View v = findViewById(R.id.full_loader);
        if (v != null) {
            v.setVisibility(show ? View.VISIBLE : View.GONE);
            View loader = ViewUtils.findViewInChildren(v, R.id.loader);
            if (loader != null) {
                if (show) {
                    ((CircleProgressView) loader).spin();
                } else {
                    ((CircleProgressView) loader).stopSpinning();
                }
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConsts.RC_REGISTER && resultCode == Activity.RESULT_OK) {
            showAuthorizationDialog(data.getStringExtra(AppConsts.PARAM_EMAIL));
        } else {
            App.getInstance().postActivityResultEvent(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        App.getInstance().postPermissionEvent(requestCode, permissions, grantResults);
    }


    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void showAuthorizationDialog(String mail) {
        try {
            authorizationDialog.dismiss();
        } catch (Exception e) {

        }

        authorizationDialog = new AuthorizationDialog(this, mail);
        authorizationDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (RootActivity.this instanceof AuthorizedActivity) {
                    if (!userInfo.isAuthorized()) {
                        finish();
                    }
                }
            }
        });
        authorizationDialog.setCanceledOnTouchOutside(false);
        authorizationDialog.show();
        authorizationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                App.getInstance().getEventBus().unregister(dialog);
            }
        });
    }

    public void closeAuthorizeDialog() {
        try {
            authorizationDialog.dismiss();
        } catch (Exception e) {

        }
    }


    public void showDialog(String title, String text, boolean showOkButton, final View.OnClickListener onOkClickListener, boolean showCancelButton,
                           final View.OnClickListener onCancelClickListener, final DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder builder;
        if (AppUtils.atLeastLollipop()) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(title)
                .setMessage(text);

        if (showOkButton) {
            builder.setPositiveButton(R.string.common_text_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (onOkClickListener != null) {
                        onOkClickListener.onClick(null);
                    }
                }
            });
        }

        if (showCancelButton) {
            builder.setNegativeButton(R.string.button_text_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (onCancelClickListener != null) {
                        onCancelClickListener.onClick(null);
                    }
                }
            });
        }

        builder.setOnDismissListener(onDismissListener);

        builder.show();
    }

    public void changeItemFavorite(final String statementId, final RestCallback<ApiResponse<Object>> onResponse) {

        final boolean isFavorite = userInfo.isStatementFavorite(statementId);

        if (isFavorite) {
            userInfo.removeFromFavorites(statementId);
        } else {
            userInfo.addToFavorites(statementId);
        }


        netApi.setFavorite(statementId, !isFavorite, new RestCallback<ApiResponse<Object>>() {
            @Override
            public void onResponse(ApiResponse<Object> response) {
                super.onResponse(response);
                if (!response.isSuccess()) {
                    if (isFavorite) {
                        userInfo.addToFavorites(statementId);
                    } else {
                        userInfo.removeFromFavorites(statementId);
                    }

                    AlertManager.showError(RootActivity.this, getString(R.string.common_text_error));
                    if (onResponse != null) {
                        onResponse.onResponse(response);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                if (isFavorite) {
                    userInfo.addToFavorites(statementId);
                } else {
                    userInfo.removeFromFavorites(statementId);
                }
                AlertManager.showError(RootActivity.this, getString(R.string.common_text_error));
                if (onResponse != null) {
                    onResponse.onFailure(t);
                }
            }
        });
    }

    public void changeUserSubscribtion(final String ownerId, final RestCallback<ApiResponse<Object>> onResponse) {

        final boolean isFavorite = userInfo.isUserSubscribed(ownerId);

        if (isFavorite) {
            userInfo.removeFromSubscribed(ownerId);
        } else {
            userInfo.subscribeUser(ownerId);
        }


        netApi.subscribeUser(ownerId, !isFavorite, new RestCallback<ApiResponse<Object>>() {
            @Override
            public void onResponse(ApiResponse<Object> response) {
                super.onResponse(response);
                if (!response.isSuccess()) {
                    if (isFavorite) {
                        userInfo.subscribeUser(ownerId);
                    } else {
                        userInfo.removeFromSubscribed(ownerId);
                    }

                    AlertManager.showError(RootActivity.this, getString(R.string.common_text_error));
                    if (onResponse != null) {
                        onResponse.onResponse(response);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                if (isFavorite) {
                    userInfo.subscribeUser(ownerId);
                } else {
                    userInfo.removeFromSubscribed(ownerId);
                }
                AlertManager.showError(RootActivity.this, getString(R.string.common_text_error));
                if (onResponse != null) {
                    onResponse.onFailure(t);
                }
            }
        });
    }

    public abstract int getLayoutId();

    public abstract boolean needEventBus();
}

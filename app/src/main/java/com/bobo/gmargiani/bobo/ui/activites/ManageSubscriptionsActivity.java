package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.SubscribedUsersEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.adapters.LazyLoaderListener;
import com.bobo.gmargiani.bobo.ui.adapters.OwnerAdapter;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ManageSubscriptionsActivity extends AuthorizedActivity implements StatementRecyclerAdapter.StatementItemClickListener, LazyLoaderListener {
    private SubscribedUsersEvent subscribedUsersEvent;
    private OwnerAdapter ownerAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ManageSubscriptionsActivity.class));
        }
    }

    @Subscribe
    public void onSubscribedUsersEvent(SubscribedUsersEvent event) {
        if (subscribedUsersEvent != event) {
            subscribedUsersEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpRecycler();
                    break;
            }
        }
    }

    private void setUpRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ownerAdapter = new OwnerAdapter(this, this, this, userInfo);
        ownerAdapter.setIsLoading(false);
        ownerAdapter.setData(subscribedUsersEvent.getUsers());
        recyclerView.setAdapter(ownerAdapter);
    }

    @Override
    public void userIsLoggedIn() {
        userInfo.requestSubscribedUsers(logInEvent.getLogInData().getUserDetails().getSubscribedUsers(), false);
    }

    @OnClick(R.id.full_retry_button)
    public void onRetry() {
        if (userInfo.isAuthorized()) {
            userInfo.requestSubscribedUsers(logInEvent.getLogInData().getUserDetails().getSubscribedUsers(), true);
        } else {
            userInfo.requestLogInEvent();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_manage_subscriptions;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_manage_subscriptions);
    }

    @Override
    public void onItemClick(int position) {
        if (subscribedUsersEvent != null && subscribedUsersEvent.getUsers().size() > position) {
            OwnerDetails item = subscribedUsersEvent.getUsers().get(position);
            if (logInEvent.getLogInData().getUserDetails().getOwnerId().equals(item.getOwnerId())) {
                RegistrationActivity.start(this, logInEvent.getLogInData().getUserDetails());
            } else {
                OwnerProfileActivity.start(this, item.getOwnerId());
            }
        }
    }

    public void refreshInfo() {
        if (ownerAdapter != null && subscribedUsersEvent.getUsers() != null) {
            for (int i = subscribedUsersEvent.getUsers().size() - 1; i >= 0; i--) {
                if (!userInfo.isUserSubscribed(subscribedUsersEvent.getUsers().get(i).getOwnerId())) {
                    subscribedUsersEvent.getUsers().remove(i);
                    break;
                }
            }
            ownerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFavoritesClick(int position) {
        try {
            if (userInfo == null || !userInfo.isAuthorized()) {
                showAuthorizationDialog(null);
            } else if (subscribedUsersEvent != null && subscribedUsersEvent.getUsers().size() > position) {

                changeUserSubscribtion(subscribedUsersEvent.getUsers().get(position).getOwnerId(), new RestCallback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(ApiResponse<Object> response) {
                        super.onResponse(response);
                       refreshInfo();

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        super.onFailure(t);
                        refreshInfo();
                    }
                });
            }


        } catch (Exception ignored) {

        }
    }

    @Override
    public void onLastItemIsVisible() {

    }

    @Override
    public void onLazyLoaderErrorClick() {

    }
}

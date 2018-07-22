package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.FavoriteStatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoritesActivity extends AuthorizedActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private StatementRecyclerAdapter adapter;
    private FavoriteStatementsEvent favoriteStatementsEvent;

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, FavoritesActivity.class));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Subscribe
    public void onFavoriteItems(FavoriteStatementsEvent event) {
        if (favoriteStatementsEvent != event) {
            favoriteStatementsEvent = event;

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    break;
            }
        }
    }

    @Override
    public void userIsLoggedIn() {
        if (userInfo.isAuthorized()) {
            userInfo.requestFavoriteStatements(false);
        }
    }

    @OnClick(R.id.full_retry)
    public void onRetry() {
        userInfo.requestLogInEvent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_favorites;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_favorites);
    }
}

package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.FavoriteStatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoritesActivity extends AuthorizedActivity implements StatementRecyclerAdapter.StatementItemClickListener {
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
                    setUpRecycler();
                    break;
            }
        }
    }

    private void setUpRecycler() {
        boolean isGrid = PreferencesApiManager.getInstance().listIsGrid();

        if (isGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (recyclerView.getAdapter().getItemViewType(position) == StatementRecyclerAdapter.HOLDER_TYPE_ITEM) {
                        return 1;
                    }
                    return 2;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        adapter = new StatementRecyclerAdapter(this, isGrid ? StatementRecyclerAdapter.ADAPTER_TYPE_GRID : StatementRecyclerAdapter.ADAPTER_TYPE_LIST,
                this, null, userInfo);
        adapter.setIsLoading(false);

        adapter.setData(favoriteStatementsEvent.getStatementItems());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void userIsLoggedIn() {
        if (userInfo.isAuthorized()) {
            userInfo.requestFavoriteStatements(false);
        }
    }

    @OnClick(R.id.full_retry)
    public void onRetry() {
        if (userInfo.isAuthorized()) {
            userInfo.requestFavoriteStatements(false);
        } else {
            userInfo.requestLogInEvent();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_favorites;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_favorites);
    }

    @Override
    public void onItemClick(int position) {
        if (favoriteStatementsEvent != null && favoriteStatementsEvent.getStatementItems().size() > position) {
            StatementItem item = favoriteStatementsEvent.getStatementItems().get(position);

            StatementDetailsActivity.start(this, item, userInfo);
        }
    }

    public void refreshInfo() {
        if (adapter != null && favoriteStatementsEvent.getStatementItems() != null) {
            for (int i = favoriteStatementsEvent.getStatementItems().size() - 1; i >=0; i--){
                if (!userInfo.isStatementFavorite(favoriteStatementsEvent.getStatementItems().get(i).getStatementId())){
                    favoriteStatementsEvent.getStatementItems().remove(i);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFavoritesClick(int position) {
        try {
            if (userInfo == null || !userInfo.isAuthorized()) {
                showAuthorizationDialog(null);
            } else if (favoriteStatementsEvent != null && favoriteStatementsEvent.getStatementItems().size() > position) {
                if (userInfo.isUsersItem(favoriteStatementsEvent.getStatementItems().get(position).getOwnerId())) {
                    NewStatementActivity.start(this, favoriteStatementsEvent.getStatementItems().get(position));
                } else {
                    changeItemFavorite(favoriteStatementsEvent.getStatementItems().get(position).getStatementId(), new RestCallback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(ApiResponse<Object> response) {
                            super.onResponse(response);
                            if (!response.isSuccess()) {
                                refreshInfo();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            super.onFailure(t);
                            refreshInfo();
                        }
                    });
                    refreshInfo();
                }
            }

        } catch (Exception ignored) {

        }
    }
}


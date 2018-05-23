package com.bobo.gmargiani.bobo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerSearchEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementSearchEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.ui.adapters.LazyLoaderListener;
import com.bobo.gmargiani.bobo.ui.adapters.OwnerAdapter;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class SearchListFragment extends RootFragment implements LazyLoaderListener, RecyclerItemClickListener {
    public static final int LIST_TYPE_STATEMENT = 0;
    public static final int LIST_TYPE_OWNER = 10;

    private OwnerAdapter ownerAdapter;
    private StatementRecyclerAdapter statementAdapter;

    private OwnerSearchEvent ownerSearchEvent;
    private StatementSearchEvent statementSearchEvent;

    private RecyclerView recyclerView;

    private int listType;
    private String searchQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        searchQuery = getArguments().getString(AppConsts.PARAM_SEARCH_QUERY);
        listType = getArguments().getInt(AppConsts.PARAM_LIST_TYPE, LIST_TYPE_STATEMENT);
    }

    public void setSearchQuery(String query) {
        searchQuery = query;
        ownerAdapter = null;
        statementAdapter = null;
        setUpRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        App.getInstance().getEventBus().register(this);
        setUpRecyclerView();

        if (ownerAdapter != null) {
            ownerAdapter.checkLoader(recyclerView);
        }

        if (statementAdapter != null) {
            statementAdapter.checkLoader(recyclerView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getInstance().getEventBus().unregister(this);
    }

    @Subscribe
    public void onOwnerDetails(OwnerSearchEvent event) {
        if (ownerSearchEvent != event && ownerAdapter != null) {
            ownerSearchEvent = event;

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    ownerAdapter.setIsLoading(true);
                    break;
                case RootEvent.STATE_ERROR:
                    ownerAdapter.setError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    ownerAdapter.setData(event.getOwners());
                    ownerAdapter.setIsLoading(event.canLoadMore());
                    break;
            }
        }
    }

    @Subscribe
    public void onStatementItems(StatementSearchEvent event) {
        if (statementSearchEvent != event && statementAdapter != null) {
            statementSearchEvent = event;

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    statementAdapter.setIsLoading(true);
                    break;
                case RootEvent.STATE_ERROR:
                    statementAdapter.setError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    statementAdapter.setData(event.getStatements());
                    statementAdapter.setIsLoading(event.canLoadMore());
                    break;
            }
        }
    }

    private void setUpRecyclerView() {
        if (statementAdapter == null && ownerAdapter == null) {
            boolean isGrid = PreferencesApiManager.getInstance().listIsGrid();
            if (isGrid) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
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
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            ownerSearchEvent = null;
            statementSearchEvent = null;

            switch (listType) {
                case LIST_TYPE_OWNER:
                    ownerAdapter = new OwnerAdapter(getContext(), this, this);
                    ownerAdapter.setIsLoading(true);
                    ownerAdapter.setData(new ArrayList<>());
                    recyclerView.setAdapter(ownerAdapter);
                    break;
                default:
                    statementAdapter = new StatementRecyclerAdapter(getContext(),
                            isGrid ? StatementRecyclerAdapter.ADAPTER_TYPE_GRID : StatementRecyclerAdapter.ADAPTER_TYPE_LIST, this, this);
                    statementAdapter.setIsLoading(true);
                    statementAdapter.setData(new ArrayList<>());
                    recyclerView.setAdapter(statementAdapter);
                    break;
            }

        }
    }

    @Override
    public void onLastItemIsVisible() {
        switch (listType) {
            case LIST_TYPE_OWNER:
                App.getInstance().getUserInfo().searchOwners(searchQuery, ownerAdapter.getItemCount() - 1);
                break;
            default:
                App.getInstance().getUserInfo().searchStatements(searchQuery, statementAdapter.getItemCount() - 1);
                break;
        }

    }

    @Override
    public void onLazyLoaderErrorClick() {
        onLastItemIsVisible();
    }

    @Override
    public void onRecyclerItemClick(int pos) {

    }
}


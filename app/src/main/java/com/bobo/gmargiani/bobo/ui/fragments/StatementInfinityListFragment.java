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
import com.bobo.gmargiani.bobo.evenbuts.events.StatementSearchEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.ui.adapters.LazyLoaderListener;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class StatementInfinityListFragment extends RootFragment implements LazyLoaderListener, RecyclerItemClickListener {
    private StatementRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private StatementSearchEvent statementSearchEvent;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        App.getInstance().getEventBus().register(this);
        setUpRecyclerView();
        adapter.checkLoader(recyclerView);
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getInstance().getEventBus().unregister(this);
    }

    @Subscribe
    public void onStatementItems(final StatementSearchEvent event) {
        if (statementSearchEvent != event) {
            statementSearchEvent = event;

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    adapter.setIsLoading(true);
                    break;
                case RootEvent.STATE_ERROR:
                    adapter.setError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    adapter.setData(event.getStatements());
                    adapter.setIsLoading(event.canLoadMore());
                    break;
            }
        }
    }

    private void setUpRecyclerView() {
        if (adapter == null) {
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

            adapter = new StatementRecyclerAdapter(getContext(), isGrid ? StatementRecyclerAdapter.ADAPTER_TYPE_GRID : StatementRecyclerAdapter.ADAPTER_TYPE_LIST, this, this);
            adapter.setIsLoading(true);
            statementSearchEvent = null;
            adapter.setData(new ArrayList<StatementItem>());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onRecyclerItemClick(int pos) {

    }

    @Override
    public void onLastItemIsVisible() {
        App.getInstance().getUserInfo().searchStatements(searchQuery, adapter.getItemCount() - 1);
    }

    @Override
    public void onLazyLoaderErrorClick() {
        onLastItemIsVisible();
    }
}

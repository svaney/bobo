package com.bobo.gmargiani.bobo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerSearchEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementSearchEvent;
import com.bobo.gmargiani.bobo.ui.adapters.LazyLoaderListener;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.utils.AppConsts;

public class OwnerInfinityListFragment extends RootFragment implements LazyLoaderListener, RecyclerItemClickListener {
    private StatementRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private OwnerSearchEvent ownerSearchEvent;
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
    public void onLastItemIsVisible() {
        
    }

    @Override
    public void onLazyLoaderErrorClick() {

    }

    @Override
    public void onRecyclerItemClick(int pos) {

    }
}

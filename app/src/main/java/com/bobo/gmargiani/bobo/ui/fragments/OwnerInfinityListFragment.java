package com.bobo.gmargiani.bobo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.adapters.LazyLoaderListener;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;

public class OwnerInfinityListFragment extends RootFragment implements LazyLoaderListener, RecyclerItemClickListener {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onRecyclerItemClick(int pos) {

    }

    @Override
    public void onLastItemIsVisible() {

    }

    @Override
    public void onLazyLoaderErrorClick() {

    }
}

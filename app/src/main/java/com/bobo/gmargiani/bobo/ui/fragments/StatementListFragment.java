package com.bobo.gmargiani.bobo.ui.fragments;

import android.content.Intent;
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
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.ui.activites.StatementDetailsActivity;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.parceler.Parcels;

import java.util.ArrayList;

public class StatementListFragment extends RootFragment implements RecyclerItemClickListener {

    private ArrayList<StatementItem> statements;

    private RecyclerView recyclerView;
    private StatementRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statements = App.getInstance().getUserInfo().getOwnerStatements(getArguments().getLong(AppConsts.PARAM_OWNER_ID, -1));

        if (statements != null) {
            recyclerView = view.findViewById(R.id.recycler_view);

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

            adapter = new StatementRecyclerAdapter(getContext(), isGrid ? StatementRecyclerAdapter.ADAPTER_TYPE_GRID : StatementRecyclerAdapter.ADAPTER_TYPE_LIST, this, null);
            adapter.setIsLoading(false);

            adapter.setData(statements);
            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onRecyclerItemClick(int pos) {
        if (statements != null && statements.size() > pos) {
            StatementItem item = statements.get(pos);

            StatementDetailsActivity.start(getContext(), item);
        }
    }
}

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
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.activites.OwnerProfileActivity;
import com.bobo.gmargiani.bobo.ui.activites.RootActivity;
import com.bobo.gmargiani.bobo.ui.activites.StatementDetailsActivity;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.parceler.Parcels;

import java.util.ArrayList;

public class StatementListFragment extends RootFragment implements StatementRecyclerAdapter.StatementItemClickListener {

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
        statements = userInfo.getOwnerStatements(getArguments().getString(AppConsts.PARAM_OWNER_ID));

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

            adapter = new StatementRecyclerAdapter(getContext(), isGrid ? StatementRecyclerAdapter.ADAPTER_TYPE_GRID : StatementRecyclerAdapter.ADAPTER_TYPE_LIST,
                    this, null, userInfo);
            adapter.setIsLoading(false);

            adapter.setData(statements);
            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onItemClick(int position) {
        if (statements != null && statements.size() > position) {
            StatementItem item = statements.get(position);

            StatementDetailsActivity.start(getContext(), item, userInfo);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInfo();
    }

    public void refreshInfo() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFavoritesClick(int position) {
        try {
            if (userInfo == null || !userInfo.isAuthorized()) {
                ((RootActivity) getActivity()).showAuthorizationDialog(null);
            } else if (statements != null && statements.size() > position) {
                ((RootActivity) getActivity()).changeItemFavorite(statements.get(position).getStatementId(), new RestCallback<ApiResponse<Object>>() {
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

        } catch (Exception ignored) {

        }
    }

}

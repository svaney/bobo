package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.utils.DummyRecyclerViewHolder;

import java.util.ArrayList;

public abstract class InfinityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HOLDER_TYPE_LOADER = 10;
    public static final int HOLDER_TYPE_ITEM = 20;
    public static final int HOLDER_TYPE_ERROR = 30;

    LazyLoaderListener lazyLoaderListener;


    ArrayList<?> items;

    boolean isLoading;
    boolean isError;
    boolean loaderChecked;

    Context context;

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            checkIfLastItemIsVisible(recyclerView);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            checkIfLastItemIsVisible(recyclerView);
        }

        private void checkIfLastItemIsVisible(RecyclerView recyclerView) {

            if (lazyLoaderListener != null && recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int pos = layoutManager.findLastVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
                if (isLoading && pos >= numItems - 1) {
                    lazyLoaderListener.onLastItemIsVisible();
                } else if (!loaderChecked) {
                    loaderChecked = true;
                    notifyItems();
                }
            }
        }
    };

    public void setData(ArrayList<?> items) {
        this.items = items;
        notifyItems();
    }

    public void checkLoader(RecyclerView recyclerView) {
        loaderChecked = false;
        onScrollListener.onScrolled(recyclerView, 0, 0);
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
        this.isError = false;
        notifyItems();
    }

    public void setError() {
        this.isError = true;
        this.isLoading = false;
        notifyItems();
    }

    void notifyItems() {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                LazyLoaderListener temp = lazyLoaderListener;
                lazyLoaderListener = null;
                notifyDataSetChanged();
                lazyLoaderListener = temp;
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
    }


    @Override
    public int getItemViewType(int position) {
        if ((isLoading || isError) && position == getItemCount() - 1) {
            return isError ? HOLDER_TYPE_ERROR : HOLDER_TYPE_LOADER;
        }

        return HOLDER_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return isLoading || isError ? getListSize() + 1 : getListSize();
    }

    private int getListSize() {
        return items == null ? 0 : items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HOLDER_TYPE_ERROR) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_error_wrapper, parent, false);
            return new ErrorHolder(view);
        } else if (viewType == HOLDER_TYPE_LOADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_loader, parent, false);
            return new DummyRecyclerViewHolder(view);
        }
        return onCreateItemViewHolder(parent, viewType);
    }

    abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    class ErrorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View errorButton;

        public ErrorHolder(View itemView) {
            super(itemView);
            errorButton = itemView.findViewById(R.id.full_retry_button);
            errorButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (lazyLoaderListener != null) {
                lazyLoaderListener.onLazyLoaderErrorClick();
            }
        }
    }
}

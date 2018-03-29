package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.utils.DummyRecyclerViewHolder;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

import java.util.ArrayList;

public class StatementRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int HOLDER_TYPE_LOADER = 10;
    private static int HOLDER_TYPE_ITEM = 20;
    private static int HOLDER_TYPE_ERROR = 30;

    private Context context;
    private LazyLoaderListener lazyLoaderListener;
    private ArrayList<StatementItem> items;

    private boolean isGrid;
    private boolean isLoading;
    private boolean isError;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
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
                }
            }
        }
    };

    public StatementRecyclerAdapter(Context context, boolean isGrid, LazyLoaderListener listener) {
        this.context = context;
        this.isGrid = isGrid;
        this.lazyLoaderListener = listener;
    }

    public void checkLoader(RecyclerView recyclerView) {
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

    private void notifyItems() {
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

    public void setData(ArrayList<StatementItem> items) {
        this.items = items;
        notifyItems();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (items == null ? 0 : items.size())) {
            return isError ? HOLDER_TYPE_ERROR : HOLDER_TYPE_LOADER;
        }
        return HOLDER_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(isGrid ? R.layout.recycler_item_statement_grid : R.layout.recycler_item_statement, parent, false);
            return new StatementHolder(view);
        }

        if (viewType == HOLDER_TYPE_ERROR) {
            View view = LayoutInflater.from(context).inflate(R.layout.component_retry_view, parent, false);
            return new ErrorHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(isGrid ? R.layout.recycler_item_loader_grid : R.layout.recycler_item_loader, parent, false);
        return new DummyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h.getItemViewType() == HOLDER_TYPE_ITEM) {
            StatementHolder holder = (StatementHolder) h;
            StatementItem item = items.get(position);
            holder.title.setText(item.getTitle());
            holder.price.setText(String.valueOf(item.getPrice()));

            ImageLoader.load(holder.image)
                    .setUrl(item.getMainImage())
                    .setPlaceHolderId(R.drawable.image_error_place_holder)
                    .setErroPlaceHolder(R.drawable.image_error_place_holder)
                    .build();
        }
    }

    @Override
    public int getItemCount() {
        int hasExtraSize = isLoading || isError ? 1 : 0;
        int size = items == null ? hasExtraSize : items.size() + hasExtraSize;
        return size;
    }

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

    class StatementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView price;
        ImageView image;

        public StatementHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.statement_title);
            price = itemView.findViewById(R.id.statement_price);
            image = itemView.findViewById(R.id.statement_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface LazyLoaderListener {
        void onLastItemIsVisible();

        void onLazyLoaderErrorClick();
    }
}

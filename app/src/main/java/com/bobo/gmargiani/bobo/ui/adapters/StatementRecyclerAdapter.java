package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
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

    private Context context;

    private ArrayList<StatementItem> items;

    private boolean isGrid;

    public StatementRecyclerAdapter(Context context, boolean isGrid) {
        this.context = context;
        this.isGrid = isGrid;
    }

    public void setData(ArrayList<StatementItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (items == null ? 0 : items.size())) {
            return HOLDER_TYPE_LOADER;
        }
        return HOLDER_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(isGrid ? R.layout.recycler_item_statement_grid : R.layout.recycler_item_statement, parent, false);
            return new StatementHolder(view);
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
                    .setPlaceHolderId(R.drawable.transparent_placeholder)
                    .setErroPlaceHolder(R.drawable.image_error_place_holder)
                    .build();
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 1 : items.size() + 1;
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
}

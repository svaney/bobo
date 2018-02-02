package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.BasicRecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.NewProductAdapterListener;
import com.bobo.gmargiani.bobo.utils.ModelUtils;

import java.util.ArrayList;

/**
 * Created by gmargiani on 2/1/2018.
 */

public class NewProductRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_HOLDER_TYPE_PRODUCT_ITEM = 10;
    private static final int VIEW_HOLDER_TYPE_COMMENT = 20;


    private ArrayList<ProductItem> items;
    private Context context;
    private NewProductAdapterListener listener;

    public NewProductRecyclerAdapter(ArrayList<ProductItem> items, Context context, NewProductAdapterListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < items.size()) {
            return VIEW_HOLDER_TYPE_PRODUCT_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HOLDER_TYPE_PRODUCT_ITEM:
                return new ProductItemHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_new_product_item, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_HOLDER_TYPE_PRODUCT_ITEM:

                ProductItemHolder itemHolder = (ProductItemHolder) holder;
                ProductItem item = items.get(position);

                itemHolder.title.setText(item.getTitle());
                itemHolder.description.setText(ModelUtils.getNewProductDescription(item));
                break;
            case VIEW_HOLDER_TYPE_COMMENT:


                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ProductItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, description;
        public View delete;

        public ProductItemHolder(View view) {
            super(view);
            title = view.findViewById(R.id.product_title);
            description = view.findViewById(R.id.product_description);
            delete = view.findViewById(R.id.delete_product);

            view.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                if (view == delete) {
                    listener.onItemDelete(getAdapterPosition());
                }else {
                    listener.onItemClick(getAdapterPosition());
                }
            }
        }
    }

}

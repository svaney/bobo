package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.dialogs.ListDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_TYPE_SINGLE = 10;
    private static final int HOLDER_TYPE_MULTIPLE = 20;

    private int listType;
    private Context context;
    private ArrayList<Pair<String, Boolean>> data;

    public DialogListAdapter(Context context, ArrayList<Pair<String, Boolean>> data) {
        this(context, data, ListDialog.DIALOG_LIST_TYPE_SINGLE);
    }

    public DialogListAdapter(Context context, ArrayList<Pair<String, Boolean>> data, int type) {
        this.listType = type;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (listType == ListDialog.DIALOG_LIST_TYPE_MULTIPLE) {
            return HOLDER_TYPE_MULTIPLE;
        }
        return HOLDER_TYPE_SINGLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_MULTIPLE) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycle_item_dialog_multi, parent, false);
            return new MultiItemHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_dialog_single, parent, false);
        return new SingleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HOLDER_TYPE_MULTIPLE:
                MultiItemHolder h = (MultiItemHolder) holder;
                h.itemText.setText(data.get(position).first);
                h.itemCheck.setChecked(data.get(position).second);
                break;
            default:
                SingleItemHolder h1 = (SingleItemHolder) holder;
                h1.itemText.setText(data.get(position).first);
                h1.itemRadio.setChecked(data.get(position).second);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MultiItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemText;
        CheckBox itemCheck;
        View itemWrapper;

        public MultiItemHolder(View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.item_text);
            itemCheck = itemView.findViewById(R.id.item_check);
            itemWrapper = itemView.findViewById(R.id.item_wrapper);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    class SingleItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemText;
        RadioButton itemRadio;
        View itemWrapper;

        public SingleItemHolder(View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.item_text);
            itemRadio = itemView.findViewById(R.id.item_radio);
            itemWrapper = itemView.findViewById(R.id.item_wrapper);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

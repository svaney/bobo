package com.bobo.gmargiani.bobo.ui.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.adapters.DialogListAdapter;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;

import java.util.ArrayList;

public class ListDialog extends BaseDialog implements RecyclerItemClickListener {
    public static final int DIALOG_LIST_TYPE_SINGLE = 10;
    public static final int DIALOG_LIST_TYPE_MULTIPLE = 20;

    private int listType;

    private View buttonContainer;
    private Button okButton;
    private Button cancelButton;
    private RecyclerView recyclerView;

    private DialogListAdapter adapter;
    private ArrayList<DialogPair> data;

    private ListDialogItemsSelectedListener listener;

    public ListDialog(Context context, ListDialogItemsSelectedListener listener) {
        this(context, DIALOG_LIST_TYPE_SINGLE, listener);
    }

    public ListDialog(Context context, int type, ListDialogItemsSelectedListener listener) {
        super(context);
        this.listType = type;
        this.listener = listener;

        findViews();

        this.adapter = new DialogListAdapter(getContext(), data, listType, this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(adapter);
    }

    private void findViews() {
        buttonContainer = findViewById(R.id.button_wrapper);
        okButton = findViewById(R.id.ok_button);
        cancelButton = findViewById(R.id.cancel_button);
        recyclerView = findViewById(R.id.recycler_view);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && adapter != null) {
                    listener.onItemsSelected(adapter.getSelectedPositions());
                }
                dismiss();
            }
        });
    }

    public void setList(ArrayList<DialogPair> data) {
        this.data = data;
        if (adapter != null) {
            adapter.setData(data);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_list;
    }

    @Override
    public void onRecyclerItemClick(int pos) {
        okButton.callOnClick();
    }

    public interface ListDialogItemsSelectedListener {
        void onItemsSelected(ArrayList<Integer> itemPositions);
    }

}

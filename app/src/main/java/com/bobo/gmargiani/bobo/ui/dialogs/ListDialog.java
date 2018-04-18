package com.bobo.gmargiani.bobo.ui.dialogs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bobo.gmargiani.bobo.R;

public class ListDialog extends BaseDialog {
    public static final int DIALOG_LIST_TYPE_SINGLE = 10;
    public static final int DIALOG_LIST_TYPE_MULTIPLE = 20;

    private int listType;

    private View buttonContainer;
    private Button okButton;
    private Button cancelButton;
    private RecyclerView recyclerView;

    public ListDialog(Context context) {
        this(context, DIALOG_LIST_TYPE_SINGLE);
    }

    public ListDialog(Context context, int type) {
        super(context);
        listType = type;
        findViews();
    }

    private void findViews() {
        buttonContainer = findViewById(R.id.button_wrapper);
        okButton = findViewById(R.id.ok_button);
        cancelButton = findViewById(R.id.cancel_button);
        recyclerView = findViewById(R.id.recycler_view);

        buttonContainer.setVisibility(listType == DIALOG_LIST_TYPE_MULTIPLE ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_list;
    }
}

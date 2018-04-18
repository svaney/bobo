package com.bobo.gmargiani.bobo.ui.activites;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ModelGenerator;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class FilterActivity extends RootDetailedActivity {
    @BindView(R.id.statement_type_radio_group)
    RadioGroup statementTypeRadioGroup;

    @BindView(R.id.radio_sell)
    RadioButton radioSell;

    @BindView(R.id.radio_rent)
    RadioButton radioRent;

    @BindView(R.id.price_from)
    EditText priceFrom;

    @BindView(R.id.price_to)
    EditText priceTo;

    @BindView(R.id.list_type_grid)
    ImageView icListTypeGrid;

    @BindView(R.id.list_type)
    ImageView icListType;


    @BindView(R.id.order_by_value)
    TextView orderByValue;

    @BindView(R.id.period_value)
    TextView periodValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferencesApiManager.getInstance().listIsGrid()) {
            onGridClick();
        } else {
            onListTypeClick();
        }
    }

    @OnClick(R.id.filter_button)
    public void onFilterClick() {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClick() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.category)
    public void onCategoryClick() {

    }

    @OnClick(R.id.location)
    public void onLocationClick() {

    }

    @OnClick(R.id.order_by)
    public void onOrderByClick() {

    }

    @OnClick(R.id.show_period)
    public void onPeriodClick() {

    }


    @OnClick(R.id.list_type_grid)
    public void onGridClick() {

        ImageLoader.load(icListTypeGrid)
                .setRes(R.drawable.ic_list_type_grid)
                .applyTint(R.color.colorAccent)
                .build();

        ImageLoader.load(icListType)
                .setRes(R.drawable.ic_list_type)
                .build();

        PreferencesApiManager.getInstance().setListGrid(true);
    }

    @OnClick(R.id.list_type)
    public void onListTypeClick() {

        ImageLoader.load(icListType)
                .setRes(R.drawable.ic_list_type)
                .applyTint(R.color.colorAccent)
                .build();

        ImageLoader.load(icListTypeGrid)
                .setRes(R.drawable.ic_list_type_grid)
                .build();

        PreferencesApiManager.getInstance().setListGrid(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    @Override
    protected int getHeaderText() {
        return (R.string.activity_name_filter);
    }

}

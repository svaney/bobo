package com.bobo.gmargiani.bobo.ui.activites;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.Utils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import org.w3c.dom.Text;

import butterknife.BindView;

public class FilterActivity extends RootDetailedActivity {
    @BindView(R.id.filter_button)
    Button filterButton;

    @BindView(R.id.cancel_button)
    Button cancelButton;

    @BindView(R.id.radio_sell)
    RadioButton radioSell;

    @BindView(R.id.radio_rent)
    RadioButton radioRent;

    @BindView(R.id.price_from)
    EditText priceFrom;

    @BindView(R.id.price_to)
    EditText priceTo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        priceFrom.clearFocus();
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

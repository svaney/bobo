package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.ui.dialogs.DialogPair;
import com.bobo.gmargiani.bobo.ui.dialogs.ListDialog;
import com.bobo.gmargiani.bobo.ui.views.FilterTextView;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.parceler.Parcels;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class FilterActivity extends RootDetailedActivity implements CompoundButton.OnCheckedChangeListener {
    public static final int FILTER_PARAMS_SIZE = 7;

    public static final int FILTER_PARAM_POS_SELL = 0;
    public static final int FILTER_PARAM_POS_RENT = 1;
    public static final int FILTER_PARAM_POS_CATEGORY = 2;
    public static final int FILTER_PARAM_POS_LOCATION = 3;
    public static final int FILTER_PARAM_POS_PRICE_FROM = 4;
    public static final int FILTER_PARAM_POS_PRICE_TO = 5;
    public static final int FILTER_PARAM_POS_ORDER_BY = 6;

    @BindView(R.id.check_sell)
    CheckBox checkSell;

    @BindView(R.id.check_rent)
    CheckBox checkRent;

    @BindView(R.id.price_from)
    EditText priceFrom;

    @BindView(R.id.price_to)
    EditText priceTo;

    @BindView(R.id.list_type_grid)
    ImageView icListTypeGrid;

    @BindView(R.id.list_type)
    ImageView icListType;

    @BindView(R.id.category_values_wrapper)
    LinearLayout categoryValuesWrapper;

    @BindView(R.id.location_values_wrapper)
    LinearLayout locationValuesWrapper;

    @BindView(R.id.order_by_values_wrapper)
    LinearLayout orderByValueWrapper;

    private ArrayList<String> filterValues;

    private LocationsEvent locationsEvent;
    private CategoriesEvent categoriesEvent;

    public static void startWithResult(FragmentActivity context, ArrayList<String> filterValues) {
        if (context != null) {
            Intent intent = new Intent(context, FilterActivity.class);
            if (filterValues != null) {
                intent.putExtra(AppConsts.PARAM_FILTER_PARAMS, Parcels.wrap(filterValues));
            }
            context.startActivityForResult(intent, AppConsts.RC_FILTER);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferencesApiManager.getInstance().listIsGrid()) {
            onGridClick();
        } else {
            onListTypeClick();
        }

        if (getIntent().getParcelableExtra(AppConsts.PARAM_FILTER_PARAMS) != null) {
            filterValues = Parcels.unwrap(getIntent().getParcelableExtra(AppConsts.PARAM_FILTER_PARAMS));
        } else {
            filterValues = new ArrayList<>();
            for (int i = 0; i < FILTER_PARAMS_SIZE; i++) {
                filterValues.add("");
            }
        }

        categoriesEvent = userInfo.getCategoriesEvent();
        locationsEvent = userInfo.getLocationsEvent();

        setValues();
    }

    private void setValues() {
        setSellRentValues();
        setCategoryValues();
        setLocationValues();
        setPriceValues();
        setOrderByValue();
    }

    private void setSellRentValues() {
        checkSell.setOnCheckedChangeListener(null);
        checkRent.setOnCheckedChangeListener(null);

        checkSell.setChecked(!"N".equals(filterValues.get(FILTER_PARAM_POS_SELL)));
        checkRent.setChecked(!"N".equals(filterValues.get(FILTER_PARAM_POS_RENT)));

        checkRent.setOnCheckedChangeListener(this);
        checkSell.setOnCheckedChangeListener(this);
    }

    private void setPriceValues() {
        priceFrom.setText("");
        priceTo.setText("");

        try {
            priceFrom.setText(String.valueOf(new BigDecimal(filterValues.get(FILTER_PARAM_POS_PRICE_FROM))));
        } catch (Exception ignored) {
            filterValues.set(FILTER_PARAM_POS_PRICE_FROM, "");
        }

        try {
            priceTo.setText(String.valueOf(new BigDecimal(filterValues.get(FILTER_PARAM_POS_PRICE_TO))));
        } catch (Exception ignored) {
            filterValues.set(FILTER_PARAM_POS_PRICE_TO, "");
        }


    }

    private void setOrderByValue() {
        orderByValueWrapper.removeAllViews();

        String value = filterValues.get(FILTER_PARAM_POS_ORDER_BY);

        FilterTextView txt = new FilterTextView(this);
        txt.setText(getString(R.string.filter_option_time));
        orderByValueWrapper.addView(txt);

        if (!TextUtils.isEmpty(value)) {
            txt.setText(value);
        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOrderByClick();
            }
        });
    }

    private void setCategoryValues() {
        categoryValuesWrapper.removeAllViews();

        String allValues = filterValues.get(FILTER_PARAM_POS_CATEGORY);

        if (!TextUtils.isEmpty(allValues) && allValues.split(";").length > 0) {
            ArrayList<String> keys = new ArrayList<>(Arrays.asList(allValues.split(";")));

            for (final String currKey : keys) {
                final FilterTextView txt = new FilterTextView(this);
                txt.setText(categoriesEvent.getValueByKey(currKey));
                txt.showCloseButton(true);
                categoryValuesWrapper.addView(txt);

                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCategoryClick();
                    }
                });

                txt.setOnCloseClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newVal = filterValues.get(FILTER_PARAM_POS_CATEGORY);
                        filterValues.set(FILTER_PARAM_POS_CATEGORY, newVal.replace(currKey + ";", ""));
                        setCategoryValues();

                        try {
                            TransitionManager.beginDelayedTransition(categoryValuesWrapper);
                        } catch (Exception ignored) {
                        }
                    }
                });
            }

        } else {
            FilterTextView txt = new FilterTextView(this);
            txt.setText(getString(R.string.common_text_all));
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCategoryClick();
                }
            });
            categoryValuesWrapper.addView(txt);
        }
    }

    private void setLocationValues() {
        locationValuesWrapper.removeAllViews();

        String allValues = filterValues.get(FILTER_PARAM_POS_LOCATION);

        if (!TextUtils.isEmpty(allValues) && allValues.split(";").length > 0) {
            ArrayList<String> keys = new ArrayList<>(Arrays.asList(allValues.split(";")));

            for (final String currKey : keys) {
                final FilterTextView txt = new FilterTextView(this);
                txt.setText(locationsEvent.getValueByKey(currKey));
                txt.showCloseButton(true);
                locationValuesWrapper.addView(txt);

                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLocationClick();
                    }
                });

                txt.setOnCloseClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newVal = filterValues.get(FILTER_PARAM_POS_LOCATION);
                        filterValues.set(FILTER_PARAM_POS_LOCATION, newVal.replace(currKey + ";", ""));
                        setLocationValues();

                        try {
                            TransitionManager.beginDelayedTransition(locationValuesWrapper);
                        } catch (Exception ignored) {
                        }
                    }
                });
            }

        } else {
            FilterTextView txt = new FilterTextView(this);
            txt.setText(getString(R.string.common_text_all));
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLocationClick();
                }
            });
            locationValuesWrapper.addView(txt);
        }
    }

    @OnClick(R.id.filter_button)
    public void onFilterClick() {
        Intent resultIntent = new Intent();

        try {
            filterValues.set(FILTER_PARAM_POS_PRICE_FROM, String.valueOf(new BigDecimal(priceFrom.getText().toString())));
        }catch (Exception e){
            filterValues.set(FILTER_PARAM_POS_PRICE_FROM,"");
        }

        try {
            filterValues.set(FILTER_PARAM_POS_PRICE_TO, String.valueOf(new BigDecimal(priceTo.getText().toString())));
        }catch (Exception e){
            filterValues.set(FILTER_PARAM_POS_PRICE_TO,"");
        }

        resultIntent.putExtra(AppConsts.PARAM_FILTER_PARAMS, Parcels.wrap(filterValues));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClick() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.category)
    public void onCategoryClick() {
        ListDialog categoryDialog = new ListDialog(this, ListDialog.DIALOG_LIST_TYPE_MULTIPLE, new ListDialog.ListDialogItemsSelectedListener() {
            @Override
            public void onItemsSelected(ArrayList<Integer> itemPositions) {
                if (itemPositions != null) {
                    String newCategories = "";
                    for (Integer i : itemPositions) {
                        newCategories += categoriesEvent.getCategories().get(i).getKey() + ";";
                    }
                    filterValues.set(FILTER_PARAM_POS_CATEGORY, newCategories);
                    setCategoryValues();
                }
            }
        });

        ArrayList<DialogPair> data = new ArrayList<>();

        for (KeyValue kv : categoriesEvent.getCategories()) {
            data.add(new DialogPair(kv.getValue(), filterValues.get(FILTER_PARAM_POS_CATEGORY).contains(kv.getKey() + ";")));
        }

        categoryDialog.setList(data);
        categoryDialog.show();
    }

    @OnClick(R.id.location)
    public void onLocationClick() {
        ListDialog locationDialog = new ListDialog(this, ListDialog.DIALOG_LIST_TYPE_MULTIPLE, new ListDialog.ListDialogItemsSelectedListener() {
            @Override
            public void onItemsSelected(ArrayList<Integer> itemPositions) {
                if (itemPositions != null) {
                    String newLocations = "";
                    for (Integer i : itemPositions) {
                        newLocations += locationsEvent.getLocations().get(i).getKey() + ";";
                    }
                    filterValues.set(FILTER_PARAM_POS_LOCATION, newLocations);
                    setLocationValues();
                }
            }
        });

        ArrayList<DialogPair> data = new ArrayList<>();

        for (KeyValue kv : locationsEvent.getLocations()) {
            data.add(new DialogPair(kv.getValue(), filterValues.get(FILTER_PARAM_POS_LOCATION).contains(kv.getKey() + ";")));
        }

        locationDialog.setList(data);
        locationDialog.show();

    }

    @OnClick(R.id.order_by)
    public void onOrderByClick() {

    }

    @Override
    public void onCheckedChanged(CompoundButton checkView, boolean checked) {
        String value = checked ? "Y" : "N";

        if (checkView == checkSell) {
            filterValues.set(FILTER_PARAM_POS_SELL, value);
        } else if (checkView == checkRent) {
            filterValues.set(FILTER_PARAM_POS_RENT, value);
        }
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
    protected String getHeaderText() {
        return getString(R.string.activity_name_filter);
    }
}

package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.adapters.NewProductRecyclerAdapter;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.BasicRecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.NewProductAdapterListener;
import com.bobo.gmargiani.bobo.ui.views.DropDownChooser;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ImageUtils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;
import com.bobo.gmargiani.bobo.utils.consts.AppConsts;
import com.bobo.gmargiani.bobo.utils.consts.ModelConsts;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewOrderActivity extends RootDetailedActivity implements NewProductAdapterListener, BasicRecyclerItemClickListener {
    private static final int LIST_TYPE_MEASURMENT = 0;
    private static final int LIST_TYPE_PICTURE = 1;


    private static final int KG_POSITION = 0;
    private static final int GRAM_POSITION = 1;

    private static final int LITRE_POSITION = 0;
    private static final int MILLILITRE_POSITION = 1;


    @BindView(R.id.products_recycler)
    RecyclerView productsRecycler;

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;

    @BindView(R.id.button_container)
    View newProductButtonsContainer;

    @BindView(R.id.new_product_button)
    View newProductButton;

    @BindView(R.id.add_button)
    Button addButton;

    @BindView(R.id.dismiss_button)
    Button cancelButton;

    @BindView(R.id.add_picture_button)
    Button addPictureButton;

    @BindView(R.id.product_image)
    ImageView productImage;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.drop_down)
    DropDownChooser dropDownChooser;

    @BindView(R.id.amount_input_et)
    EditText amountInputET;

    @BindView(R.id.other_comment_et)
    EditText commentET;

    @BindView(R.id.product_title)
    EditText productTitleET;

    @BindView(R.id.dummy_view)
    View dummyView;

    @BindView(R.id.size_container)
    View sizeContainer;

    @BindView(R.id.radio_container)
    View radioContainer;

    @BindView(R.id.input_container)
    View inputContainer;

    @BindView(R.id.radio_other)
    RadioButton radioOther;

    @BindView(R.id.radio_volume)
    RadioButton radioVolume;

    @BindView(R.id.radio_weight)
    RadioButton radioWeight;

    private File file;
    private int selectedMeasureTypePosition;
    private int selectedUnitType;

    private ArrayList<String> weights = new ArrayList<>();
    private ArrayList<String> volumes = new ArrayList<>();

    private ArrayList<ProductItem> orderItems = new ArrayList<>();
    private NewProductRecyclerAdapter adapter;

    private ProductItem editItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productsRecycler.setLayoutManager(new LinearLayoutManager(productsRecycler.getContext()));
        adapter = new NewProductRecyclerAdapter(orderItems, this, this);
        productsRecycler.setAdapter(adapter);

        setUpInitialScreen();
    }

    private void setUpInitialScreen() {

        weights.add(getString(R.string.weight_kg_long));
        weights.add(getString(R.string.weight_gram_long));

        volumes.add(getString(R.string.volume_litre_long));
        volumes.add(getString(R.string.volume_millilitre_long));


        newProductButtonsContainer.setVisibility(View.GONE);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    newProductButtonsContainer.setVisibility(View.GONE);
                    newProductButton.setVisibility(View.VISIBLE);
                    ViewUtils.closeKeyboard(dummyView);
                } else {
                    newProductButtonsContainer.setVisibility(View.VISIBLE);
                    newProductButton.setVisibility(View.GONE);
                }
            }
        });

        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        setUpViewClickListeners();

        radioOther.setChecked(true);

    }

    private void clearData() {
        productTitleET.setText("");
        commentET.setText("");
        amountInputET.setText("");
        selectedMeasureTypePosition = 0;
        file = null;
        setPicture();

        radioOther.setChecked(true);
    }

    private void setUpViewClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.closeKeyboard(dummyView);

                if (TextUtils.isEmpty(productTitleET.getText().toString().replace(" ", ""))) {
                    AlertManager.showError(NewOrderActivity.this, getString(R.string.error_text_fill_all_input));
                    ViewUtils.shakeView(productTitleET);
                    return;
                }
                switch (selectedUnitType) {
                    case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                    case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                        if (TextUtils.isEmpty(amountInputET.getText().toString().replace(" ", ""))) {
                            AlertManager.showError(NewOrderActivity.this, getString(R.string.error_text_fill_all_input));
                            ViewUtils.shakeView(amountInputET);
                            return;
                        }
                        break;
                    case ModelConsts.PRODUCT_UNIT_TYPE_OTHER:
                        if (TextUtils.isEmpty(commentET.getText().toString().replace(" ", ""))) {
                            AlertManager.showError(NewOrderActivity.this, getString(R.string.error_text_fill_all_input));
                            ViewUtils.shakeView(commentET);
                            return;
                        }
                        break;
                }
                createAndAddItem();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                editItem = null;
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.closeKeyboard(dummyView);
                addProductImage();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductImage();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_weight:
                        selectedUnitType = ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT;
                        break;
                    case R.id.radio_volume:
                        selectedUnitType = ModelConsts.PRODUCT_UNIT_TYPE_VOLUME;
                        break;
                    case R.id.radio_other:
                        selectedUnitType = ModelConsts.PRODUCT_UNIT_TYPE_OTHER;
                        break;
                }

                typeOrUnitChanged();
                ViewUtils.closeKeyboard(dummyView);

            }
        });
    }

    @OnClick(R.id.new_product_button)
    protected void onNewProductButtonClick() {
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @OnClick(R.id.dismiss_button)
    protected void onCancelClick() {
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    @OnClick(R.id.next_step)
    protected void onNextStepClick() {
        if (orderItems.size() == 0) {
            AlertManager.showError(this, getString(R.string.new_order_error_now_items));
        }
    }

    public void addNewProduct(ProductItem productItem) {
        orderItems.add(0, productItem);
        adapter.notifyItemInserted(0);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    @Override
    public void onItemClick(int position) {
        editItem = orderItems.get(position);

        productTitleET.setText(editItem.getTitle());

        commentET.setText(editItem.getComment());

        if (editItem.getAmount() != null) {
            amountInputET.setText(String.valueOf(editItem.getAmount()));
        }

        selectedUnitType = editItem.getUnitType();

        switch (editItem.getMeasureUnitType()) {
            case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE:
            case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM:
                selectedUnitType = 0;
                break;

            case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE:
            case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM:
                selectedUnitType = 1;
                break;
        }

        file = editItem.getFile();

        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        typeOrUnitChanged();
        setPicture();
    }

    @Override
    public void onItemDelete(int position) {
        orderItems.remove(position);
        adapter.notifyItemRemoved(position);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_new_order;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    private void createAndAddItem() {
        if (editItem == null) {
            ProductItem item = new ProductItem();
            item.setTitle(productTitleET.getText().toString());
            item.setFile(file);

            switch (selectedUnitType) {
                case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                    item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT);
                    item.setUnitMeasureType(selectedMeasureTypePosition == KG_POSITION ? ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM : ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM);
                    item.setAmount(new BigDecimal(amountInputET.getText().toString()));
                    break;
                case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                    item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_VOLUME);
                    item.setUnitMeasureType(selectedMeasureTypePosition == LITRE_POSITION ? ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE : ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE);
                    item.setAmount(new BigDecimal(amountInputET.getText().toString()));
                    break;
                case ModelConsts.PRODUCT_UNIT_TYPE_OTHER:
                    item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_OTHER);
                    item.setComment(commentET.getText().toString());
                    break;
            }

            addNewProduct(item);
        } else {
            adapter.notifyDataSetChanged();
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        clearData();
    }


    private void setPicture() {
        if (file != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            productImage.setVisibility(View.VISIBLE);
            addPictureButton.setText(getString(R.string.button_text_change_picture));
            ImageLoader.loadImage(productImage, myBitmap);
        } else {
            productImage.setVisibility(View.GONE);
            addPictureButton.setText(getString(R.string.button_text_add_picture));
        }

        newProductButton.setVisibility(View.GONE);
    }


    private void addProductImage() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(getString(R.string.select_picture_gallery));
        arr.add(getString(R.string.select_picture_camera));
        if (file != null) {
            arr.add(getString(R.string.select_picture_remove));
        }
        showTextListDialog("", arr, LIST_TYPE_PICTURE, this);
    }


    @Override
    public void onItemClick(int position, int listType) {
        switch (listType) {
            case LIST_TYPE_MEASURMENT:
                selectedMeasureTypePosition = position;
                typeOrUnitChanged();
                break;
            case LIST_TYPE_PICTURE:
                if (position == 0) {
                    ImageUtils.manageGallery(this);
                } else if (position == 1) {
                    ImageUtils.manageCamera(this);
                } else {
                    file = null;
                    setPicture();
                }
                break;
        }

    }

    private void typeOrUnitChanged() {
        switch (selectedUnitType) {
            case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                setUpWeightInput();
                break;
            case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                setUpVolumeInput();
                break;
            case ModelConsts.PRODUCT_UNIT_TYPE_OTHER:
                setUpOtherInput();
                break;
        }
    }

    private void setUpWeightInput() {
        sizeContainer.setVisibility(View.VISIBLE);
        commentET.setVisibility(View.GONE);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextListDialog(getString(R.string.product_dialog_radio_weight_title),
                        weights, LIST_TYPE_MEASURMENT, NewOrderActivity.this);
            }
        });

        dropDownChooser.setText(weights.get(selectedMeasureTypePosition));
        amountInputET.setHint(R.string.product_dialog_radio_weight_title);
    }

    private void setUpVolumeInput() {
        sizeContainer.setVisibility(View.VISIBLE);
        commentET.setVisibility(View.GONE);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextListDialog(getString(R.string.product_dialog_radio_volume_title),
                        volumes, LIST_TYPE_MEASURMENT, NewOrderActivity.this);
            }
        });

        dropDownChooser.setText(volumes.get(selectedMeasureTypePosition));
        amountInputET.setHint(R.string.product_dialog_radio_volume_title);
    }

    private void setUpOtherInput() {
        sizeContainer.setVisibility(View.GONE);
        commentET.setVisibility(View.VISIBLE);
    }


    @Subscribe
    public void onPermissionsGranted(GrantedPermissionsEvent event) {
        if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
            ImageUtils.openCamera(this);
        } else if (event.getRequestCode() == AppConsts.PERMISSION_EXTERNAL_STORAGE) {
            ImageUtils.openGallery(this);
        }
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (resultCode) {
            case RESULT_OK:

                if (requestCode == AppConsts.RC_CAMERA || requestCode == AppConsts.RC_GALLERY) {
                    String path = null;
                    if (requestCode == AppConsts.RC_CAMERA) {
                        path = ImageUtils.handleCameraResult(this);
                    } else if (requestCode == AppConsts.RC_GALLERY) {
                        path = ImageUtils.handleGalleryResult(data, this);
                    }

                    if (!TextUtils.isEmpty(path)) {
                        File imgFile = new File(path);
                        if (imgFile.exists()) {
                            this.file = imgFile;
                            setPicture();
                        } else {
                            setPicture();
                        }
                    } else {
                        setPicture();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            onCancelClick();
        } else {
            super.onBackPressed();
        }

    }
}

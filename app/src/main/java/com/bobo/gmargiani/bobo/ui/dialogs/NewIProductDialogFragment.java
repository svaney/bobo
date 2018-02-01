package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.activites.NewOrderActivity;
import com.bobo.gmargiani.bobo.ui.activites.RootActivity;
import com.bobo.gmargiani.bobo.ui.adapters.BasicRecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.views.DropDownChooser;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ImageUtils;
import com.bobo.gmargiani.bobo.utils.consts.AppConsts;
import com.bobo.gmargiani.bobo.utils.consts.ModelConsts;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewIProductDialogFragment extends DialogFragment implements BasicRecyclerItemClickListener {

    private static final int LIST_TYPE_MEASURMENT = 0;
    private static final int LIST_TYPE_PICTURE = 1;


    private static final int KG_POSITION = 0;
    private static final int GRAM_POSITION = 1;

    private static final int LITRE_POSITION = 0;
    private static final int MILLILITRE_POSITION = 1;


    private ArrayList<String> weights = new ArrayList<>();
    private ArrayList<String> volumes = new ArrayList<>();


    private Button addButton;
    private Button cancelButton;
    private Button addPictureButton;

    private ImageView productImage;

    private RadioGroup radioGroup;

    private DropDownChooser dropDownChooser;

    private EditText amountInputET;
    private EditText commentET;
    private EditText productTitleET;

    private View dummyView;
    private View sizeContainer;
    private View commentContainer;

    private int selectedSizePosition = KG_POSITION;
    private int selectedSizeType = ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_add_new_item_details, null);

        builder.setView(v);

        weights.add(getString(R.string.weight_kg_long));
        weights.add(getString(R.string.weight_gram_long));

        volumes.add(getString(R.string.volume_litre_long));
        volumes.add(getString(R.string.volume_millilitre_long));

        setUpViews(v);
        setUpScreenData();

        return builder.create();

    }

    @Override
    public void onStart() {
        super.onStart();
        App.getInstance().getEventBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getInstance().getEventBus().unregister(this);
    }

    private void setUpViews(View v) {

        cancelButton = v.findViewById(R.id.dismiss_button);
        addButton = v.findViewById(R.id.add_button);
        addPictureButton = v.findViewById(R.id.add_picture_button);

        radioGroup = v.findViewById(R.id.radio_group);
        dummyView = v.findViewById(R.id.dummy_view);
        productImage = v.findViewById(R.id.product_image);

        amountInputET = v.findViewById(R.id.amount_input_et);
        dropDownChooser = v.findViewById(R.id.drop_down);
        sizeContainer = v.findViewById(R.id.size_container);
        commentContainer = v.findViewById(R.id.comment_container);
        commentET = v.findViewById(R.id.other_comment_et);
        productTitleET = v.findViewById(R.id.product_title);


        amountInputET.addTextChangedListener(textWatcher);
        commentET.addTextChangedListener(textWatcher);
        productTitleET.addTextChangedListener(textWatcher);


    }

    private void setUpScreenData() {

        AppUtils.closeKeyboard(dummyView);

        setUpViewClickListeners();

        ((RadioButton) radioGroup.findViewById(R.id.radio_weight)).setChecked(true);

        ImageLoader.loadImage(productImage, R.drawable.new_product_image_placeholder);

    }

    private void setUpWeightInput() {
        sizeContainer.setVisibility(View.VISIBLE);
        commentContainer.setVisibility(View.GONE);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RootActivity) getActivity()).showTextListDialog(getString(R.string.product_dialog_radio_weight_title),
                        weights, LIST_TYPE_MEASURMENT, NewIProductDialogFragment.this);
            }
        });

        dropDownChooser.setText(weights.get(selectedSizePosition));
        amountInputET.setHint(R.string.product_dialog_radio_weight_title);

        handleAddButton();

    }

    private void setUpVolumeInput() {
        sizeContainer.setVisibility(View.VISIBLE);
        commentContainer.setVisibility(View.GONE);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RootActivity) getActivity()).showTextListDialog(getString(R.string.product_dialog_radio_volume_title),
                        volumes, LIST_TYPE_MEASURMENT,NewIProductDialogFragment.this);
            }
        });

        dropDownChooser.setText(volumes.get(selectedSizePosition));
        amountInputET.setHint(R.string.product_dialog_radio_volume_title);

        handleAddButton();
    }

    private void setUpOtherInput() {
        sizeContainer.setVisibility(View.GONE);
        commentContainer.setVisibility(View.VISIBLE);

        handleAddButton();
    }

    private void handleAddButton() {
        switch (selectedSizeType) {
            case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
            case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                addButton.setEnabled(!TextUtils.isEmpty(productTitleET.getText().toString()) && !TextUtils.isEmpty(amountInputET.getText().toString()));
                break;
            case ModelConsts.PRODUCT_UNIT_TYPE_OTHER:
                addButton.setEnabled(!TextUtils.isEmpty(productTitleET.getText().toString()) && !TextUtils.isEmpty(commentET.getText().toString()));
                break;
        }
    }


    private void setUpViewClickListeners() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndAddItem();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                AppUtils.closeKeyboard(dummyView);
                switch (checkedId) {
                    case R.id.radio_weight:
                        selectedSizeType = ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT;
                        break;
                    case R.id.radio_volume:
                        selectedSizeType = ModelConsts.PRODUCT_UNIT_TYPE_VOLUME;
                        break;
                    case R.id.radio_other:
                        selectedSizeType = ModelConsts.PRODUCT_UNIT_TYPE_OTHER;
                        break;
                }

                typeOrUnitChanged();
            }
        });
    }

    private void addProductImage() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(getString(R.string.select_picture_gallery));
        arr.add(getString(R.string.select_picture_camera));
        ((RootActivity) getActivity()).showTextListDialog("", arr, LIST_TYPE_PICTURE, this);
    }


    @Override
    public void onItemClick(int position, int listType) {
        switch (listType){
            case LIST_TYPE_MEASURMENT:
                selectedSizePosition = position;
                typeOrUnitChanged();
                break;
            case LIST_TYPE_PICTURE:
                if (position == 0){
                    ImageUtils.manageGallery(getActivity());
                } else {
                    ImageUtils.manageCamera(getActivity());
                }
                break;
        }

    }

    private void typeOrUnitChanged() {
        switch (selectedSizeType) {
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

    private void createAndAddItem() {
        ProductItem item = new ProductItem();
        item.setTitle(productTitleET.getText().toString());

        switch (selectedSizeType) {
            case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT);
                item.setUnitMeasureType(selectedSizePosition == KG_POSITION ? ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM : ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM);
                item.setAmount(new BigDecimal(amountInputET.getText().toString()));
                break;
            case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_VOLUME);
                item.setUnitMeasureType(selectedSizePosition == LITRE_POSITION ? ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE : ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE);
                item.setAmount(new BigDecimal(amountInputET.getText().toString()));
                break;
            case ModelConsts.PRODUCT_UNIT_TYPE_OTHER:
                item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_OTHER);
                item.setComment(commentET.getText().toString());
                break;
        }

        ((NewOrderActivity) getActivity()).addNewProduct(item);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            handleAddButton();
        }
    };


    @Subscribe
    public void onPermissionsGranted(GrantedPermissionsEvent event) {
        if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
            ImageUtils.openCamera(getActivity());
        } else if (event.getRequestCode() == AppConsts.PERMISSION_EXTERNAL_STORAGE) {
            ImageUtils.openGallery(getActivity());
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == RESULT_OK) {

            String path = null;
            if (activityResultEvent.getRequestCode() == AppConsts.RC_CAMERA) {
                path = ImageUtils.handleCameraResult(getActivity());
            } else if (activityResultEvent.getRequestCode() == AppConsts.RC_GALLERY) {
                path = ImageUtils.handleGalleryResult(activityResultEvent.getData(), getActivity());
            }

            if (!TextUtils.isEmpty(path)) {
                ImageLoader.loadImage(productImage, -1);
                ImageLoader.loadImage(productImage, path);
            }
        }
    }
}

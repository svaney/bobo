package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.activites.NewOrderActivity;
import com.bobo.gmargiani.bobo.ui.activites.RootActivity;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.BasicRecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.views.AmountIncrementView;
import com.bobo.gmargiani.bobo.ui.views.DropDownChooser;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ImageUtils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;
import com.bobo.gmargiani.bobo.utils.consts.AppConsts;
import com.bobo.gmargiani.bobo.utils.consts.ModelConsts;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewProductDialogFragment extends DialogFragment implements BasicRecyclerItemClickListener {
    private static final int LIST_TYPE_MEASURMENT = 0;
    private static final int LIST_TYPE_PICTURE = 1;

    private static final int KG_POSITION = 0;
    private static final int GRAM_POSITION = 1;

    private static final int LITRE_POSITION = 0;
    private static final int MILLILITRE_POSITION = 1;

    private Button addButton, cancelButton, addPictureButton, deleteButton;
    private EditText amountInputET, commentET, productTitleET;
    private RadioButton radioVolume, radioWeight;
    private TextView commentTitle, sizeTitle;
    private View dummyView, inputContainer;
    private ImageView productImage;
    private RadioGroup radioGroup;
    private DropDownChooser dropDownChooser;
    private CheckBox sizeCheckBox;
    private AmountIncrementView amountView;

    private File file;
    private int selectedMeasureTypePosition;
    private int selectedUnitType;

    private ArrayList<String> weights = new ArrayList<>();
    private ArrayList<String> volumes = new ArrayList<>();

    private ProductItem editItem;

    private int editItemPosition;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_add_new_item_details, null);

        builder.setView(v);

        findViews(v);

        setUpInitialScreen();

        return builder.create();

    }

    public void setEditItemPosition(int position) {
        editItemPosition = position;
    }

    private void findViews(View v) {
        addButton = v.findViewById(R.id.add_button);

        cancelButton = v.findViewById(R.id.dismiss_button);

        addPictureButton = v.findViewById(R.id.add_picture_button);

        productImage = v.findViewById(R.id.product_image);

        radioGroup = v.findViewById(R.id.radio_group);

        dropDownChooser = v.findViewById(R.id.drop_down);

        amountInputET = v.findViewById(R.id.amount_input_et);

        commentET = v.findViewById(R.id.other_comment_et);

        productTitleET = v.findViewById(R.id.product_title);

        dummyView = v.findViewById(R.id.dummy_view);

        inputContainer = v.findViewById(R.id.input_container);

        radioVolume = v.findViewById(R.id.radio_volume);

        radioWeight = v.findViewById(R.id.radio_weight);

        commentTitle = v.findViewById(R.id.comment_title);

        sizeTitle = v.findViewById(R.id.size_title);

        sizeCheckBox = v.findViewById(R.id.size_check_box);

        amountView = v.findViewById(R.id.amount_view);

        deleteButton = v.findViewById(R.id.delete_button);
    }

    private void setUpInitialScreen() {

        weights.add(getString(R.string.measure_unit_weight_kg));
        weights.add(getString(R.string.measure_unit_weight_gram));

        volumes.add(getString(R.string.measure_unit_volume_litre));
        volumes.add(getString(R.string.measure_unit_volume_millilitre));

        setUpViewClickListeners();

        if (editItem != null) {
            editItem();
        }

        deleteButton.setVisibility(editItem == null ? View.GONE : View.VISIBLE);
    }


    private void setUpViewClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(productTitleET.getText().toString().replace(" ", ""))) {
                    //  AlertManager.showError((getActivity()), getString(R.string.error_text_fill_all_input));

                    productTitleET.setBackground(ViewUtils.getErrorRectangleBackground(getContext()));
                    ViewUtils.shakeView(productTitleET);
                    return;
                }
                if (sizeCheckBox.isChecked()) {
                    switch (selectedUnitType) {
                        case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                        case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                            if (TextUtils.isEmpty(amountInputET.getText().toString().replace(" ", ""))) {
                                amountInputET.setBackground(ViewUtils.getErrorRectangleBackground(getContext()));
                                ViewUtils.shakeView(amountInputET);
                                return;
                            }
                            break;
                    }
                }
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
                }

                typeOrUnitChanged();
                ViewUtils.closeKeyboard(dummyView);
            }
        });

        sizeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                inputContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                radioWeight.setChecked(true);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewOrderActivity) getActivity()).onItemDelete(editItemPosition);
                dismiss();
            }
        });
    }

    @OnClick(R.id.dismiss_button)
    protected void onCancelClick() {
        dismiss();
    }


    public void setEditItem(ProductItem item) {
        editItem = item;
    }

    public void editItem() {
        productTitleET.setText(editItem.getTitle());

        commentET.setText(editItem.getComment());

        amountView.setAmount(editItem.getAmount());

        if (editItem.getSize() != null) {
            amountInputET.setText(String.valueOf(editItem.getSize()));
        }
        switch (editItem.getMeasureUnitType()) {
            case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE:
            case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM:
                selectedMeasureTypePosition = 0;
                break;

            case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE:
            case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM:
                selectedMeasureTypePosition = 1;
                break;
        }

        sizeCheckBox.setChecked(editItem.hasSize());

        if (editItem.hasSize()) {
            switch (editItem.getUnitType()) {
                case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                    radioWeight.setChecked(true);
                    break;
                case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                    radioVolume.setChecked(true);
                    break;
            }
        }

        file = editItem.getFile();
        setPicture();
    }

    private void createAndAddItem() {
        if (editItem == null) {
            ProductItem item = new ProductItem();
            setDataToItem(item);
            ((NewOrderActivity) getActivity()).addNewProduct(item);
        } else {
            setDataToItem(editItem);
            ((NewOrderActivity) getActivity()).notifyDataSetChanged();
        }

        dismiss();
    }

    private void setDataToItem(ProductItem item) {
        item.setTitle(productTitleET.getText().toString());
        item.setFile(file);
        item.setHasSize(sizeCheckBox.isChecked());
        item.setComment(commentET.getText().toString());
        item.setAmount(amountView.getAmount());

        if (item.hasSize()) {
            switch (selectedUnitType) {
                case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                    item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT);
                    item.setUnitMeasureType(selectedMeasureTypePosition == KG_POSITION ? ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM : ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM);
                    item.setSize(new BigDecimal(amountInputET.getText().toString()));
                    break;
                case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                    item.setUnitType(ModelConsts.PRODUCT_UNIT_TYPE_VOLUME);
                    item.setUnitMeasureType(selectedMeasureTypePosition == LITRE_POSITION ? ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE : ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE);
                    item.setSize(new BigDecimal(amountInputET.getText().toString()));
                    break;
            }
        } else {
            item.setSize(null);
        }

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
    }


    private void addProductImage() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(getString(R.string.select_picture_gallery));
        arr.add(getString(R.string.select_picture_camera));
        if (file != null) {
            arr.add(getString(R.string.select_picture_remove));
        }
        ((RootActivity) getActivity()).showTextListDialog("", arr, LIST_TYPE_PICTURE, this);
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
                    ImageUtils.manageGallery(getActivity());
                } else if (position == 1) {
                    ImageUtils.manageCamera(getActivity());
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

        }
    }

    private void setUpWeightInput() {

        sizeTitle.setText(R.string.product_dialog_radio_weight_title);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RootActivity) getActivity()).showTextListDialog(getString(R.string.product_dialog_radio_weight_title),
                        weights, LIST_TYPE_MEASURMENT, NewProductDialogFragment.this);
            }
        });

        dropDownChooser.setText(weights.get(selectedMeasureTypePosition));

    }

    private void setUpVolumeInput() {

        sizeTitle.setText(R.string.product_dialog_radio_volume_title);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RootActivity) getActivity()).showTextListDialog(getString(R.string.product_dialog_radio_volume_title),
                        volumes, LIST_TYPE_MEASURMENT, NewProductDialogFragment.this);
            }
        });

        dropDownChooser.setText(volumes.get(selectedMeasureTypePosition));

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

    @Subscribe
    public void onPermissionsGranted(GrantedPermissionsEvent event) {
        if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
            ImageUtils.openCamera(getActivity());
        } else if (event.getRequestCode() == AppConsts.PERMISSION_EXTERNAL_STORAGE) {
            ImageUtils.openGallery(getActivity());
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent event) {
        switch (event.getResultCode()) {
            case RESULT_OK:

                if (event.getRequestCode() == AppConsts.RC_CAMERA || event.getRequestCode() == AppConsts.RC_GALLERY) {
                    String path = null;
                    if (event.getRequestCode() == AppConsts.RC_CAMERA) {
                        path = ImageUtils.handleCameraResult(getActivity());
                    } else if (event.getRequestCode() == AppConsts.RC_GALLERY) {
                        path = ImageUtils.handleGalleryResult(event.getData(), getActivity());
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
}

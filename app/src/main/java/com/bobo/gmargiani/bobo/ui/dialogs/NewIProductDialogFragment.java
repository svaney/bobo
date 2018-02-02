package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.activites.NewOrderActivity;
import com.bobo.gmargiani.bobo.ui.activites.RootActivity;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.BasicRecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.views.DropDownChooser;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ImageUtils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;
import com.bobo.gmargiani.bobo.utils.consts.AppConsts;
import com.bobo.gmargiani.bobo.utils.consts.ModelConsts;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
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

    private View radioContainer;
    private View inputContainer;
    private View pictureContainer;

    private File file;
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
        setUpInitialScreen();

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

        radioContainer = v.findViewById(R.id.radio_container);
        inputContainer = v.findViewById(R.id.input_container);
        pictureContainer = v.findViewById(R.id.picture_container);

    }

    private void setUpInitialScreen() {

        setUpViewClickListeners();

        setPicture();

        handleViews();

        ViewUtils.focusEditText(productTitleET);

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
    }

    private void setUpVolumeInput() {
        sizeContainer.setVisibility(View.VISIBLE);
        commentContainer.setVisibility(View.GONE);

        dropDownChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RootActivity) getActivity()).showTextListDialog(getString(R.string.product_dialog_radio_volume_title),
                        volumes, LIST_TYPE_MEASURMENT, NewIProductDialogFragment.this);
            }
        });

        dropDownChooser.setText(volumes.get(selectedSizePosition));
        amountInputET.setHint(R.string.product_dialog_radio_volume_title);
    }

    private void setUpOtherInput() {
        sizeContainer.setVisibility(View.GONE);
        commentContainer.setVisibility(View.VISIBLE);
    }

    private void handleAddButton() {
        addButton.setEnabled(pictureContainer.getVisibility() == View.VISIBLE);
    }


    private void setUpViewClickListeners() {

        productTitleET.addTextChangedListener(titleTextWatcher);
        amountInputET.addTextChangedListener(amountTextWatcher);
        commentET.addTextChangedListener(amountTextWatcher);

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
                clearTextViews();
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
                if (group.getCheckedRadioButtonId() != -1) {
                    handleViews();
                    if (commentContainer.getVisibility() != View.VISIBLE) {
                        ViewUtils.focusEditText(amountInputET);
                    } else {
                        ViewUtils.focusEditText(commentET);
                    }
                }

            }
        });
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
                selectedSizePosition = position;
                typeOrUnitChanged();
                break;
            case LIST_TYPE_PICTURE:
                if (position == 0) {
                    ImageUtils.manageGallery(getActivity());
                } else if (position == 1) {
                    ImageUtils.manageCamera(getActivity());
                } else {
                    setPicture();
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
        item.setFile(file);

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
        dismiss();
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
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == RESULT_OK) {

            String path = null;
            if (activityResultEvent.getRequestCode() == AppConsts.RC_CAMERA) {
                path = ImageUtils.handleCameraResult(getActivity());
            } else if (activityResultEvent.getRequestCode() == AppConsts.RC_GALLERY) {
                path = ImageUtils.handleGalleryResult(activityResultEvent.getData(), getActivity());
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
    }

    private void setPicture() {
        if (file != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            productImage.setVisibility(View.VISIBLE);
            ImageLoader.loadImage(productImage, myBitmap);
        } else {
            productImage.setVisibility(View.GONE);
        }
    }

    private TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            handleViews();
        }
    };


    private TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            handleViews();
        }
    };

    private void handleViews() {
        if (productTitleET.getText().length() > 0) {
            radioContainer.setVisibility(View.VISIBLE);
        } else {
            radioContainer.setVisibility(View.GONE);
            inputContainer.setVisibility(View.GONE);
            pictureContainer.setVisibility(View.GONE);
            radioGroup.clearCheck();
            file = null;
            clearTextViews();
            handleAddButton();
            return;
        }

        if (radioGroup.getCheckedRadioButtonId() != -1) {
            inputContainer.setVisibility(View.VISIBLE);
        } else {
            inputContainer.setVisibility(View.GONE);
            pictureContainer.setVisibility(View.GONE);
            file = null;
            clearTextViews();
            handleAddButton();
            return;
        }

        if (commentET.getText().length() > 0 || amountInputET.getText().length() > 0) {
            pictureContainer.setVisibility(View.VISIBLE);
            file = null;
        } else {
            pictureContainer.setVisibility(View.GONE);
        }

        setPicture();

        handleAddButton();
    }

    private void clearTextViews() {
        amountInputET.removeTextChangedListener(amountTextWatcher);
        commentET.removeTextChangedListener(amountTextWatcher);
        amountInputET.setText("");
        commentET.setText("");
        amountInputET.addTextChangedListener(amountTextWatcher);
        commentET.addTextChangedListener(amountTextWatcher);
    }
}

package com.bobo.gmargiani.bobo.utils;

import android.text.TextUtils;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.model.datamodels.ProductItem;
import com.bobo.gmargiani.bobo.utils.consts.ModelConsts;

/**
 * Created by gmarg on 2/1/2018.
 */

public class ModelUtils {

    public static String getNewProductDescription(ProductItem item) {
        String desc = "";

        if (item != null) {
            if (item.hasSize()) {
                switch (item.getUnitType()) {
                    case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                        desc = getFormattedWeightText(item) + "; ";
                        break;
                    case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                        desc = getFormattedVolumeText(item) + "; ";
                        break;
                }
            }
            if (!TextUtils.isEmpty(item.getComment())) {
                desc += item.getComment();
            }
        }

        return desc;
    }

    public static String getFormattedWeightText(ProductItem item) {
        String ans = "";

        if (item != null && item.hasSize() && item.getUnitType() == ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT) {
            ans = item.getSize() + " ";
            switch (item.getMeasureUnitType()) {
                case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM:
                    ans += App.getInstance().getString(R.string.measure_unit_weight_kg);
                    break;
                case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM:
                    ans += App.getInstance().getString(R.string.measure_unit_weight_gram);
                    break;
            }
        }
        return ans;
    }

    public static String getFormattedVolumeText(ProductItem item) {
        String ans = "";

        if (item != null && item.hasSize() && item.getUnitType() == ModelConsts.PRODUCT_UNIT_TYPE_VOLUME) {
            ans = item.getSize() + " ";
            switch (item.getMeasureUnitType()) {
                case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE:
                    ans += App.getInstance().getString(R.string.measure_unit_volume_litre);
                    break;
                case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE:
                    ans += App.getInstance().getString(R.string.measure_unit_volume_millilitre);
                    break;
            }
        }
        return ans;
    }
}

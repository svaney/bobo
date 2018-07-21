package com.bobo.gmargiani.bobo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.model.UserInfo;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class RootFragment extends Fragment {
    protected UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userInfo = App.getInstance().getUserInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.userInfo = App.getInstance().getUserInfo();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

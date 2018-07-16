package com.seanz.library.widget.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.seanz.library.R;


/**
 * Created by SeanZ on 2017/9/21.
 */

public abstract class BaseRightDialog extends DialogFragment {

    private static final String TAG = "base_Right_dialog";

    private static final float DEFAULT_DIM = 0.2f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.RightDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        if (getWidth() > 0) {
            params.width = getWidth();
        } else {
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = Gravity.RIGHT;

        window.setAttributes(params);
    }


    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void bindView(View v);

    public int getWidth() {
        return -1;
    }

    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public boolean getCancelOutside() {
        return true;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getFragmentTag());
    }
}

package com.seanz.library.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seanz.library.R;
import com.seanz.library.configs.IConstants;
import com.seanz.library.utils.ViewUtils;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
//import rx.Subscriber;


public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public ContentPage contentPage;
    public ProgressDialog pdLoading;
    private ArrayList<Disposable> subscribers;
    private TextView mResetButton;
    private String contentPageType;

    protected void onClickFailureResetButton(View view) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * 初始化pdLoading
         */
        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdLoading.setMessage("请稍后");
        pdLoading.setCanceledOnTouchOutside(false);
        pdLoading.setCancelable(true);
        /**
         * 创建Subscriber容器
         */
        subscribers  = new ArrayList<>();
        if (contentPage == null) {
            contentPage = new ContentPage(getActivity()) {
                @Override
                protected Object loadData() {
                    contentPageType = (String) requestData();
                    return contentPageType;
                }
                @Override
                protected View createSuccessView() {
                    return getSuccessView();
                }
            };
            if (contentPageType == IConstants.STATE_FAILED) {
                mResetButton = (TextView) contentPage.findViewById(R.id.reset_button);
                mResetButton.setOnClickListener(BaseFragment.this);
            }
        } else {
            ViewUtils.removeSelfFromParent(contentPage);
        }
        return contentPage;
    }

    /**
     * 返回据的fragment填充的具体View
     */
    protected abstract View getSuccessView();

    /**
     * 返回请求服务器的数据
     */
    protected abstract Object requestData();

    public void refreshPage(Object o) {
        contentPage.refreshPage(o);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(Disposable subscriber:subscribers){
            if(!subscriber.isDisposed()){
                subscriber.dispose();
            }
        }
    }

    public Disposable addSubscriber(Disposable subscriber) {
        subscribers.add(subscriber);
        return subscriber;
    }

    @Override
    public final void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivTitlebarLeft) {

        } else if (i == R.id.reset_button) {
            onClickFailureResetButton(v);
            //如果使用黄油刀，请注释掉这里
        } else {

        }
    }
}




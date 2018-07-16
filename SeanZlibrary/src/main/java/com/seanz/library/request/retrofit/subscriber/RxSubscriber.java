package com.seanz.library.request.retrofit.subscriber;

import android.widget.Toast;

import com.seanz.library.BaseApplication;
import com.seanz.library.helper.DialogHelper;
import com.seanz.library.request.exception.ApiException;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author
 * @version 1.0
 * @date 2018/3/14
 */

public class RxSubscriber<T> extends ErrorSubscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
        DialogHelper.showProgressDlg(BaseApplication.getContext(), "正在加载数据");
    }

    @Override
    protected void onError(ApiException ex) {
        DialogHelper.stopProgressDlg();
        Toast.makeText(BaseApplication.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onComplete() {
        DialogHelper.stopProgressDlg();
    }
}


abstract class ErrorSubscriber<T> extends DisposableSubscriber<T> {
    @Override
    public void onError(Throwable e) {
        if(e instanceof ApiException){
            onError((ApiException)e);
        }else{
            onError(new ApiException(e,123));
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);
}

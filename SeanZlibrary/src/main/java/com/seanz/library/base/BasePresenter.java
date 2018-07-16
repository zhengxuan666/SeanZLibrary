package com.seanz.library.base;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by WuXiaolong on 2015/9/23.
 * github:https://github.com/WuXiaolong/
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
public class BasePresenter<V> {

    protected String tag;
    public V mvpView;
    private CompositeDisposable compositeDisposable;

    public void attachView(String tag, V mvpView) {
        this.tag = tag;
        this.mvpView = mvpView;
    }

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void detachView() {
        if (!TextUtils.isEmpty(tag)) {
            OkGo.getInstance().cancelTag(tag);
        }
        this.mvpView = null;
        dispose();
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

}

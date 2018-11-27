
package com.seanz.library.mvp;

import android.support.annotation.UiThread;

import com.seanz.library.mvp.root.IMvpPresenter;
import com.seanz.library.mvp.root.IMvpView;

import java.lang.ref.WeakReference;

/**
 * Presenter基础实现
 *
 * @param <V>
 */
public class MvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {

    /*View弱引用*/
    private WeakReference<V> viewRef;

    /**
     * 获取view
     *
     * @return
     */
    @UiThread
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * 判断View是否已经添加
     *
     * @return
     */
    @UiThread
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * 绑定View
     *
     * @param view
     */
    @UiThread
    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<V>(view);
    }

    /**
     * 移除View
     */
    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    @Override
    public void destroy() {
    }

}

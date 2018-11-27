package com.seanz.library.mvp.delegate;


import com.seanz.library.mvp.root.IMvpPresenter;
import com.seanz.library.mvp.root.IMvpView;

/**
 * V/P 媒介
 *
 * @param <V>
 * @param <P>
 */
public interface MvpDelegateCallback<V extends IMvpView, P extends IMvpPresenter<V>> {

    /**
     * Gets the presenter.
     */
    P[] getPresenter();

    /**
     * Gets the MvpView for the presenter
     *
     * @return The view associated with the presenter
     */
    V[] getMvpView();

}


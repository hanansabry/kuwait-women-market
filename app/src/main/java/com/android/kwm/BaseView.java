package com.android.kwm;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}

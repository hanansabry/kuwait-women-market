package com.android.kwm.shopping.itemdetails;

import com.android.kwm.BasePresenter;
import com.android.kwm.BaseView;
import com.android.kwm.data.advertisers.AdvertiserRepository;

import java.util.ArrayList;

public interface ModelItemDetailsContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        int getItemImagesSize();

        void onBindItemImageAtPosition(ItemImageRowView holder, int position);

        void bindItemImagesUris(ArrayList<String> uris);

        void getAdvertiserDetails(String id, AdvertiserRepository.RetrieveAdvertiserCallback callback);
    }
}

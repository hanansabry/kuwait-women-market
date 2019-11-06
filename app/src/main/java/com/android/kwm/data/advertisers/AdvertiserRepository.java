package com.android.kwm.data.advertisers;

import com.android.kwm.model.Advertiser;

public interface AdvertiserRepository {

    interface RetrieveAdvertiserCallback {
        void onAdvertiserRetrieved(Advertiser advertiser);

        void onAdvertiserRetrievedFailed(String errmsg);
    }

    void retrieveAdvertiserById(String id, RetrieveAdvertiserCallback callback);
}

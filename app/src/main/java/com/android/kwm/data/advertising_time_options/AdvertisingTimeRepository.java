package com.android.kwm.data.advertising_time_options;

import com.android.kwm.model.AdvertisingTimeOption;
import com.android.kwm.model.Category;

import java.util.ArrayList;

public interface AdvertisingTimeRepository {

    interface AdvertisingTimeCallback {
        void onAdvertisingTimeRetrieved(ArrayList<AdvertisingTimeOption> options);

        void onAdvertisingTimesRetrievedFailed(String errmsg);
    }

    void retrieveAdvertisingTimeOptions(AdvertisingTimeCallback callback);
}

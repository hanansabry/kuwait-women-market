package com.android.kwm.data.advertising_time_options;

import com.android.kwm.model.AdvertisingTimeOption;
import com.android.kwm.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class AdvertisingTimeRepositoryImp implements AdvertisingTimeRepository {

    public static final String ADVERTISING_TIME_NODE = "advertising-time-options";
    private final DatabaseReference mDatabase;

    public AdvertisingTimeRepositoryImp() {
        mDatabase = FirebaseDatabase.getInstance().getReference(ADVERTISING_TIME_NODE);
    }

    @Override
    public void retrieveAdvertisingTimeOptions(final AdvertisingTimeCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<AdvertisingTimeOption> options = new ArrayList<>();

                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot categorySnapshot : dataSnapshots) {
                    String id = categorySnapshot.getKey();
                    AdvertisingTimeOption option = categorySnapshot.getValue(AdvertisingTimeOption.class);
                    options.add(option);
                }

                callback.onAdvertisingTimeRetrieved(options);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onAdvertisingTimesRetrievedFailed(databaseError.getMessage());
            }
        });
    }
}

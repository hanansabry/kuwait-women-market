package com.android.kwm.data.advertisers;

import com.android.kwm.model.Advertiser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class AdvertiserRepositoryImpl implements AdvertiserRepository {

    private FirebaseDatabase mDatabase;
    private static final String ADVERTISERS_NODE = "advertisers";

    public AdvertiserRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void retrieveAdvertiserById(String id, final RetrieveAdvertiserCallback callback) {
        mDatabase.getReference(ADVERTISERS_NODE).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Advertiser advertiser = dataSnapshot.getValue(Advertiser.class);
                callback.onAdvertiserRetrieved(advertiser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onAdvertiserRetrievedFailed(databaseError.getMessage());
            }
        });
    }
}

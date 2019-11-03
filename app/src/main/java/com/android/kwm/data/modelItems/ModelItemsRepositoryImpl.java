package com.android.kwm.data.modelItems;

import android.net.Uri;
import android.util.Log;

import com.android.kwm.Injection;
import com.android.kwm.data.storage.ImagesStorage;
import com.android.kwm.model.ModelItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;

public class ModelItemsRepositoryImpl implements ModelItemsRepository {

    private static final String MODELS_NODE = "models";
    private static final String ADVERTISERS_NODE = "advertisers";
    private final FirebaseDatabase mDatabase;
    private SimpleDateFormat df;
    private String advertiserId;
    private final ImagesStorage mImagesStorage;

    public ModelItemsRepositoryImpl(ImagesStorage imagesStorage) {
        mImagesStorage = imagesStorage;
        mDatabase = FirebaseDatabase.getInstance();
        df = Injection.getDateFormatter();
        advertiserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void addNewModelItem(final ModelItem modelItem, final ModelItemsInsertionCallback callback) {
        final DatabaseReference modelsDbRef = mDatabase.getReference(MODELS_NODE);
        modelItem.setAdvertiserId(advertiserId);
        //set start and end advertise time
        modelItem.setAdvertiseStartDate(getAdvertiseStartDate());
        modelItem.setAdvertiseEndDate(getAdvertiseEndDate(modelItem.getAdvertisingTimeType(), modelItem.getAdvertisingTime()));

        final String modelId = modelsDbRef.push().getKey();

        //upload model images to firebase storage
        final ArrayList<String> firebaseStorageImages = new ArrayList<>();
        for (String uri : modelItem.getModelImages()) {
            mImagesStorage.uploadImage(Uri.parse(uri), advertiserId, modelItem.getName(), new ImagesStorage.UploadImageCallback() {
                @Override
                public void onSuccessfullyImageUploaded(String imgUri) {
                    firebaseStorageImages.add(imgUri);
                    if (firebaseStorageImages.size() == modelItem.getModelImages().size()) {
                        modelItem.setModelImages(firebaseStorageImages);
                        modelsDbRef.child(modelId).setValue(modelItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    callback.onSuccessfullyAddingModelItem();
                                    addModelToAdvertiser(modelId);
                                } else {
                                    callback.onAddingModelItemFailed(task.getException().getMessage());
                                }
                            }
                        });
                    }
                }

                @Override
                public void onImageUploadedFailed(String errmsg) {
                    callback.onAddingModelItemFailed(errmsg);
                }
            });
        }
    }

    private void addModelToAdvertiser(String modelId) {
        DatabaseReference advertisersDbRef = mDatabase.getReference(ADVERTISERS_NODE);
        HashMap<String, Object> modelIdValue = new HashMap<>();
        modelIdValue.put(modelId, true);
        advertisersDbRef.child(advertiserId).child("models").updateChildren(modelIdValue);
    }

    private String getAdvertiseStartDate() {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("Date", df.format(currentTime));
        return df.format(currentTime);
    }

    private String getAdvertiseEndDate(ModelItem.AdvertisingTimeType type, int advertisingTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(getAdvertiseStartDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (type == ModelItem.AdvertisingTimeType.Month) {
            calendar.add(Calendar.MONTH, advertisingTime);
        } else {
            calendar.add(Calendar.DATE, advertisingTime);
        }
        Log.d("Date", df.format(calendar.getTime()));
        return df.format(calendar.getTime());
    }

    @Override
    public void retrieveModelItems(final RetrieveModelItemsCallback callback) {
        DatabaseReference advertiserRef = mDatabase.getReference(ADVERTISERS_NODE).child(advertiserId);

        //get ids of advertiser's models
        advertiserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("models").exists()) {
                    ArrayList<String> modelIds = new ArrayList<>();
                    DataSnapshot modelsSnapshot = dataSnapshot.child("models");
                    for (DataSnapshot modelSnapshot : modelsSnapshot.getChildren()) {
                        modelIds.add(modelSnapshot.getKey());
                    }

                    //get models items
                    getModelItems(modelIds, callback);
                } else {
                    callback.onModelItemsRetrieved(new ArrayList<ModelItem>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onModelItemsRetrievedFailed(databaseError.getMessage());
            }
        });
    }

    private void getModelItems(final ArrayList<String> modelIds, final RetrieveModelItemsCallback callback) {
        DatabaseReference modelsRef = mDatabase.getReference(MODELS_NODE);
        modelsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ModelItem> modelItems = new ArrayList<>();
                for (DataSnapshot modelSnapshot : dataSnapshot.getChildren()) {
                    if (modelIds.contains(modelSnapshot.getKey())) {
                        ModelItem modelItem = modelSnapshot.getValue(ModelItem.class);
                        Date currentDay = Calendar.getInstance().getTime();
                        try {
                            Date endDate = Injection.getDateFormatter().parse(modelItem.getAdvertiseEndDate());
                            modelItem.setActive(currentDay.before(endDate));
                        } catch (ParseException e) {
                            modelItem.setActive(false);
                        }
                        modelItems.add(modelItem);
                    }
                }

                callback.onModelItemsRetrieved(modelItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onModelItemsRetrievedFailed(databaseError.getMessage());
            }
        });
    }
}

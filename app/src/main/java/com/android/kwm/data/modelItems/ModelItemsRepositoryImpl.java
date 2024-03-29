package com.android.kwm.data.modelItems;

import android.net.Uri;
import android.util.Log;

import com.android.kwm.Injection;
import com.android.kwm.data.advertisers.AdvertiserRepository;
import com.android.kwm.data.storage.ImagesStorage;
import com.android.kwm.model.Advertiser;
import com.android.kwm.model.Category;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class ModelItemsRepositoryImpl implements ModelItemsRepository {

    private static final String MODELS_NODE = "models";
    private static final String ADVERTISERS_NODE = "advertisers";
    private static final String CATEGORIES_NODE = "categories";
    private final FirebaseDatabase mDatabase;
    private SimpleDateFormat df;
    private String advertiserId;
    private final ImagesStorage mImagesStorage;
    private final AdvertiserRepository mAdvertiserRepository;

    public ModelItemsRepositoryImpl(ImagesStorage imagesStorage, AdvertiserRepository advertiserRepository) {
        mImagesStorage = imagesStorage;
        mAdvertiserRepository = advertiserRepository;
        mDatabase = FirebaseDatabase.getInstance();
        df = Injection.getDateFormatter();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            advertiserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }

    @Override
    public void addNewModelItem(final ModelItem modelItem, final ModelItemsInsertionCallback callback) {
        final DatabaseReference modelsDbRef = mDatabase.getReference(MODELS_NODE);
        //set start and end advertise time
        modelItem.setAdvertiseStartDate(getAdvertiseStartDate());
        modelItem.setAdvertiseEndDate(getAdvertiseEndDate(modelItem.getAdvertisingTimeType(), modelItem.getAdvertisingTime()));

        final String modelId = modelsDbRef.push().getKey();


        //firstly get advertiser object then upload images to firebase storage then add model to model nodes
        mAdvertiserRepository.retrieveAdvertiserById(advertiserId, new AdvertiserRepository.RetrieveAdvertiserCallback() {
            @Override
            public void onAdvertiserRetrieved(Advertiser advertiser) {
                modelItem.setAdvertiser(advertiser);
                //upload model images to firebase storage
                final ArrayList<String> firebaseStorageImages = new ArrayList<>();
                for (String uri : modelItem.getModelImages()) {
                    mImagesStorage.uploadImage(Uri.parse(uri), modelItem.getCategory().getId(), modelId, new ImagesStorage.UploadImageCallback() {
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
                                            addModelToAdvertiserAndCategory(modelId, modelItem.getCategory().getId());
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

            @Override
            public void onAdvertiserRetrievedFailed(String errmsg) {
                callback.onAddingModelItemFailed(errmsg);
            }
        });
    }

    private void addModelToAdvertiserAndCategory(String modelId, String categoryId) {
        DatabaseReference advertisersDbRef = mDatabase.getReference(ADVERTISERS_NODE);
        DatabaseReference categoriesDbRef = mDatabase.getReference(CATEGORIES_NODE);
        HashMap<String, Object> modelIdValue = new HashMap<>();
        modelIdValue.put(modelId, true);
        advertisersDbRef.child(advertiserId).child("models").updateChildren(modelIdValue);
        categoriesDbRef.child(categoryId).child("models").updateChildren(modelIdValue);
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
        if (type == ModelItem.AdvertisingTimeType.Week) {
            calendar.add(Calendar.WEEK_OF_MONTH, advertisingTime);
        } else {
            calendar.add(Calendar.DATE, advertisingTime);
        }
        Log.d("Date", df.format(calendar.getTime()));
        return df.format(calendar.getTime());
    }

    @Override
    public void retrieveModelItemsByAdvertiser(final RetrieveModelItemsCallback callback) {
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
                    getModelItems(false, modelIds, callback);
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

    private void getModelItems(final boolean getActiveOnly, final ArrayList<String> modelIds, final RetrieveModelItemsCallback callback) {
        DatabaseReference modelsRef = mDatabase.getReference(MODELS_NODE);
        modelsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ModelItem> modelItems = new ArrayList<>();
                for (DataSnapshot modelSnapshot : dataSnapshot.getChildren()) {
                    if (modelIds.contains(modelSnapshot.getKey())) {
                        ModelItem modelItem = modelSnapshot.getValue(ModelItem.class);
                        modelItem.setId(modelSnapshot.getKey());
                        Date currentDay = Calendar.getInstance().getTime();
                        try {
                            Date endDate = Injection.getDateFormatter().parse(modelItem.getAdvertiseEndDate());
                            boolean isSameDay = isCurrentDay(currentDay, endDate);
                            modelItem.setActive(currentDay.before(endDate) || isSameDay);
                        } catch (ParseException e) {
                            modelItem.setActive(false);
                        }
                        if (getActiveOnly) {
                            if (modelItem.isActive()) {
                                modelItems.add(modelItem);
                            }
                        } else {
                            modelItems.add(modelItem);
                        }
                    }
                }

                callback.onModelItemsRetrieved(sortBydate(modelItems));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onModelItemsRetrievedFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void retrieveModelItemsByCategory(String categoryId, final ModelItemsRepository.RetrieveModelItemsCallback callback) {
        DatabaseReference advertiserRef = mDatabase.getReference(CATEGORIES_NODE).child(categoryId);

        //get ids of categories' models
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
                    getModelItems(true, modelIds, callback);
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

    private boolean isCurrentDay(Date currentDay, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentDay);
        cal2.setTime(endDate);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    private ArrayList<ModelItem> sortBydate(ArrayList<ModelItem> modelItems) {
        Collections.sort(modelItems, new Comparator<ModelItem>() {
            @Override
            public int compare(ModelItem o1, ModelItem o2) {
                return o2.getAdvertiseStartDate().compareTo(o1.getAdvertiseStartDate());
            }
        });
        return modelItems;
    }

    @Override
    public void retrieveFilteredModelItemsByPrice(Category category, final double minValue, final double maxValue, final RetrieveModelItemsCallback callback) {
        retrieveModelItemsByCategory(category.getId(), new RetrieveModelItemsCallback() {
            @Override
            public void onModelItemsRetrieved(ArrayList<ModelItem> modelItems) {
                ArrayList<ModelItem> filteredModelItems = new ArrayList<>();
                for (ModelItem modelItem : modelItems) {
                    if (modelItem.getSellingPrice() >= minValue
                            && (modelItem.getSellingPrice() <= maxValue || maxValue == 0)) {
                        filteredModelItems.add(modelItem);
                    }
                }

                callback.onModelItemsRetrieved(filteredModelItems);
            }

            @Override
            public void onModelItemsRetrievedFailed(String errmsg) {
                callback.onModelItemsRetrievedFailed(errmsg);
            }
        });
    }

    @Override
    public void updateModelNumberOfViews(String modelId, int currentViews) {
        DatabaseReference modelRef = mDatabase.getReference(MODELS_NODE).child(modelId);
        HashMap<String, Object> viewsValue = new HashMap<>();
        viewsValue.put("numberOfViews", currentViews + 1);
        modelRef.updateChildren(viewsValue);
    }
}

package com.android.kwm.data.categories;

import com.android.kwm.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class CategoriesRepositoryImpl implements CategoriesRepository {

    public static final String CATEGORIES_NODE = "categories";
    private final DatabaseReference mDatabase;

    public CategoriesRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(CATEGORIES_NODE);
    }

    @Override
    public void retrieveCategories(final CategoriesCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Category> categories = new ArrayList<>();

                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot categorySnapshot : dataSnapshots) {
                    String id = categorySnapshot.getKey();
                    Category category = categorySnapshot.getValue(Category.class);
                    category.setId(id);
                    categories.add(category);
                }

                callback.onCategoriesRetrieved(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCategoriesRetrievedFailed(databaseError.getMessage());
            }
        });
    }
}

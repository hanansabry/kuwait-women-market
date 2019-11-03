package com.android.kwm.data.users;

import com.android.kwm.model.Advertiser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class FirebaseUsersRepositoryImpl implements UsersRepository {

    public static String USERS_COLLECTION = "advertisers";
    private final DatabaseReference mDatabase;

    public FirebaseUsersRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(USERS_COLLECTION);
    }

    @Override
    public void insertNewUser(Advertiser user, final UserInsertionCallback callback) {
        String userId = user.getId();
        mDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onUserInsertedSuccessfully();
                } else {
                    callback.onUserInsertedFailed(task.getException().getMessage());
                }
            }
        });
    }
}

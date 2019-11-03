package com.android.kwm.data.users;


import com.android.kwm.model.Advertiser;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    void insertNewUser(Advertiser user, UserInsertionCallback callback);
}

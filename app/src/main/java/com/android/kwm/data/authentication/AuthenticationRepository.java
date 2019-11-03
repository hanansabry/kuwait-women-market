package com.android.kwm.data.authentication;

import com.android.kwm.model.Advertiser;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationRepository {

    interface RegistrationCallback {
        void onSuccessfulRegistration(FirebaseUser firebaseUser);

        void onFailedRegistration(String errmsg);
    }

    interface LoginCallback {
        void onSuccessLogin(FirebaseUser firebaseUser);

        void onFailedLogin(String errmsg);
    }

    void registerNewUser(Advertiser user, RegistrationCallback callback);

    void login(String email, String password, LoginCallback callback);

}

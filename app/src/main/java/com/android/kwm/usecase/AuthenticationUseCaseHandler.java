package com.android.kwm.usecase;

import com.android.kwm.data.authentication.AuthenticationRepository;
import com.android.kwm.model.Advertiser;

public class AuthenticationUseCaseHandler {

    private AuthenticationRepository mRepository;

    public AuthenticationUseCaseHandler(AuthenticationRepository repository) {
        mRepository = repository;
    }

    public void registerNewUser(Advertiser user, AuthenticationRepository.RegistrationCallback callback){
        mRepository.registerNewUser(user, callback);
    }

    public void login(String email, String password, AuthenticationRepository.LoginCallback callback) {
        mRepository.login(email, password, callback);
    }

}

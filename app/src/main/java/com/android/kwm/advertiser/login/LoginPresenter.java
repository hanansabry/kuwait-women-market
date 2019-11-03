package com.android.kwm.advertiser.login;

import com.android.kwm.data.authentication.AuthenticationRepository;
import com.android.kwm.usecase.AuthenticationUseCaseHandler;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mLoginView;
    private final AuthenticationUseCaseHandler mUseCasHandler;

    public LoginPresenter(LoginContract.View loginView, AuthenticationUseCaseHandler useCasHandler) {
        mLoginView = loginView;
        mUseCasHandler = useCasHandler;

        mLoginView.setPresenter(this);
    }

    @Override
    public void login(String email, String password, AuthenticationRepository.LoginCallback callback) {
        mUseCasHandler.login(email, password, callback);
    }

    @Override
    public boolean validateLoginData(String email, String password) {
        boolean validate = true;
        if (email == null || email.isEmpty()) {
            validate = false;
            mLoginView.setEmailInputTextErrorMessage();
        }

        if (password == null || password.isEmpty()) {
            validate = false;
            mLoginView.setPasswordInputTextErrorMessage();
        }
        return validate;
    }

    @Override
    public void start() {

    }
}

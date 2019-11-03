package com.android.kwm.advertiser.login;

import com.android.kwm.BasePresenter;
import com.android.kwm.BaseView;
import com.android.kwm.data.authentication.AuthenticationRepository;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void setEmailInputTextErrorMessage();

        void setPasswordInputTextErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends BasePresenter {
        void login(String email, String password, AuthenticationRepository.LoginCallback callback);

        boolean validateLoginData(String email, String password);
    }
}

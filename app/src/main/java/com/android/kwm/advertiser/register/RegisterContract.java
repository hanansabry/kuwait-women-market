package com.android.kwm.advertiser.register;

import com.android.kwm.BasePresenter;
import com.android.kwm.BaseView;
import com.android.kwm.data.authentication.AuthenticationRepository;
import com.android.kwm.model.Advertiser;

public interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void setNameInputTextErrorMessage();

        void setUserNameInputTextErrorMessage();

        void setPasswordInputTextErrorMessage(String errorMessage);

        void setEmailInputTextErrorMessage();

        void setPhoneInputTextErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }

    interface Presenter extends BasePresenter {

        void registerNewUser(Advertiser user, AuthenticationRepository.RegistrationCallback callback);

        boolean validateUserData(Advertiser user);

    }

}

package com.android.kwm;

import com.android.kwm.data.advertisers.AdvertiserRepository;
import com.android.kwm.data.advertisers.AdvertiserRepositoryImpl;
import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepository;
import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepositoryImp;
import com.android.kwm.data.authentication.AuthenticationRepository;
import com.android.kwm.data.authentication.AuthenticationRepositoryImpl;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.data.categories.CategoriesRepositoryImpl;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.data.modelItems.ModelItemsRepositoryImpl;
import com.android.kwm.data.storage.ImagesStorageImpl;
import com.android.kwm.usecase.AddModelUseCaseHandler;
import com.android.kwm.usecase.AuthenticationUseCaseHandler;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Injection {

    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }

    public static AuthenticationUseCaseHandler provideAuthenticationUseCaseHandler() {
        return new AuthenticationUseCaseHandler(provideAuthenticationRepository());
    }

    public static CategoriesRepository provideCategoriesRepository() {
        return new CategoriesRepositoryImpl();
    }

    public static AddModelUseCaseHandler provideAddModelUseCaseHandler() {
        return new AddModelUseCaseHandler(provideCategoriesRepository(), provideModelItemsRepository(), provideAdvertisingTimeOptions());
    }

    private static AdvertisingTimeRepository provideAdvertisingTimeOptions() {
        return new AdvertisingTimeRepositoryImp();
    }

    public static ModelItemsRepository provideModelItemsRepository() {
        return new ModelItemsRepositoryImpl(new ImagesStorageImpl());
    }

    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    }

    public static AdvertiserRepository provideAdvertiserRepository() {
        return new AdvertiserRepositoryImpl();
    }
}

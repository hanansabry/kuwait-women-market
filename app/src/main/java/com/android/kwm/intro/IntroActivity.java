package com.android.kwm.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.kwm.R;
import com.android.kwm.advertiser.login.LoginActivity;
import com.android.kwm.advertiser.register.RegisterActivity;
import com.android.kwm.advertiser.store.AdvertiserStoreActivity;
import com.android.kwm.shopping.main.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void goShoppingHomeScreen(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void onAdvertiserLoginClicked(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent mainIntent = new Intent(this, AdvertiserStoreActivity.class);
            startActivity(mainIntent);
        } else {
            Intent mainIntent = new Intent(this, LoginActivity.class);
            startActivity(mainIntent);
        }
//        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onRegisterAdvertiserClicked(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

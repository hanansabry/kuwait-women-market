package com.android.kwm.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.kwm.R;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-ServiceListActivity. */
//                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                    Intent mainIntent = new Intent(SplashScreen.this, HomeActivity.class);
//                    startActivity(mainIntent);
//                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, IntroActivity.class);
                    startActivity(mainIntent);
//                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

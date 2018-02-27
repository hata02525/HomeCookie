package com.app.homecookie.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;

public class SplashActivity extends AppCompatActivity {


    private boolean isPaused;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startNextScreen();
    }


    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        if (!isPaused) {
            startNextScreen();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    private void startNextScreen() {
        if (!isPaused) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isPaused) {
                        if(new SharedPreference(SplashActivity.this).getBoolean(Constants.SESSION,false)){
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                        }else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                         finish();
                    }
                }
            }, 3000);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}


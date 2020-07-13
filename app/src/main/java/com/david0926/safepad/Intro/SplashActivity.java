package com.david0926.safepad.Intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.david0926.safepad.Auth.LoginActivity;
import com.david0926.safepad.MainActivity;
import com.david0926.safepad.R;
import com.david0926.safepad.util.SharedPreferenceUtil;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //fullscreen

        new Handler().postDelayed(() -> {
            SharedPreferenceUtil.putInt(this, "alert", 0);
            SharedPreferenceUtil.putInt(this, "time", 0);
            boolean isLandingShown = getSharedPreferences(this).getBoolean("landing_shown", false);
            isLandingShown = false; //remove this line, to show landing page only once

            if (isLandingShown)
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            else {
                getSharedPreferences(this)
                        .edit()
                        .putBoolean("landing_shown", true)
                        .apply();
                startActivity(new Intent(SplashActivity.this, LandingActivity.class));
            }

            finish();
        }, 2000); //wait 2 sec
    }
}

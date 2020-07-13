package com.david0926.safepad.Intro;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.david0926.safepad.Auth.LoginActivity;
import com.david0926.safepad.R;
import com.david0926.safepad.util.SharedPreferenceUtil;
import com.github.paolorotolo.appintro.AppIntro;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class LandingActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addSlide(LandingFragment.newInstance(R.layout.activity_landing1));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing2));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing3));

        showSkipButton(false);
        setProgressButtonEnabled(true);
        showSeparator(false);
        setIndicatorColor(getColor(R.color.colorPrimary), getColor(R.color.materialGray5));
        setImageNextButton(getDrawable(R.drawable.ic_navigate_next_primary_24dp));
        setColorTransitionsEnabled(true);
        setSkipText("Skip");
        setColorSkipButton(getColor(R.color.colorPrimary));
        setSkipTextTypeface(R.font.productb);
        setDoneText("Get Started");
        setColorDoneText(getColor(R.color.colorPrimary));
        setDoneTextTypeface(R.font.productb);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        finishLanding();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finishLanding();
    }


    private void finishLanding() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted() {
                        SharedPreferenceUtil.putBoolean(LandingActivity.this, "landing_shown", true);
                        startActivity(new Intent(LandingActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage("권한에 동의해 주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }
}

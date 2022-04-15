package com.maviay.pazaryeri.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Pair;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.maviay.pazaryeri.R;
import com.maviay.pazaryeri.Utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    Context mContext;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;
    Animation logoAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        post();

    }

    private void init(){
        mContext = this;
        ButterKnife.bind(this);
        logoAnim = AnimationUtils.loadAnimation(mContext,R.anim.logo_anim);
        imgLogo.startAnimation(logoAnim);
    }

    private synchronized void post(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goNextActivity();
            }
        }, 500);
    }

    private void goNextActivity(){
        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, Pair.create(imgLogo, "splashLogo"));
            startActivity(intent, activityOptions.toBundle());
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    finish();
                }
            }.start();
        }else {
            Util.goMain(mContext);
            finish();
        }
    }

}
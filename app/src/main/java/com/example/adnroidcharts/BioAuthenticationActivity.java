package com.example.adnroidcharts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class BioAuthenticationActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    FingerprintManager fingerprintManager;

    private FingerprintManager.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_authentication);

        lottieAnimationView = findViewById(R.id.fpanimation);
        fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                lottieAnimationView.setAnimation(R.raw.fail);
                lottieAnimationView.setSpeed(1);
                lottieAnimationView.playAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieAnimationView.setSpeed(-1);
                        lottieAnimationView.playAnimation();
                    }
                }, 3000);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                lottieAnimationView.setAnimation(R.raw.success);
                lottieAnimationView.setSpeed(1);
                lottieAnimationView.playAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BioAuthenticationActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                lottieAnimationView.setAnimation(R.raw.fail);
                lottieAnimationView.setSpeed(1);
                lottieAnimationView.playAnimation();


                new Handler().postDelayed(new Runnable() {

                    // Using handler with postDelayed called runnable run method

                    @Override

                    public void run() {
                        lottieAnimationView.setSpeed(-1);
                        lottieAnimationView.playAnimation();

                    }

                }, 5000);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        fingerprintManager.authenticate(null,null,0,authenticationCallback,null);
    }
}
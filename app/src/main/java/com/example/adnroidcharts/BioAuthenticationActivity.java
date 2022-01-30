package com.example.adnroidcharts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class BioAuthenticationActivity extends AppCompatActivity {
    private TextView textView;
    private String password = "123456";
    private EditText passwordTxt;
    private Button loginBtn;
    private AlertDialog dialog;

    LottieAnimationView lottieAnimationView;
    FingerprintManager fingerprintManager;

    private FingerprintManager.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_authentication);

        //Get fullscreen without clock on the top or other things ......
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        textView = findViewById(R.id.openWithpassword);
        String text = "Click here to open with password.";
        lottieAnimationView = findViewById(R.id.fpanimation);
        fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);


        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BioAuthenticationActivity.this);
//                builder.setTitle(getString(R.string.login));

                //Inflate the login_dialog view
                View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
                passwordTxt = view.findViewById(R.id.passwordTxt);
                loginBtn = view.findViewById(R.id.loginBtn);
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((passwordTxt.getText().toString()).equals(password)){
                            Toast.makeText(BioAuthenticationActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BioAuthenticationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(BioAuthenticationActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                // set this view to dialog
                builder.setView(view);

                // create dialog now
                dialog = builder.create();
                dialog.show();



            }
        };

        spannableString.setSpan(clickableSpan,6,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

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
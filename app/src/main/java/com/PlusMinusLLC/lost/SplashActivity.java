package com.PlusMinusLLC.lost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.PlusMinusLLC.lost.Registration.RegisterActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageView1;
    private SpinKitView lottieAnimationView;
    private FirebaseAuth firebaseAuth;
    private int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.button_animation);

        //Hooks
        imageView1 = findViewById(R.id.logo);
        imageView1.setAnimation(topAnim);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent registerIntent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
            lottieAnimationView.setVisibility(View.GONE);
            finish();
        } else {
            Map<String, Object> updateStatus = new HashMap<>();
            updateStatus.put("userStatus", "Active");
            updateStatus.put("Last seen", FieldValue.serverTimestamp());
            FirebaseFirestore.getInstance().collection("USERS")
                    .document(currentUser.getUid())
                    .update(updateStatus)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                lottieAnimationView.setVisibility(View.GONE);
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
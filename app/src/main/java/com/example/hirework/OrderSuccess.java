package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class OrderSuccess extends AppCompatActivity {

    Button btn;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        btn=findViewById(R.id.goBack);
        animationView=findViewById(R.id.animation_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationView.playAnimation();
            }
        }, 2000);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CustomerOrderStats.class));
            }
        });
    }
}
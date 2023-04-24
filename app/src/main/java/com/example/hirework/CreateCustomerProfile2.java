package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateCustomerProfile2 extends AppCompatActivity {

    Button completeBtn;
    TextView gaming, video, website, contentWriting, seo, designing;

    SharedPreferences sp;

    int counter1,counter2,counter3,counter4,counter5,counter6=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer_profile2);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);

        gaming =findViewById(R.id.ft1);
        video =findViewById(R.id.ft2);
        website =findViewById(R.id.ft3);
        contentWriting =findViewById(R.id.ft4);
        seo =findViewById(R.id.ft5);
        designing =findViewById(R.id.ft6);

        completeBtn=findViewById(R.id.profileCompleteBtn);


        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter1++;
                if(counter1 % 2==0){
                    gaming.setBackgroundResource(R.drawable.txt_bg);
                    sp.edit().putBoolean("gaming",false).apply();
                }
                else{
                    gaming.setBackgroundResource(R.drawable.txt_bg2);
                    sp.edit().putBoolean("gaming",true).apply();
                }

            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter2++;
                if(counter2 % 2==0){
                    video.setBackgroundResource(R.drawable.txt_bg);
                    sp.edit().putBoolean("video",false).apply();

                }
                else{
                    video.setBackgroundResource(R.drawable.txt_bg2);
                    sp.edit().putBoolean("video",true).apply();

                }
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter3++;
                if(counter3 % 2==0){
                    website.setBackgroundResource(R.drawable.txt_bg);
                    sp.edit().putBoolean("website",false).apply();
                }
                else{
                    website.setBackgroundResource(R.drawable.txt_bg2);
                    sp.edit().putBoolean("website",true).apply();
                }
            }
        });

        contentWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter4++;
                if(counter4 % 2==0){
                    contentWriting.setBackgroundResource(R.drawable.txt_bg);
                    sp.edit().putBoolean("contentWriting",false).apply();
                }
                else{
                    contentWriting.setBackgroundResource(R.drawable.txt_bg2);
                    sp.edit().putBoolean("contentWriting",true).apply();
                }
            }
        });

        seo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter5++;
                if(counter5 % 2==0){
                    seo.setBackgroundResource(R.drawable.txt_bg);
                    sp.edit().putBoolean("seo",false).apply();
                }
                else{
                    seo.setBackgroundResource(R.drawable.txt_bg2);
                    sp.edit().putBoolean("seo",true).apply();
                }
            }
        });

        designing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter6++;
                if(counter6 % 2==0){
                    designing.setBackgroundResource(R.drawable.txt_bg);
                    sp.edit().putBoolean("designing",false).apply();
                }
                else{
                    designing.setBackgroundResource(R.drawable.txt_bg2);
                    sp.edit().putBoolean("designing",true).apply();
                }
            }
        });
    }
}
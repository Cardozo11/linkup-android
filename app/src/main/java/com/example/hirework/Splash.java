package com.example.hirework;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    SharedPreferences sp;
    Customer customer;

    String freelancer_email;

    TextView app_name;

    private static final int SPLASH_TIME_OUT=4000;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app_name=findViewById(R.id.app_Name);
        sp = getSharedPreferences("proj", Context.MODE_PRIVATE);

        customer=new Customer(this);


        START_ACTIVITY();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void START_ACTIVITY(){
        customer.CustomerEmail=sp.getString("email","");
        freelancer_email=sp.getString("email_f","");

        new Handler().postDelayed(() -> {

            if (!customer.CustomerEmail.isEmpty()) {
               // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                //Bundle b=ActivityOptions.makeSceneTransitionAnimation(Splash.this,app_name,"trans_anim").toBundle();
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(Splash.this).toBundle();
                startActivity(i,b);
            }
            else if(!freelancer_email.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
            else {
//                startActivity(new Intent(getApplicationContext(), Login.class));
                Intent i=new Intent(getApplicationContext(),Login.class);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(Splash.this).toBundle();
                startActivity(i,b);
            }
        },SPLASH_TIME_OUT);
    }
}
package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class CustomerSettings extends AppCompatActivity {

    String INTENT_NAME="url";
    SharedPreferences sp;
    ImageView img;

    Customer customer;

    //    URLS
    final String TERMS_AND_CONDITIONS_URL="https://a1freelance.blogspot.com/p/terms-and-conditions.html";
    final String ABOUT_US_URL="https://a1freelance.blogspot.com/p/about-us.html";
    final String CONTACT_US_URL="https://a1freelance.blogspot.com/p/contact-us.html";
    final String PRIVACY_POLICY_URL="https://a1freelance.blogspot.com/p/privacy-policy.html";
    final String BLOG_URL="https://a1freelance.blogspot.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);
        img=findViewById(R.id.Settings_customer_profile_1);
        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        customer=new Customer(this);

        customer.CUSTOMER_PHOTO=sp.getString(customer.SP_CUSTOMER_PROFILE_PHOTO,"");

        if(customer.CUSTOMER_PHOTO.equals("")){
            getProfile();
        }
        else{
            Glide.with(this).load(customer.CUSTOMER_PHOTO).into(img);

        }


    }

    public void getProfile(){
        Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
    }



    public void openChatBot(View view){
        Intent i=new Intent(getApplicationContext(),CONTACT_US.class);
        startActivity(i);
    }

    public void openTerms(View view){
        Intent i=new Intent(getApplicationContext(),TermsAndConditions.class);
        i.putExtra(INTENT_NAME,TERMS_AND_CONDITIONS_URL);
        startActivity(i);
    }

    public void openAboutUs(View view){
        Intent i=new Intent(getApplicationContext(),TermsAndConditions.class);
        i.putExtra(INTENT_NAME,ABOUT_US_URL);
        startActivity(i);
    }

    public void openContactUs(View view){
        Intent i=new Intent(getApplicationContext(),TermsAndConditions.class);
        i.putExtra(INTENT_NAME,CONTACT_US_URL);
        startActivity(i);
    }


    public void openPrivacyPolicy(View view){
        Intent i=new Intent(getApplicationContext(),TermsAndConditions.class);
        i.putExtra(INTENT_NAME,PRIVACY_POLICY_URL);
        startActivity(i);
    }

    public void openBLOG(View view){
        Intent i=new Intent(getApplicationContext(),TermsAndConditions.class);
        i.putExtra(INTENT_NAME,BLOG_URL);
        startActivity(i);
    }

    public void LogOut(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(i);
    }
}
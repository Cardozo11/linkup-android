package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FreelancerSettings extends AppCompatActivity {

    String INTENT_NAME="url";
    SharedPreferences sp;

//    URLS
    final String TERMS_AND_CONDITIONS_URL="https://a1freelance.blogspot.com/p/terms-and-conditions.html";
    final String ABOUT_US_URL="https://a1freelance.blogspot.com/p/about-us.html";
    final String CONTACT_US_URL="https://a1freelance.blogspot.com/p/contact-us.html";
    final String PRIVACY_POLICY_URL="https://a1freelance.blogspot.com/p/privacy-policy.html";
    final String BLOG_URL="https://a1freelance.blogspot.com/";



    String LOAD_FREELANCER_PROFILE_PIC_URL;
    private static final String JSON_ARRAY ="result";
    private static final String IMAGE_URL = "url";
    private String imagesJSON;
    private JSONArray arrayImages= null;
    private int TRACK = 0;
    String email;
    ImageView img;

    Freelancer freelancer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_settings);

        freelancer=new Freelancer(this);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        img=findViewById(R.id.setting_freelancer_profile_1);

        email=sp.getString(freelancer.SP_FREELANCER_EMAIL,"");
        //email=sp.getString("email_f","");
        LOAD_FREELANCER_PROFILE_PIC_URL="https://hirework.tech/FPROFILE.php?fEmail="+email;

        freelancer.FREELANCER_PHOTO=sp.getString(freelancer.SP_FREELANCER_PROFILE_PHOTO,"");


        //getProfile(); // for profile pic of freelancer
        if(freelancer.FREELANCER_PHOTO.equals("")){
            getProfile();
        }
        else{
            Glide.with(this).load(freelancer.FREELANCER_PHOTO).into(img);

        }
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

    public void openChatBot(View view){
        Intent i=new Intent(getApplicationContext(),CONTACT_US.class);
        startActivity(i);
    }


    public void LogOut(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }

    public void openPayment(View view){
        Intent i=new Intent(getApplicationContext(),Withdraw.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),freelancerProfile.class);
        startActivity(i);
    }

    private void getProfile(){
        class GetAllImages extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FreelancerSettings.this, "Fetching Data...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                imagesJSON = s;
                extractJSON();
                showImage();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetAllImages gai = new GetAllImages();
        gai.execute(LOAD_FREELANCER_PROFILE_PIC_URL);
    }

    private void showImage(){
        try {
            JSONObject jsonObject = arrayImages.getJSONObject(TRACK);
            getImage(jsonObject.getString(IMAGE_URL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractJSON(){
        try {
            JSONObject jsonObject = new JSONObject(imagesJSON);
            arrayImages = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getImage(String urlToImage){
        class GetImage extends AsyncTask<String,Void, Bitmap>{
            ProgressDialog loading;
            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;
                Bitmap image = null;

                String urlToImage = params[0];
                try {
                    url = new URL(urlToImage);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FreelancerSettings.this,"Loading Image...","Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                img.setImageBitmap(bitmap);

            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }
}
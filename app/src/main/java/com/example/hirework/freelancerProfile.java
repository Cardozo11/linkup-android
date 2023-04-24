package com.example.hirework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class freelancerProfile extends AppCompatActivity {
    TextView email_txt,username_txt,name_txt,phone_txt,qualify_txt,exp_txt;
    ImageView profilePic,profilePic2,setting_img;
    SharedPreferences sp;
    String email,username,name,qualification,exp;
    int phone;

    private static final String JSON_ARRAY ="result";
    private static final String IMAGE_URL = "url";
    private String imagesJSON;
    private JSONArray arrayImages= null;
    private int TRACK = 0;
    String LOAD_FREELANCER_PROFILE_PIC_URL;

    Freelancer freelancer;

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_profile);

        freelancer=new Freelancer(this);




        email_txt=findViewById(R.id.freelancer_email);
        username_txt=findViewById(R.id.freelancer_username);
        name_txt=findViewById(R.id.freelancer_name);
        profilePic=findViewById(R.id.freelancer_profile_1);
        profilePic2=findViewById(R.id.freelancer_profile_2);
        phone_txt=findViewById(R.id.freelancer_phone);
        qualify_txt=findViewById(R.id.freelancer_qualification);
        exp_txt=findViewById(R.id.freelancer_experience);

        setting_img=findViewById(R.id.setting_img);




        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
//        email=sp.getString("email_f","");
//        username=sp.getString("username_f","");
//        name=sp.getString("name_f","");
//        phone=sp.getInt("phone_f",0);
//        qualification=sp.getString("qualification","");
//        exp=sp.getString("exp","");

        email=sp.getString(freelancer.SP_FREELANCER_EMAIL,"");
        username=sp.getString(freelancer.SP_FREELANCER_USERNAME,"");
        name=sp.getString(freelancer.SP_FREELANCER_NAME,"");
        String ph=sp.getString(freelancer.SP_FREELANCER_PHONE,"");
        qualification=sp.getString(freelancer.SP_FREELANCER_QUALIFICATION,"");
        exp=sp.getString(freelancer.SP_FREELANCER_EXP,"");
        freelancer.FREELANCER_PHOTO=sp.getString(freelancer.SP_FREELANCER_PROFILE_PHOTO,"");


        LOAD_FREELANCER_PROFILE_PIC_URL="https://hirework.tech/FPROFILE.php?fEmail="+email;

        email_txt.setText(email);
        username_txt.setText(username);
        name_txt.setText("Name : "+name);
        phone_txt.setText("Phone No : "+String.valueOf(ph));
        qualify_txt.setText("Qualification : "+qualification);
        exp_txt.setText("Experience : "+exp);

        if(freelancer.FREELANCER_PHOTO.equals("")){
            getProfile();
        }
        else{
            Glide.with(this).load(freelancer.FREELANCER_PHOTO).into(profilePic);
            Glide.with(this).load(freelancer.FREELANCER_PHOTO).into(profilePic2);
        }


        setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),CONTACT_US.class));
                Intent intent=new Intent(getApplicationContext(),FreelancerSettings.class);
                startActivity(intent);

            }
        });



        bottomNavigationView=findViewById(R.id.pnav);
        bottomNavigationView.setSelectedItemId(R.id.fProfile);//home
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.DashboardMenu:
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.gigs:
                        startActivity(new Intent(getApplicationContext(),Gigs.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.fProfile:
                        startActivity(new Intent(getApplicationContext(),freelancerProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }

        });
    }

    private void getProfile(){
        class GetAllImages extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(freelancerProfile.this, "Fetching Data...","Please Wait...",true,true);
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
                loading = ProgressDialog.show(freelancerProfile.this,"Loading Image...","Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                profilePic.setImageBitmap(bitmap);
                profilePic2.setImageBitmap(bitmap);

            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
        //Toast.makeText(this, urlToImage, Toast.LENGTH_SHORT).show();
        sp.edit().putString(freelancer.SP_FREELANCER_PROFILE_PHOTO,urlToImage).apply();
    }
}
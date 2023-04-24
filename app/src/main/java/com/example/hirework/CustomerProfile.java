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

public class CustomerProfile extends AppCompatActivity {

    ImageView profilePic,profilePic2,settings;
    SharedPreferences sp;

    TextView email_txt,username_txt,name_txt,phone_txt;

    String phone;


    String email,username,name;
    String LOAD_CUSTOMER_PROFILE_PIC_URL;

    Customer customer;

    private String imagesJSON;
    private JSONArray arrayImages= null;
    private int TRACK = 0;
    private static final String JSON_ARRAY ="result";
    private static final String IMAGE_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        customer=new Customer(this);




        email_txt=findViewById(R.id.customer_email);
        name_txt=findViewById(R.id.customer_name);
        username_txt=findViewById(R.id.customer_username);

        profilePic=findViewById(R.id.customer_profile_1);
        profilePic2=findViewById(R.id.customer_profile_2);

        settings=findViewById(R.id.customer_setting_img);

        phone_txt=findViewById(R.id.customer_phone);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        email=sp.getString(customer.SP_CUSTOMER_EMAIL_KEY,"");
        name=sp.getString(customer.SP_CUSTOMER_NAME_KEY,"");
        username=sp.getString(customer.SP_CUSTOMER_USERNAME_KEY,"");

        phone=sp.getString(customer.SP_PHONE_NUMBER,"");
        LOAD_CUSTOMER_PROFILE_PIC_URL = "https://hirework.tech/test5.php?cEmail="+email;
        customer.CUSTOMER_PHOTO=sp.getString(customer.SP_CUSTOMER_PROFILE_PHOTO,"");
       // getAllImages();

        if(customer.CUSTOMER_PHOTO.equals("")){
            getAllImages();
        }
        else{
            Glide.with(this).load(customer.CUSTOMER_PHOTO).into(profilePic);
            Glide.with(this).load(customer.CUSTOMER_PHOTO).into(profilePic2);
        }

        email_txt.setText(email);
        name_txt.setText("Name : "+name);
        username_txt.setText(username);
        phone_txt.setText("Phone No : "+phone);



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CustomerSettings.class));
            }
        });



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation2);


        bottomNavigationView.setSelectedItemId(R.id.profileMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.HomeMenu:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                    case R.id.orderMenu:
                        startActivity(new Intent(getApplicationContext(),CustomerOrderStats.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                    case R.id.profileMenu:
                        startActivity(new Intent(getApplicationContext(),CustomerProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    private void getAllImages() {
        class GetAllImages extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CustomerProfile.this, "Fetching Data...","Please Wait...",true,true);
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
        gai.execute(LOAD_CUSTOMER_PROFILE_PIC_URL);
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
                loading = ProgressDialog.show(CustomerProfile.this,"Downloading Image...","Please wait...",true,true);
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
        sp.edit().putString(customer.SP_CUSTOMER_PROFILE_PHOTO,urlToImage).apply();
    }
}
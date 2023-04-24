package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class CreateCustomerProfile extends AppCompatActivity {

//    success

    Button nextBtn;

    String em,unm,pas;

    EditText nameEd,phoneEd,emailEd;

    ImageView profilePhoto;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    private Uri filePath;

    Customer customer;

    SharedPreferences sharedPreferences;

    private static final String UPLOAD_URL = "https://hirework.tech/test3.php";
    public static final String UPLOAD_KEY = "cImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer_profile);

        customer=new Customer(this);

        sharedPreferences=getSharedPreferences("proj", Context.MODE_PRIVATE);



        profilePhoto=findViewById(R.id.ccpImg);


        nextBtn=findViewById(R.id.cpNextBtn);



        em=getIntent().getStringExtra(customer.INTENT_EMAIL);
        unm=getIntent().getStringExtra(customer.INTENT_USERNAME);
        pas=getIntent().getStringExtra(customer.INTENT_PASSWORD);



        nameEd=findViewById(R.id.ccpName);
        phoneEd=findViewById(R.id.ccpPhoneNo);
        emailEd=findViewById(R.id.ccpEmail);




        emailEd.setText(em);
        emailEd.setEnabled(false);
        emailEd.setTextColor(Color.BLACK);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfile();
            }
        });



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate(){
        String n=nameEd.getText().toString().trim();
        String ph=phoneEd.getText().toString().trim();

        if (n.isEmpty() || ph.isEmpty()){
            Toast.makeText(this, "Please fill All Details", Toast.LENGTH_SHORT).show();
        }
        else if (filePath==null){
            Toast.makeText(this, "Upload Photo", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadImage(n,ph);
        }
}


    private void uploadProfile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(String n,String ph){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CreateCustomerProfile.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
               // if(s.equals("Successfully Uploaded")){
                    Intent i=new Intent(CreateCustomerProfile.this,CreateCustomerProfile2.class);
                    sharedPreferences.edit().putString(customer.SP_CUSTOMER_EMAIL_KEY,em).apply();
                    sharedPreferences.edit().putString(customer.SP_CUSTOMER_USERNAME_KEY,unm).apply();
                    sharedPreferences.edit().putString(customer.SP_CUSTOMER_NAME_KEY,n).apply();
                    sharedPreferences.edit().putString(customer.SP_PHONE_NUMBER,ph).apply();
                    startActivity(i);

               // }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(customer.CUSTOMER_EMAIL_DB_KEY, em);
                data.put(customer.CUSTOMER_USERNAME_DB_KEY, unm);
                data.put(customer.CUSTOMER_PASSWORD_DB_KEY, pas);
                data.put(customer.CUSTOMER_NAME_DB_KEY,n);
                data.put(customer.CUSTOMER_PHONE_DB_KEY,ph);
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

public class CreateFreelancerProfile extends AppCompatActivity {

    String em,unm,pas;

    Freelancer freelancer;

    Button nextBtn;
    EditText nameEd,phoneEd,emailEd;
    ImageView img;

    private int PICK_IMAGE_REQUEST = 1;
    SharedPreferences sp;
    private Bitmap bitmap;
    private Uri filePath;
    public static final String UPLOAD_KEY = "fImage";
    private static final String UPLOAD_URL = "https://hirework.tech/registerFreelancer.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_freelancer_profile);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);

        freelancer=new Freelancer(this);

        nameEd=findViewById(R.id.cfpName);
        phoneEd=findViewById(R.id.cfpPhoneNo);
        emailEd=findViewById(R.id.cfpEmail);

        img=findViewById(R.id.cfpImg);



//        em=getIntent().getStringExtra("em");
//        unm=getIntent().getStringExtra("unm");
        //pas=getIntent().getStringExtra("pas");
        em=getIntent().getStringExtra(freelancer.INTENT_EMAIL);
        unm=getIntent().getStringExtra(freelancer.INTENT_USERNAME);
        pas=getIntent().getStringExtra(freelancer.INTENT_PASSWORD);

        nextBtn=findViewById(R.id.fpNextBtn);

        emailEd.setText(em);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfile();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),CreateFreelancerProfile2.class));
                validate();
            }
        });
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
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            uploadDetails(n,ph);
        }
    }

    private void uploadDetails(String n,String ph){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CreateFreelancerProfile.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                if(s.equals("Successfully Uploaded")){
                    Intent i=new Intent(CreateFreelancerProfile.this,CreateFreelancerProfile2.class);
                    sp.edit().putString(freelancer.SP_FREELANCER_EMAIL,em).apply();
                    sp.edit().putString(freelancer.SP_FREELANCER_USERNAME,unm).apply();
                    sp.edit().putString(freelancer.SP_FREELANCER_NAME,n).apply();
                    sp.edit().putString(freelancer.SP_FREELANCER_PHONE,ph).apply();

                    startActivity(i);

                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put("fEmail", em);
                data.put("fUsername", unm);
                data.put("fPassword", pas);
                data.put("fName",n);
                data.put("fPhone",ph);
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
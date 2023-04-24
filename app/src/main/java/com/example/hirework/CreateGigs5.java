package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class CreateGigs5 extends AppCompatActivity {



    Button chooseFile,uploadFile;

    ImageView image1;

    GigModel gig;


    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;
    String price,completionDays;

    SharedPreferences sp;
    String Fusername;

   private static final String UPLOAD_URL = "https://hirework.tech/upGig.php";
    //private static final String UPLOAD_URL = "https://hirework.tech/uploadGigs.php";
    public static final String UPLOAD_KEY = "image1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gigs5);
        image1=findViewById(R.id.image1);
        chooseFile=findViewById(R.id.chooseFile);
        uploadFile=findViewById(R.id.uploadFile);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        Fusername=sp.getString("username_f","");

        gig=new GigModel(this);
        //Toast.makeText(this, Fusername, Toast.LENGTH_SHORT).show();


        //gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_TITLE);
        gig.GigCategory=getIntent().getStringExtra(gig.INTENT_GIG_CATEGORY);
        gig.GigSubCategory=getIntent().getStringExtra(gig.INTENT_GIG_SUB_CATEGORY);
        gig.GigDescription=getIntent().getStringExtra(gig.INTENT_GIG_DESCRIPTION);
        gig.price=getIntent().getIntExtra(gig.INTENT_GIG_PRICE,0);
        price=String.valueOf(gig.price);
        gig.CompletionDays=getIntent().getIntExtra(gig.INTENT_GIG_COMPLETION_DAYS,0);
        completionDays=String.valueOf(gig.CompletionDays);
        gig.Attachment=getIntent().getStringExtra(gig.INTENT_GIG_ATTACHMENT);
        gig.GigFileFormat=getIntent().getStringExtra(gig.INTENT_GIG_FILE_FORMAT);
        gig.AdditionalInfo=getIntent().getStringExtra(gig.INTENT_GIG_ADDITIONAL_INFO);


        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CreateGigs5.this, "choose file", Toast.LENGTH_SHORT).show();
                uploadGig();
            }
        });




        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });


    }


    private void uploadGig(){
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
            chooseFile.setVisibility(View.INVISIBLE);
            uploadFile.setVisibility(View.VISIBLE);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CreateGigs5.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                if(s.equals("Successfully Uploaded")){
                    Intent i=new Intent(CreateGigs5.this,Gigs.class);
                    startActivity(i);

                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(gig.DB_FREELANCER_USERNAME,Fusername);
                data.put(gig.DB_GIG_TITLE,gig.GigTitle);
                data.put(gig.DB_GIG_CATEGORY,gig.GigCategory);
                data.put(gig.DB_GIG_SUB_CATEGORY,gig.GigSubCategory);
                data.put(gig.DB_GIG_DESCRIPTION,gig.GigDescription);
                data.put(gig.DB_GIG_PRICE,price);
                data.put(gig.DB_GIG_COMPLETION_DAYS,completionDays);
                data.put(gig.DB_GIG_ATTACHMENT,gig.Attachment);
                data.put(gig.DB_GIG_FILE_FORMAT,gig.GigFileFormat);
                data.put(gig.DB_GIG_ADDITIONAL_INFO,gig.AdditionalInfo);
                data.put(gig.DB_GIG_IMAGE, uploadImage);

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
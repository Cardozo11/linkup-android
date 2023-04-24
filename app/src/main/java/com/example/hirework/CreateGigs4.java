package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateGigs4 extends AppCompatActivity {

    Button nextBtn;
    SharedPreferences sp;
    EditText gigFileFormat,gigAdditionalInfo;
    Spinner gigAttachment;
    GigModel gig;
    Freelancer freelancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gigs4);

        gig=new GigModel(this);
        freelancer=new Freelancer(this);

        gigAttachment=findViewById(R.id.gigAttachment);
        gigFileFormat=findViewById(R.id.gigFileFormat);
        gigAdditionalInfo=findViewById(R.id.gigAdditionalInfo);
        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);


        gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_TITLE);
        gig.GigCategory=getIntent().getStringExtra(gig.INTENT_GIG_CATEGORY);
        gig.GigSubCategory=getIntent().getStringExtra(gig.INTENT_GIG_SUB_CATEGORY);
        gig.GigDescription=getIntent().getStringExtra(gig.INTENT_GIG_DESCRIPTION);
        gig.price=getIntent().getIntExtra(gig.INTENT_GIG_PRICE,0);
        gig.CompletionDays=getIntent().getIntExtra(gig.INTENT_GIG_COMPLETION_DAYS,0);

        gig.fUsername=sp.getString("username_f","");

        String[] items = new String[]{"Yes", "No", "Optional"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        gigAttachment.setAdapter(adapter);


        nextBtn=findViewById(R.id.cgNextBtn4);




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean valid=validate();
               if (valid){
                   //SendData();
                   temp();
               }

               else{
                   Toast.makeText(CreateGigs4.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private boolean validate(){
        if(gigAttachment.getSelectedItem().toString().equals("") || gigFileFormat.getText().toString().equals("")){
            return false;
        }
        else{
            return true;
        }

    }


    private void temp(){
        gig.Attachment=gigAttachment.getSelectedItem().toString().trim();
        gig.GigFileFormat=gigFileFormat.getText().toString().trim();
        gig.AdditionalInfo=gigAdditionalInfo.getText().toString().trim();
        Intent i=new Intent(getApplicationContext(),CreateGigs5.class);
        i.putExtra(gig.INTENT_GIG_ID,gig.GigId);
        i.putExtra(gig.INTENT_GIG_FREELANCER_USERNAME,gig.fUsername);
        i.putExtra(gig.INTENT_GIG_TITLE,gig.GigTitle);
        i.putExtra(gig.INTENT_GIG_CATEGORY,gig.GigCategory);
        i.putExtra(gig.INTENT_GIG_SUB_CATEGORY,gig.GigSubCategory);
        i.putExtra(gig.INTENT_GIG_DESCRIPTION,gig.GigDescription);
        i.putExtra(gig.INTENT_GIG_PRICE,gig.price);
        i.putExtra(gig.INTENT_GIG_COMPLETION_DAYS,gig.CompletionDays);
        i.putExtra(gig.INTENT_GIG_ATTACHMENT,gig.Attachment);
        i.putExtra(gig.INTENT_GIG_FILE_FORMAT,gig.GigFileFormat);
        i.putExtra(gig.INTENT_GIG_ADDITIONAL_INFO,gig.AdditionalInfo);

        startActivity(i);
    }




}
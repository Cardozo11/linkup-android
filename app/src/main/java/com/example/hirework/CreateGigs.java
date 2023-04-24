package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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


public class CreateGigs extends AppCompatActivity {

    EditText gigTitle;
    Spinner category,subCategory;

    Button nextBtn;
    int gigId;

    GigModel gig;

    private static final String GIG_DATASET_1="https://phantomcity.in/gigSet1.php";

    private static final String GET_LAST_ID="https://hirework.tech/getid.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gigs);



        gigTitle=findViewById(R.id.gigTitle);

        gig=new GigModel(this);

        category=findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.gig_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category.setAdapter(adapter);


        subCategory=findViewById(R.id.subCategorySpinner);


        loadID();


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String choice=category.getSelectedItem().toString();

                if(choice.equals("Audio")){
                    ArrayAdapter<CharSequence>adapter2= ArrayAdapter.createFromResource(getApplicationContext(), R.array.Audio, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    subCategory.setAdapter(adapter2);
                }
                else if(choice.equals("Graphics")){
                    ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(getApplicationContext(), R.array.Graphics, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    subCategory.setAdapter(adapter);
                }

                else if(choice.equals("Video")){
                    ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(getApplicationContext(), R.array.Video, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    subCategory.setAdapter(adapter);
                }

                else if(choice.equals("Content Writing")){
                    ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(getApplicationContext(), R.array.Content_Writing, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    subCategory.setAdapter(adapter);
                }

                else if(choice.equals("Digital Marketing")){
                    ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(getApplicationContext(), R.array.Digital_Marketing, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    subCategory.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        nextBtn=findViewById(R.id.cgNext);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean ckCat=validate();
                if (ckCat){

                    gig.GigTitle=gigTitle.getText().toString().trim();
                    gig.GigCategory=category.getSelectedItem().toString();
                    gig.GigSubCategory=subCategory.getSelectedItem().toString();

                    Intent i=new Intent(CreateGigs.this,CreateGigs2.class);
                    i.putExtra(gig.INTENT_GIG_ID,gigId);
                    i.putExtra(gig.INTENT_GIG_TITLE,gig.GigTitle);
                    i.putExtra(gig.INTENT_GIG_CATEGORY,gig.GigCategory);
                    i.putExtra(gig.INTENT_GIG_SUB_CATEGORY,gig.GigSubCategory);

                    startActivity(i);


                }
                else{
                    Toast.makeText(CreateGigs.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private boolean validate(){
    if(category.getSelectedItem().toString().equals("Choose Category") || subCategory.getSelectedItem().toString().equals("Choose") || gigTitle.getText().toString().equals("")){
        return false;
    }

    else{
        return true;
    }
    }

    private void loadID(){
        StringRequest request = new StringRequest(Request.Method.POST, GET_LAST_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gigId=Integer.valueOf(response)+1;



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(CreateGigs.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(CreateGigs.this);
        requestQueue.add(request);
    }
}
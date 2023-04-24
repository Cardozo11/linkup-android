package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGigs2 extends AppCompatActivity {

    EditText description;

    Button nextBtn;
    int gId;

    GigModel gig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gigs2);

        gig=new GigModel(this);



        gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_TITLE);
        gig.GigCategory=getIntent().getStringExtra(gig.INTENT_GIG_CATEGORY);
        gig.GigSubCategory=getIntent().getStringExtra(gig.INTENT_GIG_SUB_CATEGORY);




        description=findViewById(R.id.gigDescription);


        nextBtn=findViewById(R.id.cgNextBtn2);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid=validate();

                if (valid){
                    gig.GigDescription=description.getText().toString().trim();
                    Intent i=new Intent(CreateGigs2.this,CreateGigs3.class);
                    i.putExtra(gig.INTENT_GIG_ID,gig.GigId);
                    i.putExtra(gig.INTENT_GIG_TITLE,gig.GigTitle);
                    i.putExtra(gig.INTENT_GIG_CATEGORY,gig.GigCategory);
                    i.putExtra(gig.INTENT_GIG_SUB_CATEGORY,gig.GigSubCategory);
                    i.putExtra(gig.INTENT_GIG_DESCRIPTION,gig.GigDescription);
                    startActivity(i);
                }

            }
        });
    }

    private boolean validate(){
        if(description.getText().toString().equals("")){
            return false;
        }

        else{
            return true;
        }
    }
}
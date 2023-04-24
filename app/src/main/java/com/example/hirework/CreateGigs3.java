package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGigs3 extends AppCompatActivity {

    EditText gigPrice,gigCompletionDays;

    Button nextBtn;

    GigModel gig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gigs3);

        gig=new GigModel(this);

        gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_TITLE);
        gig.GigCategory=getIntent().getStringExtra(gig.INTENT_GIG_CATEGORY);
        gig.GigSubCategory=getIntent().getStringExtra(gig.INTENT_GIG_SUB_CATEGORY);
        gig.GigDescription=getIntent().getStringExtra(gig.INTENT_GIG_DESCRIPTION);

        gigPrice=findViewById(R.id.gigPrice);
        gigCompletionDays=findViewById(R.id.gigCompletionDays);

        nextBtn=findViewById(R.id.cgNextBtn3);

        //Toast.makeText(this, gig.GigCategory, Toast.LENGTH_SHORT).show();


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid=validate();
                if (valid){
                    String gPrice=gigPrice.getText().toString().trim();
                    String gCompletionDays=gigCompletionDays.getText().toString().trim();

                    gig.price=Integer.valueOf(gPrice);
                    gig.CompletionDays=Integer.valueOf(gCompletionDays);
                    Intent i=new Intent(CreateGigs3.this,CreateGigs4.class);
                    i.putExtra(gig.INTENT_GIG_ID,gig.GigId);
                    i.putExtra(gig.INTENT_GIG_TITLE,gig.GigTitle);
                    i.putExtra(gig.INTENT_GIG_CATEGORY,gig.GigCategory);
                    i.putExtra(gig.INTENT_GIG_SUB_CATEGORY,gig.GigSubCategory);
                    i.putExtra(gig.INTENT_GIG_DESCRIPTION,gig.GigDescription);
                    i.putExtra(gig.INTENT_GIG_PRICE,gig.price);
                    i.putExtra(gig.INTENT_GIG_COMPLETION_DAYS,gig.CompletionDays);
                    startActivity(i);
                }

            }
        });
    }

    private boolean validate(){
        if(gigPrice.getText().toString().equals("") || gigCompletionDays.getText().toString().equals("")){
            return false;
        }
        else{
            return true;
        }
    }
}
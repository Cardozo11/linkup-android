package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GigView2 extends AppCompatActivity {

    TextView gigId_txt,title,dec,price,file_format,days;
    ImageView img;
    Button btn;
    Drawable mImage;

    GigModel gig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_view2);


        gigId_txt=findViewById(R.id.gigId);
        title =findViewById(R.id.g_title);
        dec=findViewById(R.id.g_des);
        price=findViewById(R.id.g_price);
        days=findViewById(R.id.g_CompletionDays);
        file_format=findViewById(R.id.g_FileFormat);
        img=findViewById(R.id.g_img);
        btn=findViewById(R.id.GEditBtn);


        gig=new GigModel(this);


        gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_TITLE);
        gig.GigDescription=getIntent().getStringExtra(gig.INTENT_GIG_DESCRIPTION);
        gig.price=getIntent().getIntExtra(gig.INTENT_GIG_PRICE,0);
        gig.CompletionDays=getIntent().getIntExtra(gig.INTENT_GIG_COMPLETION_DAYS,0);
        gig.GigFileFormat=getIntent().getStringExtra(gig.INTENT_GIG_FILE_FORMAT);
        gig.Image1=getIntent().getStringExtra(gig.INTENT_GIG_IMAGE);

        gigId_txt.setText("Gig ID : "+String.valueOf(gig.GigId));
        title.setText(gig.GigTitle);
        dec.setText(gig.GigDescription);
        price.setText("₹ "+String.valueOf(gig.price));
        days.setText(String.valueOf(gig.CompletionDays+" Days"));
        file_format.setText(gig.GigFileFormat);

        Glide.with(GigView2.this).load(gig.Image1).into(img);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImage=getDrawable(R.drawable.ic_edit);
            mImage.setTint(Color.WHITE);
            btn.setCompoundDrawablesWithIntrinsicBounds(mImage, null, null, null);

        }





    }
}
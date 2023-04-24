package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Account extends AppCompatActivity {


    Button customerBtn,freelancerBtn;

    String em,unm,pas;

    Customer customer;
    Freelancer freelancer;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        customer=new Customer(this);

        customerBtn=findViewById(R.id.customerBtn);
        freelancerBtn=findViewById(R.id.freelancerBtn);

        freelancer=new Freelancer(this);
        user=new User();


//        em=getIntent().getStringExtra(customer.INTENT_EMAIL);
//        unm=getIntent().getStringExtra(customer.INTENT_USERNAME);
//        pas=getIntent().getStringExtra(customer.INTENT_PASSWORD);

        em=getIntent().getStringExtra(user.INTENT_EMAIL);
        unm=getIntent().getStringExtra(user.INTENT_USERNAME);
        pas=getIntent().getStringExtra(user.INTENT_PASSWORD);



        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Account.this,CreateCustomerProfile.class);
                i.putExtra(customer.INTENT_EMAIL,em);
                i.putExtra(customer.INTENT_USERNAME,unm);
                i.putExtra(customer.INTENT_PASSWORD,pas);
                startActivity(i);
            }
        });



        freelancerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Account.this,CreateFreelancerProfile.class);
                //i.putExtra("em",em);
                //i.putExtra("unm",unm);
                //i.putExtra("pas",pas);
                i.putExtra(freelancer.INTENT_EMAIL,em);
                i.putExtra(freelancer.INTENT_USERNAME,unm);
                i.putExtra(freelancer.INTENT_PASSWORD,pas);
                startActivity(i);
            }
        });
    }
}
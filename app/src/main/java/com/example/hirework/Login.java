package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    TextView signup;
    EditText email_txt, password_txt;

    Switch checkUser;

    Button login;

    SharedPreferences sharedPreferences;

    Customer customer;
    Freelancer freelancer;

    String sEmail;// variable to store email from email textview
    String sPassword; // variable to store password from password textview






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_txt = findViewById(R.id.loginEmail);
        password_txt = findViewById(R.id.loginPassword);
        checkUser = findViewById(R.id.userType);

        // Buttons
        signup = findViewById(R.id.signUpTxt);
        login = findViewById(R.id.loginBtn);

        customer=new Customer(this);
        freelancer=new Freelancer(this);

        sharedPreferences = getSharedPreferences("proj", Context.MODE_PRIVATE);
        customer.CustomerEmail = sharedPreferences.getString(customer.SP_CUSTOMER_EMAIL_KEY, customer.STRING_DEFAULT);

        freelancer.FreelancerEmail= sharedPreferences.getString(freelancer.SP_FREELANCER_EMAIL,freelancer.STRING_DEFAULT);

        if (!customer.CustomerEmail.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else if(!freelancer.FreelancerEmail.isEmpty()){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }


//        signup button will redirect the user to the signup page where he can create a new account in linkup
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });


//        login button to check if users is already registered
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEmail = email_txt.getText().toString().trim();
                sPassword = password_txt.getText().toString().trim();
                if (checkUser.isChecked()) {
                    customer.customerLogin(sEmail,sPassword);
                } else {
                    freelancer.FREELANCER_LOGIN(sEmail,sPassword);
                }
            }
        });
    }
}
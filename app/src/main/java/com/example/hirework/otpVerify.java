package com.example.hirework;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class otpVerify extends AppCompatActivity {

    int code;
    EditText passCode1,passCode2,passCode3,passCode4;

    Button sendOTP,verifyBtn;

    TextView timer;

    TextView msg;
    CountDownTimer mCountDownTimer;

    long duration=60000;


    //String emailC,unm,pas;

    Customer customer;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        customer=new Customer(this);
        user=new User();

        sendOTP=findViewById(R.id.sendOTP);
        verifyBtn=findViewById(R.id.otpVerifyBtn);

        passCode1=findViewById(R.id.d1);
        passCode2=findViewById(R.id.d2);
        passCode3=findViewById(R.id.d3);
        passCode4=findViewById(R.id.d4);

        // TextView
        timer=findViewById(R.id.otpTimer);
        msg=findViewById(R.id.verifyMessage);


        // get values from intent of previous activity
//        emailC=getIntent().getStringExtra(customer.INTENT_EMAIL);
//        unm=getIntent().getStringExtra(customer.INTENT_USERNAME);
//        pas=getIntent().getStringExtra(customer.INTENT_PASSWORD);

        user.USER_EMAIL=getIntent().getStringExtra(user.INTENT_EMAIL);
        user.USER_USERNAME=getIntent().getStringExtra(user.INTENT_USERNAME);
        user.USER_PASSWORD=getIntent().getStringExtra(user.INTENT_PASSWORD);

        msg.setText("Please enter the verification code sent on your email "+user.USER_EMAIL);

        sendVerifyEmail();


        passCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    passCode2.requestFocus();
                }
            }
        });

        passCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    passCode3.requestFocus();
                }
            }
        });

        passCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    passCode4.requestFocus();
                }
            }
        });


        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerifyEmail();
                sendOTP.setVisibility(View.INVISIBLE);
                passCode1.setBackgroundResource(R.drawable.txt_bg);
                passCode2.setBackgroundResource(R.drawable.txt_bg);
                passCode3.setBackgroundResource(R.drawable.txt_bg);
                passCode4.setBackgroundResource(R.drawable.txt_bg);
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkCode();
                startAct();
                //startActivity(new Intent(getApplicationContext(),Account.class));
            }
        });

        passCode4.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    public void sendVerifyEmail(){
        Random random=new Random();
        code=random.nextInt(8999)+1000;

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.SEND_OTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(otpVerify.this, response, Toast.LENGTH_SHORT).show();

                // Start timer to send code Again
                startTimer();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(otpVerify.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",user.USER_EMAIL);
                params.put("code",String.valueOf(code));
                return params;
            }
        };

        requestQueue.add(stringRequest);


    }


    public void checkCode(){
        String inputCode;
        inputCode=passCode1.getText().toString()+passCode2.getText().toString()+passCode3.getText().toString()+passCode4.getText().toString();
        if(inputCode.equals(String.valueOf(code))){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(otpVerify.this,Account.class);
            i.putExtra(user.INTENT_EMAIL,user.USER_EMAIL);
            i.putExtra(user.INTENT_USERNAME,user.USER_USERNAME);
            i.putExtra(user.INTENT_PASSWORD,user.USER_PASSWORD);
            startActivity(i);
        }

        else{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            passCode1.setBackgroundResource(R.drawable.invalid_data);
            passCode2.setBackgroundResource(R.drawable.invalid_data);
            passCode3.setBackgroundResource(R.drawable.invalid_data);
            passCode4.setBackgroundResource(R.drawable.invalid_data);
        }
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            @Override
            public void onFinish() {
                timer.setText("00:00");
                sendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    // to be deleted
    private void startAct(){
        Intent i=new Intent(otpVerify.this,Account.class);
        i.putExtra(user.INTENT_EMAIL,user.USER_EMAIL);
        i.putExtra(user.INTENT_USERNAME,user.USER_USERNAME);
        i.putExtra(user.INTENT_PASSWORD,user.USER_PASSWORD);
        startActivity(i);
    }
}
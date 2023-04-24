package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Withdraw extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private Button mWithdrawButton;
    Button linkAccountBtn;

    SharedPreferences sp;
    String freelancer_username;

    Freelancer freelancer;
    String temp;
    String completed;
    String username;
    String balance_amount;

    TextView balance;

    private static final String FREELANCER_ACTIVE_ORDERS_URL="https://hirework.tech/fActiveOrders.php?fUsername=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        freelancer=new Freelancer(this);
        balance=findViewById(R.id.freelancerBalance);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        //freelancer_username=sp.getString("username_f","");
        freelancer_username=sp.getString(freelancer.SP_FREELANCER_USERNAME,"");

        mProgressBar = findViewById(R.id.progress_bar);
        mWithdrawButton = findViewById(R.id.withdraw_button);
        linkAccountBtn=findViewById(R.id.linkAccountBtn);
        getData();
        mProgressBar.setMax(500);
        String temp=balance.getText().toString();
        //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();

        if(Integer.valueOf(temp)>=500){
            Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
        }



        if (mProgressBar.getProgress() >= mProgressBar.getMax()) {
            mWithdrawButton.setEnabled(true);
        } else {
            mWithdrawButton.setEnabled(false);
        }

        linkAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BankAccount.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),FreelancerSettings.class));
    }


    public void getData(){
        String url="https://hirework.tech/getData.php?fUsername="+freelancer_username;
        // String url="https://hirework.tech/getData.php?fUsername=fatima";
        StringRequest stringRequest=new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showJSON(response);
                        //activeOrders.setText(String.valueOf(z));
                        //progressDialog.dismiss();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast=Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray result=jsonObject.getJSONArray("result");
            JSONObject data=result.getJSONObject(0);
            // data column name
            temp =data.getString("active");
            completed=data.getString("completed");
            balance_amount=data.getString("balance");
            if(Integer.parseInt(balance_amount)>=500){
                //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this, balance_amount, Toast.LENGTH_SHORT).show();
            // row number
        } catch (JSONException e){
            e.printStackTrace();
        }
       // completedOrders.setText(String.valueOf(completed));
        balance.setText(String.valueOf(balance_amount));
        mProgressBar.setProgress(Integer.parseInt(balance_amount));
        //return temp;
//        if(Integer.valueOf(balance_amount)>=500){
//            Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
//        }

    }
}
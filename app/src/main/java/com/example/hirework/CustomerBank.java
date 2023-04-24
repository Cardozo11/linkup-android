package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class CustomerBank extends AppCompatActivity {
    OrderModel order;
    Button btn;

    TextView holderName,accNumber,bankName,bankCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bank);
        order=new OrderModel(this);
        btn=findViewById(R.id.continueBtn2);
        holderName=findViewById(R.id.holderName2);
        accNumber=findViewById(R.id.accNumber2);
        bankName=findViewById(R.id.bankName2);
        bankCode=findViewById(R.id.bankCode2);




        order.orderId=getIntent().getIntExtra(order.INTENT_ORDER_ID,0);
        order.freelancerUsername=getIntent().getStringExtra(order.INTENT_ORDER_FREELANCER);
        order.orderAmount=getIntent().getIntExtra(order.INTENT_ORDER_PRICE,0);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean v= validate();
                if(v){
                    cancelOrder();
                }
            }
        });

    }

    private boolean validate(){
        if(holderName.getText().toString().equals("") || accNumber.getText().toString().equals("") || bankName.getText().toString().equals("") || bankCode.getText().toString().equals("")){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    private void cancelOrder(){
        String temp=String.valueOf(order.orderId);
        String UPDATE_CANCELED_ORDER_URL="https://hirework.tech/updateCanceled.php";
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_CANCELED_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            startActivity(new Intent(getApplicationContext(),paymentWithdraw.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(PlaceOrder2.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error.getMessage()!=null){
                    Toast toast=Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("oId", temp);
                params.put("fUsername", order.freelancerUsername);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CustomerBank.this);
        requestQueue.add(request);
    }
}
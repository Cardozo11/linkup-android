package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Customer {

    String CustomerEmail;
    String CustomerName;
    String CustomerUsername;
    int CustomerPhone;
    public String CUSTOMER_PHOTO;


    // customer intent keys
    public final String INTENT_EMAIL="em";
    public final String INTENT_USERNAME="unm";
    public final String INTENT_PASSWORD="pas";

    SharedPreferences sp;

    //SharedPreferences customer keys
    public final String SP_CUSTOMER_EMAIL_KEY="email";
    public final String SP_CUSTOMER_USERNAME_KEY="username";
    public final String SP_CUSTOMER_NAME_KEY="name";
    public final String STRING_DEFAULT="";
    public final String SP_PHONE_NUMBER="phone";
    public final String SP_CUSTOMER_PROFILE_PHOTO="CUSTOMER_PROFILE_PIC";



    String KEY_CUSTOMER_USERNAME = "cUsername ";
    private static final String JSON_ARRAY = "result";

    // Customer Database Keys
    public final String CUSTOMER_EMAIL_DB_KEY="cEmail";
    public final String CUSTOMER_USERNAME_DB_KEY="cUsername";
    public final String CUSTOMER_PASSWORD_DB_KEY="cPassword";
    public final String CUSTOMER_NAME_DB_KEY="cName";
    public final String CUSTOMER_PHONE_DB_KEY="cPhone";


    private static final String LOGIN_URL = "https://hirework.tech/login.php";
    private static final String LOGIN_DATA_URL = "https://hirework.tech/loginData.php?cEmail=";



    final Context context;

    public Customer(Context context) {
        this.context=context;
        sp=context.getSharedPreferences("proj",0);
    }

    public Customer(String customerEmail, String customerName, String customerUsername, int customerPhone, Context context) {
        CustomerEmail = customerEmail;
        CustomerName = customerName;
        CustomerUsername = customerUsername;
        CustomerPhone = customerPhone;
        this.context = context;
    }

    public void customerLogin(String sEmail, String sPassword){
        if (sEmail.equals("")) {
            Toast.makeText(context, "Enter Email to Proceed", Toast.LENGTH_SHORT).show();
        }
        else if (sPassword.equals("")) {
            Toast.makeText(context, "Enter Password to Proceed", Toast.LENGTH_SHORT).show();
        }
        else {
            StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    if (response.trim().equals("Login successfully")) {
                        sp.edit().putString(SP_CUSTOMER_EMAIL_KEY, sEmail).apply();
                        loadCustomerDetails(sEmail);
                        context.startActivity(new Intent(context,MainActivity.class));
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //progressDialog.dismiss();
                    Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(CUSTOMER_EMAIL_DB_KEY, sEmail);
                    params.put(CUSTOMER_PASSWORD_DB_KEY, sPassword);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        }

    }

    public void loadCustomerDetails(String sEmail){
        String urll = LOGIN_DATA_URL + sEmail;
        StringRequest stringRequest = new StringRequest(urll,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showJSON(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            CustomerUsername = data.getString(KEY_CUSTOMER_USERNAME);
            CustomerName=data.getString(CUSTOMER_NAME_DB_KEY);
            CustomerPhone=data.getInt(CUSTOMER_PHONE_DB_KEY);


        } catch (JSONException e) {
            Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        sp.edit().putString(SP_CUSTOMER_USERNAME_KEY, CustomerUsername).apply();
        sp.edit().putString(SP_CUSTOMER_NAME_KEY, CustomerName).apply();
        String temp=String.valueOf(CustomerPhone);
        sp.edit().putString(SP_PHONE_NUMBER, temp).apply();


    }
}

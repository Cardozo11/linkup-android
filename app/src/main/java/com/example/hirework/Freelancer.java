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

public class Freelancer {

    String FreelancerEmail,FreelancerUsername;
    String FreelancerName,FreelancerQualification,FreelancerExperience,Institute;
    int FreelancerPhone;
    int activeOrders,completedOrders,canceledOrders;
    final Context context;
    public String FREELANCER_PHOTO;



    // freelancer intent
    public final String INTENT_USERNAME="fuser";
    public final String INTENT_EMAIL="FREELANCER_EMAIL";
    public final String INTENT_PHONE="FREELANCER_PHONE";
    public final String INTENT_PASSWORD="FREELANCER_PASSWORD";


    SharedPreferences sp;

    //SharedPreferences Freelancer keys
    public final String SP_FREELANCER_EMAIL="email_f";
    public final String STRING_DEFAULT="";
    public final String SP_FREELANCER_USERNAME="username_f";
    public final String SP_FREELANCER_NAME="name_f";
    public final String SP_FREELANCER_PHONE="FREELANCER_PHONE";
    public final String SP_FREELANCER_QUALIFICATION="FREELANCER_QUALIFICATION";
    public final String SP_FREELANCER_EXP="FREELANCER_EXP";
    public final String SP_FREELANCER_PROFILE_PHOTO="FREELANCER_PROFILE_PIC";

    private static final String LOGIN_FREELANCER_URL = "https://hirework.tech/loginFreelancer.php";
    private static final String FREELANCER_LOGIN_DATA_URL = "https://hirework.tech/freelancerLoginData.php?fEmail=";


    private static final String JSON_ARRAY = "result"; // JSON Array Key
    String KEY_FREELANCER_USERNAME="fUsername "; //freelancer username key


    public Freelancer(Context context){
        this.context=context;
        sp=context.getSharedPreferences("proj",0);
    }

    public void FREELANCER_LOGIN(String sEmail,String sPassword){
        if (sEmail.equals("")) {
            Toast.makeText(context, "Enter Email to Proceed", Toast.LENGTH_SHORT).show();
        } else if (sPassword.equals("")) {
            Toast.makeText(context, "Enter Password to Proceed", Toast.LENGTH_SHORT).show();
        } else {
//            sEmail = email_txt.getText().toString().trim();
//            sPassword = password_txt.getText().toString().trim();


            StringRequest request = new StringRequest(Request.Method.POST, LOGIN_FREELANCER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    if (response.trim().equals("Login successfully")) {
                        sp.edit().putString(SP_FREELANCER_EMAIL, sEmail).apply();
                       loadFreelancerData(sEmail);
                        context.startActivity(new Intent(context, Dashboard.class));
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
                    params.put("fEmail", sEmail);
                    params.put("fPassword", sPassword);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        }
    }

    private void loadFreelancerData(String sEmail){
        String urll = FREELANCER_LOGIN_DATA_URL + sEmail;
        StringRequest stringRequest = new StringRequest(urll,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showsJSON(response);

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
    private void showsJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            FreelancerUsername = data.getString(KEY_FREELANCER_USERNAME);
            FreelancerName = data.getString("fName ");
            FreelancerPhone=data.getInt("fPhone ");
            FreelancerQualification = data.getString("qualification");
            FreelancerExperience = data.getString("exp");

        } catch (JSONException e) {
            Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        sp.edit().putString(SP_FREELANCER_USERNAME, FreelancerUsername).apply();
        sp.edit().putString(SP_FREELANCER_NAME,FreelancerName).apply();
        String temp=String.valueOf(FreelancerPhone);
        sp.edit().putString(SP_FREELANCER_PHONE,temp).apply();
        sp.edit().putString(SP_FREELANCER_QUALIFICATION,FreelancerQualification).apply();
        sp.edit().putString(SP_FREELANCER_EXP,FreelancerExperience).apply();
    }
}

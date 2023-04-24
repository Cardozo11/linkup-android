package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class CreateFreelancerProfile2 extends AppCompatActivity {

    String email;

    SharedPreferences sp;

    Button nextBtn;
    AutoCompleteTextView inst;
    Spinner qSpinner;
    EditText exp;

    Freelancer freelancer;

    public static final  String FREELANCER_QUALIFICATION_URL="https://hirework.tech/freelancer2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_freelancer_profile2);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        freelancer=new Freelancer(this);

        //email=sp.getString("email_f","");
        email=sp.getString(freelancer.SP_FREELANCER_EMAIL,"");

        nextBtn=findViewById(R.id.fpNextBtn2);
        inst=findViewById(R.id.txtInstitute);
        qSpinner=findViewById(R.id.qSpinner);
        exp=findViewById(R.id.exp);

        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.qualification_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        qSpinner.setAdapter(adapter);


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.institution_list));

        inst.setAdapter(arrayAdapter);
        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                inst.showDropDown();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate(){
        String q=qSpinner.getSelectedItem().toString().trim();
        String i=inst.getText().toString().trim();
        String xp=exp.getText().toString().trim();

        if(q.isEmpty() || i.isEmpty() || xp.isEmpty()){
            Toast.makeText(this, "Please fill All the Details", Toast.LENGTH_SHORT).show();

        }

        else{
            uploadData(q,i,xp);
        }

    }

    private void uploadData(String q,String i,String xp){

        StringRequest request = new StringRequest(Request.Method.POST, FREELANCER_QUALIFICATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //if(response.equals("updated Successful")) {
                    Toast.makeText(CreateFreelancerProfile2.this, response, Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),CreateFreelancerProfile3.class);
                    sp.edit().putString(freelancer.SP_FREELANCER_QUALIFICATION,q).apply();
                    sp.edit().putString(freelancer.SP_FREELANCER_EXP,xp).apply();
                    startActivity(i);
               // }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateFreelancerProfile2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fEmail",email);
                params.put("qualification", q);
                params.put("institute",i);
                params.put("exp", xp);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CreateFreelancerProfile2.this);
        requestQueue.add(request);

    }
}
package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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

public class SignUp extends AppCompatActivity {

    EditText emailEd,passwordEd,usernameEd,confirmPasswordEd;
    TextView  termsTv;
    Button signUpBtn;
    CheckBox termsCheckbox;

    boolean res;
    SharedPreferences sharedPreferences;

    Customer customer;
    User user;


//    private static final String VALIDATE_USER="https://hirework.tech/validateUser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        customer=new Customer(this);

        sharedPreferences=getSharedPreferences("proj", Context.MODE_PRIVATE);

        // Edit text
        emailEd=findViewById(R.id.signUpEmail);
        passwordEd=findViewById(R.id.signUpPassword);
        usernameEd=findViewById(R.id.signUpUsername);
        confirmPasswordEd=findViewById(R.id.confirmPassword);

        // TextView
        termsTv=findViewById(R.id.termsLinker);



        // CheckBox
        termsCheckbox=findViewById(R.id.termsCheckbox);

        // Button

        signUpBtn=findViewById(R.id.signUpBtn);

        user=new User();

        SpannableString spannableString = new SpannableString(termsTv.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://a1freelance.blogspot.com/p/terms-and-conditions.html";
                Intent intent = new Intent(SignUp.this, TermsAndConditions.class);
                intent.putExtra(TermsAndConditions.EXTRA_URL, url);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK); // set the color of the ClickableSpan text
            }
        };
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTv.setText(spannableString);
        termsTv.setMovementMethod(LinkMovementMethod.getInstance());



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });



        confirmPasswordEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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

    public boolean  validate(){
        String password=passwordEd.getText().toString().trim();
        String confirmPassword=confirmPasswordEd.getText().toString().trim();
        if(emailEd.getText().toString().equals("") || passwordEd.getText().toString().equals("") || usernameEd.getText().toString().equals("")){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(!confirmPassword.equals(password)){
            Toast.makeText(this, "Password Does Not Match !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!termsCheckbox.isChecked()){
            Toast.makeText(this, "Please Check the Terms CheckBox", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!checkAvailability()){
            return false;
        }

        else{
            return true;
        }
    }

    private void usernameAvailable(final VolleyCallback callback){
        String sEmail=emailEd.getText().toString().toLowerCase().trim();
        String sUsername=usernameEd.getText().toString().toLowerCase().trim();

        StringRequest request = new StringRequest(Request.Method.POST, Urls.VALIDATE_USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("cEmail",sEmail);
                params.put("cUsername",sUsername);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(SignUp.this);
        requestQueue.add(request);


    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }


    private boolean checkAvailability(){

        usernameAvailable(new VolleyCallback(){
            @Override
            public void onSuccess(String response){
                if(response.equals("Email")){
                    emailEd.setBackgroundResource(R.drawable.invalid_data);
                    usernameEd.setBackgroundResource(R.drawable.txt_bg);
                }
                else if(response.equals("Username")){
                    emailEd.setBackgroundResource(R.drawable.txt_bg);
                    usernameEd.setBackgroundResource(R.drawable.invalid_data);
                }
                else if(response.equals("Available")){
                    Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                    //String em=emailEd.getText().toString().toLowerCase().trim();
//                    String unm=usernameEd.getText().toString().toLowerCase().trim();
//                    String pas=passwordEd.getText().toString().trim();
                    user.USER_EMAIL=emailEd.getText().toString().toLowerCase().trim();
                    user.USER_USERNAME=usernameEd.getText().toString().toLowerCase().trim();
                    user.USER_PASSWORD=passwordEd.getText().toString().trim();


                    Intent i=new Intent(SignUp.this,otpVerify.class);
//                    i.putExtra(customer.INTENT_EMAIL,em);
//                    i.putExtra(customer.INTENT_USERNAME,unm);
//                    i.putExtra(customer.INTENT_PASSWORD,pas);
                    i.putExtra(user.INTENT_EMAIL,user.USER_EMAIL);
                    i.putExtra(user.INTENT_USERNAME,user.USER_USERNAME);
                    i.putExtra(user.INTENT_PASSWORD,user.USER_PASSWORD);

                    startActivity(i);
                }

            }
        });
        return res;

    }
}
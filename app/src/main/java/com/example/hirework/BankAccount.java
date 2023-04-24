package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BankAccount extends AppCompatActivity {

    Button editBtn,saveBtn;
    Drawable mImage,mImage2;
    EditText holderName,accNo,bankName,bankCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);

        editBtn=findViewById(R.id.editBtn);
        saveBtn=findViewById(R.id.saveBtn);
        holderName=findViewById(R.id.holderName);
        accNo=findViewById(R.id.accNumber);
        bankName=findViewById(R.id.bankName);
        bankCode=findViewById(R.id.bankCode);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImage=getDrawable(R.drawable.ic_edit);
            mImage2=getDrawable(R.drawable.ic_save);
            mImage.setTint(Color.WHITE);
            mImage2.setTint(Color.WHITE);
            editBtn.setCompoundDrawablesWithIntrinsicBounds(mImage, null, null, null);
            saveBtn.setCompoundDrawablesWithIntrinsicBounds(mImage2, null, null, null);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holderName.setEnabled(true);
                accNo.setEnabled(true);
                bankName.setEnabled(true);
                bankCode.setEnabled(true);

                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

            }
        });


    }

    public void saveChanges(View view){
                        holderName.setEnabled(false);
                accNo.setEnabled(false);
                bankName.setEnabled(false);
                bankCode.setEnabled(false);
                saveBtn.setVisibility(View.GONE);
                editBtn.setVisibility(View.VISIBLE);
    }

    public void goBack(View view){
        startActivity(new Intent(getApplicationContext(),Withdraw.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Withdraw.class));
    }
}
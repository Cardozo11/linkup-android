package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CancelOrder extends AppCompatActivity {

    TextView id;
    Button cancel;
    Spinner spinner;

    OrderModel order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        order=new OrderModel(this);
        id=findViewById(R.id.cancelId);
        cancel=findViewById(R.id.cancelBtn);
        spinner=findViewById(R.id.CancelSpinner);

        order.orderId=getIntent().getIntExtra(order.INTENT_ORDER_ID,0);
        order.freelancerUsername=getIntent().getStringExtra(order.INTENT_ORDER_FREELANCER);
        order.orderAmount=getIntent().getIntExtra(order.INTENT_ORDER_PRICE,0);

        id.setText("Order ID : "+String.valueOf(order.orderId));

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.cancel_reason, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();

            }
        });

    }

    private void form(){
        Intent i=new Intent(getApplicationContext(),CustomerBank.class);
        i.putExtra(order.INTENT_ORDER_ID,order.orderId);
        i.putExtra(order.INTENT_ORDER_FREELANCER,order.freelancerUsername);
        i.putExtra(order.INTENT_ORDER_PRICE,order.orderAmount);
        startActivity(i);
    }
}
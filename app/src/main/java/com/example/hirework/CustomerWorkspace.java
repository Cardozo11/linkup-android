package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.HashMap;
import java.util.Map;

public class CustomerWorkspace extends AppCompatActivity {

    String order_description,order_status,order_customer,order_freelancer,order_tracking_status,order_title;
    int gig_id,order_price;


    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Accepted","In Development", "Submitted","Completed"};

    TextView ord_title2,des2, orderDays_txt, fileFormat_txt, price_txt,attach2,wsFreelancerUsername;
    ImageView more;

    Button completeBtn;


    OrderModel order;
    Freelancer freelancer;

    Button viewBtn,downloadBtn;

    TextView rating_txt,orderId_txt;

    String INTENT_NAME="url";

    private static final  String FREELANCER_UPDATE_TRACKING_URL="https://hirework.tech/WorkStarted.php";
    final String TERMS_AND_CONDITIONS_URL="https://a1freelance.blogspot.com/p/terms-and-conditions.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_workspace);

        viewBtn=findViewById(R.id.viewFile);
        downloadBtn=findViewById(R.id.downloadFile);
        stateProgressBar =findViewById(R.id.order_tracking2);
        stateProgressBar.setStateDescriptionData(descriptionData);
        more=findViewById(R.id.moreOptions);

        completeBtn=findViewById(R.id.markCompletedBtn);
        freelancer=new Freelancer(this);

        rating_txt=findViewById(R.id.rating);
        orderId_txt=findViewById(R.id.orderId);

        ord_title2=findViewById(R.id.ord_title2);
        des2=findViewById(R.id.des2);
        orderDays_txt =findViewById(R.id.gCompletionDays2);
        fileFormat_txt =findViewById(R.id.gFileFormat2);
        price_txt =findViewById(R.id.ord_price2);
        //attach2=findViewById(R.id.attach2);
        wsFreelancerUsername=findViewById(R.id.wsFreelancerUsername);

        order=new OrderModel(this);



        order.orderId=getIntent().getIntExtra("order_id",0);
        gig_id=getIntent().getIntExtra("order_gig_id",0);
        order_price=getIntent().getIntExtra("order_price",0);

        order_description=getIntent().getStringExtra("order_desc");
        order_status=getIntent().getStringExtra("order_status");
        order_customer=getIntent().getStringExtra("order_customer");
        order_freelancer=getIntent().getStringExtra("order_freelancer");
        order_tracking_status=getIntent().getStringExtra("order_Tracking_status");
        order_title=getIntent().getStringExtra("order_title");


        order.completionDays=getIntent().getStringExtra("order_days");
        order.fileForamt=getIntent().getStringExtra("order_file_format");
        order.additionalInfo=getIntent().getStringExtra("order_Additional");

        orderId_txt.setText("Order ID : #"+String.valueOf(order.orderId));
        ord_title2.setText(order_title);
        des2.setText(order_description);
        orderDays_txt.setText(order.completionDays+" Days");
        fileFormat_txt.setText(order.fileForamt);
        price_txt.setText("₹ "+String.valueOf(order_price));
        //gCompletionDays2.setText(String.valueOf(orde));
        wsFreelancerUsername.setText(order_freelancer);



        checkStatus();

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteOrder("Completed");
                UPDATE_COMPLETED_ORDERS();
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),viewPDF.class);
                intent.putExtra("order_id",order.orderId);
                intent.putExtra("purpose","view");
                startActivity(intent);
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),viewPDF.class);
                intent.putExtra("order_id",order.orderId);
                intent.putExtra("purpose","download");
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CustomerWorkspace.this, more);
                popupMenu.getMenuInflater().inflate(R.menu.help_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.terms:
                                openTerms();
                                break;
                            case R.id.cancel:
                                cancelOrder();
                                break;
                            default:
                                //return super.onOptionsItemSelected(item);
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void checkStatus(){
        if (order_tracking_status.equals("In Development")){
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("TWO"));
        }

        else if(order_tracking_status.equals("Submitted")){
            completeBtn.setVisibility(View.VISIBLE);
            viewBtn.setVisibility(View.VISIBLE);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("THREE"));
        }

        else if(order_tracking_status.equals("Completed")){
            completeBtn.setVisibility(View.GONE);
            downloadBtn.setVisibility(View.VISIBLE);
            more.setVisibility(View.GONE);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("FOUR"));

        }
    }


    private void CompleteOrder(String TrackingStatus){
        String orderId=String.valueOf(order.orderId);
        StringRequest request = new StringRequest(Request.Method.POST,FREELANCER_UPDATE_TRACKING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Updated Successfully")) {
                    Toast.makeText(CustomerWorkspace.this, response, Toast.LENGTH_SHORT).show();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("FOUR"));
                    completeBtn.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),CustomerOrderStats.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerWorkspace.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("oId",orderId);
                params.put("WorkStatus", TrackingStatus);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CustomerWorkspace.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),CustomerOrderStats.class));
    }

    private void UPDATE_COMPLETED_ORDERS(){
        String price=String.valueOf(order_price);
       String UPDATE_COMPLETED_ORDER_URL="https://hirework.tech/updateCompleted.php";
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_COMPLETED_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                params.put("fUsername", order_freelancer);
                params.put("balance",price);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CustomerWorkspace.this);
        requestQueue.add(request);
    }

    private void openTerms(){
        Intent i=new Intent(getApplicationContext(),TermsAndConditions.class);
        i.putExtra(INTENT_NAME,TERMS_AND_CONDITIONS_URL);
        startActivity(i);
    }

    private void cancelOrder(){
    Intent i=new Intent(getApplicationContext(),CancelOrder.class);
    i.putExtra(order.INTENT_ORDER_ID,order.orderId);
    i.putExtra(order.INTENT_ORDER_FREELANCER,order_freelancer);
    i.putExtra(order.INTENT_ORDER_PRICE,order_price);
    startActivity(i);
    }
}
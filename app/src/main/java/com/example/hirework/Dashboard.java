package com.example.hirework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    RecyclerView activeOrderRecycler;
    ImageView moreOrders;
    TextView activeOrders,completedOrders,balance;

    ActiveOrderAdapter adapter;
    List<OrderModel> orderList;
    RecyclerView.LayoutManager RecyclerViewLayoutManager2;
    LinearLayoutManager HorizontalLayout2;

    SharedPreferences sp;
    String freelancer_username;

    Freelancer freelancer;
    String temp;
    String completed;
    String username;
    String balance_amount;



    private static final String FREELANCER_ACTIVE_ORDERS_URL="https://hirework.tech/fActiveOrders.php?fUsername=";

    private static final String FREELANCER_ORDER_HISTORY="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        freelancer=new Freelancer(this);

        moreOrders=findViewById(R.id.ord);
        activeOrders=findViewById(R.id.orderActive);
        completedOrders=findViewById(R.id.orderCompleted);
        balance=findViewById(R.id.balance);



//
//
//
//
        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        //freelancer_username=sp.getString("username_f","");
        freelancer_username=sp.getString(freelancer.SP_FREELANCER_USERNAME,"");




        activeOrderRecycler=findViewById(R.id.freelancerDashboardOrders);
        orderList = new ArrayList<>();
        RecyclerViewLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        HorizontalLayout2 = new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false);
        activeOrderRecycler.setLayoutManager(RecyclerViewLayoutManager2);
        activeOrderRecycler.setLayoutManager(HorizontalLayout2);

        getData();




        loadActiveGigs();

        moreOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FreelancerOrders.class));
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setSelectedItemId(R.id.DashboardMenu);//home
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.DashboardMenu:
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.gigs:
                        startActivity(new Intent(getApplicationContext(),Gigs.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.fProfile:
                        startActivity(new Intent(getApplicationContext(),freelancerProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadActiveGigs(){
        String url=FREELANCER_ACTIVE_ORDERS_URL+freelancer_username;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray orders=new JSONArray(response);

                            for(int i=0;i<orders.length();i++){
                                JSONObject orderObject= orders.getJSONObject(i);

                                int id=orderObject.getInt("oId");
                                String description=orderObject.getString("oDescription");
                                String orderStatus=orderObject.getString("oPayStatus");
                                String cUsername=orderObject.getString("cUsername");
                                String fUsername=orderObject.getString("fUsername");
                                int gId=orderObject.getInt("gigId");
                                int price= orderObject.getInt("orderPrice");
                                String orderTrackingStatus=orderObject.getString("WorkStatus");
                                String orderTitle=orderObject.getString("title");
                                String  completionDays=orderObject.getString("completionDays");
                                String  fileFormat=orderObject.getString("fileFormat");
                                String  additionalInfo=orderObject.getString("additionalinfo");
                                String orderPlaced=orderObject.getString("oPlacedOn");


                                OrderModel ord=new OrderModel(description,orderStatus,cUsername,fUsername,gId,price,orderTrackingStatus,orderTitle,completionDays,
                                        fileFormat,additionalInfo,orderPlaced,id);
                                orderList.add(ord);

                            }
                            adapter=new ActiveOrderAdapter(getApplicationContext(), orderList);
                            activeOrderRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }


    public void getData(){
       String url="https://hirework.tech/getData.php?fUsername="+freelancer_username;
       // String url="https://hirework.tech/getData.php?fUsername=fatima";
        StringRequest stringRequest=new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String z=showJSON(response);
                        activeOrders.setText(String.valueOf(z));
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

    private String showJSON(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray result=jsonObject.getJSONArray("result");
            JSONObject data=result.getJSONObject(0);
            // data column name
            temp =data.getString("active");
            completed=data.getString("completed");
            balance_amount=data.getString("balance");
            // row number
        } catch (JSONException e){
            e.printStackTrace();
        }
        completedOrders.setText(String.valueOf(completed));
        balance.setText("₹"+String.valueOf(balance_amount));
        return temp;

    }
}
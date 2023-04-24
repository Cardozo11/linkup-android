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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderStats extends AppCompatActivity {

    RecyclerView activeOrderRecycler;
    ActiveOrderAdapter2 adapter;
    List<OrderModel> orderList;
    RecyclerView.LayoutManager RecyclerViewLayoutManager2;
    LinearLayoutManager HorizontalLayout2;


    List<OrderModel> orderList2;
    RecyclerView previousOrderRecycler;
    PreviousOrdersAdapter adapter2;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    SharedPreferences sp;
    String customer_username;

    ImageView more;

    private static final String CUSTOMER_ACTIVE_ORDERS_URL="https://hirework.tech/cActiveOrders.php?cUsername=";
    private static final String CUSTOMER_PREVIOUS_ORDERS_URL="https://hirework.tech/cPreviousOrders.php?cUsername=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_stats);

        more=findViewById(R.id.moreOrders);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        customer_username=sp.getString("username","");

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        activeOrderRecycler=findViewById(R.id.customerCurrentOrders);
        previousOrderRecycler=findViewById(R.id.previous_recycler);

        orderList = new ArrayList<>();
        RecyclerViewLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        HorizontalLayout2 = new LinearLayoutManager(CustomerOrderStats.this, LinearLayoutManager.HORIZONTAL, false);
        activeOrderRecycler.setLayoutManager(RecyclerViewLayoutManager2);
        activeOrderRecycler.setLayoutManager(HorizontalLayout2);



        orderList2 = new ArrayList<>();
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        HorizontalLayout = new LinearLayoutManager(CustomerOrderStats.this, LinearLayoutManager.HORIZONTAL, false);
        previousOrderRecycler.setLayoutManager(RecyclerViewLayoutManager);
        previousOrderRecycler.setLayoutManager(HorizontalLayout);


       loadCustomerCurrentOrders();

        loadCustomerPreviousOrder();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),CustomerOrders.class));
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.orderMenu);



        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.HomeMenu:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,R.anim.slide_out_left);
                        return true;
                    case R.id.orderMenu:
                        startActivity(new Intent(getApplicationContext(),CustomerOrderStats.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profileMenu:
                        startActivity(new Intent(getApplicationContext(),CustomerProfile.class));
                        overridePendingTransition(0,R.anim.slide_out_right);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadCustomerCurrentOrders(){
        String url=CUSTOMER_ACTIVE_ORDERS_URL+customer_username;
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
                                String gigTitle=orderObject.getString("title");
                                String  completionDays=orderObject.getString("completionDays");
                                String  fileFormat=orderObject.getString("fileFormat");
                                String  additionalInfo=orderObject.getString("additionalinfo");


                                OrderModel ord=new OrderModel(id,description,orderStatus,cUsername,fUsername,gId,price,orderTrackingStatus,gigTitle,completionDays,
                                        fileFormat,additionalInfo);
                                orderList.add(ord);

                            }
                            adapter=new ActiveOrderAdapter2(getApplicationContext(), orderList);
                            activeOrderRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerOrderStats.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadCustomerPreviousOrder(){
        String url=CUSTOMER_PREVIOUS_ORDERS_URL+customer_username;
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
                                String gigTitle=orderObject.getString("title");
                                String  completionDays=orderObject.getString("completionDays");
                                String  fileFormat=orderObject.getString("fileFormat");
                                String  additionalInfo=orderObject.getString("additionalinfo");


                                OrderModel ord=new OrderModel(id,description,orderStatus,cUsername,fUsername,gId,price,orderTrackingStatus,gigTitle,completionDays,
                                        fileFormat,additionalInfo);
                                orderList2.add(ord);

                            }

                            adapter2=new PreviousOrdersAdapter(getApplicationContext(),orderList2);
                            previousOrderRecycler.setAdapter(adapter2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerOrderStats.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreelancerOrders extends AppCompatActivity {

    RecyclerView recycler;
    OrdersAdapter adapter;
    List<OrderModel> orderList;
    RecyclerView.LayoutManager RecyclerViewLayoutManager2;
    LinearLayoutManager HorizontalLayout2;

    SharedPreferences sp;
    String freelancer_username;

    private final String FREELANCER_ORDERS_URL="https://hirework.tech/freelancerOrders.php?fUsername=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_orders);

        recycler=findViewById(R.id.Recycler);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        freelancer_username=sp.getString("username_f","");
        //Toast.makeText(this, freelancer_username, Toast.LENGTH_SHORT).show();

        orderList = new ArrayList<>();
        RecyclerViewLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        HorizontalLayout2 = new LinearLayoutManager(FreelancerOrders.this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(RecyclerViewLayoutManager2);

        loadOrder();
    }

    private void loadOrder(){
        String url=FREELANCER_ORDERS_URL+freelancer_username;
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
                                String paymentStatus=orderObject.getString("orderPayment");
                                String orderTrackingStatus="none";
                                String gigTitle=orderObject.getString("title");
                                String  completionDays=orderObject.getString("completionDays");
                                String  fileFormat=orderObject.getString("fileFormat");
                                String  additionalInfo=orderObject.getString("additionalinfo");


                                OrderModel ord=new OrderModel(id,description,orderStatus,cUsername,fUsername,gId,price,paymentStatus,orderTrackingStatus,
                                        gigTitle,completionDays,fileFormat,additionalInfo);
                                orderList.add(ord);

                            }
                            adapter=new OrdersAdapter(getApplicationContext(), orderList);
                            recycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FreelancerOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
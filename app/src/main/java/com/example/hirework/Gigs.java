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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Gigs extends AppCompatActivity {

    FloatingActionButton addGig;

    SharedPreferences sp;
    String username;
    String GIGS_LIST_URL;


    RecyclerView gRecycler;

    CustomerGigsAdapter adapter;
    List<GigModel> gigList;
    Freelancer freelancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gigs);
        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        freelancer=new Freelancer(this);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setSelectedItemId(R.id.gigs);//home
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
        username=sp.getString(freelancer.SP_FREELANCER_USERNAME,"");
        GIGS_LIST_URL="https://hirework.tech/gigsList2.php?fuser="+username;

        addGig=findViewById(R.id.addGig);
        addGig.setScaleType(FloatingActionButton.ScaleType.CENTER_INSIDE);

        gRecycler =findViewById(R.id.cGigsRecycler);
        gRecycler.setLayoutManager(new LinearLayoutManager(this));

        gRecycler.setHasFixedSize(true);

        gigList = new ArrayList<>();

        loadGigs();


        addGig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CreateGigs.class));
            }
        });
    }


    private void loadGigs(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, GIGS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray gigs=new JSONArray(response);

                            for(int i=0;i<gigs.length();i++){
                                JSONObject gigObject= gigs.getJSONObject(i);
                                int id=gigObject.getInt("id");
                                String title=gigObject.getString("title");
                                String des=gigObject.getString("description");
                                int price=gigObject.getInt("price");
                                int days=gigObject.getInt("completionDays");
                                String fileFormat=gigObject.getString("fileFormat");
                                String img=gigObject.getString("image1");


                                GigModel gig=new GigModel (id,title,des,price,days,fileFormat,img);
                                gigList.add(gig);

                            }
                            adapter=new CustomerGigsAdapter(getApplicationContext(), gigList);
                            gRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast=Toast.makeText(Gigs.this, String.valueOf(error), Toast.LENGTH_SHORT);
                       toast.show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
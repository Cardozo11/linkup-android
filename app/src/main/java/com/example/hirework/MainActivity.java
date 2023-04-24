package com.example.hirework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url1 = "https://static-cse.canva.com/_next/static/assets/01_featureblock_photo-editor_desktop_w1260xh921_98bdfc1ab604cbafc7385fbbd74fcecc4159d3eca7ffe3a0066b1e995e196398.png";
    String url2 = "https://scitechdaily.com/images/Blue-Moon.jpg";
    String url3 = "https://media.macphun.com/img/uploads/macphun/blog/585/PhotosMac.png";


    private static final String GIGS_LIST_URL="https://hirework.tech/gigLists.php";

    GigAdapter adapter2;
    List<GigModel> gigList;

    ImageView searchBtn;


    RecyclerView recyclerView,dealsRecycler;

    CategoryAdapter adapter;


    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;
    LinearLayoutManager HorizontalLayout2;

    // suspect
    View ChildView;
    int RecyclerViewItemPosition;

//    suspect

    ArrayList<String> source;

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    RecyclerView.LayoutManager RecyclerViewLayoutManager2;

    SharedPreferences sp;
    String username;
    TextView txt_username;

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customer=new Customer(this);
        txt_username=findViewById(R.id.cUnm);

        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        username=sp.getString(customer.SP_CUSTOMER_USERNAME_KEY,customer.STRING_DEFAULT);
        txt_username.setText("Hello "+username);
        //Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        searchBtn=findViewById(R.id.searchIcon);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                Intent i=new Intent(getApplicationContext(),SearchActivity.class);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(i,b);
            }
        });




        recyclerView = (RecyclerView)findViewById(R.id.categoryRecycler);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList();

        adapter = new CategoryAdapter(this,source);
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);

        // end of recycler 1//

        dealsRecycler=findViewById(R.id.dealsRecycler);
        gigList = new ArrayList<>();
        RecyclerViewLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        HorizontalLayout2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        dealsRecycler.setLayoutManager(RecyclerViewLayoutManager2);
        dealsRecycler.setLayoutManager(HorizontalLayout2);



        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.HomeMenu);//home

        //
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = findViewById(R.id.slider);

        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        loadDeals();




        //


        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.HomeMenu:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.orderMenu:
                        startActivity(new Intent(getApplicationContext(),CustomerOrderStats.class));
                        overridePendingTransition(0,R.anim.slide_out_right);
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

    public void AddItemsToRecyclerViewArrayList()
    {
        // Adding items to ArrayList
        source = new ArrayList<>();
        source.add("Audio");
        source.add("Video");
        source.add("Seo");
        source.add("Marketing");
        source.add("Designing");
        source.add("Gaming");
    }



    private void loadDeals(){

        StringRequest stringRequest=new StringRequest(Request.Method.GET, GIGS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray gigs=new JSONArray(response);

                            for(int i=0;i<gigs.length();i++){
                                JSONObject gigObject= gigs.getJSONObject(i);

                                int id=gigObject.getInt("id");
                                String fUsername=gigObject.getString("fuser");
                                String title=gigObject.getString("title");
                                String description=gigObject.getString("description");
                                String image=gigObject.getString("image1");
                                int price= gigObject.getInt("price");
                                int GCD=gigObject.getInt("completionDays");
                                String fileFormat=gigObject.getString("fileFormat");
                                String freelancerProfile=gigObject.getString("fImage");


                                GigModel gig=new GigModel (id,fUsername,title,description,image,price,GCD,fileFormat,freelancerProfile);
                                gigList.add(gig);

                            }
                            adapter2=new GigAdapter(getApplicationContext(), gigList);
                            dealsRecycler.setAdapter(adapter2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);


    }



}
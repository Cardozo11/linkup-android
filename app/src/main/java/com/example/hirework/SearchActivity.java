package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchActivity extends AppCompatActivity {


    public final String SUB_CATEGORY_KEY="sub_category_name";

   private String Sub_Category;

    SearchView searchView;
    RecyclerView searchRecycler;
    GigAdapter adapter;
    List<GigModel> gigList;

    private static final String GIGS_LIST_URL="https://hirework.tech/gigLists.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Sub_Category=getIntent().getStringExtra(SUB_CATEGORY_KEY);

        searchRecycler=findViewById(R.id.searchRecycler);
        searchView = (SearchView) findViewById(R.id.searchBar);
        searchView.clearFocus();

        searchView.setQueryHint("Filter by category");

        if (Sub_Category != null && !Sub_Category.isEmpty()) {
            // Set the SearchView's query to the filter
            searchView.setQuery(Sub_Category, false);
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });



        searchRecycler.setHasFixedSize(true);

        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        gigList = new ArrayList<>();

        loadGigs();
    }

    private void filterList(String text){
        List<GigModel> filteredList=new ArrayList<>();
        for (GigModel gig: gigList){
//            if(gig.getGigTitle().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(gig);
//            }

            if(gig.getGigSubCategory().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(gig);
            }

        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilteredList(filteredList);
        }
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

                                String title=gigObject.getString("title");
                                String freelancer=gigObject.getString("fuser");
                                String image=gigObject.getString("image1");
                                int price= gigObject.getInt("price");
                                int GCD=gigObject.getInt("completionDays");
                                String subCategory=gigObject.getString("subcategory");
                                String freelancer_img=gigObject.getString("fImage");


                                GigModel gig=new GigModel (title,freelancer,image,price,GCD,subCategory,freelancer_img);
                                gigList.add(gig);

                            }
                            adapter=new GigAdapter(getApplicationContext(), gigList);
                            searchRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);


    }
}
package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    TextView category_title;

    // sub cateogry recycler
    RecyclerView subCatRecycler;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    SubCategoryAdapter adapter;
    ArrayList<String> source;

//    sub category reculer end






    public String CATEGORY_NAME_KEY="category_name";
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        category_title=findViewById(R.id.catName);
        subCatRecycler=findViewById(R.id.subCategoryRecycler);

        categoryName=getIntent().getStringExtra(CATEGORY_NAME_KEY);
        category_title.setText(String.valueOf(categoryName));


        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        subCatRecycler.setLayoutManager(RecyclerViewLayoutManager);
        AddItemsToRecyclerViewArrayList();
        adapter = new SubCategoryAdapter(this,source);
        subCatRecycler.setAdapter(adapter);
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
}
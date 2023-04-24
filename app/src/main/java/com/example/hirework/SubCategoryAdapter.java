package com.example.hirework;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyView> {
    private List<String> list;
    private Context mCtx;
    public final String SUB_CATEGORY_KEY="sub_category_name";
    //Activity activity;


    public class MyView
            extends RecyclerView.ViewHolder {

        TextView textView;



        public MyView(View view)
        {
            super(view);


            textView = (TextView)view
                    .findViewById(R.id.subCat);
        }
    }

    public SubCategoryAdapter(Context mCtx,List<String> horizontalList)
    {
        this.mCtx=mCtx;
        this.list = horizontalList;
        //this.activity=activity;
    }

    public MyView onCreateViewHolder(ViewGroup parent,
                                                     int viewType)
    {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_items, parent, false);

        // return itemView
        return new SubCategoryAdapter.MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final SubCategoryAdapter.MyView holder,
                                 final int position)
    {


        holder.textView.setText(list.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String category=holder.textView.getText().toString().trim();
                Intent i=new Intent(mCtx,SearchActivity.class);
                i.putExtra(SUB_CATEGORY_KEY,category);
                //Bundle b=ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                mCtx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}

package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyView> {
    private List<String> list;
    private Context mCtx;

public class MyView
        extends RecyclerView.ViewHolder {

    TextView textView;



    public MyView(View view)
    {
        super(view);


        textView = (TextView)view
                .findViewById(R.id.textview);
    }
}


    public CategoryAdapter(Context mCtx,List<String> horizontalList)
    {
        this.mCtx=mCtx;
        this.list = horizontalList;
    }


    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        // return itemView
        return new MyView(itemView);
    }


    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {


        holder.textView.setText(list.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category=holder.textView.getText().toString().trim();
                Intent i=new Intent(mCtx,Category.class);
                i.putExtra("category_name",category);
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

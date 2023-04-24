package com.example.hirework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyView>{
    private List<String> list;



    public class MyView
            extends RecyclerView.ViewHolder {

        TextView textView;


        public MyView(View view)
        {
            super(view);


            textView = (TextView)view
                    .findViewById(R.id.gig_review);
        }
    }

    public ReviewAdapter(List<String> horizontalList)
    {
        this.list = horizontalList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(MyView holder, int position) {
        holder.textView.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

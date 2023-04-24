package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomerGigsAdapter extends RecyclerView.Adapter<CustomerGigsAdapter.GigViewHolder>{
    //RecyclerView.Adapter<GigAdapter.GigViewHolder>

    private Context mCtx;
    private List<GigModel> gigList;

    public CustomerGigsAdapter(Context mCtx, List<GigModel> gigList) {
        this.mCtx = mCtx;
        this.gigList = gigList;
    }

    
    @Override
    public GigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.gig_item,parent,false);
        GigViewHolder viewHolder=new GigViewHolder(view);
        return viewHolder;
    }

    

    @Override
    public void onBindViewHolder(GigViewHolder holder, int position) {
        //holder.textView.setText(list.get(position));

        GigModel gig= gigList.get(position);
        // newly added line of code
        GigModel temp=gigList.get(position);

        holder.gig_title.setText(gig.getGigTitle());


        holder.gig_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCtx,GigView2.class);
                intent.putExtra(gig.INTENT_GIG_ID,temp.getGigId());
                intent.putExtra(gig.INTENT_GIG_TITLE,temp.getGigTitle());
                intent.putExtra(gig.INTENT_GIG_DESCRIPTION,temp.getGigDescription());
                intent.putExtra(gig.INTENT_GIG_PRICE,temp.getPrice());
                intent.putExtra(gig.INTENT_GIG_COMPLETION_DAYS,temp.getGCD());
                intent.putExtra(gig.INTENT_GIG_FILE_FORMAT,temp.getGigFileFormat());
                intent.putExtra(gig.INTENT_GIG_IMAGE,temp.getImage1());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return gigList.size();
    }

    class GigViewHolder extends RecyclerView.ViewHolder {

        TextView gig_title;

        public GigViewHolder(View itemView) {
            super(itemView);

            gig_title = itemView.findViewById(R.id.cGigTitle);

        }
    }
}

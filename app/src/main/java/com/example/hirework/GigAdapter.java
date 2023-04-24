package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class GigAdapter extends RecyclerView.Adapter<GigAdapter.GigViewHolder>{


    private Context mCtx;
    private List<GigModel> gigList;
    GigModel gig;

    public GigAdapter(Context mCtx, List<GigModel> gigList) {
        this.mCtx = mCtx;
        this.gigList = gigList;
        gig=new GigModel(mCtx);
    }


    public void setFilteredList(List<GigModel> filteredList){
        this.gigList=filteredList;
        notifyDataSetChanged();
    }



    @Override
    public GigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.gig_search_item,parent,false);
        GigViewHolder viewHolder=new GigViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GigViewHolder holder, int position) {

        GigModel gig= gigList.get(position);
        // newly added line of code
        GigModel temp=gigList.get(position);


        holder.gig_fUsername.setText(gig.getfUsername());
        holder.gig_title.setText(gig.getGigTitle());

        holder.gig_price.setText("Price : ₹"+String.valueOf(gig.getPrice()));

        Glide.with(mCtx)
                .load(gig.getImage1())
                .into(holder.gig_img);
        //holder.product_img.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

        holder.gig_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCtx,GigView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(gig.INTENT_GIG_ID,temp.getGigId());
                intent.putExtra(gig.INTENT_GIG_FREELANCER_USERNAME,temp.getfUsername());
                intent.putExtra(gig.INTENT_GIG_TITLE,temp.getGigTitle());
                intent.putExtra(gig.INTENT_GIG_DESCRIPTION,temp.getGigDescription());
                intent.putExtra(gig.INTENT_GIG_PRICE,temp.getPrice());
                intent.putExtra(gig.INTENT_GIG_COMPLETION_DAYS,temp.getGCD());
                intent.putExtra(gig.INTENT_GIG_IMAGE,temp.getImage1());
                intent.putExtra(gig.INTENT_GIG_FILE_FORMAT,temp.getGigFileFormat());
                intent.putExtra(gig.INTENT_GIG_FREELANCER_PROFILE,temp.getFreelancerProfile());
                mCtx.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return gigList.size();
    }




    class GigViewHolder extends RecyclerView.ViewHolder {

        ImageView gig_img;
        TextView gig_fUsername,gig_title,gig_price;

        public GigViewHolder(View itemView) {
            super(itemView);

            gig_fUsername=itemView.findViewById(R.id.funm);
            gig_title = itemView.findViewById(R.id.gig_title);
            gig_price = itemView.findViewById(R.id.gig_price);
            gig_img = itemView.findViewById(R.id.gig_img);

        }
    }
}

package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PreviousOrdersAdapter extends RecyclerView.Adapter<PreviousOrdersAdapter.OrderViewHolder2> {

    private Context mCtx;
    private List<OrderModel> orderList;

    public PreviousOrdersAdapter(Context mCtx, List<OrderModel> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history,parent,false);
        OrderViewHolder2 viewHolder=new OrderViewHolder2(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder2 holder, int position) {

        OrderModel ord= orderList.get(position);
        // newly added line of code
        OrderModel temp=orderList.get(position);

        //holder.ord_title.setText(ord.getCustomerUsername());
        holder.ord_title.setText(ord.getGigOrderTitle());

        holder.previous_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCtx,CustomerWorkspace.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("order_id",temp.getOrderId());
                intent.putExtra("order_desc",temp.getDescription());
                intent.putExtra("order_status",temp.getOrderStatus());
                intent.putExtra("order_customer",temp.getCustomerUsername());
                intent.putExtra("order_freelancer",temp.getFreelancerUsername());
                intent.putExtra("order_gig_id",temp.getGigId());
                intent.putExtra("order_price",temp.getOrderAmount());
                intent.putExtra("order_Tracking_status",temp.getOrderTrackingStatus());
                intent.putExtra("order_title",temp.getGigOrderTitle());
                intent.putExtra("order_days",temp.getCompletionDays());
                intent.putExtra("order_file_format",temp.getFileForamt());
                intent.putExtra("order_Additional",temp.getAdditionalInfo());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    class OrderViewHolder2 extends RecyclerView.ViewHolder {
        TextView ord_title;

        ConstraintLayout previous_layout;

        public OrderViewHolder2(View itemView) {
            super(itemView);

            ord_title = itemView.findViewById(R.id.previous_order_title);
            previous_layout=itemView.findViewById(R.id.previous_layout);

        }
    }
}

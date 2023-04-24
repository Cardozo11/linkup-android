package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.List;

public class ActiveOrderAdapter2 extends RecyclerView.Adapter<ActiveOrderAdapter2.OrderViewHolder>{

    private Context mCtx;
    private List<OrderModel> orderList;


    public ActiveOrderAdapter2(Context mCtx, List<OrderModel> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_active_orders,parent,false);
        OrderViewHolder viewHolder=new OrderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActiveOrderAdapter2.OrderViewHolder holder, int position) {

        OrderModel ord= orderList.get(position);
        // newly added line of code
        OrderModel temp=orderList.get(position);


//        holder.gig_fUsername.setText(gig.getfUsername());
        //holder.gig_title.setText(gig.getGigTitle());g
        holder.ord_title.setText(ord.getGigOrderTitle());
        holder.gig_price.setText("₹ "+String.valueOf(ord.getOrderAmount()));

        if(ord.getOrderTrackingStatus().equals("In Development")){
            holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("TWO"));
        }
        else if(ord.getOrderTrackingStatus().equals("Submitted")){
            holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("THREE"));
        }

        holder.btn.setOnClickListener(new View.OnClickListener() {
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

    class OrderViewHolder extends RecyclerView.ViewHolder {

        StateProgressBar stateProgressBar;
        TextView gig_fUsername,ord_title,gig_price,gig_GCD;
        Button btn;

        public OrderViewHolder(View itemView) {
            super(itemView);

            ord_title = itemView.findViewById(R.id.dashTitle);
            stateProgressBar=itemView.findViewById(R.id.active_progress);
            btn=itemView.findViewById(R.id.viewOrder);
            gig_price=itemView.findViewById(R.id.cost);

        }
    }
}

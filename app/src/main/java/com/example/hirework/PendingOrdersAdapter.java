package com.example.hirework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kofigyan.stateprogressbar.StateProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.OrderViewHolder>{

    private Context mCtx;
    private List<OrderModel> orderList;

    public PendingOrdersAdapter(Context mCtx, List<OrderModel> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
    }



    @NonNull
    @NotNull
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gig_item,parent,false);
        OrderViewHolder viewHolder=new OrderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

        OrderModel ord= orderList.get(position);
        // newly added line of code
        OrderModel temp=orderList.get(position);



       holder.title.setText(ord.getGigOrderTitle());
      // holder.title.setText(String.valueOf(temp.getPaymentStatus()));
        //Toast.makeText(mCtx, ord.getPaymentStatus(), Toast.LENGTH_SHORT).show();



        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCtx,PlaceOrder2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("purpose","pendingOrders");
                intent.putExtra(ord.INTENT_ORDER_ID,temp.getOrderId());
                intent.putExtra("order_desc",temp.getDescription());
                intent.putExtra(ord.INTENT_ORDER_STATUS,temp.getOrderStatus());
                intent.putExtra("order_customer",temp.getCustomerUsername());
                intent.putExtra("order_freelancer",temp.getFreelancerUsername());
                intent.putExtra("order_gig_id",temp.getGigId());
                intent.putExtra(ord.INTENT_ORDER_PRICE,temp.getOrderAmount());
                intent.putExtra("order_Tracking_status",temp.getOrderTrackingStatus());
                intent.putExtra("order_title",temp.getGigOrderTitle());
                intent.putExtra("order_days",temp.getCompletionDays());
                intent.putExtra("order_file_format",temp.getFileForamt());
                intent.putExtra("order_Additional",temp.getAdditionalInfo());
                intent.putExtra(ord.INTENT_ORDER_PAYMENT_STATUS,temp.getPaymentStatus());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public OrderViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cGigTitle);

        }

    }
}

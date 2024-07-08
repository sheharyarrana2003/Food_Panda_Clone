package com.example.foodpanda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class deliveryAdapter extends RecyclerView.Adapter<deliveryAdapter.OrderViewHolder> {

    private Context context;
    private List<userOrder> ordersList;

    public deliveryAdapter(Context context, List<userOrder> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        userOrder userOrder = ordersList.get(position);
        holder.tvUserPhoneNumber.setText("Phone Number: " + userOrder.getPhoneNumber());
        holder.tvTotal.setText("Total Payment: " + userOrder.getTotalPayment() + " Rs");

        holder.itemContainer.removeAllViews();
        for (deliveryItems item : userOrder.getItems()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_order, holder.itemContainer, false);
            TextView tvItemName = itemView.findViewById(R.id.tvItemName);
            TextView tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            TextView tvItemAvailability = itemView.findViewById(R.id.tvItemAvailability);

            tvItemName.setText(item.getItemName());
            tvItemPrice.setText(item.getItemPrice() + " Rs");
            tvItemAvailability.setText(item.getIsAvailable());

            holder.itemContainer.addView(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserPhoneNumber, tvTotal;
        LinearLayout itemContainer;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserPhoneNumber = itemView.findViewById(R.id.tvPhoneOrder);
            tvTotal = itemView.findViewById(R.id.tvCOrder);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }
    }
}

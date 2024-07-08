package com.example.foodpanda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final RecyclerClickInterface recyclerClickInterface;
    private Context context;
    private List<Item> items;


    public ItemAdapter(Context context, List<Item> items,RecyclerClickInterface recyclerClickInterface) {
        this.context = context;
        this.items = items;
        this.recyclerClickInterface=recyclerClickInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);

        return new ItemViewHolder(v,recyclerClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item currentItem = items.get(position);
        holder.ivFood.setImageResource(currentItem.getImage());
        holder.tvItem.setText(currentItem.getName());
        holder.tvC.setText(currentItem.getCost());
    }
    public void updateList(List<Item> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvItem, tvC;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView,RecyclerClickInterface recyclerClickInterface) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFoodCart);
            tvItem = itemView.findViewById(R.id.tvItemOrder);
            tvC = itemView.findViewById(R.id.tvCOrder);
            cardView = itemView.findViewById(R.id.cardViewOrder);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerClickInterface!=null){
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerClickInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
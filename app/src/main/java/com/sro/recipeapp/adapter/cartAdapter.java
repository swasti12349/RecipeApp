package com.sro.recipeapp.adapter;


import android.content.Context;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sro.recipeapp.R;
import com.sro.recipeapp.activity.RecipeDetailActivity;
import com.sro.recipeapp.model.CartModel;
import com.sro.recipeapp.model.RecipeModel;

import java.util.List;
import java.util.Map;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.cartViewHolder> {
    List<CartModel> recipeModelList;
    Context context;

    public cartAdapter(List<CartModel> recipeModelList, Context context) {
        this.recipeModelList = recipeModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public cartAdapter.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppinglistitem, parent, false);
        cartAdapter.cartViewHolder vh = new cartAdapter.cartViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.cartViewHolder holder, int position) {
        holder.itemName.setText(recipeModelList.get(holder.getAdapterPosition()).getItemName());

        holder.deleteItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeDetailActivity.getCartList(context).remove(recipeModelList.get(holder.getAdapterPosition()));
                recipeModelList.remove(holder.getAdapterPosition());
                RecipeDetailActivity.saveCartList(context, recipeModelList);
                notifyDataSetChanged();

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnt = holder.itemAmt.getText().toString();
                int count = Integer.parseInt(cnt);
                count++;
                holder.itemAmt.setText(String.valueOf(count));
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnt = holder.itemAmt.getText().toString();
                int count = Integer.parseInt(cnt);

                if (count >= 2) {
                    count--;
                }

                holder.itemAmt.setText(String.valueOf(count));
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeModelList.size();
    }

    public static class cartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemAmt;
        ImageView deleteItemCart, minus, plus;

        public cartViewHolder(@NonNull View itemView) {

            super(itemView);
            itemName = itemView.findViewById(R.id.itemNamecart);
            itemAmt = itemView.findViewById(R.id.amtcart);
            deleteItemCart = itemView.findViewById(R.id.deleteCartItem);
            minus = itemView.findViewById(R.id.minusicon);
            plus = itemView.findViewById(R.id.plusicon);

        }
    }
}
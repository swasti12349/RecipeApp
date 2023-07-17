package com.sro.recipeapp.adapter;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sro.recipeapp.R;
import com.sro.recipeapp.activity.RecipeDetailActivity;
import com.sro.recipeapp.model.RecipeModel;

import java.io.Serializable;
import java.util.List;

public class RecipeAdater extends RecyclerView.Adapter<RecipeAdater.RecipeViewHolder> {
    Context context;
    List<RecipeModel> recipeModelList;

    public RecipeAdater(Context context, List<RecipeModel> recipeModelList) {
        this.context = context;
        this.recipeModelList = recipeModelList;
    }

    @NonNull
    @Override
    public RecipeAdater.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipeitem, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdater.RecipeViewHolder holder, int position) {
        holder.itemname.setText(recipeModelList.get(holder.getAdapterPosition()).getTitle());
        holder.itemid.setText(recipeModelList.get(holder.getAdapterPosition()).getId());

        Glide.with(context)
                .load(recipeModelList.get(holder.getAdapterPosition()).getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loadicon)
                        .error(R.drawable.ic_launcher_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.itemimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("id", recipeModelList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", recipeModelList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("image", recipeModelList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("ingredient", recipeModelList.get(holder.getAdapterPosition()).getIngredients());
                intent.putExtra("instruction", recipeModelList.get(holder.getAdapterPosition()).getInstruction());
                intent.putExtra("ingredientimglist", (Serializable) recipeModelList.get(holder.getAdapterPosition()).getIngredientImage());
                context.startActivity(intent);

            }
        });

    }


    public void searchNote(List<RecipeModel> filterName) {
        this.recipeModelList = filterName;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeModelList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView itemimg;
        TextView itemname;
        TextView itemid;

        public RecipeViewHolder(@NonNull View itemView) {

            super(itemView);
            itemimg = itemView.findViewById(R.id.img);
            itemname = itemView.findViewById(R.id.itemName);
            itemid = itemView.findViewById(R.id.itemid);

        }
    }
}

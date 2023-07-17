package com.sro.recipeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sro.recipeapp.R;
import com.sro.recipeapp.activity.RecipeDetailActivity;
import com.sro.recipeapp.model.CartModel;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    List<String> ingredientNameList;
    List<String> ingredientQuantityList;
    Context context;

    public IngredientAdapter(List<String> ingredientNameList, List<String> ingredientQuantityList, Context context) {
        this.ingredientNameList = ingredientNameList;
        this.ingredientQuantityList = ingredientQuantityList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredientitem, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        holder.txt_ing_name.setText(ingredientNameList.get(position));
        holder.txt_ing_quantity.setText(ingredientQuantityList.get(position));

        holder.btn_addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel cartModel = new CartModel(ingredientNameList.get(holder.getAdapterPosition()), ingredientNameList.get(holder.getAdapterPosition()));

                int i=0;
                for ( i = 0; i < RecipeDetailActivity.cartModelList.size(); i++) {
                    if (RecipeDetailActivity.cartModelList.get(i).getItemName() == cartModel.getItemName()){
                        break;
                    }
                }

                if (i==RecipeDetailActivity.cartModelList.size()){
                    RecipeDetailActivity.cartModelList.add(cartModel);

                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientQuantityList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ing_name;
        TextView txt_ing_quantity;
        ImageButton btn_addicon;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ing_name = itemView.findViewById(R.id.ingname);
            txt_ing_quantity = itemView.findViewById(R.id.ingquantity);
            btn_addicon = itemView.findViewById(R.id.addicon);
        }
    }
}

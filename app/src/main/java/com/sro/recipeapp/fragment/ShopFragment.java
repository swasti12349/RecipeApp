package com.sro.recipeapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sro.recipeapp.R;
import com.sro.recipeapp.activity.RecipeDetailActivity;
import com.sro.recipeapp.adapter.cartAdapter;
import com.sro.recipeapp.model.CartModel;
import com.sro.recipeapp.model.RecipeModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    RecyclerView shoppingRV;
    RecyclerView.LayoutManager layoutManager;
    cartAdapter cartAdapter;
    SharedPreferences sharedPreferences;
    private String PREF_NAME = "MyCartPreferences";
    private String LIST_KEY_CART = "myCartList";
    List<CartModel> recipeModelList;

    List<String> shoppingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        requireActivity().setTitle("Shop Cart");

        shoppingRV = view.findViewById(R.id.shoppingRV);
        layoutManager = new LinearLayoutManager(getContext());
        shoppingRV.setLayoutManager(layoutManager);


        sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (RecipeDetailActivity.getCartList(getContext()) != null) {
            recipeModelList = new ArrayList<>(RecipeDetailActivity.getCartList(getContext()));
        } else {
            recipeModelList = new ArrayList<>();
        }

        cartAdapter = new cartAdapter(recipeModelList, getContext());
        shoppingRV.setAdapter(cartAdapter);
        return view;
    }


}
package com.sro.recipeapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sro.recipeapp.R;
import com.sro.recipeapp.adapter.IngredientAdapter;
import com.sro.recipeapp.adapter.RecipeAdater;
import com.sro.recipeapp.model.CartModel;
import com.sro.recipeapp.model.RecipeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String PREF_NAME_CART = "MyCartPreferences";
    private static final String LIST_KEY_CART = "myCartList";
    public static List<CartModel> cartModelList;
    String id;
    String title;
    String image;
    String ingStr;
    String insStr;
    TextView txt_instruction;
    ImageView img_item;
    ImageView img_fav;
    RecyclerView ingredientsRV;
    RecyclerView.LayoutManager layoutManager;
    public static List<String> favItemIdList;
    private static final String PREF_NAME = "MyPreferences";
    private static final String LIST_KEY = "myList";
    public static List<String> ingredientList;
    public static List<String> ingredientNames;
    public static List<String> ingredientQuantity;
    public static List<String> ingredientQuantityList;
    public IngredientAdapter ingredientAdapter;
    String ingStrCopy;
    public static List<String> ingredientImgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        id = getIntent().getStringExtra("id");
        ingredientImgList = new ArrayList<>();
        ingredientQuantity = new ArrayList<>();
        ingredientNames = new ArrayList<>();
        ingredientList = new ArrayList<>();
        ingredientQuantityList = new ArrayList<>();
        favItemIdList = new ArrayList<>();

        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        ingredientsRV = findViewById(R.id.ingredientRV);
        layoutManager = new LinearLayoutManager(this);

        txt_instruction = findViewById(R.id.inslist);
        img_item = findViewById(R.id.imgitems);
        img_fav = findViewById(R.id.favicon);
        ingredientsRV.setLayoutManager(layoutManager);

        if (getList(RecipeDetailActivity.this) != null) {
            favItemIdList = new ArrayList<>(getList(RecipeDetailActivity.this));
        } else {
            favItemIdList = new ArrayList<>();
        }

        if (getCartList(RecipeDetailActivity.this) != null) {
            cartModelList = new ArrayList<>(getCartList(RecipeDetailActivity.this));
        } else {
            cartModelList = new ArrayList<>();
        }

        if (favItemIdList.contains(id)) {
            img_fav.setImageResource(R.drawable.fav);
        } else {
            img_fav.setImageResource(R.drawable.favred);
        }

        ingStr = getIntent().getStringExtra("ingredient");
        ingStrCopy = ingStr;
        ingStrCopy = "\n" + ingStrCopy;
        insStr = getIntent().getStringExtra("instruction");


        ingredientNames.addAll(extractIngredientNames(ingStrCopy));
        ingredientList.addAll(ingredientNames);

        Glide.with(RecipeDetailActivity.this)
                .load(image)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loadicon)
                        .error(R.drawable.ic_launcher_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(img_item);

        txt_instruction.setText(insStr);

        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favItemIdList.contains(id)) {
                    img_fav.setImageResource(R.drawable.fav);
                    addToFav();
                    Toast.makeText(RecipeDetailActivity.this, "Added to favourite", Toast.LENGTH_SHORT).show();

                } else {
                    img_fav.setImageResource(R.drawable.favred);
                    Toast.makeText(RecipeDetailActivity.this, "Removed from favourite", Toast.LENGTH_SHORT).show();
                    removeFromFav();

                }
            }
        });

        ingredientQuantity.addAll(extractIngredientQuantities(ingStrCopy));
        ingredientQuantityList.addAll(ingredientQuantity);
        ingredientAdapter = new IngredientAdapter(ingredientList, ingredientQuantityList, RecipeDetailActivity.this);
        ingredientsRV.setAdapter(ingredientAdapter);
        ingredientImgList.clear();
        ingredientImgList.addAll(getIntent().getStringArrayListExtra("ingredientimglist"));

    }

    private static List<String> extractIngredientQuantities(String input) {
        List<String> ingredientQuantities = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\b(\\d+(?:\\.\\d+)?)\\s*([a-zA-Z]+)\\b");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String quantity = matcher.group(1) + " g";
            ingredientQuantities.add(quantity.trim());
        }

        return ingredientQuantities;
    }

    private static List<String> extractIngredientNames(String input) {
        List<String> ingredientNames = new ArrayList<>();

        Pattern pattern = Pattern.compile("\n(.*?) -");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String ingredientName = matcher.group(1);
            ingredientNames.add(ingredientName.trim());
        }

        return ingredientNames;
    }

    public static void saveList(Context context, List<String> myList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(myList);

        editor.putString(LIST_KEY, json);
        editor.apply();
    }

    public static List<String> getList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(LIST_KEY, null);

        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveCartList(Context context, List<CartModel> myList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME_CART, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(myList);

        editor.putString(LIST_KEY_CART, json);
        editor.apply();
    }

    public static List<CartModel> getCartList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME_CART, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(LIST_KEY_CART, null);

        Type type = new TypeToken<List<CartModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private void removeFromFav() {
        favItemIdList.remove(id);
        saveList(RecipeDetailActivity.this, favItemIdList);
    }

    private void addToFav() {

        if (!favItemIdList.contains(id)) {
            favItemIdList.add(id);
        }
        saveList(RecipeDetailActivity.this, favItemIdList);

        for (int i = 0; i < cartModelList.size(); i++) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        saveList(RecipeDetailActivity.this, favItemIdList);
        saveCartList(RecipeDetailActivity.this, cartModelList);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveList(RecipeDetailActivity.this, favItemIdList);
        saveCartList(RecipeDetailActivity.this, cartModelList);


    }
}
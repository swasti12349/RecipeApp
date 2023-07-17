package com.sro.recipeapp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sro.recipeapp.R;
import com.sro.recipeapp.adapter.RecipeAdater;
import com.sro.recipeapp.model.RecipeModel;
import com.sro.recipeapp.room.RecipeDatabase;
import com.sro.recipeapp.room.RoomDao;
import com.sro.recipeapp.room.RoomModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public static RecipeAdater recipeAdater;
    public static List<RecipeModel> list;
    RecipeDatabase recipeDatabase;
    RoomDao dataDao;
    List<String> imageList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        requireActivity().setTitle("Home");
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recipeDatabase = RecipeDatabase.getInstance(getContext());
        dataDao = recipeDatabase.roomDao();
        imageList = new ArrayList<>();
        if (isNetworkAvailable(getContext())) {
            volleyNetwork();
        } else {
            class threadClass extends Thread {
                @Override
                public void run() {
                    super.run();
                    retrieveDataFromRoom();
                }
            }
            new threadClass().start();
        }

        return view;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void volleyNetwork() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String url = "https://api.spoonacular.com/recipes/complexSearch";
        String query = "all";

        String requestUrl = url + "?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("id");
                        fetchInstructionAndIngredients(id, object);
                    }

                    recipeAdater = new RecipeAdater(getContext(), list);
                    recyclerView.setAdapter(recipeAdater);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", "d213f0be8c70400cb540db53edc58464");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void retrieveDataFromRoom() {
        class RoomThread extends Thread {
            @Override
            public void run() {
                super.run();
                List<RoomModel> roomDataList = dataDao.getData();

                for (RoomModel roomModel : roomDataList) {
                    RecipeModel recipeModel = new RecipeModel(String.valueOf(roomModel.getItemid()),
                            roomModel.getTitle(),
                            roomModel.getImage(),
                            roomModel.getIngredients(),
                            roomModel.getInstruction(), roomModel.getIngredientImage());

                    list.add(recipeModel);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipeAdater = new RecipeAdater(getContext(), list);
                        recyclerView.setAdapter(recipeAdater);
                    }
                });
            }
        }

        new RoomThread().start();
    }

    private void fetchInstructionAndIngredients(String id, JSONObject object) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String instructionUrl = "https://api.spoonacular.com/recipes/" + id + "/analyzedInstructions";
        JsonArrayRequest instructionRequest = new JsonArrayRequest(Request.Method.GET, instructionUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ijfjgiodig", "onResponse: " + response.toString());
                try {
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        JSONArray stepsArray = jsonObject.getJSONArray("steps");

                        stringBuilder.append(name).append("\n\n");

                        for (int j = 0; j < stepsArray.length(); j++) {
                            JSONObject stepObject = stepsArray.getJSONObject(j);
                            int number = stepObject.getInt("number");
                            String step = stepObject.getString("step");

                            String stepLine = "Step " + number + ": " + step;
                            stringBuilder.append(stepLine).append("\n\n");
                        }

                        stringBuilder.append("\n");
                    }

                    String instruction = stringBuilder.toString().trim();

                    String ingredientUrl = "https://api.spoonacular.com/recipes/" + id + "/ingredientWidget.json";
                    JsonObjectRequest ingredientRequest = new JsonObjectRequest(Request.Method.GET, ingredientUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("ijfjgiodigi", "onResponse: " + response.toString());
                            try {
                                JSONArray ingredientArray = response.getJSONArray("ingredients");
                                StringBuilder ingredientBuilder = new StringBuilder();

                                for (int i = 0; i < ingredientArray.length(); i++) {
                                    JSONObject ingredientObject = ingredientArray.getJSONObject(i);
                                    String name = ingredientObject.getString("name");
                                    String image = ingredientObject.getString("image");
                                    JSONObject metricObject = ingredientObject.getJSONObject("amount").getJSONObject("metric");
                                    String unit = metricObject.getString("unit");
                                    double value = metricObject.getDouble("value");
                                    imageList.add(image);
                                    String ingredientLine = name + " - " + value + " " + unit;
                                    ingredientBuilder.append(ingredientLine).append("\n");
                                    Log.d("dsdlfopekgsf", String.valueOf(ingredientArray.length()));
                                }

                                Log.d("dsdlfopekgsf", imageList.toString());
                                String ingredient = ingredientBuilder.toString();

                                RecipeModel recipeModel = new RecipeModel(object.getString("id"),
                                        object.getString("title"),
                                        object.getString("image"), ingredient, instruction, imageList);

                                RoomModel model = new RoomModel(object.getString("id"), object.getString("title"),
                                        object.getString("image"), ingredient, instruction, imageList);

                                class newClass extends Thread {
                                    @Override
                                    public void run() {
                                        super.run();

                                        recipeDatabase = RecipeDatabase.getInstance(getContext());
                                        dataDao = recipeDatabase.roomDao();

                                        if (dataDao.getCount() <= 10) {
                                            dataDao.insert(model);
                                        }
                                    }
                                }

                                new newClass().start();
                                list.add(recipeModel);
                                recipeAdater.notifyDataSetChanged();

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ijfjgiodig", error.getLocalizedMessage());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("x-api-key", "d213f0be8c70400cb540db53edc58464");
                            return headers;
                        }
                    };

                    requestQueue.add(ingredientRequest);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ijfjgiodig", error.getLocalizedMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", "d213f0be8c70400cb540db53edc58464");
                return headers;
            }
        };

        requestQueue.add(instructionRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                itemFilter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void itemFilter(String s) {

        ArrayList<RecipeModel> arrayList = new ArrayList<>();

        for (RecipeModel n : list) {
            if (n.getTitle().contains(s)) {
                arrayList.add(n);
            }
        }

        recipeAdater.searchNote(arrayList);
    }

}
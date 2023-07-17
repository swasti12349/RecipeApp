package com.sro.recipeapp.Network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sro.recipeapp.model.RecipeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeNetworkClient {
    private static RecipeNetworkClient instance;
    private RequestQueue requestQueue;
    private Context context;

    private RecipeNetworkClient(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized RecipeNetworkClient getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeNetworkClient(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public interface RecipeCallback {
        void onSuccess(List<RecipeModel> recipes);
        void onError(String errorMessage);
    }

    public void getRecipes(final RecipeCallback callback) {
        String url = "https://api.spoonacular.com/recipes/complexSearch";
        String query = "all";

        String requestUrl = url + "?query=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("results");
                            List<RecipeModel> recipes = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String id = object.getString("id");
//                                fetchInstructionAndIngredients(id, object, recipes);
                            }

                            callback.onSuccess(recipes);
                        } catch (JSONException e) {
                            callback.onError("Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Network error: " + error.getMessage());
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

//    private void fetchInstructionAndIngredients(String id, JSONObject object, final List<RecipeModel> recipes) {
//        String instructionUrl = "https://api.spoonacular.com/recipes/" + id + "/analyzedInstructions";
//        JsonArrayRequest instructionRequest = new JsonArrayRequest(Request.Method.GET, instructionUrl, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            StringBuilder stringBuilder = new StringBuilder();
//
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                String name = jsonObject.getString("name");
//                                JSONArray stepsArray = jsonObject.getJSONArray("steps");
//
//                                stringBuilder.append(name).append("\n\n");
//
//                                for (int j = 0; j < stepsArray.length(); j++) {
//                                    JSONObject stepObject = stepsArray.getJSONObject(j);
//                                    int number = stepObject.getInt("number");
//                                    String step = stepObject.getString("step");
//
//                                    String stepLine = "Step " + number + ": " + step;
//                                    stringBuilder.append(stepLine).append("\n\n");
//                                }
//
//                                stringBuilder.append("\n");
//                            }
//
//                            String instruction = stringBuilder.toString().trim();
//
//                            String ingredientUrl = "https://api.spoonacular.com/recipes/" + id + "/ingredientWidget.json";
//                            JsonObjectRequest ingredientRequest = new JsonObjectRequest(Request.Method.GET, ingredientUrl, null,
//                                    new Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//                                            try {
//                                                JSONArray ingredientArray = response.getJSONArray("ingredients");
//                                                StringBuilder ingredientBuilder = new StringBuilder();
//
//                                                for (int i = 0; i < ingredientArray.length(); i++) {
//                                                    JSONObject ingredientObject = ingredientArray.getJSONObject(i);
//                                                    String name = ingredientObject.getString("name");
//                                                    String image = ingredientObject.getString("image");
//                                                    JSONObject metricObject = ingredientObject.getJSONObject("amount").getJSONObject("metric");
//                                                    String unit = metricObject.getString("unit");
//                                                    double value = metricObject.getDouble("value");
//
//                                                    String ingredientLine = name + " - " + value + " " + unit;
//                                                    ingredientBuilder.append(ingredientLine).append("\n");
//                                                }
//
//                                                String ingredient = ingredientBuilder.toString();
//
//                                                RecipeModel recipeModel = new RecipeModel(
//                                                        object.getString("id"),
//                                                        object.getString("title"),
//                                                        object.getString("image"),
//                                                        ingredient,
//                                                        instruction
//                                                );
//
//                                                recipes.add(recipeModel);
//
//                                            } catch (JSONException e) {
//                                                callback.onError("Error parsing ingredient JSON");
//                                            }
//                                        }
//                                    },
//                                    new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            callback.onError("Network error: " + error.getMessage());
//                                        }
//                                    }) {
//                                @Override
//                                public Map<String, String> getHeaders() {
//                                    Map<String, String> headers = new HashMap<>();
//                                    headers.put("x-api-key", "d213f0be8c70400cb540db53edc58464");
//                                    return headers;
//                                }
//                            };
//
//                            requestQueue.add(ingredientRequest);
//                        } catch (JSONException e) {
//                            callback.onError("Error parsing instruction JSON");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        callback.onError("Network error: " + error.getMessage());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("x-api-key", "d213f0be8c70400cb540db53edc58464");
//                return headers;
//            }
//        };
//
//        requestQueue.add(instructionRequest);
//    }
}

package com.sro.recipeapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sro.recipeapp.R;
import com.sro.recipeapp.activity.RecipeDetailActivity;
import com.sro.recipeapp.room.RecipeDatabase;
import com.sro.recipeapp.room.RoomDao;
import com.sro.recipeapp.room.RoomModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MealsFragment extends Fragment {

    Spinner sunbreak, sunlun, sundin;
    Spinner monbreak, monlun, mondin;
    Spinner tuebreak, tuelun, tuedin;
    Spinner wedbreak, wedlun, weddin;
    Spinner thubreak, thulun, thudin;
    Spinner fribreak, frilun, fridin;
    Spinner satbreak, satlun, satdin;
    RecipeDatabase recipeDatabase;
    RoomDao roomDao;
    List<RoomModel> roomModelList;
    public static List<String> mySpinnerList;
    public static List<String> menuList;
    ArrayAdapter<String> adapter;
    List<Spinner> spinnerList;
    Button btn_save;

    private static final String PREFS_NAME = "MymenuPrefs";
    private static final String LIST_KEY = "listmenuKey";

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requireActivity().setTitle("Meals");
        roomModelList = new ArrayList<>();
        spinnerList = new ArrayList<>();
        mySpinnerList = new ArrayList<>();
        if (retrieveListFromSharedPreferences() != null) {
            menuList = new ArrayList<>(Objects.requireNonNull(retrieveListFromSharedPreferences()));

        } else {
            menuList = new ArrayList<>();
        }

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mySpinnerList);

        View view = inflater.inflate(R.layout.fragment_meals, container, false);
        btn_save = view.findViewById(R.id.savemealbtn);
        sunbreak = view.findViewById(R.id.sundaybreakfast);
        sunlun = view.findViewById(R.id.sundaylunch);
        sundin = view.findViewById(R.id.sundaydinner);

        monbreak = view.findViewById(R.id.mondaybreakfast);
        monlun = view.findViewById(R.id.mondaylunch);
        mondin = view.findViewById(R.id.mondaydinner);

        tuebreak = view.findViewById(R.id.tuesdaybreakfast);
        tuelun = view.findViewById(R.id.tuesdaylunch);
        tuedin = view.findViewById(R.id.tuesdaydinner);

        wedbreak = view.findViewById(R.id.wedbrealkfast);
        wedlun = view.findViewById(R.id.wedlunch);
        weddin = view.findViewById(R.id.weddinner);

        thubreak = view.findViewById(R.id.thursbreakfast);
        thulun = view.findViewById(R.id.thurslunch);
        thudin = view.findViewById(R.id.thursdinner);

        fribreak = view.findViewById(R.id.fribreakfast);
        frilun = view.findViewById(R.id.frilunch);
        fridin = view.findViewById(R.id.fridinner);

        satbreak = view.findViewById(R.id.satbreakfast);
        satlun = view.findViewById(R.id.satlunch);
        satdin = view.findViewById(R.id.satdinner);

        spinnerList.add(sunbreak);
        spinnerList.add(sunlun);
        spinnerList.add(sundin);

        spinnerList.add(monbreak);
        spinnerList.add(monlun);
        spinnerList.add(mondin);

        spinnerList.add(tuebreak);
        spinnerList.add(tuelun);
        spinnerList.add(tuedin);

        spinnerList.add(wedbreak);
        spinnerList.add(wedlun);
        spinnerList.add(weddin);

        spinnerList.add(thubreak);
        spinnerList.add(thulun);
        spinnerList.add(thudin);

        spinnerList.add(fribreak);
        spinnerList.add(frilun);
        spinnerList.add(fridin);

        spinnerList.add(satbreak);
        spinnerList.add(satlun);
        spinnerList.add(satdin);


        recipeDatabase = RecipeDatabase.getInstance(getContext());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
//                shoppingList();
            }
        });


        class myClass extends Thread {
            @Override
            public void run() {
                super.run();
                roomDao = recipeDatabase.roomDao();
                roomModelList.addAll(roomDao.getData());

                for (int i = 0; i < roomModelList.size(); i++) {
                    mySpinnerList.add(roomModelList.get(i).getTitle().toString());
                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sunbreak.setAdapter(adapter);
                        sunlun.setAdapter(adapter);
                        sundin.setAdapter(adapter);
                        monbreak.setAdapter(adapter);
                        monlun.setAdapter(adapter);
                        mondin.setAdapter(adapter);
                        tuebreak.setAdapter(adapter);
                        tuelun.setAdapter(adapter);
                        tuedin.setAdapter(adapter);
                        wedbreak.setAdapter(adapter);
                        wedlun.setAdapter(adapter);
                        weddin.setAdapter(adapter);
                        thubreak.setAdapter(adapter);
                        thulun.setAdapter(adapter);
                        thudin.setAdapter(adapter);
                        fribreak.setAdapter(adapter);
                        frilun.setAdapter(adapter);
                        fridin.setAdapter(adapter);
                        satbreak.setAdapter(adapter);
                        satlun.setAdapter(adapter);
                        satdin.setAdapter(adapter);

                        setDataToSpinners(menuList);

                    }
                });

            }
        }
        new myClass().start();


        return view;
    }

    private void setDataToSpinners(List<String> menuList) {

        int minSize = Math.min(menuList.size(), spinnerList.size());

        for (int i = 0; i < minSize; i++) {
            Spinner spinner = spinnerList.get(i);

            String menuString = menuList.get(i);

            int position = adapter.getPosition(menuString);
            spinner.setSelection(position);

        }

    }

    private void saveData() {
        Spinner spinner;
        menuList.clear();
        for (int i = 0; i < spinnerList.size(); i++) {
            spinner = spinnerList.get(i);
            menuList.add(spinner.getSelectedItem().toString());
        }

        saveListToSharedPreferences(menuList);
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

    }

    private List<String> retrieveListFromSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(LIST_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            return gson.fromJson(json, type);
        }

        return null;
    }

    private void saveListToSharedPreferences(List<String> list) {
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(LIST_KEY, json);
        editor.apply();
    }

    public void shoppingList() {

    }


}
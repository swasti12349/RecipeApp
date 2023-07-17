package com.sro.recipeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sro.recipeapp.R;
import com.sro.recipeapp.activity.RecipeDetailActivity;
import com.sro.recipeapp.adapter.RecipeAdater;
import com.sro.recipeapp.model.RecipeModel;
import com.sro.recipeapp.room.RecipeDatabase;
import com.sro.recipeapp.room.RoomDao;
import com.sro.recipeapp.room.RoomModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesFragment extends Fragment {

    RecipeDatabase recipeDatabase;
    RoomDao dataDao;
    List<RoomModel> list;

    List<RecipeModel> favList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecipeAdater recipeAdater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        requireActivity().setTitle("Favourites");
        recyclerView = view.findViewById(R.id.favrv);
        favList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        getData();

        return view;
    }

    public void getData() {

        class newThread extends Thread {
            @Override
            public void run() {
                super.run();
                recipeDatabase = RecipeDatabase.getInstance(getContext());
                dataDao = recipeDatabase.roomDao();
                list.addAll(dataDao.getData());


                if (RecipeDetailActivity.getList(getContext()) != null) {
                    for (int i = 0; i < list.size(); i++) {

                        RecipeModel model = new RecipeModel(
                                list.get(i).getItemid(),
                                list.get(i).getTitle(),
                                list.get(i).getImage(),
                                list.get(i).getIngredients(),
                                list.get(i).getInstruction(),
                                list.get(i).getIngredientImage()
                        );

                        if (RecipeDetailActivity.getList(getContext()).contains(list.get(i).getItemid())) {
                            favList.add(model);
                        }
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipeAdater = new RecipeAdater(getContext(), favList);
                        recyclerView.setAdapter(recipeAdater);
                    }
                });

            }


        }

        new newThread().start();
    }


}

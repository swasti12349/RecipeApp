package com.sro.recipeapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sro.recipeapp.R;

public class ProfileFragment extends Fragment {

    EditText ed_name, ed_age, ed_diet, ed_cuisine;
    Button btn_save;
    String name, age, diet, cuisine;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ed_name = view.findViewById(R.id.name);
        ed_age = view.findViewById(R.id.age);
        ed_diet = view.findViewById(R.id.diet);
        ed_cuisine = view.findViewById(R.id.cuisine);
        btn_save = view.findViewById(R.id.savebtn);
        requireActivity().setTitle("Profile");
        sharedPreferences = getContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
        getProfileDetails();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ed_name.getText().toString();
                age = ed_age.getText().toString();
                diet = ed_diet.getText().toString();
                cuisine = ed_cuisine.getText().toString();

                saveDetails(name, age, diet, cuisine);
            }
        });
        return view;
    }

    private void getProfileDetails() {

        String name = sharedPreferences.getString("name", "");
        String age = sharedPreferences.getString("age", "");
        String diet = sharedPreferences.getString("diet", "");
        String cuisine = sharedPreferences.getString("cuisine", "");

        ed_name.setText(name);
        ed_age.setText(age);
        ed_diet.setText(diet);
        ed_cuisine.setText(cuisine);

    }

    private void saveDetails(String name, String age, String diet, String cuisine) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("age", age);
        editor.putString("diet", diet);
        editor.putString("cuisine", cuisine);

        editor.apply();

        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

    }
}
package com.sro.recipeapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {RoomModel.class}, version = 6)
@TypeConverters(ListConverter.class)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase INSTANCE;
    public static RecipeDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, "recipe-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract RoomDao roomDao();


}



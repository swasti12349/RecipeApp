package com.sro.recipeapp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoomModel roomModel);

    @Query("SELECT * FROM recipetable")
    List<RoomModel> getData();

    @Query("DELETE FROM recipetable")
    void delete();

    @Query("SELECT COUNT(*) FROM recipetable")
    int getCount();

    @Insert
    void insertList(List<RoomModel> roomModelList);

}

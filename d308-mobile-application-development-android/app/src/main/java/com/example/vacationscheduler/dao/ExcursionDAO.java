package com.example.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationscheduler.UI.ExcursionDetails;
import com.example.vacationscheduler.entities.Excursions;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursions excursion);

    @Query("SELECT * FROM Excursions ORDER BY id ASC")
    List<Excursions> getAllExcursions();

    @Delete
    void delete(Excursions excursion);

    @Update
    void update(Excursions excursion);
}

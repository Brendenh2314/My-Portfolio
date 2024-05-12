package com.example.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationscheduler.entities.Vacations;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacations vacation);

    @Query("SELECT * FROM Vacations ORDER BY vacationID ASC")
    List<Vacations> getAllVacations();

    @Delete
    void delete(Vacations vacation);

    @Update
    void update(Vacations vacation);

}
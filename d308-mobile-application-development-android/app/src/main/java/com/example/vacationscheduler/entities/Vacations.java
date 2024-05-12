package com.example.vacationscheduler.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacations {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    @ColumnInfo(name = "title")
    public String vacationName;

    @ColumnInfo(name = "hotel_place_of_stay")
    public String hotelPlaceOfStay;

    @ColumnInfo(name = "start_date")
    public String startDate;

    @ColumnInfo(name = "end_date")
    public String endDate;

    // Constructor
    public Vacations(int vacationID, String vacationName, String hotelPlaceOfStay, String startDate, String endDate) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotelPlaceOfStay = hotelPlaceOfStay;
        this.startDate = startDate;
        this.endDate = endDate;

    }


    // Getters and Setters
    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getHotelPlaceOfStay() {
        return hotelPlaceOfStay;
    }

    public void setHotelPlaceOfStay(String hotelPlaceOfStay) {this.hotelPlaceOfStay = hotelPlaceOfStay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
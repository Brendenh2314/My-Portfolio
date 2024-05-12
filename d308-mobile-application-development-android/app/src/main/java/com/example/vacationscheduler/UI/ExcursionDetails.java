package com.example.vacationscheduler.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    int vacationID;
    String title;
    String date;
    int id;
    String startDate;
    String endDate;

    EditText editTitle;
    EditText editDate;

    Repository repository;


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);


        repository = new Repository(getApplication());

        vacationID = getIntent().getIntExtra("vacationID", -1);

        title = getIntent().getStringExtra("title");
        editTitle = findViewById(R.id.excursionTitleText);
        editTitle.setText(title);

        date = getIntent().getStringExtra("date");
        editDate = findViewById(R.id.excursionDateText);
        editDate.setText(date);

        id = getIntent().getIntExtra("id", -1);

        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");


        Log.d("ExcursionDetails", "Received vacationID: " + vacationID);
        Log.d("ExcursionDetails", "Received title: " + title);
        Log.d("ExcursionDetails", "Received date: " + date);
        Log.d("ExcursionDetails", "Received ID: " + id);
        Log.d("ExcursionDetails", "Received startDate: " + startDate);
        Log.d("ExcursionDetails", "Received endDate: " + endDate);

        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        try {
            startDate = String.valueOf(dateFormat.parse(startDate));
            endDate = String.valueOf(dateFormat.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            }


    }

    public void showDatePickerDialog(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar chosenDate = Calendar.getInstance();
                        chosenDate.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        editDate.setText(dateFormat.format(chosenDate.getTime()));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.excursionSave) {
            String title = editTitle.getText().toString().trim();
            String dateStr = editDate.getText().toString().trim();  // Get the date from the EditText

            if (vacationID == -1) {
                Toast.makeText(this, "Cannot save excursion without a valid vacation. Please select a vacation first.", Toast.LENGTH_LONG).show();
                return true;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date excursionDate = dateFormat.parse(dateStr);
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);
                if (excursionDate.before(start) || excursionDate.after(end)) {
                    Toast.makeText(this, "Excursion date must be within the vacation dates.", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date format.", Toast.LENGTH_SHORT).show();
                return true;
            }

            date = dateStr;  // Update the date variable with the new value from editDate

            // Create or update Excursions
            Excursions excursion = new Excursions(title, date, vacationID);
            if (id == -1) {
                repository.insert(excursion);
                Toast.makeText(this, "Excursion added", Toast.LENGTH_SHORT).show();
            } else { // Otherwise, we're updating an existing excursion
                Excursions updatedExcursion = new Excursions(title, date, vacationID);
                updatedExcursion.setId(id); // Make sure to set the existing ID on the excursion
                // Update the excursion in the database
                repository.update(updatedExcursion);
                Toast.makeText(this, "Excursion updated", Toast.LENGTH_SHORT).show();
            }

            finish(); // Close the activity and return
            return true;

        } else if (item.getItemId() == R.id.excursionDelete) {
            // Check if there is an existing excursion to delete
            if (id != -1) {
                Excursions excursionToDelete = new Excursions(); // Placeholder for your actual constructor
                excursionToDelete.setId(id);
                repository.delete(excursionToDelete);
                Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "No existing excursion to delete", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (item.getItemId() == R.id.excursionNotify) {
            // Notify logic
            setExcursionNotifications(title, date);
        }
        return super.onOptionsItemSelected(item);
    }
    private void setExcursionNotifications (String title, String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        try {
            Date excursionDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(excursionDate);
            calendar.set(Calendar.HOUR_OF_DAY, 8); // Set notification time (adjust as needed)

            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("key", "Excursion '" + title + "' is starting today!");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Notification set for excursion start date", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing the date", Toast.LENGTH_SHORT).show();
        }
    }
}
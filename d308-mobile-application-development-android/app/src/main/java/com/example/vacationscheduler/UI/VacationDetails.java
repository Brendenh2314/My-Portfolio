package com.example.vacationscheduler.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursions;
import com.example.vacationscheduler.entities.Vacations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VacationDetails extends AppCompatActivity {
    int vacationID;
    String vacationName;
    String hotelPlaceOfStay;
    String startDate;
    String endDate;
    EditText editVacationName;
    EditText editHotelPlaceOfStay;
    EditText editStartDate;
    EditText editEndDate;
    Calendar startDateCalendar = Calendar.getInstance();
    Calendar endDateCalendar = Calendar.getInstance();

    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        repository = new Repository(getApplication());

        vacationName = getIntent().getStringExtra("vacationName");
        hotelPlaceOfStay = getIntent().getStringExtra("hotelPlaceOfStay");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        vacationID = getIntent().getIntExtra("vacationID", -1);

        editVacationName = findViewById(R.id.titleText);
        editHotelPlaceOfStay = findViewById(R.id.hotelText);
        editStartDate = findViewById(R.id.startDateText);
        editEndDate = findViewById(R.id.endDateText);

        editVacationName.setText(vacationName);
        editHotelPlaceOfStay.setText(hotelPlaceOfStay);

        Button shareButton = findViewById(R.id.button_share);
        Button copyButton = findViewById(R.id.button_copy);

        String vacationDetails = getVacationDetailsForSharing(vacationName, hotelPlaceOfStay, startDate, endDate);


        shareButton.setOnClickListener(v -> {
            String details = getVacationDetailsForSharing(
                    editVacationName.getText().toString(),
                    editHotelPlaceOfStay.getText().toString(),
                    editStartDate.getText().toString(),
                    editEndDate.getText().toString()
            );
            shareVacationDetails(details);
        });
        copyButton.setOnClickListener(v -> {
            String details = getVacationDetailsForSharing(
                    editVacationName.getText().toString(),
                    editHotelPlaceOfStay.getText().toString(),
                    editStartDate.getText().toString(),
                    editEndDate.getText().toString()
            );
            copyToClipboard("Vacation Details", details);
        });


        if (startDate != null && !startDate.isEmpty()) {
            editStartDate.setText(startDate);

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDate);
                startDateCalendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (endDate != null && !endDate.isEmpty()) {
            editEndDate.setText(endDate);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate);
                endDateCalendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate.setOnClickListener(v -> showDatePickerDialog(editStartDate, startDateCalendar));
        editEndDate.setOnClickListener(v -> showDatePickerDialog(editEndDate, endDateCalendar));


        FloatingActionButton fab = findViewById(R.id.floatingActionButtonVacationDetails);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vacationID == -1) {  // Assuming -1 means no valid vacationID has been set
                    Toast.makeText(VacationDetails.this, "Cannot add an excursion without a saved vacation.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                    intent.putExtra("vacationID", vacationID);
                    intent.putExtra("startDate", startDate); // Ensure these dates are strings in "yyyy-MM-dd" format
                    intent.putExtra("endDate", endDate);
                    startActivity(intent);
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RecyclerView recyclerView = findViewById(R.id.vacationDetailsRecyclerView);
        repository = new Repository(getApplication());
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        List<Excursions> filteredExcursions = new ArrayList<>();
        for (Excursions e : repository.getAllExcursions()) {
            Log.d("VacationDetails", "Excursion vacationID: " + e.getVacationID() + " for Excursion Title: " + e.getTitle());
            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, filteredExcursions, startDate, endDate);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filteredExcursions = new ArrayList<>();
        for (Excursions e : repository.getAllExcursions()) {
            // Log the vacationID of the current Excursion being checked
            Log.d("VacationDetails", "Excursion vacationID: " + e.getVacationID() + " for Excursion Title: " + e.getTitle());

            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }
        Log.d("VacationDetails", "Vacation Name: " + vacationName);
        Log.d("VacationDetails", "Hotel: " + hotelPlaceOfStay);
        Log.d("VacationDetails", "Start Date: " + startDate);
        Log.d("VacationDetails", "End Date: " + endDate);
        Log.d("VacationDetails", "Vacation ID: " + vacationID);
// Log the number of excursions that passed the filter and are being set in the adapter
        Log.d("VacationDetails", "Setting " + filteredExcursions.size() + " filtered excursions in adapter");
        excursionAdapter.setmExcursions(filteredExcursions);
    }
    private void showDatePickerDialog(final EditText dateField, final Calendar dateCalendar) {
        int year = dateCalendar.get(Calendar.YEAR);
        int month = dateCalendar.get(Calendar.MONTH);
        int day = dateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    dateCalendar.set(Calendar.YEAR, year1);
                    dateCalendar.set(Calendar.MONTH, monthOfYear);
                    dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateField.setText(dateFormat.format(dateCalendar.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }
    private boolean validateDates() {
        if (startDateCalendar.after(endDateCalendar)) {
            Toast.makeText(this, "Start date must be before the end date.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacationSave) {
            Vacations vacations;
            if (!validateDates()) {
                return true;
            }
            if (vacationID == -1) {
                if (repository.getAllVacations().size() ==
                0) vacationID = 1;
                else vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;

                vacations = new Vacations(vacationID, editVacationName.getText().toString(), editHotelPlaceOfStay.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacations);
                Toast.makeText(VacationDetails.this, "Vacation saved", Toast.LENGTH_LONG).show();
            } else {
                vacations = new Vacations(vacationID, editVacationName.getText().toString(), editHotelPlaceOfStay.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.update(vacations);
                Toast.makeText(VacationDetails.this, "Vacation updated", Toast.LENGTH_LONG).show();
            }
            finish();
            return true;
        } else if (item.getItemId() == R.id.vacationDelete) {
            if (item.getItemId() == R.id.vacationDelete) {
                int numExcursions = 0;
                for (Excursions excursion : repository.getAllExcursions()) {
                    if (excursion.getVacationID() == vacationID) ++numExcursions;
                }

                if (numExcursions == 0) {
                    Vacations currentVacation = null;
                    for (Vacations vacation : repository.getAllVacations()) {
                        if (vacation.getVacationID() == vacationID) currentVacation = vacation;
                    }

                    if (currentVacation != null) {
                        repository.delete(currentVacation);
                        Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                        finish(); // Exit the activity, returning to the previous screen
                    }
                } else {
                    Toast.makeText(VacationDetails.this, "Can't delete a vacation with associated excursions", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        } else if(item.getItemId() == R.id.vacationNotify){
            String startDateFromScreen = editStartDate.getText().toString();
            String endDateFromScreen = editEndDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            try {
                Date currentDate = sdf.parse(sdf.format(new Date())); // Formats today's date and parse it back to remove time part

                // Set up start date notification
                Date startDate = sdf.parse(startDateFromScreen);
                if (currentDate.equals(startDate)) {
                    Intent startIntent = new Intent(VacationDetails.this, MyReceiver.class);
                    startIntent.setAction("START_ACTION");
                    startIntent.putExtra("key", vacationName + " is starting today!");
                    int startRequestCode = vacationID + 1000;
                    PendingIntent startSender = PendingIntent.getBroadcast(VacationDetails.this, startRequestCode, startIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), startSender); // Trigger immediately
                    Log.d("VacationDetails", "Start notification set for today: " + sdf.format(startDate));
                }

                // Set up end date notification
                Date endDate = sdf.parse(endDateFromScreen);
                if (currentDate.equals(endDate)) {
                    Intent endIntent = new Intent(VacationDetails.this, MyReceiver.class);
                    endIntent.setAction("END_ACTION");
                    endIntent.putExtra("key", vacationName + " is ending today!");
                    int endRequestCode = vacationID + 2000;
                    PendingIntent endSender = PendingIntent.getBroadcast(VacationDetails.this, endRequestCode, endIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), endSender); // Trigger immediately
                    Log.d("VacationDetails", "End notification set for today: " + sdf.format(endDate));
                }
                Toast.makeText(this, "Your notifications are set", Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error parsing the date", Toast.LENGTH_SHORT).show();
                return true; // Handle parse exception properly
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateExcursionList();
    }

    private void updateExcursionList() {
        List<Excursions> filteredExcursions = new ArrayList<>();
        for (Excursions e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }
        RecyclerView recyclerView = findViewById(R.id.vacationDetailsRecyclerView);
        String startDate = getIntent().getStringExtra("startDate");  // Assume these are set correctly at activity start
        String endDate = getIntent().getStringExtra("endDate");

        // Check if adapter is already set and just update data
        if (recyclerView.getAdapter() != null) {
            ExcursionAdapter adapter = (ExcursionAdapter) recyclerView.getAdapter();
            adapter.setmExcursions(filteredExcursions);
            adapter.notifyDataSetChanged();
        } else {
            ExcursionAdapter adapter = new ExcursionAdapter(this, filteredExcursions, startDate, endDate);
            recyclerView.setAdapter(adapter);
        }
        Log.d("VacationDetails", "Filtered Excursions Count: " + filteredExcursions.size());

        Log.d("VacationDetails", "Excursion list updated. Count: " + filteredExcursions.size());
    }
    private List<Excursions> getExcursionsForVacation(int vacationID) {
        List<Excursions> excursionsForVacation = new ArrayList<>();
        for (Excursions excursion : repository.getAllExcursions()) {
            if (excursion.getVacationID() == vacationID) {
                excursionsForVacation.add(excursion);
            }
        }
        return excursionsForVacation;
    }
    private String getVacationDetailsForSharing(String vacationName, String hotelPlaceOfStay, String startDate, String endDate) {
        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("Vacation Details:\n")
                .append("Vacation Name: ").append(vacationName).append("\n")
                .append("Hotel: ").append(hotelPlaceOfStay).append("\n")
                .append("Start Date: ").append(startDate).append("\n")
                .append("End Date: ").append(endDate).append("\n");

        List<Excursions> excursionsForVacation = getExcursionsForVacation(vacationID);
        if (!excursionsForVacation.isEmpty()) {
            detailsBuilder.append("Excursions:\n");
            for (Excursions excursion : excursionsForVacation) {
                detailsBuilder.append("Excursion Name: ").append(excursion.getTitle()).append("\n")
                        .append("Date: ").append(excursion.getDate()).append("\n");
                // Add any other details specific to each excursion
                Log.d("VacationDetails", "Excursion Name: " + excursion.getTitle());
                Log.d("VacationDetails", "Excursion Date: " + excursion.getDate());
            }
        }

        return detailsBuilder.toString();
    }

    private void shareVacationDetails(String vacationDetails) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
        Log.d("VacationDetails", "Vacation Details for Sharing: " + vacationDetails);
    }
    private void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
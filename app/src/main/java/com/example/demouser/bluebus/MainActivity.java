package com.example.demouser.bluebus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    //Get all GUI components
    public static ToggleButton toggleButton;
    public static TimePicker timePicker;
    public static Spinner dayoftheWeekSpinner;
    public static Button goButton;
    public static TextView startLabel;

    public static Boolean location;
    public static int hour;
    public static int minute;
    public static String AM_PM;
    public static String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);
        timePicker = findViewById(R.id.timePicker);
        goButton = findViewById(R.id.goButton);
        dayoftheWeekSpinner = findViewById(R.id.timeSpinner);
        startLabel = findViewById(R.id.startLabel);

        ScheduleFetcher sf = new ScheduleFetcher();
        sf.execute();

        ArrayAdapter<CharSequence> days = ArrayAdapter.createFromResource(this, R.array.days_array, android.R.layout.simple_spinner_item);
        days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayoftheWeekSpinner.setAdapter(days);
        dayoftheWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent){

        }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSchedule(view);
            }
        });
    }

    public void gotoSchedule(View view){
        Intent intent = new Intent(this, scheduleActivity.class);
        startActivity(intent);
    }

    //obtain information from GUI
    public static boolean getLoction()
    {
        return toggleButton.isChecked();
    }

    public static int getHour()
    {
        timePicker.clearFocus();
        Log.d("hour", Integer.toString(timePicker.getCurrentHour()));
        return timePicker.getCurrentHour();
    }

    public static int getMinute()
    {
        timePicker.clearFocus();
        Log.d("minute", Integer.toString(timePicker.getCurrentMinute()));
        return timePicker.getCurrentMinute();
    }

    public static String getAmPm(int hourOfDay)
    {
        return (hourOfDay < 12) ? "AM" : "PM";
    }

}

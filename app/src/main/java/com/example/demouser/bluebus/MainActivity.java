package com.example.demouser.bluebus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    //Get all GUI components
    ToggleButton toggleButton;
    TimePicker timePicker;
    Spinner dayoftheWeekSpinner;
    Button goButton;
    TextView startLabel;
    String selected;

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

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

}


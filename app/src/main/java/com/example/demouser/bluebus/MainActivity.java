package com.example.demouser.bluebus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner dayoftheWeekSpinner;
    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScheduleFetcher sf = new ScheduleFetcher();
        sf.execute();

        dayoftheWeekSpinner = findViewById(R.id.daySpinner);
        goButton = findViewById(R.id.goButton);

        ArrayAdapter<CharSequence> days = ArrayAdapter.createFromResource(this, R.array.days_array, android.R.layout.simple_spinner_item);
        days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayoftheWeekSpinner.setAdapter(days);

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

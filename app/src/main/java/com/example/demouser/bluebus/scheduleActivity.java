package com.example.demouser.bluebus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class scheduleActivity extends AppCompatActivity {

    // Data for populating the table
    List<Time> daySchedule = new ArrayList<>();

    Boolean haverford;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //get haverford information
        haverford = MainActivity.getLoction();

        //fill array lists, here
        String daySelected = MainActivity.day.toLowerCase();
        daySchedule = ScheduleFetcher.schedule.get(daySelected);

        //populate array lists
        addHeaders();
        addData();
    }

    private void populateArrayLists() {

    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        if (!haverford) {
            tr.addView(getTextView(0, "Leave Bryn Mawr", Color.WHITE, Typeface.BOLD, Color.BLUE));
            tr.addView(getTextView(0, "Arrive Haverford", Color.WHITE, Typeface.BOLD, Color.BLUE));
        } else {
            tr.addView(getTextView(0, "Leave Haverford", Color.WHITE, Typeface.BOLD, Color.BLUE));
            tr.addView(getTextView(0, "Arrive Bryn Mawr", Color.WHITE, Typeface.BOLD, Color.BLUE));
        }
        tl.addView(tr, getTblLayoutParams());
    }

    /**
     * This function add the data to the table
     **/
    public void addData() {
        if (daySchedule == null) {
            return;
        }
        int numTimes = daySchedule.size();
        TableLayout tl = findViewById(R.id.table);
        int chosenTime = MainActivity.hour * 60 + MainActivity.minute;
        if(chosenTime == 0)
        {
            Calendar now = Calendar.getInstance();
            chosenTime = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);;
        }
        for (int i = 0; i < numTimes; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            Time t = daySchedule.get(i);
            if (!haverford) {
                if (Time.toMinutes(t.leaveBrynMawr()) > chosenTime) {
                    Log.d("brynmawrtime", String.valueOf(Time.toMinutes(t.leaveBrynMawr())));
                    tr.addView(getTextView(i + 1, t.leaveBrynMawr(), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + numTimes, t.arriveHaverford(), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                }
            } else {
                if (Time.toMinutes(t.leaveHaverford()) > chosenTime) {
                    tr.addView(getTextView(i + 1, t.leaveHaverford(), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + numTimes, t.arriveBrynMawr(), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                }
            }
            tl.addView(tr, getTblLayoutParams());
        }
    }
}
package com.example.demouser.bluebus;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleFetcher extends AsyncTask<Void, Void, Void> {
    private static final String BLUE_BUS_URL = "http://www.brynmawr.edu/transportation/bico.shtml";
    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday",
            "saturdaynight", "sunday"};
    protected static Map<String, List<Time>> schedule = new HashMap<>();

    public ScheduleFetcher()
    {
    }

    @Override
    protected Void doInBackground(Void... params) {
        fetch();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    }

    public String fetch() {
        Document doc = null;
        try {
            doc = Jsoup.connect(BLUE_BUS_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc == null) {
            return "Failed to fetch document";
        }
        Log.d("document title: ", doc.title());

        Elements siteContent = doc.select("div#siteContentContainer");
        Elements threeQuarters = siteContent.select("div.three-quarters.first");

        for (String day : days) {
            Elements currentDay = threeQuarters.select("a[name=" + day + "]");
            Elements table = null;
            if (!day.equals("thursday")) {
                table = currentDay.next();
            } else {
                table = currentDay.parents().select("p").next();
            }
            Elements tbody = table.select("tbody");
            Elements rows = table.select("tr");
            List<Time> dayschedule = new ArrayList<>();
            schedule.put(day, dayschedule);
            for (Element row : rows) {
                if (row.child(0).is("td")) {
                    schedule.get(day).add(parseRow(day, row));
                }
            }
            Log.d(day + " ", schedule.get(day).toString());
        }
        return "TRUE";
    }

    private Time parseRow(String day, Element row) {
        boolean isWeekend = isWeekend(day);
        String td0, td1, td2, td3;
        if (!isWeekend) {
            Element child0 = row.child(0);
            Element child1 = row.child(1);
            Element child2 = row.child(2);
            Element child3 = row.child(3);
            td0 = handleTd(child0);
            td1 = handleTd(child1);
            td2 = handleTd(child2);
            td3 = handleTd(child3);
        } else {
            Element child0 = row.child(0);
            Element child2 = row.child(1);
            td0 = handleTd(child0);
            td1 = "";
            td2 = handleTd(child2);
            td3 = "";
        }

        Time time = new Time(td0, td1, td2, td3);
        return time;
    }

    private boolean isWeekend(String day) {
        return day.equals("saturdaynight") || day.equals("sunday");
    }

    private String handleTd(Element td) {
        if (td.is("strong")) {
            return td.child(0).text();
        } else {
            return td.text();
        }
    }
}

class Time implements Comparable<Time> {
    private String bToHLeave, bToHArrive, hToBLeave, hToBArrive;
//    int duration;

    public Time(String bToHLeave, String bToHArrive, String hToBLeave, String hToBArrive) {
        this.bToHLeave = bToHLeave;
        this.bToHArrive = bToHArrive;
        this.hToBLeave = hToBLeave;
        this.hToBArrive = hToBArrive;
    }

    public static int toMinutes(String time)
    {
        int morning = time.indexOf("AM");
        int afternoon = time.indexOf("PM");
        boolean isAfternoon = afternoon != -1;
        String clock = isAfternoon ? time.substring(0, afternoon) : time.substring(0, morning);
        String[] tokens = clock.split(":");
        if(tokens.length != 2) return -1;
        int hour = Integer.valueOf(tokens[0].trim());
        if(isAfternoon) hour += 12;
        int minute = Integer.valueOf(tokens[1].trim());
        return hour * 60 + minute;
    }

    @Override
    public int compareTo(Time that) {
        return !MainActivity.location // location == true -> haverford
                ? toMinutes(this.bToHLeave) - toMinutes(that.bToHLeave)
                : toMinutes(this.hToBLeave) - toMinutes(that.hToBLeave);
    }

    public String toString() {
        return bToHLeave + " " + bToHArrive + " " + hToBLeave + " " + hToBArrive;
    }

    public String leaveBrynMawr() {
        return bToHLeave;
    }

    public String leaveHaverford() {
        return hToBLeave;
    }

    public String arriveHaverford() {
        return bToHArrive;
    }

    public String arriveBrynMawr() {
        return hToBArrive;
    }
}
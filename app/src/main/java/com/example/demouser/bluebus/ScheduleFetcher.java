package com.example.demouser.bluebus;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ScheduleFetcher extends AsyncTask<Void, Void, Void>
{
    private static final String BLUE_BUS_URL = "http://www.brynmawr.edu/transportation/bico.shtml";

    public ScheduleFetcher(){}

    @Override
    protected Void doInBackground(Void... params)
    {
        fetch();
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {

    }

    public String fetch()
    {
        Document doc = null;
        try
        {
            doc = Jsoup.connect(BLUE_BUS_URL).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (doc != null)
        {
            Log.d("document title: ", doc.title());
            return doc.title();
        }
        return "";
    }
}

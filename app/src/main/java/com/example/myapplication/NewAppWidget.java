package com.example.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    String futureJokeString;
    RemoteViews remoteViews;
    ComponentName watchWidget;
    AppWidgetManager appWidgetManager;
    private static final String SYNC_CLICKED = "MY_PACKAGE_NAME.WIDGET_BUTTON";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        watchWidget = new ComponentName(context, NewAppWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.sync_button, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            appWidgetManager = AppWidgetManager.getInstance(context);

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            watchWidget = new ComponentName(context, NewAppWidget.class);

            new JokeLoader().execute();

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public class JokeLoader extends AsyncTask<Void, Void, Void>
    {
        @Override
        public Void doInBackground(Void... voids)
        {
            System.out.println("1");
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");
            System.out.println("aaa");
            try {
                System.out.println("aaa111");
                JokeModel newJoke = new JokeModel(new JSONObject(jsonString));
                futureJokeString = newJoke.value.toString();
            } catch (JSONException e) {
                System.out.println("чёт не так");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            futureJokeString = "";
            remoteViews.setTextViewText(R.id.textJoke, "Loading...");
        }

        @Override
        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!futureJokeString.equals("")) {
                System.out.println(futureJokeString);
                remoteViews.setTextViewText(R.id.textJoke, futureJokeString);
                appWidgetManager.updateAppWidget(watchWidget, remoteViews);
            }
        }
    }

    private String getJson (String link)
    {
        String data = "";
        try {
            URL url = new URL(link.trim());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                data = reader.readLine();
                urlConnection.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  data;
    }
}
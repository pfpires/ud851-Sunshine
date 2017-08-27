package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;

public class SunshineSyncTask {

//  TODO (1) Create a class called SunshineSyncTask
//  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      TODO (3) Within syncWeather, fetch new weather data
//      TODO (4) If we have valid results, delete the old data and insert the new

    private static final String TAG = SunshineSyncTask .class.getSimpleName();

    public synchronized static void syncWeather(Context context) {

        String weatherDataJson = null;
        try {
            weatherDataJson = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrl(context));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Error fetching weather from network",e);
            return;
        }

        ContentValues[] values = null;
        try {
            values = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context,weatherDataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,"Error parsing JSON data received from the network",e);
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,null,null);
        contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI,values);
    }
}
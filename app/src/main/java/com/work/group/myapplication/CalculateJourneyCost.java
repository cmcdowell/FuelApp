package com.work.group.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;
import java.lang.Math.*;

public class CalculateJourneyCost extends AppCompatActivity {

    public final static String DEBUG_TAG = "fuel app";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_journey_cost);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        String destination = intent.getStringExtra(GetJourneyDetails.EXTRA_DESTINATION);
        String origin = intent.getStringExtra(GetJourneyDetails.EXTRA_ORIGIN);
        double mpg = intent.getDoubleExtra(GetJourneyDetails.EXTRA_MPG, 1);


        //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        //double latitude = currentLocation.getLatitude();
        //double longitude = currentLocation.getLongitude();



        String mapsUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin.replaceAll(" ", "%20") + "&destination=" + destination.replaceAll(" ", "%20") + "&KEY=AIzaSyDzigzuXPLVKnjNwfmGhH-k79D996m7Z9I&units=imperial";
        String fuelUrl = "http://christophermcdowell.com/price.json";
        try {

            Log.i("Fub", mapsUrl);

            //Get distance by road
            JSONObject json = new JSONObject(downloadUrl(mapsUrl));

            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray newTempARr = routes.getJSONArray("legs");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");

            Log.i("Disatance :", distOb.getString("text"));
            Double distance = distOb.getDouble("value") / 1609; //convert to miles.

            Log.d(DEBUG_TAG, distance.toString());


            //Get current fuel price
            JSONObject jsonPrice = new JSONObject(downloadUrl(fuelUrl));
            Double price = jsonPrice.getDouble("Petrol-Price");

            //calculate journey cost in pence
            double cost = (distance * price) / mpg;

            Log.d(DEBUG_TAG, String.valueOf(cost / 100));

            String outputString = origin + " to " + destination + " " + distOb.get("text") + " with a fuel cost of £" + String.format("%.2f", cost / 100);

            TextView output = (TextView) findViewById(R.id.output);
            output.setText(outputString);




        } catch (IOException e) {
            Log.d(DEBUG_TAG, "IO Failure: " + e.getMessage());
        } catch (JSONException e) {
            Log.d(DEBUG_TAG, "JSON Failure: " + e.getMessage());

            String outputString = "Could not find route between " + origin + " and " + destination + ".";
            Button button = (Button) findViewById(R.id.view_journey);
            button.setVisibility(View.GONE);
            TextView output = (TextView) findViewById(R.id.output);
            output.setText(outputString);
        }

        findViewById(R.id.content).setOnTouchListener(new OnSwipeTouchListener(CalculateJourneyCost.this) {
            Intent intent = getIntent();
            String destination = intent.getStringExtra(GetJourneyDetails.EXTRA_DESTINATION);
            String origin = intent.getStringExtra(GetJourneyDetails.EXTRA_ORIGIN);
            public void onSwipeLeft() {
                finish();
            }
            public void onSwipeBottom() {
                Toast.makeText(CalculateJourneyCost.this, "Origin: " + origin + ", Destination: " + destination, Toast.LENGTH_SHORT).show();
            }

        });



    }
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string.
            //String contentAsString = readIt(is, len);
            String contentAsString = convertStreamToString(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void viewInMaps(View view){

        Intent intent = getIntent();
        String destination = intent.getStringExtra(GetJourneyDetails.EXTRA_DESTINATION);
        String origin = intent.getStringExtra(GetJourneyDetails.EXTRA_ORIGIN);

        Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + origin + "&daddr=" + destination));
        startActivity(mapsIntent);
    }
}

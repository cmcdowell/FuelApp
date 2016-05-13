package com.work.group.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class GetJourneyDetails extends AppCompatActivity {

    public final static String EXTRA_DESTINATION = "destination";
    public final static String EXTRA_ORIGIN = "origin";
    public final static String EXTRA_MPG = "mpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_journey_details);


    }

    public void sendJourneyDetails(View view) {



        //Get origin
        EditText origin = (EditText) findViewById(R.id.origin);
        String originMessage = origin.getText().toString();


        //get destination
        EditText destination = (EditText) findViewById(R.id.destination);
        String message = destination.getText().toString();


        //get mpg
        Intent originIntent = getIntent();
        String fuelEcon = originIntent.getStringExtra(SelectProfiles.EXTRA_PROFILE_MPG);

        if (originMessage.equals("") || message.equals("")|| fuelEcon.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please fill in all fields.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            double fuel = Double.parseDouble(fuelEcon);
            Intent intent = new Intent(this, CalculateJourneyCost.class);
            intent.putExtra(EXTRA_MPG, fuel);
            intent.putExtra(EXTRA_ORIGIN, originMessage);
            intent.putExtra(EXTRA_DESTINATION, message);
            showNotification();
            startActivity(intent);
        }



    }

    public void showNotification() {
        Intent originIntent = getIntent();
        String fuelEcon = originIntent.getStringExtra(SelectProfiles.EXTRA_PROFILE_MPG);
        Intent intent = new Intent(this, GetJourneyDetails.class);
        intent.putExtra(SelectProfiles.EXTRA_PROFILE_MPG, fuelEcon);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Gas Guzzler")
                .setContentText("Journey cost calculated")
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}



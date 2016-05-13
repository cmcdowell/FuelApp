package com.work.group.myapplication;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectProfiles extends ListActivity implements AdapterView.OnItemClickListener {

    public final static String EXTRA_PROFILE_MPG = "profile_mpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profiles);

        // Retrieve student records
        String URL = "content://com.work.group.myapplication.provider.Cars/profiles";

        Uri profiles = Uri.parse(URL);
        Cursor cursor = managedQuery(profiles, null, null, null, "model");

        startManagingCursor(cursor);

        // the desired columns to be bound
        String[] columns = new String[] {ProfileProvider.NAME, ProfileProvider.EFFICIENCY};
        // the XML defined views which the data will be bound to
        int[] to = new int[] { R.id.name_entry, R.id.hidden_mpg};

        // create the adapter using the cursor pointing to the desired data as well as the layout information
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.activity_select_profiles_entry, cursor, columns, to);

        // set this adapter as your ListActivity's adapter
        this.setListAdapter(mAdapter);

        ListView listview = getListView();
        listview.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        TextView textview = (TextView) v.findViewById(R.id.hidden_mpg);
        String mpg = textview.getText().toString();
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position + " with mpg " + mpg);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, GetJourneyDetails.class);
        intent.putExtra(EXTRA_PROFILE_MPG, mpg);
        startActivity(intent);
    }

    public void createProfile(View view) {
        // Add a new profile

        String model = ((EditText) findViewById(R.id.model)).getText().toString();
        String mpg = ((EditText) findViewById(R.id.profile_mpg)).getText().toString();

        if (model.equals("") || mpg.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please fill in all fields.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            ContentValues values = new ContentValues();

            values.put(ProfileProvider.NAME, model);

            values.put(ProfileProvider.EFFICIENCY, mpg);

            Uri uri = getContentResolver().insert(
                    ProfileProvider.CONTENT_URI, values);

        }
    }
}
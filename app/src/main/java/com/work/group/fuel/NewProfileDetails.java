package com.work.group.fuel;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class NewProfileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_details);
    }

    //save profile and return to SelectProfiles view
    public void saveProfile(View view) {
        DatabaseHandler db = new DatabaseHandler(this);

        String model = "Ford Focus";

        db.addProfile(new Profile(model, 23.5, "test"));

        String photo = db.getProfile(0).getPhotoURI();
        Log.d("photo ", (String) photo);
    }
}

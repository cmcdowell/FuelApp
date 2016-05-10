package com.work.group.fuel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectProfiles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profiles);
    }

    public void startNewProfile (View view) {
        Intent intent = new Intent(this, NewProfileDetails.class);
        startActivity(intent);
    }
}

package com.example.tom.gasguzzler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CarDetail extends ActionBarActivity implements android.view.View.OnClickListener{

    Button btnAdd ,  btnDelete;
    EditText editTextName;
    EditText editTextMpg;
    private int _Car_Id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMpg = (EditText) findViewById(R.id.editTextMPG);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


        _Car_Id =0;
        Intent intent = getIntent();
        _Car_Id =intent.getIntExtra("car_Id", 0);
        CarRepo repo = new CarRepo(this);
        Car car = new Car();
        car = repo.getCarById(_Car_Id);

        editTextName.setText(car.name);
        editTextMpg.setText(car.mpg);
    }



    public void onClick(View view) {
        if (view == findViewById(R.id.btnAdd)){
            CarRepo repo = new CarRepo(this);
            Car car = new Car();
            car.mpg=editTextMpg.getText().toString();
            car.name=editTextName.getText().toString();
            car.car_ID=_Car_Id;
            Toast.makeText(this,"New Car Added",Toast.LENGTH_SHORT).show();
        }else if (view== findViewById(R.id.btnDelete)){
            CarRepo repo = new CarRepo(this);
            repo.delete(_Car_Id);
            Toast.makeText(this, "Car Deleted", Toast.LENGTH_SHORT);
            finish();
        }
    }

}

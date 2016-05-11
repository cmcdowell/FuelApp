package com.example.tom.gasguzzler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Profiles extends ListActivity  implements android.view.View.OnClickListener{

    Button btnAdd;
    TextView car_Id;

    @Override
    public void onClick(View view) {
        if (view== findViewById(R.id.btnAdd)){

            Intent intent = new Intent(this,CarDetail.class);
            intent.putExtra("student_Id",0);
            startActivity(intent);

        }else {

            CarRepo repo = new CarRepo(this);

            ArrayList<HashMap<String, String>> carList =  repo.getCarList();
            if(carList.size()!=0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        car_Id = (TextView) view.findViewById(R.id.car_Id);
                        String carId = car_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),CarDetail.class);
                        objIndent.putExtra("car_Id", Integer.parseInt( carId));
                        startActivity(objIndent);
                    }
                });
                ListAdapter adapter = new SimpleAdapter( Profiles.this,carList, R.layout.view_car_entry, new String[] { "id","name"}, new int[] {R.id.car_Id, R.id.car_name});
                setListAdapter(adapter);
            }else{
                Toast.makeText(this,"No cars available",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


    }


}
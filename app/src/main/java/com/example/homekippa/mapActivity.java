package com.example.homekippa;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;


public class mapActivity extends AppCompatActivity{

    private MapView mmapView;



    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        Log.d("here", "123456789");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mmapView = (MapView) findViewById(R.id.map_view);

    }
}

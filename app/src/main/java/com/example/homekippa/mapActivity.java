package com.example.homekippa;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;


public class mapActivity extends AppCompatActivity {
    private RelativeLayout contioner;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("27f0294947799473f5560e2568541333");
        contioner = (RelativeLayout) findViewById(R.id.map_view);
        contioner.addView(mapView);

    }

}

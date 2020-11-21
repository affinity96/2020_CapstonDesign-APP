package com.example.homekippa.ui.walk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.homekippa.AddPetDesActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.WeatheLocationResponse;
import com.example.homekippa.data.WeatherLocationData;
import com.example.homekippa.mapActivity;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalkFragment extends Fragment {

    private WalkViewModel walkViewModel;
    private Double lat;
    private Double lon;
    private LocationManager locationManager;
    private Location currentLocation;
    private Location userLocation;
    private ServiceApi service;
    private static final int REQUEST_CODE_LOCATION = 2;
    private Drawable drawable;

    private EditText editText_temperature;
    private EditText editText_weather;
    private ImageView imageView_weather;
    private Button button_startWalk;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walkViewModel =
                ViewModelProviders.of(this).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        editText_temperature = root.findViewById(R.id.editText_temperature);
        editText_weather = root.findViewById(R.id.editText_weather);
        imageView_weather = root.findViewById(R.id.imageView_weather);
        button_startWalk = root.findViewById(R.id.button_startWalk);

        userLocation =getMyLocation();
        if( userLocation != null ) {
            lat = userLocation.getLatitude();
            lon = userLocation.getLongitude();
            Log.d("weather_lat", String.valueOf(lat));
            Log.d("weather_lon", String.valueOf(lon));
        }else {
            Log.d("weather_error", "나 자고 싶엉!!");
        }
        // lat하고 lon의 값을 받아와서 weatherLocation을 통해 서버로 값을 보낸다.


        weatherLocation(new WeatherLocationData(lat, lon));


        button_startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mapActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }



    private Location getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        }
        else {
            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
            }
        }
        return currentLocation;
    }

    private void weatherLocation(WeatherLocationData data) {
        Log.i("weather_create","create");
        service.getWeatehrData(data).enqueue(new Callback<WeatheLocationResponse>() {
            @Override
            public void onResponse(Call<WeatheLocationResponse> call, Response<WeatheLocationResponse> response) {
                WeatheLocationResponse result = response.body();

                editText_temperature.setText(result.getCurrent_temperature());
                weather(result.getCurrent_weather());



                if(result.getCode() == 200){
                    Log.d("weather","server connect");
                }else{
                    Log.d("weather","server disconnect");
                }
            }
            @Override
            public void onFailure(Call<WeatheLocationResponse> call, Throwable t) {
                Log.d("weather_fail","server not response");
                t.printStackTrace();
            }

        });
    }

    private void weather(String weather){
        weather = weather.toLowerCase();

        if(weather.equals("rain")){
            drawable = getResources().getDrawable(R.drawable.rain);
            imageView_weather.setImageDrawable(drawable);
            editText_weather.setText("비");

        }
        else  if(weather.equals("snow")){
            drawable = getResources().getDrawable(R.drawable.snow);
            imageView_weather.setImageDrawable(drawable);
            editText_weather.setText("눈");
        }
        else  if(weather.equals("thunderstorm")){
            drawable = getResources().getDrawable(R.drawable.thunderstorm);
            imageView_weather.setImageDrawable(drawable);
            editText_weather.setText("천둥번개");
        }
        else  if(weather.equals("drizzle")){
            drawable = getResources().getDrawable(R.drawable.drizzle);
            imageView_weather.setImageDrawable(drawable);
            editText_weather.setText("보슬보슬비");
        }
        else  if(weather.equals("clouds")){
            drawable = getResources().getDrawable(R.drawable.cloud);
            imageView_weather.setImageDrawable(drawable);
            editText_weather.setText("구름");
        }
        else  if(weather.equals("clear")){
            drawable = getResources().getDrawable(R.drawable.clear);
            imageView_weather.setImageDrawable(drawable);
            editText_weather.setText("맑음");
        }

    }

    //petList 받기



}



package com.example.homekippa;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.GroupData;
import com.example.homekippa.ui.group.CreateGroupActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class mapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener{


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private MapPoint.GeoCoordinate mapPointGeo;
    private ArrayList<Double> latitude_arrayList = new ArrayList<>();
    private ArrayList<Double> longitude_arrayList = new ArrayList<>();
    private MapPolyline mapPolyLine = new MapPolyline();


    private MapView mapView;
    private ViewGroup mapViewContainer;
    private Button button_stopMap;
    private LinearLayout linearLayout_infor;
    private TextView textView_walkDistance;
    private TextView textView_walkTime;
//    private Button button_remove;
    private long startTime;
    private long endTime;
    private long totalTime;
    private GroupData groupData;
    private DatabaseReference mDatabase;
    private MapReverseGeoCoder reverseGeoCoder;



    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        button_stopMap =findViewById(R.id.button_stopMap);
        linearLayout_infor = findViewById(R.id.layout_infor);
//        button_remove = findViewById(R.id.button_remove);
        textView_walkDistance = findViewById(R.id.textView_walkDistance);
        textView_walkTime = findViewById(R.id.textView_walkTime);
        groupData = (GroupData) getIntent().getExtras().get("groupData");


        //firebase 백엔드 사용해서 위도 경도 저장
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("walk");
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId()));


        // mapview에 kakaoMap 연동해서 올리기
        mapView =new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        // 마커등을 올리기
        mapView.setPOIItemEventListener(this);
        //현재위치
        mapView.setCurrentLocationEventListener(this);


//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        startTime = System.currentTimeMillis();
        // zoom 설정하기
        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        //gps로 현 위치 찍어줌
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        mapView.setShowCurrentLocationMarker(true);
        mapView.setCurrentLocationRadius(10);

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        //현재 위치로 지도 올려 놓기

//setCustomCurrentLocationMarkerImage
        //-> 이를 사용해서 현 위치 아이콘을 custom 이미지 바꾸기 가능

        //거리와 시간을 팝업 창처럼 띄우기?? 이렇게 할까?



        // mark => ?


        //재시작 버튼
//        button_remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mapView.removePolyline(mapPolyLine);
//                mapPolyLine = new MapPolyline();
//
//            }
//        });


        button_stopMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPolyLine.setLineColor(Color.argb(128, 255, 51, 0));

                //addpoint를 사용해서 산책 로드를 이어준다.
//                for (int i = 0 ; i < longitude_arrayList.size();i++) {
//                    mapPolyLine.addPoint(MapPoint.mapPointWithGeoCoord(latitude_arrayList.get(i),longitude_arrayList.get(i)));
//                    Log.d("array_map_long", String.valueOf(longitude_arrayList.get(i)));
//                    Log.d("array_map_lati", String.valueOf(latitude_arrayList.get(i)));
//                }

                mapView.addPolyline(mapPolyLine);

                MapPointBounds mapPointBounds = new MapPointBounds(mapPolyLine.getMapPoints());
                int padding = 100; // px
                //mapView에 찍어준다.
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                // 거리와 시간의 layout을 보여준다.
                linearLayout_infor.setVisibility(View.VISIBLE);

                //거리 넣기
                double distanceTotal = Math.round((distanceSum(longitude_arrayList, latitude_arrayList)*100)/100);
                textView_walkDistance.setText(String.valueOf(distanceTotal));


                //시간 넣기
                endTime = System.currentTimeMillis();
                totalTime = (endTime - startTime)/1000;
                textView_walkTime.setText(String.valueOf(totalTime));
                startTime =System.currentTimeMillis();
                //위도 경도 초기화
                latitude_arrayList = new ArrayList<>();
                longitude_arrayList = new ArrayList<>();

            }
        });



    }

    public double distanceSum(ArrayList<Double> longitude_arrayList, ArrayList<Double> latitude_arrayList){

        double disSum = 0.0;

        for (int l = 0 ; l < longitude_arrayList.size();l++) {
            //마지막 list에 아무것도 없을 시 끝내기
            if((l+1) == longitude_arrayList.size()){
                break;
            }
            //distanceLocation에 넣어준다.
            disSum += distanceLocation(longitude_arrayList.get(l),latitude_arrayList.get(l),longitude_arrayList.get(l+1),latitude_arrayList.get(l+1));
        }

        return disSum;
    }

    public double distanceLocation(double locationA_long, double locationA_lati, double locationB_long, double locationB_lati){

        double dis;

        Location locationA = new Location("pointA");
        locationA.setLatitude(locationA_lati);
        locationA.setLongitude(locationA_long);

        Location locationB = new Location("pointB");
        locationB.setLatitude(locationB_lati);
        locationB.setLongitude(locationB_long);

        dis = locationA.distanceTo(locationB);
        return dis;
    }

    // POI 건들
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    // 현재위치
    //현재위치 업데이트
    int cycle = 0;
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float v) {
//        mapPointGeo = currentLocation.getMapPointGeoCoord();
//
        if(cycle >1){
            cycle =0;
            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latiatiude").setValue(mapPointGeo.latitude);
            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);
        }else{
            cycle++;
        }
        mapPointGeo = currentLocation.getMapPointGeoCoord();


        Log.d("cycleCount", String.valueOf(cycle));


    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }//


    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    //center point가 움직일 때마다 찍히는 메소드
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        mapPointGeo = mapPoint.getMapPointGeoCoord();
        latitude_arrayList.add(mapPointGeo.latitude);
        longitude_arrayList.add(mapPointGeo.longitude);
        mapPolyLine.addPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude,mapPointGeo.longitude));
        mapView.addPolyline(mapPolyLine);

        MapPointBounds mapPointBounds = new MapPointBounds(mapPolyLine.getMapPoints());
        int padding = 100; // px
        //mapView에 찍어준다.
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

//        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latiatiude").setValue(mapPointGeo.latitude);
//        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);



        //위도 경도 찍
//        Log.d("latitude", String.valueOf(mapPointGeo.latitude));
//        Log.d("longitude", String.valueOf(mapPointGeo.longitude));

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.d("zoom", String.valueOf(i));


    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {


    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}

package com.example.homekippa;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.firebase.database.DatabaseError;
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

import java.io.IOException;
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
    private MapPOIItem marker;
    private int markerCount;


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

        //마커 생성
        marker = new MapPOIItem();


        //firebase 백엔드 사용해서 위도 경도 저장
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("walk");
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId()));


        mDatabase.child("walking_group").child(String.valueOf(15));
        mDatabase.child("walking_group").child(String.valueOf(15)).child("address").setValue("우만2동");
        mDatabase.child("walking_group").child(String.valueOf(15)).child("latitude").setValue(37.27897262573242);
        mDatabase.child("walking_group").child(String.valueOf(15)).child("longitude").setValue(127.04090118408203);

        mDatabase.child("walking_group").child(String.valueOf(16));
        mDatabase.child("walking_group").child(String.valueOf(16)).child("address").setValue("우만2동");
        mDatabase.child("walking_group").child(String.valueOf(16)).child("latitude").setValue(37.28105163574219);
        mDatabase.child("walking_group").child(String.valueOf(16)).child("longitude").setValue(127.04118347167969);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //마커 초기화
                markerCount = 0;
                // 다른 그룹 위치 판별하기
                String currentAddress = dataSnapshot.child("walking_group").child(String.valueOf(groupData.getId())).child("address").getValue(String.class);
                for (DataSnapshot GroupMapData : dataSnapshot.child("walking_group").getChildren()) {
                    Log.d("database_read", String.valueOf(GroupMapData));
                    String otherGroup = GroupMapData.getKey();
                    String otherAddress = GroupMapData.child("address").getValue(String.class);
                    Double latitude = GroupMapData.child("latitude").getValue(Double.class);
                    Double longitude = GroupMapData.child("longitude").getValue(Double.class);

                    Log.d("database_read",otherAddress);
                    Log.d("database_read", String.valueOf(latitude));
                    Log.d("database_read", String.valueOf(longitude));
                    //자신 그룹은 제외 시킨다.
                    if (!otherGroup.equals(String.valueOf(groupData.getId()))) {
                        Log.d("database_read","group");
                        if (otherAddress.equals(currentAddress)) {
                            Log.d("database_read","address");
                            //다른 그룹 위치 띄우기
                            walkingOtherGroup(GroupMapData.child("latitude").getValue(Double.class), GroupMapData.child("longitude").getValue(Double.class));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("database_read_error", "Failed to read value.", error.toException());
            }
        });
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
    // 산책 중인 다른 그룹 마커로 표시

    public void walkingOtherGroup(Double latitude, Double longitude){
        Log.d("database_read","here");
        MapPoint walkingMapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude);
        marker.setItemName("otherGroup");
        marker.setTag(markerCount);
        marker.setMapPoint(walkingMapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);


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
        mapPointGeo = currentLocation.getMapPointGeoCoord();

//        if(cycle >1){
//            cycle =0;
//            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latitude").setValue(mapPointGeo.latitude);
//            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);
//
//            Geocoder g = new Geocoder(this);
//            List<Address> address = null;
//            try{
//                address =g.getFromLocation(mapPointGeo.latitude, mapPointGeo.longitude,10);
//            }catch (IOException e){
//                e.printStackTrace();
//                Log.d("test","입출력오류");
//            }
//
//            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("address").setValue(address.get(0).getThoroughfare());
//
//        }else{
//            cycle++;
//        }
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latitude").setValue(mapPointGeo.latitude);
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);
        Geocoder g = new Geocoder(this);
        List<Address> address = null;
        try{
            address =g.getFromLocation(mapPointGeo.latitude, mapPointGeo.longitude,10);
        }catch (IOException e){
            e.printStackTrace();
            Log.d("test","입출력오류");
        }

        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("address").setValue(address.get(0).getThoroughfare());



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

//        MapPointBounds mapPointBounds = new MapPointBounds(mapPolyLine.getMapPoints());
        int padding = 100; // px
        //mapView에 찍어준다.
//        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

//        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latiatiude").setValue(mapPointGeo.latitude);
//        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);



        //위도 경도 찍
        Log.d("latitude", String.valueOf(mapPointGeo.latitude));
        Log.d("longitude", String.valueOf(mapPointGeo.longitude));

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

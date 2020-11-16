package com.example.homekippa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.ui.group.CreateGroupActivity;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class mapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private List<Double> latitude_arrayList = new ArrayList<>();
    private List<Double> longitude_arrayList = new ArrayList<>();
    private Button button_stopMap;
    private MapPolyline mapPolyLine = new MapPolyline();




    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        Log.d("here", "123456789");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        button_stopMap =findViewById(R.id.button_stopMap);

        // mapview에 kakaoMap 연동해서 올리기
        mapView =new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        //
        mapView.setMapViewEventListener(this);
        // 마커등을 올리기
        mapView.setPOIItemEventListener(this);
        //현재위치
        mapView.setCurrentLocationEventListener(this);


        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        // zoom 설정하기
        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        //gps로 현 위치 찍어줌
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        // 줌 인
        mapView.zoomIn(true);

        // 줌 아웃
        mapView.zoomOut(true);

        //현재 위치로 지도 올려 놓기

//setCustomCurrentLocationMarkerImage
        //-> 이를 사용해서 현 위치 아이콘을 custom 이미지 바꾸기 가능


        //화면 중심이 현재 사용하고 있는 유저 중심으로 포커싱이 계속 바뀜

        //addpoint를 사용해서 산책 로드를 이어준다.

        //firebase 백엔드 사용해서 위도 경도 json형식으로 넘겨줌 이떄,

        // mark => ?


        //1.

        button_stopMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPolyLine.setTag(1000);
                mapPolyLine.setLineColor(Color.argb(128, 255, 51, 0));
                for (int i = 0 ; i < longitude_arrayList.size();i++){
                    mapPolyLine.addPoint(MapPoint.mapPointWithGeoCoord(longitude_arrayList.get(i),latitude_arrayList.get(i)));


                    Log.d("array_map_long", String.valueOf(longitude_arrayList.get(i)));
                    Log.d("array_map_lati", String.valueOf(latitude_arrayList.get(i)));

                }
                mapView.addPolyline(mapPolyLine);

                MapPointBounds mapPointBounds = new MapPointBounds(mapPolyLine.getMapPoints());
                int padding = 100; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
            }
        });



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
    //현재위치 업데이
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

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

    //center point가 움직일 때마다 찍히는 메소
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        latitude_arrayList.add(mapPointGeo.latitude);
        longitude_arrayList.add(mapPointGeo.longitude);

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

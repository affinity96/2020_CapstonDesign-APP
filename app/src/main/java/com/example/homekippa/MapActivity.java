package com.example.homekippa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
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
import net.daum.mf.map.api.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener{


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private MapPoint.GeoCoordinate mapPointGeo;
    private ArrayList<Double> latitude_arrayList = new ArrayList<>();
    private ArrayList<Double> longitude_arrayList = new ArrayList<>();
    private MapPolyline mapPolyLine = new MapPolyline();

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private Button button_stopMap;
    private Button button_finishMap;
    private LinearLayout linearLayout_infor;
    private TextView textView_walkDistance;
    private TextView textView_walkTime;

    private long startTime;
    private long endTime;
    private long totalTime;
    private String petName;
    private String petGender;
    private String petSpecies;
    private String petImageUrl;
    private String groupLockScope;
    private GroupData groupData;
    private UserData userData;
    private ServiceApi service;
    private DatabaseReference mDatabase;
    private ArrayList followingGroup = new ArrayList();
//    private MapPOIItem marker;
    private int markerCount;
//    private ArrayList<MapPOIItem> marker = new ArrayList<>();




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        button_stopMap =findViewById(R.id.button_stopMap);
        button_finishMap = findViewById(R.id.button_finishMap);
        linearLayout_infor = findViewById(R.id.layout_infor);
//        button_remove = findViewById(R.id.button_remove);
        textView_walkDistance = findViewById(R.id.textView_walkDistance);
        textView_walkTime = findViewById(R.id.textView_walkTime);
//        button_walktest =findViewById(R.id.button_walktest);
        groupData = (GroupData) getIntent().getExtras().get("groupData");
        userData = (UserData) getIntent().getExtras().get("userData");
        petName =  (String) getIntent().getExtras().get("petName");
        petGender =  (String) getIntent().getExtras().get("petGender");
        petSpecies =  (String) getIntent().getExtras().get("petSpecies");
        petImageUrl = (String) getIntent().getExtras().get("petImageUrl");

        groupLockScope = (String) getIntent().getExtras().get("scope");

        if(groupLockScope.equals("followScope") ){
            followingGroup = (ArrayList) getIntent().getExtras().get("followingGroup");
        }

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //마커 생성

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

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("walk");
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("petName").setValue(petName);
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("petGender").setValue(petGender);
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("petSpecies").setValue(petSpecies);
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("petImage").setValue(petImageUrl);





//        mapView.setOnTouchListener(new View.OnTouchListener(){
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });




        //firebase 백엔드 사용해서 위도 경도 저장





//setCustomCurrentLocationMarkerImage
        //-> 이를 사용해서 현 위치 아이콘을 custom 이미지 바꾸기 가능

        //거리와 시간을 팝업 창처럼 띄우기?? 이렇게 할까?



        // mark => ?
        //여기에도 alertDialog로 바꾸기
        button_stopMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_stopMap.setVisibility(View.GONE);
//                mapView.setOnTouchListener(new View.OnTouchListener(){
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return false;
//                   }
//                });
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
        // firebase에서 해당 group 삭제
        button_finishMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public void walkingOtherGroup(Double latitude, Double longitude, String groupTag){
        MapPOIItem marker = new MapPOIItem();


        MapPoint walkingMapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude);

        marker.setItemName(groupTag);
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

    private void getProfileImage(String url, ImageView imageView) {
        String[] w = url.split("/");
        String key = w[w.length - 1];

        Bitmap bit = getBitmapFromCacheDir(key);
        Activity activity = MapActivity.this;
        if(activity.isFinishing())
            return;

        if (bit != null) {
            Glide.with(MapActivity.this).load(bit).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(imageView);
        } else {
            service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String TAG = "YesGroup";
                    if (response.isSuccessful()) {
                        InputStream is = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Glide.with(MapActivity.this).load(bitmap).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(imageView);
                        saveBitmapToJpeg(bitmap, key);
                    } else {
                        Log.d(TAG, "server contact failed");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("createGroup error", t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }
    private void saveBitmapToJpeg(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getCacheDir();
        //저장할 파일 이름
        String fileName = name;

        try {
            File tempFile = new File(storage, fileName);
            tempFile.createNewFile();

            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("Yes", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("Yes", "IOException : " + e.getMessage());
        }
    }
    private Bitmap getBitmapFromCacheDir(String key) {
        String found = null;
        Bitmap bitmap = null;
        File file = new File(getCacheDir().toString());
        File[] files = file.listFiles();

        for (File tempFile : files) {
            //blackJin 이 들어가 있는 파일명을 찾습니다.
            if (tempFile.getName().contains(key)) {
                found = (tempFile.getName());
                String path = getCacheDir() + "/" + found;
                //파일경로로부터 비트맵을 생성합니다.
                bitmap = BitmapFactory.decodeFile(path);
            }
        }
        //blackJins 배열에 있는 파일 경로 중 하나를 랜덤으로 불러옵니다.
        return bitmap;
    }


    // POI 건들
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.d("mapP","please"+mapPOIItem.getItemName());

    }
    //그 위의 말풍선을 누르면 나오는 부뷴
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Log.d("mapPOIItem", mapPOIItem.getItemName());

        String selectedGroupTag = mapPOIItem.getItemName();

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_group, null);
        final TextView petNameTextView = (TextView) dialogView.findViewById(R.id.textView_groupPetName);
        final TextView petGenderTextView = (TextView) dialogView.findViewById(R.id.textView_groupPetGender);
        final TextView petSpeciesTextView = (TextView) dialogView.findViewById(R.id.textView_groupPetSpecies);
        final TextView userNameTextView = (TextView) dialogView.findViewById(R.id.textView_groupUserName);
        final TextView userGenderTextView = (TextView) dialogView.findViewById(R.id.textView_groupUserGender);
        final TextView userAgeTextView = (TextView) dialogView.findViewById(R.id.textView_groupUserAge);
        final TextView groupNameTextView = (TextView) dialogView.findViewById(R.id.textView_selectedgroupName);
        final ImageView petImageView = (ImageView) dialogView.findViewById(R.id.imageView_groupPetImage);
        final ImageView userImageView = (ImageView) dialogView.findViewById(R.id.imageView_groupUserImage);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot SelectedGroupData : dataSnapshot.child("walking_group").getChildren()) {
                    String checkGroupTag = SelectedGroupData.child("groupTag").getValue(String.class);

                    //체크된 그룹
                    if (selectedGroupTag.equals(checkGroupTag)) {
                        petNameTextView.setText(SelectedGroupData.child("petGender").getValue(String.class));
                        petGenderTextView.setText(SelectedGroupData.child("petName").getValue(String.class));
                        petSpeciesTextView.setText(SelectedGroupData.child("petSpecies").getValue(String.class));
                        userNameTextView.setText(SelectedGroupData.child("userName").getValue(String.class));
                        userAgeTextView.setText(SelectedGroupData.child("userAge").getValue(String.class));
                        userGenderTextView.setText(SelectedGroupData.child("userGender").getValue(String.class));
                        groupNameTextView.setText(SelectedGroupData.child("groupName").getValue(String.class));
                        getProfileImage(SelectedGroupData.child("petImage").getValue(String.class), petImageView);
                        getProfileImage(SelectedGroupData.child("userImage").getValue(String.class), userImageView);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("database_read_error", "Failed to read value.", error.toException());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int pos)
            {
                Log.d("dialog","here");
            }
        });
        builder.setNegativeButton("채팅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("dialog","채팅채팅");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Log.d("mapP","here");
    }

    //이거는 안 사용합니다.
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    // 현재위치
    //현재위치 업데이트
    int cycle = 0;
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float v) {

        if( cycle >2) {
            mapPointGeo = currentLocation.getMapPointGeoCoord();
            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latitude").setValue(mapPointGeo.latitude);
            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);
            Geocoder g = new Geocoder(this);
            List<Address> address = null;
            try {
                address = g.getFromLocation(mapPointGeo.latitude, mapPointGeo.longitude, 10);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("test", "입출력오류");
            }

            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("address").setValue(address.get(0).getThoroughfare());
            latitude_arrayList.add(mapPointGeo.latitude);
            longitude_arrayList.add(mapPointGeo.longitude);
            mapPolyLine.addPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
            mapView.addPolyline(mapPolyLine);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mapView.removeAllPOIItems();
                    //마커 초기화
                    markerCount = 0;
                    // 다른 그룹 위치 판별하기
                    String currentAddress = dataSnapshot.child("walking_group").child(String.valueOf(groupData.getId())).child("address").getValue(String.class);
                    String groupScope = dataSnapshot.child("walking_group").child(String.valueOf(groupData.getId())).child("scope").getValue(String.class);

                    for (DataSnapshot GroupMapData : dataSnapshot.child("walking_group").getChildren()) {
                        Log.d("mapP",GroupMapData.getKey());
                        String otherGroup = GroupMapData.getKey();
                        String otherAddress = GroupMapData.child("address").getValue(String.class);
                        String otherScope = GroupMapData.child("scope").getValue(String.class);
                        //자신 그룹은 제외 시킨다.
                        if (!otherGroup.equals(String.valueOf(groupData.getId()))) {
                            if (otherAddress.equals(currentAddress)) {
                                Log.d("database_read","address");
                                //다른 그룹 위치 띄우기
                                if(otherScope.equals("wholeScope")){
                                    Log.d("scope","here1");
                                    walkingOtherGroup(GroupMapData.child("latitude").getValue(Double.class), GroupMapData.child("longitude").getValue(Double.class), GroupMapData.child("groupTag").getValue(String.class));
                                }else if(otherScope.equals("followScope")){
                                    Log.d("follow","here Follow");
                                    for(int i =0 ; i <followingGroup.size();i++){
                                        Log.d("follow1",otherGroup);
                                        Log.d("follow1", String.valueOf(followingGroup.get(i)));
                                        if(otherGroup.equals(String.valueOf(followingGroup.get(i))))   {
                                            walkingOtherGroup(GroupMapData.child("latitude").getValue(Double.class), GroupMapData.child("longitude").getValue(Double.class), GroupMapData.child("groupTag").getValue(String.class));
                                        }
                                    }
                                }
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

            cycle = 1;
        }else if(cycle == 0){
            mapPointGeo = currentLocation.getMapPointGeoCoord();
            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("latitude").setValue(mapPointGeo.latitude);
            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("longitude").setValue(mapPointGeo.longitude);
            Geocoder g = new Geocoder(this);
            List<Address> address = null;
            try {
                address = g.getFromLocation(mapPointGeo.latitude, mapPointGeo.longitude, 10);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("test", "입출력오류");
            }

            mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("address").setValue(address.get(0).getThoroughfare());
            latitude_arrayList.add(mapPointGeo.latitude);
            longitude_arrayList.add(mapPointGeo.longitude);
            mapPolyLine.addPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
            mapView.addPolyline(mapPolyLine);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mapView.removeAllPOIItems();
                    //마커 초기화
                    markerCount = 0;
                    // 다른 그룹 위치 판별하기
                    String currentAddress = dataSnapshot.child("walking_group").child(String.valueOf(groupData.getId())).child("address").getValue(String.class);
                    for (DataSnapshot GroupMapData : dataSnapshot.child("walking_group").getChildren()) {
                        Log.d("mapP",GroupMapData.getKey());
                        String otherGroup = GroupMapData.getKey();
                        String otherAddress = GroupMapData.child("address").getValue(String.class);
                        String otherScope = GroupMapData.child("scope").getValue(String.class);
                        //자신 그룹은 제외 시킨다.
                        if (!otherGroup.equals(String.valueOf(groupData.getId()))) {
                            if (otherAddress.equals(currentAddress)) {
                                Log.d("database_read","address");
                                //다른 그룹 위치 띄우기
                                if(otherScope.equals("wholeScope")){
                                    Log.d("scope","here1");
                                    walkingOtherGroup(GroupMapData.child("latitude").getValue(Double.class), GroupMapData.child("longitude").getValue(Double.class), GroupMapData.child("groupTag").getValue(String.class));
                                }else if(otherScope.equals("followScope")){
                                    Log.d("follow","here Follow");
                                    for(int i =0 ; i <followingGroup.size();i++){
                                        Log.d("follow1",otherGroup);
                                        Log.d("follow1", String.valueOf(followingGroup.get(i)));
                                        if(otherGroup.equals(String.valueOf(followingGroup.get(i)))){
                                            walkingOtherGroup(GroupMapData.child("latitude").getValue(Double.class), GroupMapData.child("longitude").getValue(Double.class), GroupMapData.child("groupTag").getValue(String.class));
                                        }
                                    }
                                    // 팔로우 되어 있는 것만 보여주기
                                    // 산책 시작화면에서 미리 데이터
                                }

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
            cycle++;
        }
        else{
            cycle++;
        }




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
//        mapPointGeo = mapPoint.getMapPointGeoCoord();
//        latitude_arrayList.add(mapPointGeo.latitude);
//        longitude_arrayList.add(mapPointGeo.longitude);
//        mapPolyLine.addPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude,mapPointGeo.longitude));
//        mapView.addPolyline(mapPolyLine);

//        MapPointBounds mapPointBounds = new MapPointBounds(mapPolyLine.getMapPoints());
        int padding = 100; // px
        //mapView에 찍어준다.

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

package com.example.homekippa.ui.walk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GetFollowData;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.data.WeatheLocationResponse;
import com.example.homekippa.data.WeatherLocationData;
import com.example.homekippa.MapActivity;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.SingleItemPet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
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

    private ArrayList<SingleItemPet> petList = new ArrayList<>();
    private ArrayList followingArray = new ArrayList();

    private int petId;
    private String petName;
    private String petGender;
    private String petSpecies;
    private String petImageUrl = "";
    private GroupData groupData;
    private UserData userData;
    private int selectedPosition = 0;

    private Drawable drawable;

    private TextView textView_temperature;
    private TextView textView_weather;
    private TextView textView_scope;
    private ImageView imageView_weather;
    private Button button_startWalk;
    private RecyclerView listView_walk_pets;
    private CheckBox checkbox_wholeScope;
    private CheckBox checkbox_followScope;
    private CheckBox checkbox_closedScope;
    private Intent intent;
    private String userGender;

    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walkViewModel =
                ViewModelProviders.of(this).get(WalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_walk, container, false);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        textView_temperature = root.findViewById(R.id.textView_temperature);
        textView_weather = root.findViewById(R.id.textView_weather);
        imageView_weather = root.findViewById(R.id.imageView_weather);
        button_startWalk = root.findViewById(R.id.button_startWalk);
        listView_walk_pets = root.findViewById(R.id.listview_walk_pets);
        checkbox_closedScope = root.findViewById(R.id.checkbox_closedScope);
        checkbox_followScope = root.findViewById(R.id.checkbox_followScope);
        checkbox_wholeScope = root.findViewById(R.id.checkbox_wholeScope);
        intent = new Intent(getActivity(), MapActivity.class);
        textView_scope = root.findViewById(R.id.textView_scope);

        groupData = ((MainActivity) getActivity()).getGroupData();
        userData = ((MainActivity) getActivity()).getUserData();

        if (groupData != null) {
            getGroupData(userData.getGroupId());
        }


        userLocation = getMyLocation();
        setPetListView(listView_walk_pets);
        if (userLocation != null) {
            lat = userLocation.getLatitude();
            lon = userLocation.getLongitude();
            Log.d("weather_lat", String.valueOf(lat));
            Log.d("weather_lon", String.valueOf(lon));
        } else {
            Log.d("weather_error", "날씨 에러");
        }
        // lat하고 lon의 값을 받아와서 weatherLocation을 통해 서버로 값을 보낸다.


        weatherLocation(new WeatherLocationData(lat, lon));

        // 펫 id하고 userID를 넘겨준다.
        button_startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (petImageUrl.equals("")) {

                    petId = petList.get(0).getId();
                    petName = petList.get(0).getName();
                    petSpecies = petList.get(0).getSpecies();
                    petImageUrl = petList.get(0).getImage();
                    int petgender = petList.get(0).getGender();

                    if (petgender == 0) {
                        petGender = "암컷";

                    } else {
                        petGender = "수컷";

                    }
                }


                if (checkbox_wholeScope.isChecked() == false && checkbox_followScope.isChecked() == false && checkbox_closedScope.isChecked() == false) {
                    textView_scope.setError("공개범위 선택해주세요");
                } else {
                    intent.putExtra("groupData", groupData);
                    intent.putExtra("userData", userData);
                    intent.putExtra("petName", petName);
                    intent.putExtra("petSpecies", petSpecies);
                    intent.putExtra("petGender", petGender);
                    intent.putExtra("petImageUrl", petImageUrl);
                    startActivity(intent);
                }
            }
        });
        if (userData.getUserGender() == 1) {
            userGender = "남성";
        } else {
            userGender = "여성";
        }

        int yearIndex = userData.getUserBirth().indexOf("-");
        String birth = userData.getUserBirth().substring(0, yearIndex);
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int userAge = (nowYear - Integer.parseInt(birth)) + 1;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("walk");
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId()));
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("groupName").setValue(groupData.getName());
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("groupTag").setValue(groupData.getName() + groupData.getTag());
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("userName").setValue(userData.getUserName());
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("userImage").setValue(userData.getUserImage());
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("userGender").setValue(userGender);
        mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("userAge").setValue(userAge);


        checkbox_wholeScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("scope1", "scope1");

                mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("scope").setValue("wholeScope");
                checkbox_wholeScope.setChecked(true);
                checkbox_followScope.setChecked(false);
                checkbox_closedScope.setChecked(false);
                intent.putExtra("scope", "wholeScpe");
            }
        });

        checkbox_followScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("scope2", "scope2");

                mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("scope").setValue("followScope");
                checkbox_wholeScope.setChecked(false);
                checkbox_followScope.setChecked(true);
                checkbox_closedScope.setChecked(false);
                intent.putExtra("scope", "followScope");
                intent.putExtra("followingGroup", followingArray);
            }
        });

        checkbox_closedScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("scope3", "scope3");

                mDatabase.child("walking_group").child(String.valueOf(groupData.getId())).child("scope").setValue("closedScope");
                checkbox_wholeScope.setChecked(false);
                checkbox_followScope.setChecked(false);
                checkbox_closedScope.setChecked(true);
                intent.putExtra("scope", "closedScope");

            }
        });
        return root;
    }

    public void getGroupData(int ID) {


        service.getGroupData(ID).enqueue(new Callback<GroupData>() {
            @Override
            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                if (response.isSuccessful()) {
                    Log.d("그룹 확인", "성공");
                    groupData = response.body();
                    service.getFollow(ID).enqueue(new Callback<GetFollowData>() {
                        @Override
                        public void onResponse(Call<GetFollowData> call, Response<GetFollowData> response) {
                            if (response.isSuccessful()) {
                                Log.d("follow", "successful");
                                followingArray = (ArrayList) response.body().getFollowingList();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetFollowData> call, Throwable t) {
                            Log.d("로그인", "에러");
                            Log.e("로그인", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GroupData> call, Throwable t) {
                Log.d("그룹 확인", "에러");
                Log.e("그룹 확인", t.getMessage());
            }
        });


    }

    private void setPetListView(RecyclerView listView) {
        if (groupData != null) {
            service.getPetsData(groupData.getId()).enqueue(new Callback<List<SingleItemPet>>() {
                @Override
                public void onResponse(Call<List<SingleItemPet>> call, Response<List<SingleItemPet>> response) {
                    if (response.isSuccessful()) {
                        Log.d("반려동물 확인", "성공");
                        List<SingleItemPet> pets = response.body();
                        if (!pets.isEmpty()) {
                            petList.addAll(pets);
                            //TODO:나중에 바꿔야 할 부분. 일단 가장 처음 강아지의 아이디만을 petId라 해놓음!
                            petId = pets.get(0).getId();

                            ListPetAdapter petAdapter = new ListPetAdapter(petList);

                            LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                            pLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            listView.setLayoutManager(pLayoutManager);
                            listView.setItemAnimator(new DefaultItemAnimator());
                            listView.setAdapter(petAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SingleItemPet>> call, Throwable t) {
                    Log.d("반려동물 확인", "에러");
                    Log.e("반려동물 확인", t.getMessage());
                }
            });
        }

    }


    private void getPetProfileImage(ListPetAdapter.MyViewHolder holder, String url) {
        String[] w = url.split("/");
        String key = w[w.length - 1];

        Bitmap bit = getBitmapFromCacheDir(key);
        if (bit != null) {
            Glide.with(WalkFragment.this).load(bit).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(holder.petImage);
        } else {
            service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String TAG = "YesGroup";
                    if (response.isSuccessful()) {
                        InputStream is = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Glide.with(WalkFragment.this).load(bitmap).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(holder.petImage);
                        saveBitmapToJpeg(bitmap, key);
                    } else {
                        Log.d(TAG, "server contact failed");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "펫 사진 에러", Toast.LENGTH_SHORT).show();
                    Log.e("createGroup error", t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    private void saveBitmapToJpeg(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = (getContext()).getCacheDir();
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

    class ListPetAdapter extends RecyclerView.Adapter<ListPetAdapter.MyViewHolder> {
        private ArrayList<SingleItemPet> pet_Items;

        public ListPetAdapter(ArrayList<SingleItemPet> petItems) {
            this.pet_Items = petItems;
        }

        @NonNull
        @Override
        public ListPetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_pet, parent, false);
            List<View> itemViewList = new ArrayList<>();
            itemViewList.add(itemView);
            ListPetAdapter.MyViewHolder myViewHolder = new ListPetAdapter.MyViewHolder(itemView);

            return new ListPetAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ListPetAdapter.MyViewHolder holder, int position) {
            if (selectedPosition == position) {
                holder.pet.setBackgroundResource(R.drawable.round_button2);
            } else {
                holder.pet.setBackgroundResource(R.drawable.round_button);
            }
            setPetData(holder, position);
        }

        private void setPetData(ListPetAdapter.MyViewHolder holder, int position) {
            SingleItemPet selectedPet = pet_Items.get(position);
            holder.petName.setText(selectedPet.getName());
            getPetProfileImage(holder, selectedPet.getImage());
//            Glide.with(getActivity()).load(R.drawable.simplelogo).circleCrop().into(holder.petImage);
//            holder.petImage.setImageResource(R.drawable.simplelogo);
            holder.pet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    petId = petList.get(position).getId();
                    petName = petList.get(position).getName();
                    petSpecies = petList.get(position).getSpecies();
                    petImageUrl = petList.get(position).getImage();
                    int petgender = petList.get(position).getGender();

                    if (petgender == 0) {
                        petGender = "암컷";

                    } else {
                        petGender = "수컷";

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return pet_Items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView petName;
            ImageView petImage;
            LinearLayout pet;

            MyViewHolder(View view) {
                super(view);
                pet = (LinearLayout) view.findViewById(R.id.pet);
                petName = (TextView) view.findViewById(R.id.listitem_PetName);
                petImage = (ImageView) view.findViewById(R.id.listitem_PetImage);

            }
        }
    }

    private Bitmap getBitmapFromCacheDir(String key) {
        String found = null;
        Bitmap bitmap = null;
        File file = new File(getContext().getCacheDir().toString());
        File[] files = file.listFiles();

        for (File tempFile : files) {
            //blackJin 이 들어가 있는 파일명을 찾습니다.
            if (tempFile.getName().contains(key)) {
                found = (tempFile.getName());
                String path = getContext().getCacheDir() + "/" + found;
                //파일경로로부터 비트맵을 생성합니다.
                bitmap = BitmapFactory.decodeFile(path);
            }
        }
        //blackJins 배열에 있는 파일 경로 중 하나를 랜덤으로 불러옵니다.
        return bitmap;
    }


    private Location getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {
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
        Log.i("weather_create", "create");
        service.getWeatehrData(data).enqueue(new Callback<WeatheLocationResponse>() {
            @Override
            public void onResponse(Call<WeatheLocationResponse> call, Response<WeatheLocationResponse> response) {
                WeatheLocationResponse result = response.body();

                textView_temperature.setText(result.getCurrent_temperature() + "º");
                weather(result.getCurrent_weather());
                if (result.getCode() == 200) {
                    Log.d("weather", "server connect");
                } else {
                    Log.d("weather", "server disconnect");
                }
            }

            @Override
            public void onFailure(Call<WeatheLocationResponse> call, Throwable t) {
                Log.d("weather_fail", "server not response");
                t.printStackTrace();
            }

        });
    }

    private void weather(String weather) {
        weather = weather.toLowerCase();

        if (weather.equals("rain")) {
            drawable = getResources().getDrawable(R.drawable.rain);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("비");

        } else if (weather.equals("snow")) {
            drawable = getResources().getDrawable(R.drawable.snow);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("눈");
        } else if (weather.equals("thunderstorm")) {
            drawable = getResources().getDrawable(R.drawable.thunderstorm);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("천둥번개");
        } else if (weather.equals("drizzle")) {
            drawable = getResources().getDrawable(R.drawable.drizzle);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("보슬보슬비");
        } else if (weather.equals("clouds")) {
            drawable = getResources().getDrawable(R.drawable.cloud);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("구름");
        } else if (weather.equals("clear")) {
            drawable = getResources().getDrawable(R.drawable.clear);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("맑음");
        } else if (weather.equals("haze")) {
            drawable = getResources().getDrawable(R.drawable.haze);
            imageView_weather.setImageDrawable(drawable);
            textView_weather.setText("안개");
        }
    }
}



package com.example.homekippa.ui.group;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homekippa.AddPetActivity;
import com.example.homekippa.CreateDailyWorkActivity;
import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.data.DoneReportsResponse;
import com.example.homekippa.data.FollowData;
import com.example.homekippa.data.FollowResponse;
import com.example.homekippa.data.GetGroupImageResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupInviteData;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.data.UserData;
import com.example.homekippa.function.Loading;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YesGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YesGroup extends Fragment {
    private static final String ARG_PARAM1 = "object";
    private UserData userData;
    private GroupData groupData;
    private boolean myGroup;
    private int petId; // 나중에 버튼 누르면 현재의 펫 아이디가 바뀌어져 일과를 추가할때 함께 인텐트에 실어보냄

    private ServiceApi service;
    final Loading loading = new Loading();

    private ArrayList<SingleItemPet> petList = new ArrayList<>();

    private PetViewModel petViewModel;

    private TextView tv_groupName;
    private TextView tv_groupIntro;
    private Button button_Add_DW;
    private RecyclerView listView_pets;
    private RecyclerView listView_dailyWorks;
    private CircleImageView imageView_groupProfile;
    private Button button_addPet;
    private Button button_addUser;
    private Button button_join_group;
    private Button button_changeGroupCover;
    private Button button_follow_group;

    private ViewGroup root;

    private int selectedPosition = 0;

    public static YesGroup newInstance() {
        return new YesGroup();
    }

    Bitmap groupProfileBitmap;
    Bitmap petProfileBitmap;

    private String mParam1;

    public YesGroup() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static YesGroup newInstance(int position) {
        YesGroup fragment = new YesGroup();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        userData = ((MainActivity) getActivity()).getUserData();
        groupData = (GroupData) getArguments().get("groupData");
        myGroup = (boolean) getArguments().get("myGroup");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setPetListView(listView_pets);
    }

    @Override
    public void onStart() {
        super.onStart();

        tv_groupName = root.findViewById(R.id.textView_groupName);
        tv_groupIntro = root.findViewById(R.id.textView_groupIntro);
        button_Add_DW = root.findViewById(R.id.button_Add_DW);
        button_addPet = root.findViewById(R.id.button_AddPet);
        button_addUser = root.findViewById(R.id.button_Add_User);
        button_join_group = root.findViewById(R.id.button_join_group);
        button_changeGroupCover = root.findViewById(R.id.button_changeGroupCover);
        button_follow_group = root.findViewById(R.id.button_follow_group);
        listView_pets = root.findViewById(R.id.listview_pets);
        listView_dailyWorks = root.findViewById(R.id.listview_dailywork);
        imageView_groupProfile = root.findViewById(R.id.ImageView_groupProfile);


        tv_groupName.setText(groupData.getName());
        tv_groupIntro.setText(groupData.getIntroduction());
        getGroupProfileImage(groupData.getImage(), imageView_groupProfile);
        setPetListView(listView_pets);
//        setDailyWorkListView(listView_dailyWorks);

        Glide.with(YesGroup.this).load(R.drawable.dog_woong).circleCrop().into(imageView_groupProfile);
        if (!myGroup) {
            button_join_group.setVisibility(View.VISIBLE);
            button_follow_group.setVisibility(View.VISIBLE);
            button_addUser.setVisibility(View.INVISIBLE);
            button_addPet.setVisibility(View.INVISIBLE);
            button_Add_DW.setVisibility(View.INVISIBLE);
            button_changeGroupCover.setVisibility(View.INVISIBLE);

            button_follow_group.setActivated(true);
        }
        button_follow_group.setOnClickListener(new View.OnClickListener() {
            GroupData myGroup = ((MainActivity) getActivity()).getGroupData();

            @Override
            public void onClick(View v) {
                if (button_follow_group.isActivated()) {
                    service.followGroup(new FollowData(myGroup.getId(), groupData.getId())).enqueue(new Callback<FollowResponse>() {
                        @Override
                        public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("follow", "success");
                                button_follow_group.setActivated(false);
                                button_follow_group.setText("팔로잉");
                            }
                        }

                        @Override
                        public void onFailure(Call<FollowResponse> call, Throwable t) {
                            Log.d("follow", "fail");
                        }
                    });
                } else {
                    service.unfollowGroup(new FollowData(myGroup.getId(), groupData.getId())).enqueue(new Callback<FollowResponse>() {
                        @Override
                        public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("follow cancel", "success");
                                button_follow_group.setActivated(true);
                                button_follow_group.setText("팔로우");
                            }
                        }

                        @Override
                        public void onFailure(Call<FollowResponse> call, Throwable t) {
                            Log.d("follow", "fail");
                        }
                    });
                }

            }
        });

        button_join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.acceptInvite(new GroupInviteData(groupData, null, userData)).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        if (response.isSuccessful()) {
                            userData = response.body();
                            MainActivity mainActivity = (MainActivity) getActivity();
/*                            mainActivity.setUserData(userData);
                            mainActivity.setGroupData(groupData);
                            mainActivity.getNavView().getMenu().getItem(4).setChecked(true);

                            GroupFragment groupFragment = new GroupFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("groupData", groupData);
                            groupFragment.setArguments(bundle);
                            mainActivity.changeFragment(groupFragment);*/

                            mainActivity.finish();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("user", userData);
                            intent.putExtra("group", groupData);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(getContext(), "수락 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        button_Add_DW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateDailyWorkActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("groupData", groupData);
                intent.putExtra("petId", petId);
                Log.d("넘겨넘겨~", String.format("%d", petId));
                startActivity(intent);
            }
        });

        button_addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPetActivity.class);
                intent.putExtra("groupData", groupData);
                startActivity(intent);
            }
        });

        button_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupInviteActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("groupData", groupData);
                startActivity(intent);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_yes_group, container, false);

        return root;
    }

    private void setDailyWorkListView(RecyclerView listView, int petId) {

        Log.d("펫아이디", String.format("%d", petId));
        service.getDailyWorkData(petId).enqueue(new Callback<List<SingleItemDailyWork>>() {
            @Override
            public void onResponse(Call<List<SingleItemDailyWork>> call, Response<List<SingleItemDailyWork>> response) {
                Log.d("헤에", response.toString());
                if (response.isSuccessful()) {
                    Log.d("일과 확인", "성공");
                    List<SingleItemDailyWork> reports = response.body();
                    ArrayList<SingleItemDailyWork> dailyWorkList = new ArrayList<>();

                    if (!reports.isEmpty()) {
                        //ArrayList<SingleItemDailyWork> dailyWorkList = new ArrayList<>()
                        dailyWorkList.addAll(reports);
                        ListDailyWorkAdapter dailyWorkAdapter = new ListDailyWorkAdapter(dailyWorkList);

                        LinearLayoutManager dLayoutManager = new LinearLayoutManager(getActivity());
                        dLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        listView.setLayoutManager(dLayoutManager);
                        listView.setItemAnimator(new DefaultItemAnimator());
                        listView.setBackgroundColor(Color.parseColor("#ffffff"));
                        listView.setAdapter(dailyWorkAdapter);
                    } else {
                        listView.setBackgroundResource(R.drawable.no_dailywork);
                        listView.setItemAnimator(new DefaultItemAnimator());
                        ListDailyWorkAdapter dailyWorkAdapter = new ListDailyWorkAdapter(dailyWorkList);

                        listView.setAdapter(dailyWorkAdapter);

                    }

                }
            }

            @Override
            public void onFailure(Call<List<SingleItemDailyWork>> call, Throwable t) {
                Log.e("일과 확인 에러", t.getMessage());
            }


        });


    }

    private void getGroupProfileImage(String url, CircleImageView imageView) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "YesGroup";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(YesGroup.this).load(bitmap).circleCrop().into(imageView);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void getPetProfileImage(ListPetAdapter.MyViewHolder holder, String url) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "YesGroup";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(getActivity()).load(bitmap).circleCrop().into(holder.petImage);
                    holder.petImage.setImageBitmap(bitmap);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void setPetListView(RecyclerView listView) {

        service.getPetsData(groupData.getId()).enqueue(new Callback<List<SingleItemPet>>() {
            @Override
            public void onResponse(Call<List<SingleItemPet>> call, Response<List<SingleItemPet>> response) {
                if (response.isSuccessful()) {
                    Log.d("반려동물 확인", "성공");
                    List<SingleItemPet> pets = response.body();

                    petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);

                    petViewModel.getPetList().setValue(pets);

                    if (!pets.isEmpty()) {
                        petList.clear();
                        petList.addAll(pets);
                        //TODO:나중에 바꿔야 할 부분. 일단 가장 처음 강아지의 아이디만을 petId라 해놓음!
                        petId = pets.get(0).getId();
                        Log.d("펫아이디2", String.format("%d", petId));
                        setDailyWorkListView(listView_dailyWorks, pets.get(selectedPosition).getId());
                    }
                    ListPetAdapter petAdapter = new ListPetAdapter(petList);

                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                    pLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listView.setLayoutManager(pLayoutManager);
                    listView.setItemAnimator(new DefaultItemAnimator());
                    listView.setAdapter(petAdapter);


                }
            }

            @Override
            public void onFailure(Call<List<SingleItemPet>> call, Throwable t) {
                Log.d("반려동물 확인", "에러");
                Log.e("반려동물 확인", t.getMessage());
            }
        });
    }


    class ListDailyWorkAdapter extends RecyclerView.Adapter<ListDailyWorkAdapter.MyViewHolder2> {
        private ArrayList<SingleItemDailyWork> dailyWorks_Items;

        public ListDailyWorkAdapter(ArrayList<SingleItemDailyWork> dailyWorks_Items) {
            this.dailyWorks_Items = dailyWorks_Items;
        }

        @NonNull
        @Override
        public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_dailywork, parent, false);
            return new MyViewHolder2(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
            setDailyWorkData(holder, position);
        }

        private void setDailyWorkData(MyViewHolder2 holder, int position) {
            SingleItemDailyWork dailyWork = dailyWorks_Items.get(position);
            Log.d("흐아아", dailyWork.toString());
            holder.workName.setText(dailyWork.getTitle());
            holder.workTime.setText(dailyWork.getTime());
            holder.workAlarm.setText(dailyWork.getAlarm());
            holder.workDone.setText(dailyWork.getDone_time());

            if (dailyWork.getDone() != 0) {
                holder.workCheck.setBackgroundResource(R.drawable.round_button4);
                holder.workName.setTextColor(Color.parseColor("#FFFFFF"));
                holder.workTime.setTextColor(Color.parseColor("#FFFFFF"));
                holder.workAlarm.setTextColor(Color.parseColor("#FFFFFF"));
                getUserProfileImage(holder.workPersonImage, dailyWork.getDone_user_image());
                holder.workDone.setTextColor(Color.parseColor("#FFFFFF"));

            }

            holder.workCheck.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Log.d("여기왔어", "꺄륵");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("[ " + holder.workName.getText() + " ]" + " 일과를 수행하셨습니까?");
                    builder.setMessage("일과를 완료한 시각과 사용자님의 정보가 기록됩니다.");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    service.doneDailyWork(dailyWork.getId(), userData.getUserId(), userData.getUserImage()).enqueue(new Callback<DoneReportsResponse>() {
                                        @Override
                                        public void onResponse(Call<DoneReportsResponse> call, Response<DoneReportsResponse> response) {
                                            Log.d("헤에", response.toString());

                                        }

                                        @Override
                                        public void onFailure(Call<DoneReportsResponse> call, Throwable t) {
                                            Log.e("일과 완료(갱신) 에러", t.getMessage());
                                        }
                                    });

                                    Toast.makeText(v.getContext(), "일과가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                    Calendar currentTime = Calendar.getInstance();
                                    int hour24hrs = currentTime.get(Calendar.HOUR_OF_DAY);
                                    int minutes = currentTime.get(Calendar.MINUTE);

                                    holder.workCheck.setBackgroundResource(R.drawable.round_button4);
                                    holder.workDone.setText(hour24hrs + ":" + minutes);
                                    holder.workName.setTextColor(Color.parseColor("#FFFFFF"));
                                    holder.workTime.setTextColor(Color.parseColor("#FFFFFF"));
                                    holder.workAlarm.setTextColor(Color.parseColor("#FFFFFF"));
                                    getUserProfileImage(holder.workPersonImage, dailyWork.getDone_user_image());
                                    holder.workDone.setTextColor(Color.parseColor("#FFFFFF"));
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(v.getContext(), "일과 수행 후 다시 눌러주세요!", Toast.LENGTH_SHORT).show();
                                }
                            });

                    builder.show();
                }
            });
            //make image circled
            // Glide.with(getActivity()).load(R.drawable.base_cover).circleCrop().into(holder.workPersonImage);
            //  holder.workPersonImage.setImageResource(dailyWork.getWorkImage());
        }

        @Override
        public int getItemCount() {
            return dailyWorks_Items.size();
        }

        class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView workName;
            TextView workTime;
            TextView workAlarm;
            RelativeLayout workCheck;
            ImageView workPersonImage;
            TextView workDone;
            TextView workNameTitle;

            MyViewHolder2(View view) {
                super(view);
                //workNameTitle =

                workName = (TextView) view.findViewById(R.id.textView_workName);
                workPersonImage = (ImageView) view.findViewById(R.id.personImage);
                workTime = (TextView) view.findViewById(R.id.textView_workTime);
                workAlarm = (TextView) view.findViewById(R.id.textView_workAlarm);
                workCheck = (RelativeLayout) view.findViewById(R.id.work_Check);
                workDone = (TextView) view.findViewById(R.id.textView_workDone);


                workName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d("여기왔어", "꺄륵");
                    }
                });
            }
        }


    }

    class ListPetAdapter extends RecyclerView.Adapter<ListPetAdapter.MyViewHolder> {
        private ArrayList<SingleItemPet> pet_Items;

        public ListPetAdapter(ArrayList<SingleItemPet> petItems) {
            this.pet_Items = petItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_pet, parent, false);
            List<View> itemViewList = new ArrayList<>();
            itemViewList.add(itemView);
            MyViewHolder myViewHolder = new MyViewHolder(itemView);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (selectedPosition == position) {
                holder.pet.setBackgroundResource(R.drawable.round_button2);
            } else {
                holder.pet.setBackgroundResource(R.drawable.round_button);
            }
            setPetData(holder, position);
        }

        private void setPetData(MyViewHolder holder, int position) {
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
                    setDailyWorkListView(listView_dailyWorks, selectedPet.getId());
                    petId = petList.get(position).getId();
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


    private void getUserProfileImage(ImageView userProfile, String img) {
        service.getProfileImage(img).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "MainActivity";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(getActivity()).load(bitmap).circleCrop().into(userProfile);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
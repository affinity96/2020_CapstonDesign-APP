package com.example.homekippa.ui.group;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homekippa.AddPetActivity;
import com.example.homekippa.Cache;
import com.example.homekippa.CreateDailyWorkActivity;
import com.example.homekippa.EditDailyWorkActivity;
import com.example.homekippa.ImageTask;
import com.example.homekippa.MainActivity;
import com.example.homekippa.ModifyPetActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.DoneReportsResponse;
import com.example.homekippa.data.FollowData;
import com.example.homekippa.data.FollowResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupInviteData;
import com.example.homekippa.data.ModifyGroupResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.function.Loading;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YesGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YesGroup extends Fragment {
    public static YesGroup context_YesGroup;
    private static final String TAG = "YesGroup";
    private static final String ARG_PARAM1 = "object";
    private UserData userData;
    private GroupData groupData;
    private boolean myGroup;
    private int selectedPosition = 0;
    private int petId; // 나중에 버튼 누르면 현재의 펫 아이디가 바뀌어져 일과를 추가할때 함께 인텐트에 실어보냄

    private Cache cache;
    private ServiceApi service;
    private ViewGroup root;
    final Loading loading = new Loading();

    private PetViewModel petViewModel;
    private GroupFollowViewModel followViewModel;
    private GroupViewModel groupViewModel;

    private TextView tv_groupName;
    private TextView tv_groupIntro;
    private Button button_Add_DW;
    private Button button_modify_pet;
    private RecyclerView listView_pets;
    private RecyclerView listView_dailyWorks;
    private ImageView imageView_groupProfile;
    private ImageView imageView_groupCover;
    private Button button_addPet;
    private Button button_addUser;
    private Button button_join_group;
    private Button button_changeGroupCover;
    private Button button_follow_group;
    private Button button_changeProfile;
    private TextView textView_followingNum;
    private TextView textView_followerNum;
    private TextView textView_members;
    private LinearLayout ll_follower;
    private LinearLayout ll_following;
    private CharSequence[] members;
    private MainActivity main;

    private ArrayList<SingleItemPet> petList = new ArrayList<>();

    public File tempFile;
    private Boolean isPermission = true;


    private String mParam1;

    public YesGroup() {
        // Required empty public constructor
    }

    public static YesGroup newInstance() {
        return new YesGroup();
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
        context_YesGroup = this;
        main = (MainActivity) getActivity();

        Log.d("yes", "oncreage");
        cache = new Cache(getContext());
        service = RetrofitClient.getClient().create(ServiceApi.class);

        followViewModel = new ViewModelProvider(requireActivity()).get(GroupFollowViewModel.class);
        groupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);

        userData = main.getUserData();
        groupData = (GroupData) getArguments().get("groupData");
        myGroup = (boolean) getArguments().get("myGroup");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public void onResume() {

        super.onResume();
        Log.d("yes", "onresume");
//        if (myGroup) {
//            groupData = ((MainActivity) getActivity()).getGroupData();
//        }
//        getImage(groupData.getImage(), imageView_groupProfile, true);
//        getImage(groupData.getCover(), imageView_groupCover, false);


    }

    @Override
    public void onStart() {

        super.onStart();
        groupData = (GroupData) getArguments().get("groupData");
        setPetListView(listView_pets);
        Log.d("yes", "onstart");
        if (myGroup) {
            groupData = ((MainActivity) getActivity()).getGroupData();
            Log.d("갱신", groupData.getName());
        }

        tv_groupName.setText(groupData.getName());
        tv_groupIntro.setText(groupData.getIntroduction());

        if (!myGroup && groupData != null) {
            button_join_group.setVisibility(View.VISIBLE);
            button_follow_group.setVisibility(View.VISIBLE);
            button_addUser.setVisibility(View.INVISIBLE);
            button_addPet.setVisibility(View.INVISIBLE);
            button_Add_DW.setVisibility(View.INVISIBLE);
            button_modify_pet.setVisibility(View.INVISIBLE);
            button_changeGroupCover.setVisibility(View.INVISIBLE);
            button_changeProfile.setVisibility(View.INVISIBLE);

            boolean isfollowed = followViewModel.checkFollow(groupData.getId());

            if (isfollowed) {
                button_follow_group.setActivated(false);
                button_follow_group.setText("팔로잉");
            } else {
                button_follow_group.setActivated(true);
                button_follow_group.setText("팔로우");
            }
        } else {
            ll_follower.setVisibility(View.VISIBLE);
            ll_following.setVisibility(View.VISIBLE);
            if (followViewModel.getFollowerNum() != null) {
                textView_followerNum.setText(String.valueOf(followViewModel.getFollowerNum()));
            } else textView_followerNum.setText(String.valueOf(0));

            if (followViewModel.getFollowerNum() != null)
                textView_followingNum.setText(String.valueOf(followViewModel.getFollowingNum()));
            else textView_followingNum.setText(String.valueOf(0));
        }




        textView_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.getMemberData(groupData.getId()).enqueue(new Callback<List<UserData>>() {
                    @Override
                    public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                        if (response.isSuccessful()) {

                            List<String> userNames = new ArrayList<>();
                            for (UserData user : response.body()) {
                                userNames.add(user.getUserName());
                            }
                            members = userNames.toArray(new CharSequence[userNames.size()]);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("그룹원")
                                    .setItems(members, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getContext(), members[which], Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            Log.d("group", "did not success getting member data");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserData>> call, Throwable t) {
                        Log.e("group", t.getMessage());
                    }
                });


            }
        });
        button_changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ModifyGroupActivity.class);
                startActivity(intent);
            }
        });

        button_follow_group.setOnClickListener(new View.OnClickListener() {
            GroupData myG = ((MainActivity) getActivity()).getGroupData();

            @Override
            public void onClick(View v) {
                if (button_follow_group.isActivated()) {
                    service.followGroup(new FollowData(myG.getId(), groupData.getId())).enqueue(new Callback<FollowResponse>() {
                        @Override
                        public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("follow", "success");
                                followViewModel.addFollowing(groupData.getId());
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
                    service.unfollowGroup(new FollowData(myG.getId(), groupData.getId())).enqueue(new Callback<FollowResponse>() {
                        @Override
                        public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("follow cancel", "success");
                                followViewModel.cancelFollowing(groupData.getId());
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

        button_changeGroupCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PopupSelectCover.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);
            }
        });

        button_modify_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyPetActivity.class);
                intent.putExtra("petId", petList.get(selectedPosition).getId());
                intent.putExtra("petName", petList.get(selectedPosition).getName());
                intent.putExtra("petSpecies", petList.get(selectedPosition).getSpecies());
                intent.putExtra("petGender", petList.get(selectedPosition).getGender());
                intent.putExtra("petImage", petList.get(selectedPosition).getImage());
                intent.putExtra("petNeutrality", petList.get(selectedPosition).getNeutrality());
                intent.putExtra("petBirth", petList.get(selectedPosition).getBirth());
                startActivity(intent);
            }
        });

        button_Add_DW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(petList.size() == 0){
                    Log.d("여왔냐","여왔아");
                    Toast.makeText(getActivity(), "등록된 반려동물이 없습니다. 반려동물을 등록해보세요!", Toast.LENGTH_LONG).show();

                }else{
                    Intent intent = new Intent(getActivity(), CreateDailyWorkActivity.class);
                    intent.putExtra("userData", userData);
                    intent.putExtra("groupData", groupData);
                    intent.putExtra("petId", petList.get(selectedPosition).getId());
                    Log.d("넘겨넘겨~", String.format("%d", petId));
                    startActivity(intent);
                }

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

        getImage(groupData.getImage(), imageView_groupProfile, true);
        getImage(groupData.getCover(), imageView_groupCover, false);
        setPetListView(listView_pets);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_yes_group, container, false);
        tv_groupName = root.findViewById(R.id.textView_groupName);
        tv_groupIntro = root.findViewById(R.id.textView_groupIntro);
        button_Add_DW = root.findViewById(R.id.button_Add_DW);
        button_modify_pet = root.findViewById(R.id.button_modify_pet);
        button_addPet = root.findViewById(R.id.button_AddPet);
        button_addUser = root.findViewById(R.id.button_Add_User);
        button_join_group = root.findViewById(R.id.button_join_group);
        button_changeGroupCover = root.findViewById(R.id.button_changeGroupCover);
        button_follow_group = root.findViewById(R.id.button_follow_group);
        button_changeProfile = root.findViewById(R.id.button_changeProfile);
        listView_pets = root.findViewById(R.id.listview_pets);
        listView_dailyWorks = root.findViewById(R.id.listview_dailywork);
        imageView_groupCover = root.findViewById(R.id.ImageView_groupCover);
        imageView_groupProfile = root.findViewById(R.id.ImageView_groupProfile);
        imageView_groupCover = root.findViewById(R.id.ImageView_groupCover);
        textView_followerNum = root.findViewById(R.id.textView__followerNum);
        textView_followingNum = root.findViewById(R.id.textView__followingNum);
        ll_follower = root.findViewById(R.id.linearLayout_follower);
        ll_following = root.findViewById(R.id.linearLayout_following);
        textView_members = root.findViewById(R.id.textView_groupMembers);

//        if (myGroup) {
//            groupData = ((MainActivity) getActivity()).getGroupData();
//            Log.d("yes profile_createview", groupData.getImage());
//        }
//
//        getImage(groupData.getImage(), imageView_groupProfile, true);
//        getImage(groupData.getCover(), imageView_groupCover, false);
//        setPetListView(listView_pets);


        return root;
    }

    private void setDailyWorkListView(RecyclerView listView, int petId) {

        service.getDailyWorkData(petId).enqueue(new Callback<List<SingleItemDailyWork>>() {
            @Override
            public void onResponse(Call<List<SingleItemDailyWork>> call, Response<List<SingleItemDailyWork>> response) {
                if (response.isSuccessful()) {

                    List<SingleItemDailyWork> reports = response.body();
                    ArrayList<SingleItemDailyWork> dailyWorkList = new ArrayList<>();

                    if (!reports.isEmpty()) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        } else {
            if (requestCode == 1) {
                setImage();
//            } else if(requestCode == 3) {
//                Log.d("yes", "받아온거니?");
//                groupData = (GroupData)intent.getSerializableExtra("groupData");
//                ((MainActivity)getActivity()).setGroupData(groupData);
            }
        }
    }

    private void setImage() {
        if (tempFile != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            String str_groupId = String.valueOf(groupData.getId());


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), str_groupId);

            service.uploadGroupCover(id, uploadFile).enqueue(new Callback<ModifyGroupResponse>() {
                @Override
                public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                    ModifyGroupResponse result = response.body();

                    Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {
                        service.getGroupData(((MainActivity) MainActivity.context_main).getGroupData().getId()).enqueue(new Callback<GroupData>() {
                            @Override
                            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                                GroupData group = response.body();
                                main.setGroupData(group);
                                Log.d("yes modify", group.getImage());
                                imageView_groupCover.setImageBitmap(originalBm);
                                cache.saveBitmapToJpeg(originalBm, group.getCover());
                            }

                            @Override
                            public void onFailure(Call<GroupData> call, Throwable t) {

                            }
                        });
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "그룹 커버 등록 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            service.setGroupCoverDefault(groupData.getId()).enqueue(new Callback<ModifyGroupResponse>() {
                @Override
                public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                    ModifyGroupResponse result = response.body();

                    Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {
                        service.getGroupData(((MainActivity) MainActivity.context_main).getGroupData().getId()).enqueue(new Callback<GroupData>() {
                            @Override
                            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                                GroupData group = response.body();
                                main.setGroupData(group);
                                Log.d("yes modify", group.getImage());
                                imageView_groupCover.setImageResource(R.drawable.base_cover);
                            }

                            @Override
                            public void onFailure(Call<GroupData> call, Throwable t) {

                            }
                        });
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "그룹 커버 등록 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        }

    }

    private void getImage(String url, ImageView imageView, boolean isprofile) {

        String[] w = url.split("/");
        String key = w[w.length - 1];

        Bitmap bit = cache.getBitmapFromCacheDir(key);
        if (bit != null) {
            if (isprofile) {
                Glide.with(YesGroup.this).load(bit).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(imageView);
            } else {
                Glide.with(YesGroup.this).load(bit).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
            }

        } else {
//            ImageLoadTask task = new ImageLoadTask(url, imageView, getContext(), false);
//            task.execute();
            ImageTask task = new ImageTask(url, imageView, getContext(), !isprofile);
            task.getImage();
        }

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

                        setDailyWorkListView(listView_dailyWorks, pets.get(selectedPosition).getId());

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
                    Log.d("userData", userData.getUserImage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    dailyWork.setDone_user_image(userData.getUserImage());
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
                                    Log.d("daily", dailyWork.getDone_user_image());

                                    holder.workCheck.setBackgroundResource(R.drawable.round_button4);
                                    holder.workDone.setText(hour24hrs + ":" + minutes);
                                    holder.workName.setTextColor(Color.parseColor("#FFFFFF"));
                                    holder.workTime.setTextColor(Color.parseColor("#FFFFFF"));
                                    holder.workAlarm.setTextColor(Color.parseColor("#FFFFFF"));
                                    getImage(dailyWork.getDone_user_image(), holder.workPersonImage, true);
//                                    getUserProfileImage(holder.workPersonImage, dailyWork.getDone_user_image());
                                    holder.workDone.setTextColor(Color.parseColor("#FFFFFF"));
                                }
                            });
                    builder.setNeutralButton("일과 수정하기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), EditDailyWorkActivity.class);
                                    intent.putExtra("id", dailyWork.getId());
                                    intent.putExtra("title", dailyWork.getTitle());
                                    intent.putExtra("desc", dailyWork.getDesc());
                                    intent.putExtra("time", dailyWork.getTime());
                                    intent.putExtra("alarm", dailyWork.getAlarm());

                                    startActivity(intent);
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
//            List<View> itemViewList = new ArrayList<>();


//            itemViewList.add(itemView);
//            MyViewHolder myViewHolder = new MyViewHolder(itemView);

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
            petId = petList.get(position).getId();
            Log.d("pet", String.valueOf(selectedPet.getName()));
            getImage(selectedPet.getImage(), (ImageView) holder.petImage, true);
            holder.pet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    setDailyWorkListView(listView_dailyWorks, selectedPet.getId());
                    petId = petList.get(position).getId();
                    Log.d("pet click", String.valueOf(petId));
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
                    if (bitmap != null && getActivity() != null) {
                        Glide.with(getActivity()).load(bitmap).circleCrop().into(userProfile);
                    }
                } else {
                    Log.d(TAG, "server profile contact failed");
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
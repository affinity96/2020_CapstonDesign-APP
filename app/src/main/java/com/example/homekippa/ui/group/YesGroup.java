package com.example.homekippa.ui.group;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homekippa.AddPetActivity;
import com.example.homekippa.CreateDailyWorkActivity;
import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.function.Loading;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private int petId; // 나중에 버튼 누르면 현재의 펫 아이디가 바뀌어져 일과를 추가할때 함께 인텐트에 실어보냄

    private ServiceApi service;
    final Loading loading = new Loading();


    private ArrayList<SingleItemPet> petList = new ArrayList<>();
    private ArrayList<SingleItemDailyWork> dailyWorkList = new ArrayList<>();

    private TextView tv_groupName;
    private TextView tv_groupIntro;
    private Button button_Add_DW;
    private RecyclerView listView_pets;
    private RecyclerView listView_dailyWorks;
    private CircleImageView imageView_groupProfile;
    private Button button_addPet;

    public static YesGroup newInstance() {
        return new YesGroup();
    }

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
        Log.d("user", userData.getUserName());

        groupData = ((MainActivity) getActivity()).getGroupData();
        Log.d("group", groupData.getGroupName());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_yes_group, container, false);

        tv_groupName = root.findViewById(R.id.textView_groupName);
        tv_groupIntro = root.findViewById(R.id.textView_groupIntro);
        button_Add_DW = root.findViewById(R.id.button_Add_DW);
        button_addPet = root.findViewById(R.id.button_AddPet);

        button_Add_DW.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateDailyWorkActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("groupData", groupData);
                intent.putExtra("petId", petId);
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

        listView_pets = root.findViewById(R.id.listview_pets);
        listView_dailyWorks = root.findViewById(R.id.listview_dailywork);
        imageView_groupProfile = root.findViewById(R.id.ImageView_groupProfile);

        tv_groupName.setText(groupData.getGroupName());
        tv_groupIntro.setText(groupData.getGroupIntro());

        setPetListView(listView_pets);
        setDailyWorkListView(listView_dailyWorks);

        Glide.with(YesGroup.this).load(R.drawable.dog_woong).circleCrop().into(imageView_groupProfile);
        return root;
    }

    private void setDailyWorkListView(RecyclerView listView) {
        getDailyWorkData();
        ListDailyWorkAdapter workAdapter = new ListDailyWorkAdapter(dailyWorkList);

        LinearLayoutManager dLayoutManager = new LinearLayoutManager(getActivity());
        dLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(dLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(workAdapter);
    }

    private void setPetListView(RecyclerView listView) {
//        getPetData(listView);
        Log.d("반려동물 확인", "들어옴");
        service.getPetsData(groupData.getGroupId()).enqueue(new Callback<List<SingleItemPet>>() {
            @Override
            public void onResponse(Call<List<SingleItemPet>> call, Response<List<SingleItemPet>> response) {
                if (response.isSuccessful()) {
                    Log.d("반려동물 확인", "성공");
                    List<SingleItemPet> pets = response.body();
                    Log.d("반려동물 아이디 확인", pets.get(0).getName());
                    petList.addAll(pets);

                    petId = pets.get(0).getId();//나중에 바꿔야 할 부분. 일단 가장 처음 강아지의 아이디만을 petId라 해놓음!

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

    private void getDailyWorkData() {
        SingleItemDailyWork dailyWork = new SingleItemDailyWork("밥", "PM 10:23", "PM 11:23", "시은", R.drawable.base_cover);
        dailyWorkList.add(dailyWork);
        dailyWork = new SingleItemDailyWork("간식", "PM 08:00", "PM 09:23", "시은", R.drawable.base_cover);
        dailyWorkList.add(dailyWork);
        dailyWork = new SingleItemDailyWork("산책", "PM 04:20", "PM 04:40", "시은", R.drawable.base_cover);
        dailyWorkList.add(dailyWork);
        dailyWork = new SingleItemDailyWork("안", "PM 12:20", "PM 12:40", "시은", R.drawable.base_cover);
        dailyWorkList.add(dailyWork);
    }


    class ListDailyWorkAdapter extends RecyclerView.Adapter<ListDailyWorkAdapter.MyViewHolder2> {
        private ArrayList<SingleItemDailyWork> dailyWorks_Items;

        public ListDailyWorkAdapter(ArrayList<SingleItemDailyWork> petItems) {
            this.dailyWorks_Items = petItems;
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
            holder.workName.setText(dailyWork.getWorkName());
            //make image circled
            Glide.with(getActivity()).load(R.drawable.base_cover).circleCrop().into(holder.workPersonImage);
            holder.workPersonImage.setImageResource(dailyWork.getWorkImage());
        }

        @Override
        public int getItemCount() {
            return dailyWorks_Items.size();
        }

        class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView workName;
            ImageView workPersonImage;

            MyViewHolder2(View view) {
                super(view);
                workName = (TextView) view.findViewById(R.id.textView_workName);
                workPersonImage = (ImageView) view.findViewById(R.id.personImage);
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
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            setPetData(holder, position);
        }

        private void setPetData(MyViewHolder holder, int position) {
            SingleItemPet pet = pet_Items.get(position);
            holder.petName.setText(pet.getName());
            Glide.with(getActivity()).load(R.drawable.simplelogo).circleCrop().into(holder.petImage);
            holder.petImage.setImageResource(R.drawable.simplelogo);
        }

        @Override
        public int getItemCount() {
            return pet_Items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView petName;
            ImageView petImage;

            MyViewHolder(View view) {
                super(view);
                petName = (TextView) view.findViewById(R.id.listitem_PetName);
                petImage = (ImageView) view.findViewById(R.id.listitem_PetImage);

            }
        }
    }
}
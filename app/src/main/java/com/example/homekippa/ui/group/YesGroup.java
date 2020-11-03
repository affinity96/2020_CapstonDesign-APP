package com.example.homekippa.ui.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YesGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YesGroup extends Fragment {
    private static final String ARG_PARAM1 = "object";
    private ArrayList<SingleItemPet> petList = new ArrayList<>();
    private ArrayList<SingleItemDailyWork> dailyWorkList = new ArrayList<>();

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_yes_group, container, false);

        RecyclerView listView_pets = root.findViewById(R.id.listview_pets);
        RecyclerView listView_dailyWorks = root.findViewById(R.id.listview_dailywork);

        setPetListView(listView_pets);
        setDailyWorkListView(listView_dailyWorks);

        CircleImageView imageView_groupProfile = (CircleImageView) root.findViewById(R.id.ImageView_groupProfile);
        Glide.with(this).load(R.drawable.dog_woong).circleCrop().into(imageView_groupProfile);
        return root;
    }

    private void setDailyWorkListView(RecyclerView listView) {
        getDailyWorkData();
        ListDailyWorkAdapter workAdapter = new ListDailyWorkAdapter(dailyWorkList);
        listView.setAdapter(workAdapter);
        LinearLayoutManager dLayoutManager = new LinearLayoutManager(getActivity());
        dLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(dLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setPetListView(RecyclerView listView) {
        getPetData();
        ListPetAdapter petAdapter = new ListPetAdapter(petList);
        listView.setAdapter(petAdapter);
        LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        pLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(pLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
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

    private void getPetData() {
        SingleItemPet pet = new SingleItemPet("땡이 ", R.drawable.top_btn_chat);
        petList.add(pet);
        pet = new SingleItemPet("콩이 ", R.drawable.simplelogo);
        petList.add(pet);
        pet = new SingleItemPet("탄이 ", R.drawable.simplelogo);
        petList.add(pet);
        pet = new SingleItemPet("웅이 ", R.drawable.simplelogo);
        petList.add(pet);
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
            holder.petImage.setImageResource(pet.getImage());
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
                petImage = (ImageView) view.findViewById(R.id.listitem_PetIamge);
            }
        }
    }
}
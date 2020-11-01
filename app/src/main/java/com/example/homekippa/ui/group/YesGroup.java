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

import com.example.homekippa.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YesGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YesGroup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<SingleItemPet> petList=new ArrayList<>();

    public static YesGroup newInstance() {
        return new YesGroup();
    }
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YesGroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YesGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static YesGroup newInstance(String param1, String param2) {
        YesGroup fragment = new YesGroup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_yes_group, container, false);
        RecyclerView listView_pets = root.findViewById(R.id.listview_pets);
        getPetData();
        ListPetAdapter adapter = new ListPetAdapter(petList);
        LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        pLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView_pets.setLayoutManager(pLayoutManager);
        listView_pets.setItemAnimator(new DefaultItemAnimator());
        listView_pets.setAdapter(adapter);
        // Inflate the layout for this fragment
        return root;
    }

    private void getPetData() {
        SingleItemPet pet=new SingleItemPet("thang", R.drawable.top_btn_chat);
        petList.add(pet);
        pet=new SingleItemPet("yeaggggggh", R.drawable.simplelogo);
        petList.add(pet);
    }

    class ListPetAdapter extends RecyclerView.Adapter<ListPetAdapter.MyViewHolder> {
        private ArrayList<SingleItemPet> pet_Items;
        public ListPetAdapter(ArrayList<SingleItemPet> petItems){
            this.pet_Items=petItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_pet, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            SingleItemPet pet= pet_Items.get(position);
            holder.petName.setText(pet.getName());
            holder.petImage.setImageResource(pet.getImage());
        }

        @Override
        public int getItemCount() {
            return pet_Items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView petName; ImageView petImage;
            MyViewHolder(View view){
                super(view);
                petName=(TextView)view.findViewById(R.id.listitem_PetName);
                petImage=(ImageView)view.findViewById(R.id.listitem_PetIamge);
            }
        }


    }
}
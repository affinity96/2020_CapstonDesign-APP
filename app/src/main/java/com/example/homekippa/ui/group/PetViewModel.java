package com.example.homekippa.ui.group;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.homekippa.ui.group.SingleItemPet;
import com.example.homekippa.ui.group.SingleItemPost;

import java.util.ArrayList;
import java.util.List;

public class PetViewModel extends ViewModel {
    public static MutableLiveData<List<SingleItemPet>> pets = new MutableLiveData<>();

    public PetViewModel() {
    }

    public MutableLiveData<List<SingleItemPet>> getPetList() {
        if (pets == null) {
            pets = new MutableLiveData<>();
        }
        return pets;
    }

    public static void addPet(SingleItemPet p) {
        List<SingleItemPet> pet = new ArrayList<>();
        pet.addAll(pets.getValue());
        pet.add(p);
        pets.setValue(pet);
    }

//    public static void decrease(int pos) {
//        counter.setValue(counter.getValue() - 1);
//    }

}
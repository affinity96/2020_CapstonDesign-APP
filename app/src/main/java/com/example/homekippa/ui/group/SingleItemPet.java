package com.example.homekippa.ui.group;

public class SingleItemPet {
    private String petName;
    private int petImage;

    public SingleItemPet(String name, int ImageID) {
        this.petName = name;
        this.petImage = ImageID;
    }

    public String getName() {
        return petName;
    }

    public int getImage() {
        return petImage;
    }
}

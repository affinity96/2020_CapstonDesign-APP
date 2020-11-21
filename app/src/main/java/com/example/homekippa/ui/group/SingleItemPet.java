package com.example.homekippa.ui.group;

public class SingleItemPet {
    private int id;
    private int group_id;
    private String name;
    private String birth;
    private String image;
    private String species;
    private String reg_num;
    private int gender;
    private int neutrality;
    private int petImage;

    public int getId() {
        return id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getSpecies() {
        return species;
    }

    public String getReg_num() {
        return reg_num;
    }

    public int getGender() {
        return gender;
    }

    public int getNeutrality() {
        return neutrality;
    }

    public String getImage() {
        return image;
    }

    public SingleItemPet(String name, int ImageID) {
        this.name = name;
        this.petImage = ImageID;
    }

    public SingleItemPet(int id, int group_id, String name, String birth, String image, String species, String reg_num, int gender, int neutrality) {
        this.id = id;
        this.group_id = group_id;
        this.name = name;
        this.birth = birth;
        this.image = image;
        this.species = species;
        this.reg_num = reg_num;
        this.gender = gender;
        this.neutrality = neutrality;
//        this.petImage = petImage;
    }
}

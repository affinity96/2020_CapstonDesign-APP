package com.example.homekippa.ui.notifications;

public class SingleItemNoti {
    private int notiGroupProfile;
    private String notiGroupName;
    private String notiTime;


    public SingleItemNoti(int notiGroupProfile, String notiGroupName, String notiTime) {

        this.notiGroupProfile = notiGroupProfile;
        this.notiGroupName = notiGroupName;
        this.notiTime = notiTime;
    }

    public int getNotiGroupProfile() {
        return notiGroupProfile;
    }

    public String getNotiGroupName() {
        return notiGroupName;
    }

    public String getNotiTime() {
        return notiTime;
    }
}

package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class UidRespense {
    @SerializedName("result")
    private boolean result;

    public boolean getResult() {
        return this.result;
    }
}

package com.example.homekippa.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    테스트용 IP - 나중에 삭제
    private final static String BASE_URL = "http://192.168.219.113:3000";
   // private final static String BASE_URL = "http://101.101.208.180:3000";
    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

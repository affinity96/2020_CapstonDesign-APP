package com.example.homekippa.network;

import retrofit2.Retrofit;
import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    테스트용 IP - 나중에 삭제
//    private final static String BASE_URL = "http://192.168.219.113:3000";
    private final static String BASE_URL = "http://101.101.208.180:3000";

//    private final static String BASE_URL = "http://172.30.1.14:3000";

    //Sieun Home
//    private final static String BASE_URL = "http://192.168.35.145:3000";
//YP Home
//    private final static String BASE_URL = "http://192.168.0.16:3000";

    private static Retrofit retrofit = null;
    private static OkHttpClient client = new OkHttpClient.Builder().build();

    private RetrofitClient() {
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

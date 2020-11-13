package com.example.homekippa.network;

import retrofit2.Retrofit;
import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient client = new OkHttpClient.Builder().build();

    private RetrofitClient() {
    }

    public static Retrofit getClient() {
        ServerURL server = new ServerURL();
        final String BASE_URL = server.getUrl();
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

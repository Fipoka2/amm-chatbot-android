package com.example.ammvoicebot.service;

import com.example.ammvoicebot.api.AmmChatbotApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://192.168.1.40:5000/bot/api/v1.0/";
    private Retrofit mRetrofit;

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public AmmChatbotApi getChatbotApi() {
        return mRetrofit.create(AmmChatbotApi.class);
    }
}

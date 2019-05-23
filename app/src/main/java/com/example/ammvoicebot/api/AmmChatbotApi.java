package com.example.ammvoicebot.api;

import com.example.ammvoicebot.model.Answer;
import com.example.ammvoicebot.model.Question;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AmmChatbotApi {

    @POST("ask")
    public Call<Answer> ask(@Body Question data);
}

package com.example.ammvoicebot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("hash")
    @Expose
    private int hash;
    @SerializedName("question")
    @Expose
    private String question;

    public Question(int hash, String question) {
        this.hash = hash;
        this.question = question;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

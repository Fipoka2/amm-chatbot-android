package com.example.ammvoicebot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("hash")
    @Expose
    private int hash;
    @SerializedName("answer")
    @Expose
    private String answer;

    public Answer(int hash, String answer) {
        this.hash = hash;
        this.answer = answer;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

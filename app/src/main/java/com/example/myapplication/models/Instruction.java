package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Instruction implements Serializable {
    @SerializedName("display_text")
    @Expose
    private final String text;

    public Instruction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

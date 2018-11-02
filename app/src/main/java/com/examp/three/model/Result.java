package com.examp.three.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("code")
    @Expose
    int code;
    @SerializedName("message")
    @Expose
    String messsage;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public Result(int code, String messsage) {
        this.code = code;
        this.messsage = messsage;
    }
}

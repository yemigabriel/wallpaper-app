package com.doxa360.yg.android.darling.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Apple on 28/12/2016.
 */

public class ServerResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("type")
    private String type;
    @SerializedName("message")
    private String message;

    public ServerResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(ServerResponse.class);
        return json;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, ServerResponse.class);
    }



}

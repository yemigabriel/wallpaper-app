package com.doxa360.yg.android.darling.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Apple on 28/12/2016.
 */

public class AlbumFile {

    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("album_id")
    private int album_id;
    @SerializedName("name")
    private String name;

    public AlbumFile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(AlbumFile.class);
        return json;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, AlbumFile.class);
    }



}

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

public class RetrieveResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("album_name")
    private String album_name;
    @SerializedName("file_name")
    private String file_name;
    @SerializedName("album_files")
    private ArrayList<AlbumFile> album_files;

    public RetrieveResponse() {
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

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public ArrayList<AlbumFile> getAlbum_files() {
        return album_files;
    }

    public void setAlbum_files(ArrayList<AlbumFile> album_files) {
        this.album_files = album_files;
    }

    public String getJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(RetrieveResponse.class);
        return json;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, RetrieveResponse.class);
    }



}

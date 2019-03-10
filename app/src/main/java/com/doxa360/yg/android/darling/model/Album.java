package com.doxa360.yg.android.darling.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Album implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("file_count")
    private int fileCount;
    @SerializedName("files")
    private ArrayList<String> files;
    @SerializedName("filePaths")
    private ArrayList<String> filePaths;
    @SerializedName("created_at")
    private Date date;


    public Album(String name, int fileCount, ArrayList<String> files, Date date) {
        this.name = name;
        this.fileCount = fileCount;
        this.files = files;
        this.date = date;
    }

    public Album(String name, int fileCount, ArrayList<String> fileUri, ArrayList<String> filePaths, Date date) {
        this.name = name;
        this.fileCount = fileCount;
        this.files = files;
        this.date = date;
        this.filePaths = filePaths;

    }

    public Album(String name) {
        this.name = name;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Album.class);
    }


    private Album(Parcel in) {
        id = in.readInt();
        name = in.readString();
        fileCount = in.readInt();
        files = in.createStringArrayList();
        filePaths = in.createStringArrayList();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(fileCount);
//        dest.writeTypedList(files);
        dest.writeStringList(files);
        dest.writeStringList(filePaths);
    }

}

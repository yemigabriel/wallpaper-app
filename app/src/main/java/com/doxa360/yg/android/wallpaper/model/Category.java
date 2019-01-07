package com.doxa360.yg.android.wallpaper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apple on 28/12/2016.
 */

public class Category implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("icon")
    private int mIcon;
    @SerializedName("wallpaperCount")
    private int mWallpaperCount;

    public Category() {
    }

    public Category(String title, int icon, int wallpaperCount) {
        mTitle = title;
        mIcon = icon;
        mWallpaperCount = wallpaperCount;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public int getWallpaperCount() {
        return mWallpaperCount;
    }

    public void setWallpaperCount(int wallpaperCount) {
        mWallpaperCount = wallpaperCount;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Category.class);
    }

    private Category(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mIcon = in.readInt();
        mWallpaperCount = in.readInt();

    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeInt(mIcon);
        parcel.writeInt(mWallpaperCount);

    }

}

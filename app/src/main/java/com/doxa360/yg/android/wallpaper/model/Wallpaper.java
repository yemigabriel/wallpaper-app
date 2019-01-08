package com.doxa360.yg.android.wallpaper.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Apple on 28/12/2016.
 */

public class Wallpaper implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("credit")
    private String mDescription;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("category")
    private Category mCategory;
    private int mImage;
    private boolean mIsCurrent;
    private Uri mFileUri;
    private boolean mIsDownloaded;

    public Wallpaper() {
    }

    public Wallpaper(String title, String description, int image, boolean isCurrent) {
        mTitle = title;
        mDescription = description;
        mImage = image;
        mIsCurrent = isCurrent;
    }

    public Wallpaper(String title, String description, int image, Category category, boolean isCurrent) {
        mTitle = title;
        mDescription = description;
        mImage = image;
        mCategory = category;
        mIsCurrent = isCurrent;
    }

    public Wallpaper(String title, String description, String url, Category category, boolean isCurrent) {
        mTitle = title;
        mDescription = description;
        mUrl = url;
        mCategory = category;
        mIsCurrent = isCurrent;
    }

    public Wallpaper(int id, String title, String description, int image) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mImage = image;
    }

    public Wallpaper(int id, String title, String description, int image, boolean isCurrent) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mImage = image;
        mIsCurrent = isCurrent;
    }

    public Wallpaper(String title, String description, int image, Category category, boolean isCurrent, Uri fileUri, boolean isDownloaded) {
        mTitle = title;
        mDescription = description;
        mImage = image;
        mCategory = category;
        mIsCurrent = isCurrent;
        mFileUri = fileUri;
        mIsDownloaded = isDownloaded;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        if (mTitle == null)
            return "";
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        if (mDescription == null)
            return "";
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean isCurrent() {
        return mIsCurrent;
    }

    public void setCurrent(boolean current) {
        mIsCurrent = current;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Wallpaper.class);
    }

    private Wallpaper(Parcel in) {
        mCategory = in.readParcelable(Category.class.getClassLoader());
        mId = in.readInt();
        mTitle = in.readString();
        mDescription = in.readString();
//        mImage = in.readInt();
        mUrl = in.readString();
//        mIsCurrent = in.readInt() == 1;

//        mImages = in.readArrayList(AdImage.class.getClassLoader());
//        mUser = in.readParcelable(User.class.getClassLoader());
//        mCategory = in.readParcelable(Category.class.getClassLoader());


    }

    public static final Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel in) {
            return new Wallpaper(in);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mCategory, i);
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
//        parcel.writeInt(mImage);
        parcel.writeString(mUrl);
//        parcel.writeInt(mIsCurrent ? 1 : 0);


    }

}

package com.doxa360.yg.android.darling.model;

/**
 * Created by Apple on 28/12/2016.
 */

public class Dashboard {

    private int mId;
    private String mTitle;
    private String mDetail;
    private int mIcon;
    private int mColor;

    public Dashboard() {
    }

    public Dashboard(int id, String title, String detail, int icon, int color) {
        mId = id;
        mTitle = title;
        mDetail = detail;
        mIcon = icon;
        mColor = color;
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

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    //    private Dashboard(Parcel in) {
//        mImages = in.readArrayList(AdImage.class.getClassLoader());
//        mUser = in.readParcelable(User.class.getClassLoader());
//        mCategory = in.readParcelable(Category.class.getClassLoader());
//        mId = in.readInt();
//        mCategoryId = in.readInt();
//        mTitle = in.readString();
//        mDescription = in.readString();
//        mOrg = in.readString();
//        mAddress = in.readString();
//        mIndustry = in.readString();
//        mCreatedAt = in.readString();
//    }
//
//    public static final Creator<Dashboard> CREATOR = new Creator<Dashboard>() {
//        @Override
//        public Dashboard createFromParcel(Parcel in) {
//            return new Dashboard(in);
//        }
//
//        @Override
//        public Dashboard[] newArray(int size) {
//            return new Dashboard[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeList(mImages);
//        parcel.writeParcelable(mUser, i);
//        parcel.writeParcelable(mCategory, i);
//        parcel.writeInt(mId);
//        parcel.writeInt(mCategoryId);
//        parcel.writeString(mTitle);
//        parcel.writeString(mDescription);
//        parcel.writeString(mOrg);
//        parcel.writeString(mAddress);
//        parcel.writeString(mIndustry);
//        parcel.writeString(mCreatedAt);
//
//    }

}

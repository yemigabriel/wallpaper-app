package com.doxa360.yg.android.darling.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.doxa360.yg.android.darling.model.Album;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPref {
    SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAlbum(Album album) {
        ArrayList<Album> albums;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("jsonAlbumList", null);
        if (json == null) {
            albums = new ArrayList<Album>();
        } else {
            Type type = new TypeToken<ArrayList<Album>>() {
            }.getType();
            albums = gson.fromJson(json, type);
        }
        albums.add(album);
        String albumsJson = gson.toJson(albums);

        sharedPreferences.edit()
                .putString("jsonAlbumList", albumsJson)
                .apply();
    }

    public ArrayList<Album> getAlbums() {
        String json = sharedPreferences.getString("jsonAlbumList", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void removeAlbums() {
        sharedPreferences.edit()
                .remove("jsonAlbumList")
                .apply();
    }

    public void removeAlbum(Album selectedAlbum) {
        List<Album> albums = this.getAlbums();
        int position = 0;
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equalsIgnoreCase(selectedAlbum.getName())) {
                position = i;
            }
        }

        albums.remove(position);
        this.removeAlbums();
        this.setAlbums(albums);
    }

    public void setAlbums(List<Album> albums) {
        Gson gson = new Gson();
        String albumsJson = gson.toJson(albums);

        sharedPreferences.edit()
                .putString("jsonAlbumList", albumsJson)
                .apply();
    }

    public void setAutoChangeAlbum(Album album) {
        sharedPreferences.edit()
                .putString("autoChangeAlbum", album.toString())
                .apply();
    }

    public Album getAutoChangeAlbum() {
        String albumString = sharedPreferences.getString("autoChangeAlbum", null);
        if (albumString != null) {
            return new GsonBuilder().create().fromJson(albumString, Album.class);
        } else {
            return null;
        }
    }

    public void setAutoChanger(boolean b) {
        sharedPreferences.edit()
                .putBoolean("autochange", b)
                .apply();
    }

    public boolean isAutoChanger() {
        return sharedPreferences.getBoolean("autochange", false);
    }

    public void setFrequency(int timer) {
        sharedPreferences.edit()
                .putInt("timer", timer)
                .apply();
    }

    public int getFrequency() {
        return sharedPreferences.getInt("timer", 0);
    }

    public void removeFrequency() {
        sharedPreferences.edit()
                .putInt("timer", 0)
                .apply();
    }

    public void setFrequencyPos(int pos) {
        sharedPreferences.edit()
                .putInt("frequency_pos", pos)
                .apply();
    }

    public int getFrequencyPos() {
        return sharedPreferences.getInt("frequency_pos", 0);
    }

    public void setSlideShown() {
        sharedPreferences.edit()
                .putBoolean("slideShown", true)
                .apply();
    }

    public boolean isSlideShown() {
        return sharedPreferences.getBoolean("slideShown", false);
    }

    public void setUserId(int id) {
        sharedPreferences.edit()
                .putInt("userId", id)
                .apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("userId", 0);
    }

    public void setUserName(String name) {
        sharedPreferences.edit()
                .putString("userName", name)
                .apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", null);
    }

    public void setUserEmail(String email) {
        sharedPreferences.edit()
                .putString("userEmail", email)
                .apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString("userEmail", null);
    }

    public void setUserPassword(String password) {
        sharedPreferences.edit()
                .putString("userPassword", password)
                .apply();
    }

    public String getUserPassword() {
        return sharedPreferences.getString("userPassword", null);
    }

    public void setLastSync(String date) {
        sharedPreferences.edit()
                .putString("lastSync", date)
                .apply();
    }

    public String getLastSync() {
        return sharedPreferences.getString("lastSync", null);
    }

    public void setHomeTutorial(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean("homeTutorial", value)
                .apply();
    }
    
    public boolean isHomeTutorial() {
        return sharedPreferences.getBoolean("homeTutorial", false);
    }

    public void setPreviewTutorial(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean("previewTutorial", value)
                .apply();
    }

    public boolean isPreviewTutorial() {
        return sharedPreferences.getBoolean("previewTutorial", false);
    }

    public void setAlbumTutorial(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean("albumTutorial", value)
                .apply();
    }

    public boolean isAlbumTutorial() {
        return sharedPreferences.getBoolean("albumTutorial", false);
    }

    public void setAutoChangeTutorial(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean("autoChangeTutorial", value)
                .apply();
    }

    public boolean isAutoChangeTutorial() {
        return sharedPreferences.getBoolean("autoChangeTutorial", false);
    }


    public void setSyncTutorial(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean("syncTutorial", value)
                .apply();
    }

    public boolean isSyncTutorial() {
        return sharedPreferences.getBoolean("syncTutorial", false);
    }

    public void scheduleRate(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean("scheduleRate", value)
                .apply();
    }

    public boolean isScheduleRate() {
        return sharedPreferences.getBoolean("scheduleRate", false);
    }
//    public void setAutoChangeTutorial(boolean value) {
//        sharedPreferences
//                .edit()
//                .putBoolean("autoChangeTutorial", value)
//                .apply();
//    }
//
//    public boolean isAutoChangeTutorial() {
//        return sharedPreferences.getBoolean("autoChangeTutorial", false);
//    }



//    public void setCurrentUser(String jsonUser) {
//        sharedPreferences.edit()
//                .putString("jsonUser", jsonUser)
////                .commit();
//                .apply();
//    }
//
//    public User getCurrentUser() {
//        String user =  sharedPreferences.getString("jsonUser", "");
//        return new GsonBuilder().create().fromJson(user, User.class);
//    }
}

//package com.doxa360.yg.android.savingsdemoapp.helpers;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Parcelable;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import com.doxa360.yg.android.savingsdemoapp.model.User;
//import com.google.gson.GsonBuilder;
//
///**
// * Created by Apple on 23/06/15.
// */
//public class DutchSharedPref {
//    SharedPreferences sharedPreferences;
//
//    public DutchSharedPref(Context context) {
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//    }
//
//
//    public int getContactCount(){
//        return sharedPreferences.getInt("contactCount", 0);
//    }
//
//    public void setContactCount(int count){
//        sharedPreferences
//                .edit()
//                .putInt("contactCount", count)
//                .apply();
//    }
//
//    public int getCallLogCount(){
//        return sharedPreferences.getInt("callLogCount", 0);
//    }
//
//    public void setCallLogCount(int count){
//        sharedPreferences
//                .edit()
//                .putInt("callLogCount", count)
//                .apply();
//    }
//
//    public float getLattitude(){
//        return sharedPreferences.getFloat("lattitude", (float) 0);
//    }
//
//    public void setLattitude(float lattitude){
//        sharedPreferences
//                .edit()
//                .putFloat("lattitude",  lattitude)
//                .apply();
//    }
//
//    public float getLongtitude(){
//        return sharedPreferences.getFloat("longtitude", (float) 0);
//    }
//
//    public void setLongtitude(float longtitude){
//        sharedPreferences
//                .edit()
//                .putFloat("longtitude",  longtitude)
//                .apply();
//    }
//
//    public void setTutorial(boolean value) {
//        sharedPreferences
//                .edit()
//                .putBoolean("tutorial", value)
//                .apply();
//    }
//
//    public boolean isTutorial() {
//        Log.e("shared pref", sharedPreferences.getBoolean("tutorial", false)+"");
//        return sharedPreferences.getBoolean("tutorial", false);
//    }
//
//    public void setTutorial2(boolean value) {
//        sharedPreferences
//                .edit()
//                .putBoolean("tutorial2", value)
//                .apply();
//    }
//
//    public boolean isTutorial2() {
//        Log.e("shared2", sharedPreferences.getBoolean("tutorial", false)+"");
//        return sharedPreferences.getBoolean("tutorial2", false);
//    }
//
//    public void setTutorial3(boolean value) {
//        sharedPreferences
//                .edit()
//                .putBoolean("tutorial3", value)
//                .apply();
//    }
//
//    public boolean isTutorial3() {
//        Log.e("shared3", sharedPreferences.getBoolean("tutorial", false)+"");
//        return sharedPreferences.getBoolean("tutorial3", false);
//    }
//
//    public String getSentiment(){
//        return sharedPreferences.getString("sentiment", "Update your driving status" );
//    }
//
//    public void setSentiment(String sentiment){
//        sharedPreferences
//                .edit()
//                .putString("sentiment", sentiment)
//                .apply();
//    }
//
//
//    public boolean isShared() {
//        return sharedPreferences.getBoolean("pro_shared", false);
//    }
//
//    public void setShared() {
//        sharedPreferences
//                .edit()
//                .putBoolean("pro_shared", true)
//                .apply();
//    }
//
//    public void setCurrentUser(String jsonUser) {
//        sharedPreferences.edit()
//                .putString("jsonUser", jsonUser)
////                .commit();
//                .apply();
//    }
//
//    public User getCurrentUser() {
//        String user =  sharedPreferences.getString("jsonUser", "");
//        return new GsonBuilder().create().fromJson(user, User.class);
//    }
//
//    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
//    public boolean clearCurrentUser() {
//        sharedPreferences.edit()
//                .remove("jsonUser")
//                .commit();
//        return true;
//    }
//
//    public void setToken(String token) {
//        sharedPreferences.edit()
//                .putString("token", token)
//                .apply();
//    }
//
//    public String getToken() {
//        return sharedPreferences.getString("token", "");
//    }
//
//    public void setDeviceId(String deviceId) {
//        sharedPreferences.edit()
//                .putString("deviceId", deviceId)
//                .apply();
//    }
//
//    public String getDeviceId() {
//        return sharedPreferences.getString("deviceId", "");
//    }
//
////    public void setDevice(boolean value) {
////        sharedPreferences
////                .edit()
////                .putBoolean("tutorial", value)
////                .apply();
////    }
////
////    public boolean isTutorial() {
////        Log.e("shared", sharedPreferences.getBoolean("tutorial", false)+"");
////        return sharedPreferences.getBoolean("tutorial", false);
////    }
//
//}

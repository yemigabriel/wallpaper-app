package com.doxa360.yg.android.darling.service;

import com.doxa360.yg.android.darling.WallpaperApp;
import com.doxa360.yg.android.darling.model.Album;
import com.doxa360.yg.android.darling.model.Category;
import com.doxa360.yg.android.darling.model.RetrieveResponse;
import com.doxa360.yg.android.darling.model.ServerResponse;
import com.doxa360.yg.android.darling.model.User;
import com.doxa360.yg.android.darling.model.Wallpaper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {
//    @POST(WallpaperApp.SIGN_UP_API)
//    Call<User> signUpUser(@Body User user);
//
//    @POST(WallpaperApp.SIGN_IN_API)
//    Call<User> signInUser(@Body User user);

//    @POST(WallpaperApp.SIGN_IN_API)
//    Call<User> signInUser(@Body User user);

    @GET(WallpaperApp.SIGN_IN_API)
    Call<User> signInUser(@Query("email") String email, @Query("password") String password);

    @GET(WallpaperApp.SIGN_UP_API)
    Call<User> signUpUser(@Query("name") String name, @Query("email") String email, @Query("password") String password);

//    @GET(Dutch.ALL_CATEGORY_ITEMS)
//    Call<List<Ad>> allItemsByCategory(@Query("category_id") int category);

//

    @GET(WallpaperApp.RECENT_WALLPAPERS)
    Call<List<Wallpaper>> recentWallpapers();

    @GET(WallpaperApp.TOP_WALLPAPERS)
    Call<List<Wallpaper>> topWallpapers();

//    @GET(WallpaperApp.ALL_USER_ADS)
//    Call<List<Ad>> userAds(@Query("user_id") int user_id);

    @GET(WallpaperApp.ALL_CATEGORIES)
    Call<List<Category>> allCategories();

    @Multipart
    @POST(WallpaperApp.SYNC_API)
    Call<ServerResponse> syncAlbum(//@Part("file_name") RequestBody description,
//                                          @Part MultipartBody.Part file,
//                                          @Part("") Album album,
//                                @PartMap() Map<String, RequestBody> partMap,
//                                @PartMap() Map<String, Ad> partAd,
                                   @PartMap() Map<String, RequestBody> partMap,
                                   @Part("file[]") RequestBody fileKey,
                                   @Part List<MultipartBody.Part> files);


    @GET(WallpaperApp.RETRIEVE_API)
    Call<ArrayList<RetrieveResponse>> retrieveAlbum(@Query("user_id") int user_id);
}

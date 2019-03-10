package com.doxa360.yg.android.darling.service;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.doxa360.yg.android.darling.helpers.SharedPref;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class WallpaperTimerReceiver extends BroadcastReceiver {

    final String TAG = getClass().getSimpleName();
    SharedPref sharedPref;
    WallpaperManager wallpaperManager;
    Context context;
    int currentWallpaper;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        //check realm for album selected
        //check realm for location of photos
        //apply wallpaper on photo next in line
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            // Change wallpaper here
//            sharedPref = new SharedPref(context);
//
//            Log.e(TAG, "wallpaper change!");
//
//        }
        // Change wallpaper here
        sharedPref = new SharedPref(context);
        Log.e(TAG, "wallpaper change!");
        changeWallpaper();

    }

    private void changeWallpaper() {
        wallpaperManager = WallpaperManager.getInstance(context);
        List<String> filePaths = sharedPref.getAutoChangeAlbum().getFilePaths();
        Random rand = new Random();
//        String uri = uriPaths.get(rand.nextInt(uriPaths.size()));

        int randIndex = rand.nextInt(filePaths.size());
        String filePath = filePaths.get(randIndex);
        currentWallpaper = randIndex;
        Glide.with(context)
                .asBitmap()
                .load(new File(filePath))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            wallpaperManager.setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void changelaterWallpaper() {
        wallpaperManager = WallpaperManager.getInstance(context);
        List<String> filePaths = sharedPref.getAutoChangeAlbum().getFilePaths();
        Random rand = new Random();
//        int randIndex = rand.nextInt(filePaths.size());
//        if (currentWallpaper == randIndex) {
//            randIndex = rand.nextInt(filePaths.size());
//        }
        int wallpaperIndex = getRandomWithExclusion(rand, 0, filePaths.size()-1, currentWallpaper);
        String filePath = filePaths.get(wallpaperIndex);
        currentWallpaper = wallpaperIndex;
        Glide.with(context)
                .asBitmap()
                .load(new File(filePath))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            wallpaperManager.setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }
}

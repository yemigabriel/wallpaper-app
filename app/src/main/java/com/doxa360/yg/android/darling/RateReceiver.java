package com.doxa360.yg.android.darling;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.doxa360.yg.android.darling.helpers.SharedPref;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class RateReceiver extends BroadcastReceiver{

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
        createNotification();


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

    public void createNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(context, DashboardActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Darling Wallpapers", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("If you have enjoyed using this app, please give us a good rating on Google Play");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.app_logo)
                .setTicker("Darling Wallpapers")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Rate this app")
                .setContentText("If you have enjoyed using this app, please give us a good rating on Google Play")
                .setContentInfo("Rate");

        if (notificationManager != null) {
            notificationManager.notify(/*notification id*/1, notificationBuilder.build());
        }


    }

}

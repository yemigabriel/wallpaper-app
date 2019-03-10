package com.doxa360.yg.android.darling;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.service.WallpaperTimerReceiver;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button mSignup, mLogin;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        if (!sharedPref.isScheduleRate()) {
            scheduleRateNotification();
            sharedPref.scheduleRate(true);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateAway();
            }
        }, 3000);

//        navigateAway();



//        PaperOnboardingPage scr1 = new PaperOnboardingPage("HD wallpapers",
//                "Easy access to HD wallpapers to personalize your device",
//                Color.parseColor("#E1BEE7"), R.drawable.slide_hd, R.drawable.outline_wallpaper_24);
//        PaperOnboardingPage scr2 = new PaperOnboardingPage("Auto Changing Wallpapers",
//                "Schedule automatic wallpaper changes for more customization",
//                Color.parseColor("#E040FB"), R.drawable.slide_timer, R.drawable.outline_timer_24);
//        PaperOnboardingPage scr3 = new PaperOnboardingPage("Fun Categories",
//                "Well curated into simple categories",
//                Color.parseColor("#9C27B0"), R.drawable.slide_dashboard, R.drawable.outline_dashboard_24);
//
//        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
//        elements.add(scr1);
//        elements.add(scr3);
//        elements.add(scr2);
//
//        PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(elements);
//
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.container, onBoardingFragment);
//        fragmentTransaction.commit();
//
//        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
//            @Override
//            public void onRightOut() {
//                sharedPref.setSlideShown();
//                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
    }

    private void scheduleRateNotification() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
//        notificationIntent.addCategory("android.intent.category.DEFAULT");
//
//        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.SECOND, 15);
//        if (alarmManager != null) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
//        }

        AlarmManager autochangeTimer = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, RateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        if (autochangeTimer != null) {
//        autochangeTimer.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),1*1000, pendingIntent);
//        }
        if (autochangeTimer != null) {
            autochangeTimer.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 172800000, pendingIntent);
        }
//        alarmMgr.set(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + 5000,
//                pIntent
//        ) // sto

    }

    private void navigateAway() {

        if (sharedPref.isSlideShown()) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }


}

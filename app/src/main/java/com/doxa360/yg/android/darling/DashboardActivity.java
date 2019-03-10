package com.doxa360.yg.android.darling;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private ImageView currentWallpaper;
    private WallpaperManager wallpaperManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        SharedPref sharedPref = new SharedPref(this);
//        sharedPref.removeAlbums();

//        wallpaperManager = WallpaperManager.getInstance(DashboardActivity.this);
//        currentWallpaper = (ImageView) findViewById(R.id.current_wallpaper);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setPhoneWallpaper(view);
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        displayCurrentWallpaper();

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, TAG).commit();
    }

//    private void displayCurrentWallpaper() {
//        Drawable currentWallpaperDrawable = wallpaperManager.getDrawable();
//        currentWallpaper.setImageDrawable(currentWallpaperDrawable);
//    }

    private void setPhoneWallpaper(View view) {

//        Snackbar.make(view, "New wallpaper", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.appbg);
////        if (wallpaperManager.isSetWallpaperAllowed())
//        try {
//            wallpaperManager.setBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Fragment fragment = new PreviewFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.coordinator_layout, fragment, TAG).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
        }
        else{
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dashboard, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_sort) {
////
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setItems(R.array.sort_by, new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    Toast.makeText(DashboardActivity.this, R.string.demo_message, Toast.LENGTH_LONG).show();
////                }
////            });
////            AlertDialog dialog = builder.create();
////            dialog.show();
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        if (id == R.id.nav_wallpapers) {
            fragment = new HomeFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, TAG).commit();
            getSupportActionBar().setTitle("Wallpapers");

        } else if (id == R.id.nav_explore) {
            fragment = new CategoryFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, TAG).commit();
            getSupportActionBar().setTitle("Categories");

        } else if (id == R.id.nav_album) {
            fragment = new AlbumFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, TAG).commit();
            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle("Albums");

        } else if (id == R.id.nav_daily) {
            fragment = new AutoChangerFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, TAG).commit();
            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle("Auto Changer");
        } else if (id == R.id.sync_albums) {
            fragment = new AccountSyncFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, TAG).commit();
            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle("Sync");
        }
        else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_rate) {
            showRateDialog();

        } else if (id == R.id.nav_feedback) {
            sendEmailIntent("darling@devstudios.ng");
        }
        if (id == R.id.version_number) {
            createNotification();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendEmailIntent(String email) {
        String[] emailAddress = new String[] {email};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        if (intent.resolveActivity(DashboardActivity.this.getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download 'Darling Wallpaper' app on Google Play - https://play.google.com/store/apps/details?id=com.doxa360.yg.android.darling");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share App"));
    }

    public void showRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate us...")
                .setMessage("If you've enjoyed using this app, kindly rate us on Google Play")
                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + WallpaperApp.APP_NAME)));
                    }
                })
                .setNegativeButton("Later", null);
        AlertDialog dialog = builder.create();
        dialog.show();

//        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
//        dialog.dismiss();


    }

    public void createNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, DashboardActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

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

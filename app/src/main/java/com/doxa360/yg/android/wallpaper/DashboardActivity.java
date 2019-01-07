package com.doxa360.yg.android.wallpaper;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.doxa360.yg.android.wallpaper.helpers.MyToolBox;

import java.io.IOException;

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

        wallpaperManager = WallpaperManager.getInstance(DashboardActivity.this);
        currentWallpaper = (ImageView) findViewById(R.id.current_wallpaper);
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

        displayCurrentWallpaper();

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, TAG).commit();
    }

    private void displayCurrentWallpaper() {
        Drawable currentWallpaperDrawable = wallpaperManager.getDrawable();
        currentWallpaper.setImageDrawable(currentWallpaperDrawable);
    }

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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(R.array.sort_by, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(DashboardActivity.this, R.string.demo_message, Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

        } else if (id == R.id.nav_frequency) {
            MyToolBox.AlertMessage(DashboardActivity.this, getResources().getString(R.string.demo_alert_message));
        }
        else if (id == R.id.nav_daily) {
            MyToolBox.AlertMessage(DashboardActivity.this, getResources().getString(R.string.demo_alert_message));
        }
        else if (id == R.id.nav_settings) {
            MyToolBox.AlertMessage(DashboardActivity.this, getResources().getString(R.string.demo_alert_message));

        } else if (id == R.id.nav_send) {
            MyToolBox.AlertMessage(DashboardActivity.this, getResources().getString(R.string.demo_alert_message));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

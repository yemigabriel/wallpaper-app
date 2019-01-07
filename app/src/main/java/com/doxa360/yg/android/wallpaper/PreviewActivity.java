package com.doxa360.yg.android.wallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.yg.android.wallpaper.helpers.MyToolBox;
import com.doxa360.yg.android.wallpaper.model.Wallpaper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PreviewActivity extends AppCompatActivity {

    private static final int SET_WALLPAPER_REQUEST = 1923;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    private final String TAG = this.getClass().getSimpleName();
//    private ImageView mPreviewWallpaper;
    private WallpaperManager wallpaperManager;
    private Bitmap mBitmap;
    private Wallpaper mWallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get wallpaper intent
        Intent intent = getIntent();
        if (intent != null)
            mWallpaper = intent.getParcelableExtra(WallpaperApp.WALLPAPER);

        wallpaperManager = WallpaperManager.getInstance(this);
        //TODO: check if it already exist in internal storage
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        mBitmap = BitmapFactory.decodeResource(getResources(), mWallpaper.getImage(), options); // pass image here
        //apply image to imageview
        ImageView mPreviewWallpaper = (ImageView) findViewById(R.id.wallpaper_preview);
        TextView mWallpaperLabel = findViewById(R.id.wallpaper_label);
        Button mDownloadBtn = findViewById(R.id.downloadBtn);
        Button mApplyBtn = findViewById(R.id.applyBtn);

//        mPreviewWallpaper.setImageResource(mWallpaper.getImage());
        Picasso.with(this).load(mWallpaper.getImage()).placeholder(R.drawable.placeholder_demo).into(mPreviewWallpaper);
        mWallpaperLabel.setText(mWallpaper.getTitle());
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                downloadToDevice();
                checkPermissions();
            }
        });
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoneWallpaper(mBitmap);
            }
        });



    }

    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasStoragePermission = (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            if (hasStoragePermission) {
                downloadToDevice();
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(PreviewActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(PreviewActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } else {
            downloadToDevice();
        }
    }

    private void downloadToDevice() {
        if (MyToolBox.isExternalStorageAvailable()) {
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
            String fileName = "/IMG_"+timestamp;


            File fileDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES) + "/Wallpaper_Demo");//this.getString(R.string.app_name));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
                if (fileDir.canWrite()) {
                    Toast.makeText(PreviewActivity.this, "downloading...", Toast.LENGTH_SHORT).show();
//                File filesDir = this.getFilesDir(); //internal_storage
                    File imageFile = new File(fileDir + fileName + ".jpg");
                    if (!imageFile.exists()) {
                        try {
                            imageFile.createNewFile();
                            Toast.makeText(PreviewActivity.this, "just a second...", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    OutputStream os;
                    try {
                        os = new FileOutputStream(imageFile);
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                        os.flush();
                        os.close();
                        Toast.makeText(this, "Wallpaper successfully downloaded to " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error writing mBitmap", e);
                        Toast.makeText(this, "Wallpaper not successfully downloaded", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(PreviewActivity.this, "An error occurred while attempting to download the wallpaper", Toast.LENGTH_LONG).show();
                }


        }
    }

    private void setPhoneWallpaper(Bitmap bm) {
//        Toast.makeText(this, "applying wallpaper", Toast.LENGTH_SHORT).show();
//        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.appbg);
        Date now = new Date();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);

        String fileName = "IMG_"+timestamp;

        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider" ,getFileFromBitmap(bm, fileName));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Intent i = wallpaperManager.getCropAndSetWallpaperIntent(uri);
            startActivityForResult(i, SET_WALLPAPER_REQUEST);
        }
        else {
            try {
                wallpaperManager.setBitmap(mBitmap);
                Toast.makeText(this, "Wallpaper successfully applied", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Wallpaper not applied", Toast.LENGTH_LONG).show();
            }
        }



    }

    private File getFileFromBitmap(Bitmap bitmap, String name) {
        File filesDir = this.getFilesDir(); //internal_storage
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing mBitmap", e);
        }

        return imageFile;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_preview, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_wallpaper) {
//            setPhoneWallpaper();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_WALLPAPER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Wallpaper successfully applied", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Wallpaper not applied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadToDevice();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}

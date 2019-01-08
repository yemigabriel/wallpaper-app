package com.doxa360.yg.android.wallpaper;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
    private RequestOptions requestOptions;
    boolean applyWallpaper;

    private ProgressDialog mProgressDialog;

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

        ImageView mPreviewWallpaper = (ImageView) findViewById(R.id.wallpaper_preview);
        TextView mWallpaperLabel = findViewById(R.id.wallpaper_label);
        Button mDownloadBtn = findViewById(R.id.downloadBtn);
        Button mApplyBtn = findViewById(R.id.applyBtn);


        requestOptions = new RequestOptions().placeholder(R.drawable.placeholder).centerCrop();
        Glide.with(this).load(WallpaperApp.PHOTO_URL + mWallpaper.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                            .thumbnail(Glide.with(this)
                                    .load(WallpaperApp.THUMBNAIL_URL + mWallpaper.getUrl())
                                    .apply(requestOptions))
                .into(mPreviewWallpaper);
//
//        Picasso.with(this).load(mWallpaper.getImage()).placeholder(R.drawable.placeholder_demo).into(mPreviewWallpaper);
        mWallpaperLabel.setText(mWallpaper.getTitle());
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions(false);
            }
        });
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions(true);
            }
        });



    }

    private void bitmapFromRemoteUrl(final boolean applyWallpaper) {
        mProgressDialog.setMessage("Downloading wallpaper ...");
        mProgressDialog.show();
        Glide.with(this)
                .asBitmap()
                .load(WallpaperApp.PHOTO_URL + mWallpaper.getUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        downloadToDevice(resource, applyWallpaper);
                    }
                });
    }


    private void checkPermissions(boolean applyWallpaper) {
        mProgressDialog = new ProgressDialog(this);

        this.applyWallpaper = applyWallpaper;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasStoragePermission = (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            if (hasStoragePermission) {
                bitmapFromRemoteUrl(applyWallpaper);
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(PreviewActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(PreviewActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        } else {
            bitmapFromRemoteUrl(applyWallpaper);
        }
    }

    private void downloadToDevice(Bitmap bitmap, boolean applyWallpaper) {
        if (MyToolBox.isExternalStorageAvailable()) {
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
            String fileName = "/IMG_"+timestamp;


//            Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES)
            File fileDir = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/DarlingWallpaper");//this.getString(R.string.app_name));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
                if (fileDir.canWrite()) {
                mProgressDialog.setMessage("Almost done ...");
//                    Toast.makeText(PreviewActivity.this, "downloading...", Toast.LENGTH_SHORT).show();
//                File filesDir = this.getFilesDir(); //internal_storage
                    File imageFile = new File(fileDir + fileName + ".jpg");
                    if (!imageFile.exists()) {
                        try {
                            imageFile.createNewFile();
                            mProgressDialog.setMessage("Wrapping up ...");
//                            Toast.makeText(PreviewActivity.this, "just a second...", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    OutputStream os;
                    try {
                        os = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
                        os.flush();
                        os.close();
                        if (applyWallpaper){
                            mProgressDialog.setMessage("Applying wallpaper");
                            applyDeviceWallpaper(imageFile);
                        } else {
                            mProgressDialog.cancel();
                            Toast.makeText(this, "Wallpaper successfully downloaded to " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error writing mBitmap", e);
                        mProgressDialog.cancel();
                        Toast.makeText(this, "Wallpaper not successfully downloaded", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    mProgressDialog.cancel();
                    Toast.makeText(PreviewActivity.this, "An error occurred while attempting to download the wallpaper", Toast.LENGTH_LONG).show();
                }


        } else {
            mProgressDialog.cancel();
            Toast.makeText(this, "No storage available for this wallpaper", Toast.LENGTH_LONG).show();
        }
    }

    private void applyDeviceWallpaper(File imageFile) {
        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider" ,imageFile);

        Intent i = wallpaperManager.getCropAndSetWallpaperIntent(uri);
        startActivityForResult(i, SET_WALLPAPER_REQUEST);
        mProgressDialog.dismiss();

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//        }
    }

//    private void setPhoneWallpaper() {
//        Date now = new Date();
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
//
//        String fileName = "IMG_"+timestamp;
//
//        Uri uri = FileProvider.getUriForFile(this,
//                BuildConfig.APPLICATION_ID + ".provider" ,getFileFromBitmap(bm, fileName));
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            Intent i = wallpaperManager.getCropAndSetWallpaperIntent(uri);
//            startActivityForResult(i, SET_WALLPAPER_REQUEST);
//        }
//        else {
//            try {
//                wallpaperManager.setBitmap(mBitmap);
//                Toast.makeText(this, "Wallpaper successfully applied", Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Wallpaper not applied", Toast.LENGTH_LONG).show();
//            }
//        }
//
//
//
//    }
//
//    private File getFileFromBitmap(Bitmap bitmap, String name) {
//        File filesDir = this.getFilesDir(); //internal_storage
//        File imageFile = new File(filesDir, name + ".jpg");
//
//        OutputStream os;
//        try {
//            os = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            Log.e(TAG, "Error writing mBitmap", e);
//        }
//
//        return imageFile;
//    }

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
                    bitmapFromRemoteUrl(PreviewActivity.this.applyWallpaper);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}

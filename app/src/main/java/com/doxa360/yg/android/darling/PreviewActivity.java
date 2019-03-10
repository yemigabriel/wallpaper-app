package com.doxa360.yg.android.darling;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class PreviewActivity extends AppCompatActivity {

    private static final int SET_WALLPAPER_REQUEST = 1923;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    private static final String SHOWCASE_ID = "PreviewTutorial";
    private final String TAG = this.getClass().getSimpleName();
//    private ImageView mPreviewWallpaper;
    private WallpaperManager wallpaperManager;
    private Bitmap mBitmap;
    private Wallpaper mWallpaper;
    private RequestOptions requestOptions;
    boolean applyWallpaper;

    private ProgressDialog mProgressDialog;
    private ProgressBar downloadProgressBar;
    TextView progressText;
    Button mDownloadBtn, mApplyBtn;

    SharedPref sharedPref;

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


        sharedPref = new SharedPref(this);

        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mProgressDialog = new ProgressDialog(PreviewActivity.this);
//            }
//        });

        wallpaperManager = WallpaperManager.getInstance(this);

        ImageView mPreviewWallpaper = (ImageView) findViewById(R.id.wallpaper_preview);
        TextView mWallpaperLabel = findViewById(R.id.wallpaper_label);
        mDownloadBtn = findViewById(R.id.downloadBtn);
        mApplyBtn = findViewById(R.id.applyBtn);
        progressText = findViewById(R.id.progress_secondary_text);

        downloadProgressBar = findViewById(R.id.downloadProgress);

        requestOptions = new RequestOptions().fitCenter();//placeholder(R.drawable.placeholder).centerCrop();
        Glide.with(this).load(WallpaperApp.PHOTO_URL + mWallpaper.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                            .thumbnail(Glide.with(this)
                                    .load(WallpaperApp.THUMBNAIL_URL + mWallpaper.getUrl())
                                    .apply(requestOptions))
                .into(mPreviewWallpaper);

        if (!sharedPref.isPreviewTutorial()) {
            startTutorialSequence();
        }
//
//        Picasso.with(this).load(mWallpaper.getImage()).placeholder(R.drawable.placeholder_demo).into(mPreviewWallpaper);
        mWallpaperLabel.setText(mWallpaper.getTitle());
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyWallpaper = false;
//                downloadProgressBar.setVisibility(View.VISIBLE);
//                downloadProgressBar.setIndeterminate(true);
//                Toast.makeText(PreviewActivity.this, "Downloading ...", Toast.LENGTH_SHORT).show();

                if (MyToolBox.isNetworkAvailable(PreviewActivity.this)) {
                    checkPermissions(false);
                } else {
                    MyToolBox.AlertMessage(PreviewActivity.this, "Oops", "Network error. Please check your connection.");
                }
            }
        });
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyWallpaper = true;
//                downloadProgressBar.setVisibility(View.VISIBLE);
//                downloadProgressBar.setIndeterminate(true);
//                Toast.makeText(PreviewActivity.this, "Processing ...", Toast.LENGTH_SHORT).show();

                if (MyToolBox.isNetworkAvailable(PreviewActivity.this)) {
                    checkPermissions(true);
                } else {
                    MyToolBox.AlertMessage(PreviewActivity.this, "Oops", "Network error. Please check your connection.");
                }
            }
        });

//        int screenSize = getResources().getConfiguration().screenLayout &Configuration.SCREENLAYOUT_SIZE_MASK;
//        Log.e(TAG, "size is : "+screenSize);
//        switch(screenSize) {
//            case Configuration.SCREENLAYOUT_SIZE_LARGE:
//                Toast.makeText(this, "Large screen",Toast.LENGTH_LONG).show();
//                break;
//            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//                Toast.makeText(this, "Normal screen",Toast.LENGTH_LONG).show();
//                break;
//            case Configuration.SCREENLAYOUT_SIZE_SMALL:
//                Toast.makeText(this, "Small screen",Toast.LENGTH_LONG).show();
//                break;
//            default:
//                Toast.makeText(this, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
//
//        }

//        int density= getResources().getDisplayMetrics().densityDpi;
//        switch(density)
//        {
//            case DisplayMetrics.DENSITY_LOW:
//                Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
//                break;
//            case DisplayMetrics.DENSITY_MEDIUM:
//                Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
//                break;
//            case DisplayMetrics.DENSITY_HIGH:
//                Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
//                break;
//            case DisplayMetrics.DENSITY_XHIGH:
//                Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();
//        Toast.makeText(this, "width is: "+width+" height is: "+height, Toast.LENGTH_LONG).show();


        downloadProgressBar.setVisibility(View.INVISIBLE);

        MyToolBox.AlertMessage(PreviewActivity.this, "Info", "High-res images may consume more data and take a little longer to download.");



    }

    private void bitmapFromRemoteUrl(final boolean applyWallpaper) {
//        mProgressDialog.setMessage("Downloading wallpaper ...");
//        mProgressDialog.show();
//        Toast.makeText(this, "Downloading wallpaper ...", Toast.LENGTH_SHORT).show();

//        downloadProgressBar.setIndeterminate(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(PreviewActivity.this)
                        .asBitmap()
                        .apply(new RequestOptions())
                        .load(WallpaperApp.PHOTO_URL + mWallpaper.getUrl())
//                .addListener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                downloadToDevice(resource, applyWallpaper);
                            }

                        });
            }
        });
    }


    private void checkPermissions(boolean applyWallpaper) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                downloadProgressBar.setVisibility(View.VISIBLE);
//            }
//        });
        this.applyWallpaper = applyWallpaper;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasStoragePermission = (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            if (hasStoragePermission) {
//                bitmapFromRemoteUrl(applyWallpaper);

                new WallpaperAsyncTask().execute();
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
//            bitmapFromRemoteUrl(applyWallpaper);

            new WallpaperAsyncTask().execute();
        }
    }

    private void downloadToDevice(Bitmap bitmap, boolean applyWallpaper) {
        if (MyToolBox.isExternalStorageAvailable()) {
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
            String fileName = "IMG_"+timestamp;

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    getString(R.string.app_name));

//            Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES)
//            File fileDir = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/DarlingWallpaper");//this.getString(R.string.app_name));
            File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    getString(R.string.app_name));

            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
                if (fileDir.canWrite()) {
//                mProgressDialog.setMessage("Almost done ...");
//                    Toast.makeText(PreviewActivity.this, "downloading...", Toast.LENGTH_SHORT).show();
//                File filesDir = this.getFilesDir(); //internal_storage
                    File imageFile = new File(fileDir + "/" + fileName + ".jpg");
                    if (!imageFile.exists()) {
                        try {
                            imageFile.createNewFile();
//                            mProgressDialog.setMessage("Wrapping up ...");
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
//                            mProgressDialog.setMessage("Applying wallpaper");
//                            applyDeviceWallpaper(imageFile);
//                            getFileFromBitmap(bitmap, fileName);
//                            Toast.makeText(this, "Applying wallpaper", Toast.LENGTH_SHORT).show();
//                            wallpaperManager.setBitmap(bitmap);
//                            setWallpaperIntent(bitmap);
                            setWallpaperIntent(imageFile);
//                            mProgressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //downloadProgressBar.setVisibility(View.GONE);
//                                    Toast.makeText(this, "Wallpaper successfully applied ", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
//                            mProgressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //downloadProgressBar.setVisibility(View.GONE);
                                }
                            });
//                            Toast.makeText(this, "Wallpaper successfully downloaded to " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            Intent scanFileIntent = new Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile   ));
                            sendBroadcast(scanFileIntent);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error writing mBitmap", e);
//                        mProgressDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //downloadProgressBar.setVisibility(View.GONE);
                            }
                        });
//                        Toast.makeText(this, "Wallpaper not successfully downloaded", Toast.LENGTH_LONG).show();
                    }
                }
                else {
//                    mProgressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //downloadProgressBar.setVisibility(View.GONE);
                        }
                    });
//                    Toast.makeText(PreviewActivity.this, "An error occurred while attempting to download the wallpaper", Toast.LENGTH_LONG).show();
                }


        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //downloadProgressBar.setVisibility(View.GONE);
                }
            });
//            Toast.makeText(this, "No storage available for this wallpaper", Toast.LENGTH_LONG).show();
        }
    }

    private void setWallpaperIntent(File imageFile) {
        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider" ,imageFile);
//        Uri.fromFile(imageFile)
//        Intent intent = wallpaperManager.getCropAndSetWallpaperIntent(Uri.fromFile(imageFile));
//        intent = wallpaperManager.get

//        intent.putExtra("mimeType", "image/*");

//        startActivityForResult(intent, SET_WALLPAPER_REQUEST);
//        Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());

//        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
        String mime = "image/*";
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, mime);

        startActivity(intent);



    }

    private void applyDeviceWallpaper(File imageFile) {
        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider" ,imageFile);

        Intent i = wallpaperManager.getCropAndSetWallpaperIntent(uri);
        startActivityForResult(i, SET_WALLPAPER_REQUEST);
        downloadProgressBar.setVisibility(View.INVISIBLE);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //downloadProgressBar.setVisibility(View.GONE);
//            }
//        });
//        mProgressDialog.dismiss();

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
    private void getFileFromBitmap(Bitmap bitmap, String name) {
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

//        return imageFile;
        applyDeviceWallpaper(imageFile);
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
                progressText.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(this, "Wallpaper not applied", Toast.LENGTH_LONG).show();
                progressText.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    class WallpaperAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressText.setVisibility(View.VISIBLE);
            progressText.setText("Processing... Please hold");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downloadProgressBar.setVisibility(View.VISIBLE);
                    downloadProgressBar.setIndeterminate(false);
//                    mProgressDialog.setMessage("Processing ...");
//                    mProgressDialog.show();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmapFromRemoteUrl(applyWallpaper);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (mProgressDialog.isShowing()) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!applyWallpaper) {
                            Toast.makeText(PreviewActivity.this, "Downloaded successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PreviewActivity.this, "Wallpaper applied successfully", Toast.LENGTH_SHORT).show();
                        }

                        progressText.setVisibility(View.INVISIBLE);
                        downloadProgressBar.setVisibility(View.INVISIBLE);
                        mProgressDialog.dismiss();
                    }
                });
//            }
        }

    }

    private void startTutorialSequence() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(mDownloadBtn,
                "Tap to download this wallpaper to your device", "NEXT");
        sequence.addSequenceItem(mApplyBtn,
                "Tap to download this wallpaper to your device", "GOT IT");
//        sequence.addSequenceItem(tab3,
//                getString(R.string.tooltip_four), "GOT IT");
        sequence.start();

        sharedPref.setPreviewTutorial(true);
    }


    private Size fitToWidthAndKeepRatio(int width, int height) {
        WindowManager win = getWindowManager();
        Display d = win.getDefaultDisplay();
        int displayWidth = d.getWidth(); // Width of the actual device

        int fittedHeight = height;
        int fittedWidth = width;

        fittedHeight = displayWidth * fittedHeight / fittedWidth;
        fittedWidth = displayWidth;

        return new Size(fittedWidth, fittedHeight);
    }


}

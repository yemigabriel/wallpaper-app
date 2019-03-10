package com.doxa360.yg.android.darling;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;
import com.doxa360.yg.android.darling.model.AlbumFile;
import com.doxa360.yg.android.darling.model.RetrieveResponse;
import com.doxa360.yg.android.darling.service.ApiInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class RetrieveFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    private final String TAG = this.getClass().getSimpleName();
    private Bitmap mBitmap;
    private RequestOptions requestOptions;
    boolean applyWallpaper;

    private ProgressBar progressBar;
    private TextView progressText, progressSecondaryText;
    private ImageView progressIcon;

    SharedPref sharedPref;
    private Context mContext;
    ArrayList<RetrieveResponse> responseArrayList;
    ArrayList<String> filePaths;

    String syncStatus;

    public RetrieveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_retrieve, container, false);

        progressIcon = rootView.findViewById(R.id.progress_icon);
        progressText = rootView.findViewById(R.id.progress_text);
        progressBar = rootView.findViewById(R.id.progress_bar);

        progressSecondaryText = rootView.findViewById(R.id.progress_secondary_text);

        syncStatus = "";
        sharedPref = new SharedPref(mContext);
        responseArrayList = new ArrayList<RetrieveResponse>();

        filePaths = new ArrayList<String>();

        retrieveAlbums();

        return rootView;
    }

    private void retrieveAlbums() {
        int user_id = sharedPref.getUserId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WallpaperApp.SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface1 = retrofit.create(ApiInterface.class);

        Call<ArrayList<RetrieveResponse>> call = apiInterface1.retrieveAlbum(user_id);

        call.enqueue(new Callback<ArrayList<RetrieveResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RetrieveResponse>> call, Response<ArrayList<RetrieveResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() != 0) {
                        responseArrayList = response.body();
                        for (RetrieveResponse retrieveResponse : responseArrayList) {
                            ArrayList<String> fPath = new ArrayList<String>();
                            final Album album = new Album(retrieveResponse.getAlbum_name());
                            for (AlbumFile albumFile: retrieveResponse.getAlbum_files()) {
                                String path = downloadFiles(albumFile.getName());
//                                String filePath = downloadFiles(albumFile.getName());
                                fPath.add(path);
                            }
                            album.setFilePaths(fPath);
                            sharedPref.setAlbum(album);
//                            filePaths.clear();

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    syncStatus += "\n "+ album.getName() + " successfully retrieved";
                                    progressSecondaryText.setText(syncStatus);
                                }
                            });
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressText.setText("Albums Retrieved");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressText.setText("No albums to be retrieved");
                                progressSecondaryText.setText("No albums are available to be retrieved");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                } else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncStatus += "\n retrieval failed ...";
                            progressSecondaryText.setText(syncStatus);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ArrayList<RetrieveResponse>> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncStatus += "\n retrieval failed ...";
                        progressSecondaryText.setText(syncStatus);
                    }
                });

            }
        });


    }

    private String downloadFiles(String fileName) {
        if (MyToolBox.isNetworkAvailable(mContext)) {
            return checkPermissions(fileName);
        } else {
            MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection.");
        }
        return null;
    }


    private String bitmapFromRemoteUrl(String fileName) {
        final String[] path = {""};
        Glide.with(mContext)
                .asBitmap()
                .load(WallpaperApp.SYNC_URL + fileName)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        path[0] = downloadToDevice(resource);
                    }

                });

        return path[0];
    }


    private String checkPermissions(String fileName) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasStoragePermission = (ContextCompat.checkSelfPermission
                    (mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            if (hasStoragePermission) {
                return bitmapFromRemoteUrl(fileName);
//                new PreviewActivity.WallpaperAsyncTask().execute();
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        } else {
            return  bitmapFromRemoteUrl(fileName);
//            new WallpaperAsyncTask().execute();
        }
        return null;
    }

    private String downloadToDevice(Bitmap bitmap) {
        Log.e("RETRIEVE", "download over heare");
        if (MyToolBox.isExternalStorageAvailable()) {
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
            String fileName = "IMG_"+timestamp;

            File fileDir = new File( mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/Darling/Albums");//this.getString(R.string.app_name));

            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
//            if (fileDir.canWrite()) {
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


                } catch (Exception e) {
                    Log.e(TAG, "Error writing mBitmap", e);

//                        Toast.makeText(this, "Wallpaper not successfully downloaded", Toast.LENGTH_LONG).show();
                }

            Log.e("RETRIEVE", "download file " + imageFile.getAbsolutePath());
//                filePaths.add(imageFile.getAbsolutePath());
//            }
//            else {
////                    mProgressDialog.dismiss();
//
////                    Toast.makeText(PreviewActivity.this, "An error occurred while attempting to download the wallpaper", Toast.LENGTH_LONG).show();
//            }

            return imageFile.getAbsolutePath();


        } else {
//            Toast.makeText(this, "No storage available for this wallpaper", Toast.LENGTH_LONG).show();
        }

        return null;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

}

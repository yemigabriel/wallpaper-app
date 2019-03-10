package com.doxa360.yg.android.darling;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;
import com.doxa360.yg.android.darling.model.ServerResponse;
import com.doxa360.yg.android.darling.model.User;
import com.doxa360.yg.android.darling.service.ApiInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SyncFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView progressText, progressSecondaryText;
    private ImageView progressIcon;

    Context mContext;
    SharedPref sharedPref;
    int count = 0;
    String syncStatus;
    private ProgressDialog progres;

    public SyncFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sync, container, false);

        progressIcon = rootView.findViewById(R.id.progress_icon);
        progressText = rootView.findViewById(R.id.progress_text);
        progressBar = rootView.findViewById(R.id.progress_bar);

        progressSecondaryText = rootView.findViewById(R.id.progress_secondary_text);

        syncStatus = "";
        sharedPref = new SharedPref(mContext);

        if (sharedPref.getAlbums() != null && sharedPref.getAlbums().size() != 0) {
//            new SyncAlbumsAsyncTask().execute();
            SyncAlbumsAsyncTask task = new SyncAlbumsAsyncTask(new MyInterface() {
                @Override
                public void myMethod() {
                    progressText.setText("Albums Synced");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

            task.execute();

            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(now);
            sharedPref.setLastSync(timestamp);
//
//            if (count == sharedPref.getAlbums().size()) {
//                progressText.setText("Albums Synced");
//                progressBar.setVisibility(View.INVISIBLE);
//            }

        } else {
            Toast.makeText(mContext, "You do not have any albums to sync", Toast.LENGTH_SHORT).show();
            progressText.setText("No Albums To Sync");
            progressBar.setVisibility(View.INVISIBLE);
        }
        //check if album exist
            //loop album(s)
                //upload one by one

        return rootView;
    }

    private void uploadAlbum(final Album album) {

//        ArrayList<Album> allAlbums = sharedPref.getAlbums();
        final List<MultipartBody.Part> parts = new ArrayList<>();

//        if (album.getFiles() != null && album.getFiles().size() != 0) {
        if (album.getFilePaths() != null && album.getFilePaths().size() != 0) {
            for (int i = 0; i < album.getFilePaths().size(); i++) {
//                Uri uri = Uri.parse(album.getFiles().get(i));
//                if (uri != null && uri.getPath() != null) {
                    File file = new File(album.getFilePaths().get(i));
//                    new File(MyToolBox.getRealPathFromUri(mContext,uri));
                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

//                    try {
//                        mContext.getContentResolver().takePersistableUriPermission(uri, takeFlags );//, takeFlags);
////                        mContext.getContentResolver().ta
//                    } catch (SecurityException e) {
//                        e.printStackTrace();
//                    }

                    String packageName = mContext.getPackageName();
//                    mContext.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    file = new File(new URI(uri.getPath()));

//                    Glide.with(mContext)
//                            .asBitmap()
//                            .load(uri)
//                            .into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
////                                    file[0] = convertBitmapToFile(resource);
//                                    parts.add(prepareFilePart(convertBitmapToFile(resource)));
//                                }
//
//                            });

                    parts.add(prepareFilePart(file));
//                }
            }
            RequestBody description = createPartFromString("files[]");

            RequestBody albumNameBody = RequestBody.create(
                    okhttp3.MultipartBody.FORM, album.getName());
            RequestBody userIdBody = RequestBody.create(
                    okhttp3.MultipartBody.FORM, sharedPref.getUserId() + "");


            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("album_name", albumNameBody);
            map.put("user_id", userIdBody);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WallpaperApp.SERVER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface1 = retrofit.create(ApiInterface.class);
            Call<ServerResponse> call = apiInterface1.syncAlbum(map, description, parts);
            Log.e("SYNC", "over here");
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()) {
//                        Toast.makeText(mContext, album.getName() + " successfully synced", Toast.LENGTH_LONG).show();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                syncStatus += "\n "+ album.getName() + " successfully synced";
                                progressSecondaryText.setText(syncStatus);
                            }
                        });
//                        if (response.body() != null && response.body().size() != 0) {
//                            Log.e("SYNC", response.body().size() +" size");
//                        }
//                        set last sync
                        //dismiss fragment
                        count += 1;
                    } else {

//                        count += 1;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                syncStatus += "\n "+ album.getName() + " failed to sync";
                                progressSecondaryText.setText(syncStatus);
                            }
                        });
//
//                        MyToolBox.AlertMessage(mContext, "Oops", "Something went wrong while syncing your albums. Please check your connection and try again.");
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

//                    count += 1;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncStatus += "\n "+ album.getName() + " failed to sync";
                            progressSecondaryText.setText(syncStatus);
                        }
                    });
//
//                    MyToolBox.AlertMessage(mContext, "Network Error", t.getMessage()+"Please check your connection and try again.");
                }


            });
        }
        else {
            Toast.makeText(mContext, "No file to sync in album", Toast.LENGTH_LONG).show();
        }


    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(File photo) {
        String partName = "files[]";
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),photo);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, photo.getName(), requestFile);
    }


    private File convertBitmapToFile(Bitmap bitmap) {
        File file = null;
        if (bitmap != null) {
//            File fileDir = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/DarlingWallpaper");//this.getString(R.string.app_name));
//            getCacheDir(), "folder"
            File myDir = new File(mContext.getCacheDir(), "Darling");
            myDir.mkdir();
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
            String path = myDir.getAbsolutePath() + timestamp;

            file = new File(path);

            try {
                FileOutputStream outputStream = null;

                try {
                    outputStream = new FileOutputStream(file); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

//        try {
//            mCallback = (SignInFragment.OnSignUpListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement OnSignUpListener");
//        }
    }

    public interface MyInterface {
        public void myMethod();
    }

    class SyncAlbumsAsyncTask extends AsyncTask<Void, Void, Void> {

        private MyInterface mListener;


        public SyncAlbumsAsyncTask(MyInterface listener) {
            this.mListener  = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progres = new ProgressDialog(mContext);
//            progres.setMessage("syncing ...");
//            progres.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progres.setIndeterminate(false);
//            progres.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {
            for (Album album : sharedPref.getAlbums()) {
                uploadAlbum(album);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mListener != null) {
                mListener.myMethod();
            }
//            if(progres.isShowing()){
//                progres.dismiss();
//            }
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    progressText.setText("Albums Synced");
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//            });
//            if (mProgressDialog.isShowing()) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                }
//            });

//            }
        }

    }



}

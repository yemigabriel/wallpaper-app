package com.doxa360.yg.android.darling.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.doxa360.yg.android.darling.R;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;

import java.io.File;
import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class AlbumPhotoAdapter extends RecyclerView.Adapter<AlbumPhotoAdapter.DashboardItemViewHolder> {

    private static final String TAG = AlbumPhotoAdapter.class.getSimpleName();
    private Album mAlbum;
    private List<String> mPhotos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RequestOptions requestOptions;
    private SharedPref sharedPref;

    public AlbumPhotoAdapter(Album album, Context context) {
        mAlbum = album;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        requestOptions = new RequestOptions().fitCenter();
        sharedPref = new SharedPref(mContext);
    }

    class DashboardItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTextView;
        ImageView mImageView;
        ImageView mDelete;


        DashboardItemViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.photo);
            mDelete = itemView.findViewById(R.id.delete);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Album selectedAlbum = null;
//                    String albumName = mAlbum.getName();
//                    String filename = mAlbum.getFiles().get(getPosition());
//                    List<Album> allAlbums = sharedPref.getAlbums();
//                    for (Album album : allAlbums) {
//                        if ( albumName.equalsIgnoreCase(album.getName()) ) {
//                           selectedAlbum = album;
//                        }
//                    }
                    String selectedFile = null;
                    if (mAlbum.getFilePaths() != null && mAlbum.getFilePaths().size() != 0) {
//                        for (String file : mAlbum.getFiles()) {
//                            if (file.equalsIgnoreCase(filename)) {
//                                selectedFile = file;
//                            }
//
//                        }

//                        File deleteFile = new File(mAlbum.getFilePaths().get(getPosition()));
//                        if(deleteFile.exists()) {
//                            deleteFile.delete();
//                        }

                        mAlbum.getFilePaths().remove(getPosition());

//                        mAlbum = selectedAlbum;

                        sharedPref.removeAlbum(mAlbum);
                        //add to shared pref
                        sharedPref.setAlbum(mAlbum);

                        Toast.makeText(mContext, "Photo deleted from album", Toast.LENGTH_LONG).show();
//                        AlbumPhotoAdapter.this.notifyDataSetChanged();
                        AlbumPhotoAdapter.this.notifyItemRemoved(getPosition());
                        //remove album from shared pref
//                        sharedPref.removeAlbum(album);

                    }

                }
            });

        }
    }

    @Override
    public DashboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.album_photo_layout, parent, false);
        return new DashboardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardItemViewHolder holder, int position) {
        String photo = mAlbum.getFilePaths().get(position);
        Log.e(TAG, photo+" ");
        if (photo != null) {
            Glide.with(mContext).load(new File(photo)) //file uri
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(requestOptions)
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mAlbum.getFilePaths() != null)
            return mAlbum.getFilePaths().size();
        else
            return 0;
    }


}

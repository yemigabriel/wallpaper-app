package com.doxa360.yg.android.darling.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.doxa360.yg.android.darling.AlbumDetailActivity;
import com.doxa360.yg.android.darling.R;
import com.doxa360.yg.android.darling.WallpaperApp;
import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;

import java.io.File;
import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.DashboardItemViewHolder> {

    private static final String TAG = AlbumAdapter.class.getSimpleName();
    private List<Album> mAlbum;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RequestOptions requestOptions;
    private boolean isHorizontal = false;
    private SharedPref sharedPref;



    public AlbumAdapter(List<Album> album, Context context) {
        mAlbum = album;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        requestOptions = new RequestOptions().fitCenter();
    }

    public AlbumAdapter(List<Album> album, Context context, boolean isHorizontal) {
        mAlbum = album;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        requestOptions = new RequestOptions().fitCenter();
        this.isHorizontal = isHorizontal;
        sharedPref = new SharedPref(mContext);
    }

    class DashboardItemViewHolder extends RecyclerView.ViewHolder {


        TextView mTitleTextView;
        ImageView mImageView;
        RadioButton mRadioButton;

        DashboardItemViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.titleTextView);
            if (isHorizontal) {
//                mRadioButton = itemView.findViewById(R.id.radio_button);
                mImageView = itemView.findViewById(R.id.photo);

//                mRadioButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mRadioButton.isChecked())
//                            sharedPref.setAutoChangeAlbum(mAlbum.get(getPosition()));
//                    }
//                });
            } else {
                mImageView = itemView.findViewById(R.id.wallpaper_imageview);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isHorizontal) {
                        Intent intent = new Intent(mContext, AlbumDetailActivity.class);
                        intent.putExtra(WallpaperApp.ALBUM, mAlbum.get(getPosition()));
                        mContext.startActivity(intent);
                    } else {
                        MyToolBox.AlertMessage(mContext, "Album ("+ mAlbum.get(getPosition()).getName()+") selected for auto change");

                            sharedPref.setAutoChangeAlbum(mAlbum.get(getPosition()));
                        //                        int adapterPosition = getAdapterPosition();
//
//                        if (getChecked()) {
//                            mRadioButton.setChecked(false);
//                            setChecked(false);
//                        }
//                        else {
//                            mRadioButton.setChecked(true);
//                            setChecked(true);
////                            mCheckedTextView.setChecked(true);
////                            items.get(adapterPosition).setChecked(true);
//                        }
                    }
                }
            });


        }
    }

    private boolean isChecked;

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

//    public void setChecked(boolean value) {
//        if (value) {
//            mRadioButton.setChecked(true);
////            textView.setBackgroundColor(Color.LTGRAY);
//        } else {
////            textView.setBackground(null);
//            mRadioButton.setChecked(false);
//        }
////        mItem.setSelected(value);
////        textView.setChecked(value);
//    }

    @Override
    public DashboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (isHorizontal) {
            view = mLayoutInflater.inflate(R.layout.album_horizontal_layout, parent, false);
        } else {
            view = mLayoutInflater.inflate(R.layout.category_layout, parent, false);
        }
        return new DashboardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardItemViewHolder holder, int position) {
        Album Album = mAlbum.get(position);

        if (Album.getFilePaths() != null && Album.getFilePaths().size() > 0) {
            if (Album.getFilePaths().get(0) != null) {
                Glide.with(mContext).load(new File(Album.getFilePaths().get(0))) //file uri
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOptions)
                        .into(holder.mImageView);
            }
        }
        holder.mTitleTextView.setText(Album.getName());
    }

    @Override
    public int getItemCount() {
        if (mAlbum != null)
            return mAlbum.size();
        else
            return 0;
    }


}

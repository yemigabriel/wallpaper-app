package com.doxa360.yg.android.darling.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.doxa360.yg.android.darling.PreviewActivity;
import com.doxa360.yg.android.darling.R;
import com.doxa360.yg.android.darling.WallpaperApp;
import com.doxa360.yg.android.darling.model.Wallpaper;

import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.DashboardItemViewHolder> {

    private static final String TAG = WallpaperAdapter.class.getSimpleName();
    private List<Wallpaper> mWallpapers;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RequestOptions requestOptions;

    public WallpaperAdapter(List<Wallpaper> wallpapers, Context context) {
        mWallpapers = wallpapers;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        requestOptions = new RequestOptions().fitCenter();//placeholder(R.drawable.placeholder).centerCrop();
    }

     class DashboardItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

         DashboardItemViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.wallpaper_imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PreviewActivity.class);
                    intent.putExtra(WallpaperApp.WALLPAPER, mWallpapers.get(getPosition()) );
                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public DashboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.wallpaper_layout, parent, false);
        return new DashboardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardItemViewHolder holder, int position) {
        Wallpaper wallpaper = mWallpapers.get(position);

        Glide.with(mContext).load(WallpaperApp.THUMBNAIL_URL + wallpaper.getUrl())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .apply(requestOptions)
//                            .thumbnail(Glide.with(mContext)
//                                    .load(fastLoadUrl)
//                                    .apply(requestOptions))
                            .into(holder.mImageView);
//        Picasso.with(mContext).load(wallpaper.getImage()).placeholder(R.drawable.placeholder_demo).into(holder.mImageView);
//        holder.mTitleTextView.setText(wallpaper.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mWallpapers != null)
            return mWallpapers.size();
        else
            return 0;
    }


}

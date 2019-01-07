package com.doxa360.yg.android.wallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.yg.android.wallpaper.PreviewActivity;
import com.doxa360.yg.android.wallpaper.R;
import com.doxa360.yg.android.wallpaper.WallpaperApp;
import com.doxa360.yg.android.wallpaper.helpers.MyToolBox;
import com.doxa360.yg.android.wallpaper.model.Category;
import com.doxa360.yg.android.wallpaper.model.Wallpaper;
import com.squareup.picasso.Picasso;

/**
 * Created by Apple on 09/01/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.DashboardItemViewHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private Category[] mCategories;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public CategoryAdapter(Category[] categories, Context context) {
        mCategories = categories;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class DashboardItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTextView;
//        TextView mDetailTextView;
        ImageView mImageView;

        public DashboardItemViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
//            mDetailTextView = (TextView) itemView.findViewById(R.id.detailTextView);
            mImageView = (ImageView) itemView.findViewById(R.id.wallpaper_imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(mContext, "(Demo: more details)", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, PreviewActivity.class);
//                    intent.putExtra(WallpaperApp.WALLPAPER, mCategories[getPosition()]);
//                    mContext.startActivity(intent);
                    //demo alert
                    MyToolBox.AlertMessage(mContext, mContext.getString(R.string.demo_alert_message));
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
    public void onBindViewHolder(@NonNull DashboardItemViewHolder holder, int position) {
        Category category = mCategories[position];

        Picasso.with(mContext).load(category.getIcon()).placeholder(R.drawable.placeholder_demo).into(holder.mImageView);
        holder.mTitleTextView.setText(category.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mCategories != null)
            return mCategories.length;
        else
            return 0;
    }


}

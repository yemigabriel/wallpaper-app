package com.doxa360.yg.android.darling.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.doxa360.yg.android.darling.CategoryDetailActivity;
import com.doxa360.yg.android.darling.R;
import com.doxa360.yg.android.darling.WallpaperApp;
import com.doxa360.yg.android.darling.model.Category;

import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.DashboardItemViewHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private List<Category> mCategories;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RequestOptions requestOptions;

    public CategoryAdapter(List<Category> categories, Context context) {
        mCategories = categories;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        requestOptions = new RequestOptions().fitCenter();
    }

    class DashboardItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTextView;
        ImageView mImageView;

        DashboardItemViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.titleTextView);
            mImageView = itemView.findViewById(R.id.wallpaper_imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CategoryDetailActivity.class);
                    intent.putExtra(WallpaperApp.CATEGORY, mCategories.get(getPosition()) );
                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public DashboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.category_layout, parent, false);
        return new DashboardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardItemViewHolder holder, int position) {
        Category category = mCategories.get(position);

        if (category.getWallpapers() != null && category.getWallpapers().size() > 0) {
            Glide.with(mContext).load(WallpaperApp.THUMBNAIL_URL + category.getWallpapers().get(0).getUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(requestOptions)
                    .into(holder.mImageView);
        }
        holder.mTitleTextView.setText(category.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mCategories != null)
            return mCategories.size();
        else
            return 0;
    }


}

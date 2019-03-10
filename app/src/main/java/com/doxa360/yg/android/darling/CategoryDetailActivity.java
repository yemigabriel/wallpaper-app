package com.doxa360.yg.android.darling;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.doxa360.yg.android.darling.adapter.WallpaperAdapter;
import com.doxa360.yg.android.darling.model.Category;
import com.doxa360.yg.android.darling.model.Wallpaper;
import com.doxa360.yg.android.darling.service.ApiInterface;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {


    public final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Category mCategory;
    private Wallpaper[] mWallpapers;
    private List<Wallpaper> mWallpaperList;
    private WallpaperAdapter adapter;
    private ApiInterface mApiInterface;
    private ProgressBar mProgressBar;
    private ImageView mRefreshImage;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get wallpaper intent
        Intent intent = getIntent();
        if (intent != null)
            mCategory = intent.getParcelableExtra(WallpaperApp.CATEGORY);

        getSupportActionBar().setTitle(mCategory.getTitle());
        getSupportActionBar().setSubtitle(mCategory.getWallpaperCount() + " wallpaper(s)");
        shimmerFrameLayout = findViewById(R.id.parentShimmerLayout);
        shimmerFrameLayout.setAngle(ShimmerFrameLayout.MaskAngle.CW_270);
        shimmerFrameLayout.setMaskShape(ShimmerFrameLayout.MaskShape.LINEAR);

        mProgressBar = findViewById(R.id.progress_bar);
        mRefreshImage = findViewById(R.id.refresh_image);
        mRefreshImage.setVisibility(View.INVISIBLE);
        mRefreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategoryWallpapers();
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategoryWallpapers();
            }
        });

        mRecyclerView = findViewById(R.id.wallpaper_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, this.getResources().getInteger(R.integer.channels_grid_columns));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }

        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        getCategoryWallpapers();

    }

    private void getCategoryWallpapers() {
        mWallpaperList = mCategory.getWallpapers();

        adapter = new WallpaperAdapter(mWallpaperList, this);
        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.INVISIBLE);

        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

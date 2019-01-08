package com.doxa360.yg.android.wallpaper;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.doxa360.yg.android.wallpaper.R;
import com.doxa360.yg.android.wallpaper.adapter.DashboardAdapter;
import com.doxa360.yg.android.wallpaper.adapter.WallpaperAdapter;
import com.doxa360.yg.android.wallpaper.helpers.MyToolBox;
import com.doxa360.yg.android.wallpaper.model.Wallpaper;
import com.doxa360.yg.android.wallpaper.service.ApiClient;
import com.doxa360.yg.android.wallpaper.service.ApiInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private Wallpaper[] mWallpapers;
    private List<Wallpaper> mWallpaperList;
    private WallpaperAdapter adapter;
    private ApiInterface mApiInterface;
    private ProgressBar mProgressBar;
    private ImageView mRefreshImage;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mRefreshImage = rootView.findViewById(R.id.refresh_image);
        mRefreshImage.setVisibility(View.INVISIBLE);
        mRefreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWallpapers();
            }
        });

        mSwipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWallpapers();
            }
        });

        mRecyclerView = rootView.findViewById(R.id.wallpaper_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns));
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


        getWallpapers();

        return rootView;
    }

    private void getWallpapers() {

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Wallpaper>> call = mApiInterface.recentWallpapers();
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(@NonNull Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                Log.e(TAG, response.message() + response.body().size());
                if (response.isSuccessful()) {
                    mWallpaperList = response.body();
                    adapter = new WallpaperAdapter(mWallpaperList, mContext);
                    mRecyclerView.setAdapter(adapter);
                    mProgressBar.setVisibility(View.INVISIBLE);

                } else {
                    try {
                        Log.e(TAG, response.errorBody().string()+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRefreshImage.setVisibility(View.VISIBLE);
                    MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection.");
                }

                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<Wallpaper>> call, Throwable t) {
                MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection.");
                mProgressBar.setVisibility(View.INVISIBLE);
                mRefreshImage.setVisibility(View.VISIBLE);
                Log.e(TAG, t.getMessage()+"");

                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }
}

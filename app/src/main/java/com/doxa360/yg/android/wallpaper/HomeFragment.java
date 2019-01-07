package com.doxa360.yg.android.wallpaper;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doxa360.yg.android.wallpaper.R;
import com.doxa360.yg.android.wallpaper.adapter.DashboardAdapter;
import com.doxa360.yg.android.wallpaper.adapter.WallpaperAdapter;
import com.doxa360.yg.android.wallpaper.model.Wallpaper;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private Context mContext;
    private Wallpaper[] mWallpapers;
    private WallpaperAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = rootView.findViewById(R.id.wallpaper_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns));
        mRecyclerView.setLayoutManager(layoutManager);

        mWallpapers = new Wallpaper[] {
                new Wallpaper("Sample 1",null,R.drawable.appbg, "Category 1",false),
                new Wallpaper("Sample 2",null,R.drawable.nature_demo, "Category 1",false),
                new Wallpaper("Sample 3",null,R.drawable.thanos_demo, "Category 2",false),
                new Wallpaper("Sample 4",null,R.drawable.cars_demo, "Category 2",false),
                new Wallpaper("Sample 5",null,R.drawable.soccer_demo, "Category 3",false),
                new Wallpaper("Sample 6",null,R.drawable.faith_demo, "Category 4",false),
                new Wallpaper("Sample 7",null,R.drawable.intern_bg2, "Category 4",false),
                new Wallpaper("Sample 8",null,R.drawable.inspiration_demo, "Category 4",false),
//                new Wallpaper("Sample 9",null,R.drawable.movie_demo, "Category 4",false),
        };

        getWallpapers();

        return rootView;
    }

    private void getWallpapers() {
        adapter = new WallpaperAdapter(mWallpapers, mContext);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }
}

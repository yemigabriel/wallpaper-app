package com.doxa360.yg.android.wallpaper;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doxa360.yg.android.wallpaper.adapter.CategoryAdapter;
import com.doxa360.yg.android.wallpaper.model.Category;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private Category[] mCategories;
    private CategoryAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        mRecyclerView = rootView.findViewById(R.id.category_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns));
        mRecyclerView.setLayoutManager(layoutManager);

        mCategories = new Category[] {
                new Category("Nature", R.drawable.nature_demo, 3),
                new Category("Sports", R.drawable.soccer_demo, 2),
                new Category("Inspiration", R.drawable.inspiration_demo, 7),
                new Category("Cars", R.drawable.cars_demo, 5),
                new Category("Faith", R.drawable.faith_demo, 11),
                new Category("Movies", R.drawable.thanos_demo, 6),
                new Category("Urban", R.drawable.appbg, 1),
        };

        getCategories();

        return rootView;
    }

    private void getCategories() {
        adapter = new CategoryAdapter(mCategories, mContext);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

}

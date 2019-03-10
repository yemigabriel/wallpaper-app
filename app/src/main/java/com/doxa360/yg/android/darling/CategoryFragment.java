package com.doxa360.yg.android.darling;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.doxa360.yg.android.darling.adapter.CategoryAdapter;
import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.model.Category;
import com.doxa360.yg.android.darling.service.ApiClient;
import com.doxa360.yg.android.darling.service.ApiInterface;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private Category[] mCategories;
    private CategoryAdapter adapter;
    private ApiInterface mApiInterface;
    private ProgressBar mProgressBar;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ImageView mRefreshImage;

    private List<Category> mCategoryList;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        shimmerFrameLayout = rootView.findViewById(R.id.parentShimmerLayout);
        shimmerFrameLayout.setAngle(ShimmerFrameLayout.MaskAngle.CW_270);
        shimmerFrameLayout.setMaskShape(ShimmerFrameLayout.MaskShape.LINEAR);

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mRefreshImage = rootView.findViewById(R.id.refresh_image);
        mRefreshImage.setVisibility(View.INVISIBLE);
        mRefreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategories();
            }
        });

        mSwipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategories();
            }
        });

        mRecyclerView = rootView.findViewById(R.id.category_recyclerview);
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
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        getCategories();

        return rootView;
    }

    private void getCategories() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Category>> call = mApiInterface.allCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    mCategoryList = response.body();
                    adapter = new CategoryAdapter(mCategoryList, mContext);
                    mRecyclerView.setAdapter(adapter);
                    mProgressBar.setVisibility(View.INVISIBLE);

                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRefreshImage.setVisibility(View.VISIBLE);
                    MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection.");
                }

                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection.");
                mProgressBar.setVisibility(View.INVISIBLE);
                mRefreshImage.setVisibility(View.VISIBLE);

                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }

}

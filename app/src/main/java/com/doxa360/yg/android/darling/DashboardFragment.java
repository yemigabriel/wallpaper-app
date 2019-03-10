package com.doxa360.yg.android.darling;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doxa360.yg.android.darling.adapter.DashboardAdapter;
import com.doxa360.yg.android.darling.model.Dashboard;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    private Context mContext;
    private RecyclerView mRecyclerView;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.dashboard_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns));
        mRecyclerView.setLayoutManager(layoutManager);

        Dashboard dashboard1 = new Dashboard(1, "Total Contributions", "N 200,000", R.drawable.profile_two, getResources().getColor(R.color.oldaccent));
        Dashboard dashboard2 = new Dashboard(1, "Total Loans", "N 150,000", R.drawable.profile_two, getResources().getColor(R.color.colorRed));
        Dashboard dashboard3 = new Dashboard(1, "Recent Payments", "N 30,000", R.drawable.profile_two, getResources().getColor(R.color.accent));
        Dashboard dashboard4 = new Dashboard(1, "Next Due Date", "5th Nov 2017", R.drawable.profile_two, getResources().getColor(R.color.colorGreen));

        List<Dashboard> dashboardItems = new ArrayList<Dashboard>();
        dashboardItems.add(dashboard1);
        dashboardItems.add(dashboard2);
        dashboardItems.add(dashboard3);
        dashboardItems.add(dashboard4);

        DashboardAdapter adapter = new DashboardAdapter(dashboardItems, mContext);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


}

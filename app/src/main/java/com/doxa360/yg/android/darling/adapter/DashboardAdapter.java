package com.doxa360.yg.android.darling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.yg.android.darling.R;
import com.doxa360.yg.android.darling.model.Dashboard;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardItemViewHolder> {

    private static final String TAG = DashboardAdapter.class.getSimpleName();
    private List<Dashboard> mDashboardItems;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public DashboardAdapter(List<Dashboard> dashboardItems, Context context) {
        mDashboardItems = dashboardItems;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class DashboardItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTextView;
        TextView mDetailTextView;
        ImageView mIconImageView;

        public DashboardItemViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            mDetailTextView = (TextView) itemView.findViewById(R.id.detailTextView);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "(Demo: more details)", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public DashboardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.dashboard_layout, parent, false);
        return new DashboardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardItemViewHolder holder, int position) {
        Dashboard dashboardItem = mDashboardItems.get(position);

        Picasso.with(mContext).load(dashboardItem.getIcon()).into(holder.mIconImageView);
        holder.mTitleTextView.setBackgroundColor(dashboardItem.getColor());
        holder.mTitleTextView.setText(dashboardItem.getTitle());
        holder.mDetailTextView.setText(dashboardItem.getDetail());
    }

    @Override
    public int getItemCount() {
        if (mDashboardItems != null)
            return mDashboardItems.size();
        else
            return 0;
    }


}

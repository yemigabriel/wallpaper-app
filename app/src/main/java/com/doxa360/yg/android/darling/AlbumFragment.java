package com.doxa360.yg.android.darling;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doxa360.yg.android.darling.adapter.AlbumAdapter;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;

import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    private static final String SHOWCASE_ID = "AlbumShowcase";
    private final String TAG = getClass().getSimpleName().toString();
    private static final int ADD_ALBUM_BOTTOM_SHEET = 19122;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private FloatingActionButton add_album_fab;
    private List<Album> albums;
    private AlbumAdapter adapter;
    private SharedPref sharedPref;


    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        sharedPref = new SharedPref(mContext);

        mRecyclerView = rootView.findViewById(R.id.album_recyclerview);
        add_album_fab = rootView.findViewById(R.id.add_album_fab);
        add_album_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlbumBottomSheetFragment bottomSheetDialog = AddAlbumBottomSheetFragment.getInstance();
                bottomSheetDialog.setTargetFragment(AlbumFragment.this, ADD_ALBUM_BOTTOM_SHEET);
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Custom Bottom Sheet");
            }
        });

        mRecyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns));
        mRecyclerView.setLayoutManager(layoutManager);

        getAlbums();

        if (!sharedPref.isAlbumTutorial()) {
            startTutorialSequence();
        }

        return rootView;
    }

    private void getAlbums() {
        //get album list from shared preferences
        albums = sharedPref.getAlbums();
        //pass list to adapter
        this.adapter = new AlbumAdapter(this.albums, mContext);
        mRecyclerView.setAdapter(this.adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_ALBUM_BOTTOM_SHEET) {
                Log.e(TAG, "dialog closed");
                getAlbums();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAlbums();
    }

    private void startTutorialSequence() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(add_album_fab,
                "Create albums and add photos from your gallery to it. Tap here to begin", "GOT IT");
//        sequence.addSequenceItem(mApplyBtn,
//                "Tap to download this wallpaper to your device", "GOT IT");
//        sequence.addSequenceItem(tab3,
//                getString(R.string.tooltip_four), "GOT IT");
        sequence.start();

        sharedPref.setAlbumTutorial(true);
    }

}

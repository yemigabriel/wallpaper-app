package com.doxa360.yg.android.darling;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.doxa360.yg.android.darling.helpers.SharedPref;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSyncFragment extends Fragment {

    private static final String SHOWCASE_ID = "AccountSyncShowcase";
    Button syncBtn, retrieveBtn;
    TextView lastSynced, syncDesc;
    private Context mContext;
    SharedPref sharedPref;


    public AccountSyncFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account_sync, container, false);

        syncBtn = rootView.findViewById(R.id.syncBtn);
        retrieveBtn = rootView.findViewById(R.id.retrieveBtn);
        lastSynced = rootView.findViewById(R.id.last_sync);
        syncDesc = rootView.findViewById(R.id.not_sync);

        sharedPref = new SharedPref(mContext);
        if (sharedPref.getLastSync()!=null) {
            lastSynced.setText("Last synced: " + sharedPref.getLastSync());
        }

        if (!sharedPref.isSyncTutorial()) {
            startTutorialSequence();
        }

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if () do shared pref
                if (sharedPref.getUserId() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("")
                            .setMessage("Syncing requires an account. Please sign in or create an account.")
                            .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // check for albums
                                    Intent intent = new Intent(mContext, AccountActivity.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //do sync
                    syncAlbums();
                }
            }
        });

        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if () do shared pref
                if (sharedPref.getUserId() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("")
                            .setMessage("Retrieving synced albums requires an account. Please sign in or create an account.")
                            .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // check for albums
                                    Intent intent = new Intent(mContext, AccountActivity.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //do retrieve
                    retrieveAlbums();
                }
            }
        });

        return rootView;
    }

    private void syncAlbums() {
        SyncFragment fragment = new SyncFragment();
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, "SYNC_FRAGMENT").commit();
    }

    private void retrieveAlbums() {
        RetrieveFragment fragment = new RetrieveFragment();
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, "RETRIEVE_FRAGMENT").commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void startTutorialSequence() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(syncBtn,
                "Tap to sync your albums and have it available on multiple devices", "NEXT");
        sequence.addSequenceItem(retrieveBtn,
                "Tap to retrieve your already synced albums", "GOT IT");
//        sequence.addSequenceItem(mRecyclerView,
//                "Choose the album containing the photos you would like to use for auto change. Ensure you have created an album first", "GOT IT");
        sequence.start();

        sharedPref.setSyncTutorial(true);
    }


}

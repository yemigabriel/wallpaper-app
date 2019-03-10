package com.doxa360.yg.android.darling;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.doxa360.yg.android.darling.adapter.AlbumAdapter;
import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;
import com.doxa360.yg.android.darling.service.WallpaperTimerReceiver;

import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AutoChangerFragment extends Fragment {


    private static final String SHOWCASE_ID = "AutoChangerShowcase";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private Spinner mFrequencySpinner;
    private SwitchCompat mAutoChangeSwitch;
    private SharedPref sharedPref;

    public AutoChangerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_auto_changer, container, false);

        sharedPref = new SharedPref(mContext);

        mAutoChangeSwitch = rootView.findViewById(R.id.autochange_switch);
        mFrequencySpinner = rootView.findViewById(R.id.frequency_options);
        mRecyclerView = rootView.findViewById(R.id.album_recyclerview);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        if (sharedPref.isAutoChanger()) {
            mAutoChangeSwitch.setChecked(true);
        } else {
            mAutoChangeSwitch.setChecked(false);
        }

        mAutoChangeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutoChangeSwitch.isChecked()) {
                    if (!checkFrequency()) {
                        MyToolBox.AlertMessage(mContext, "Auto change", "Please choose an auto change frequency");
                        mAutoChangeSwitch.setChecked(false);
                    }
                    else if (!checkAlbumExists()) {
                        MyToolBox.AlertMessage(mContext, "Auto change", "You do not have any albums. Please create an album for auto change to work");
                        mAutoChangeSwitch.setChecked(false);
                    }
                    else if (!checkAlbumHasPhotos()) {
                        MyToolBox.AlertMessage(mContext, "Auto change", "The album you have selected has no images. Please add an image to the album");
                        mAutoChangeSwitch.setChecked(false);
                    }
                    else if (checkAlbumHasMoreThanOnePhoto()) {
                        MyToolBox.AlertMessage(mContext, "Auto change", "The album you have selected has only one image. Please add at least one more image to this album to enable auto change");
                        mAutoChangeSwitch.setChecked(false);
                    }
                    else {
                        setAutoChanger();
                    }
                }
                else {
                    Toast.makeText(mContext, "Auto change disabled", Toast.LENGTH_SHORT).show();
                    sharedPref.setAutoChanger(false);
                    removeAlarmManager();
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mFrequencySpinner.setAdapter(adapter);

        if (sharedPref.getFrequency() != 0) {
            mFrequencySpinner.setSelection(sharedPref.getFrequencyPos());
        }

        mFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int timer = 0;
                switch (position) {
                    case 0:
                        timer = 60;
                        break;
                    case 1:
                        timer = 15 * 60;
                        break;
                    case 2:
                        timer = 60 * 60;
                        break;
                    case 3:
                        timer = 6 * 60 * 60;
                        break;
                    case 4:
                        timer = 12 * 60 * 60;
                        break;
                    case 5:
                        timer = 24 * 60 * 60;
                        break;
                    case 6:
                        timer = 24 * 7 * 60 * 60;
                        break;
                }
                sharedPref.setFrequency(timer);
                sharedPref.setFrequencyPos(position);
//                Toast.makeText(mContext, "Auto change timer set", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (!sharedPref.isAutoChangeTutorial()) {
            startTutorialSequence();
        }

        getAlbums();
        return rootView;
    }

    private void setAutoChanger() {
        sharedPref.setAutoChanger(true);
        MyToolBox.AlertMessage(mContext, "Auto change", "Auto changer is successfully enabled");
        setTimer(sharedPref.getFrequency());
    }

    private boolean checkAlbumExists() {
        if (sharedPref.getAutoChangeAlbum() != null) {
            return true;
        }
        return false;
    }

    private boolean checkAlbumHasPhotos() {
        if (sharedPref.getAutoChangeAlbum().getFilePaths() != null && sharedPref.getAutoChangeAlbum().getFilePaths().size() != 0) {
            return true;
        }
        return false;
    }

    private boolean checkAlbumHasMoreThanOnePhoto() {
        Log.e("AutoFragment", " photo: "+ sharedPref.getAutoChangeAlbum().getFilePaths().size());
        if (sharedPref.getAutoChangeAlbum().getFilePaths().size() == 1) {
            return true;
        }
        return false;
    }

    private boolean checkFrequency() {
        if (sharedPref.getFrequency() != 0) {
            return true;
        }
        return false;
    }

    private void removeAlarmManager() {
        Intent intent = new Intent(mContext, WallpaperTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        //remove from shared pref as well
        sharedPref.removeFrequency();

    }


    private void getAlbums() {
        List<Album> albums = sharedPref.getAlbums();
        //pass list to adapter
        AlbumAdapter adapter = new AlbumAdapter(albums, mContext, true);
        mRecyclerView.setAdapter(adapter);

    }

    private void setTimer(int timer) {
        //timer is already in seconds
//        timer = 15;
//        int repeatTime = 60 * timer;  //Repeat alarm time in seconds
        AlarmManager autochangeTimer = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(mContext, WallpaperTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (autochangeTimer != null) {
            autochangeTimer.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),timer*1000, pendingIntent);
        }

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
        sequence.addSequenceItem(mAutoChangeSwitch,
                "Auto change allows you to automatically change your wallpaper at scheduled intervals.", "NEXT");
        sequence.addSequenceItem(mFrequencySpinner,
                "Choose the intervals you want the wallpaper to change", "NEXT");
        sequence.addSequenceItem(mRecyclerView,
                "Choose the album containing the photos you would like to use for auto change. Ensure you have created an album first", "GOT IT");
        sequence.start();

        sharedPref.setAutoChangeTutorial(true);
    }

}

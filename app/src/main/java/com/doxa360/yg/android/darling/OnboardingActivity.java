package com.doxa360.yg.android.darling;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.doxa360.yg.android.darling.helpers.SharedPref;

public class OnboardingActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button skipButton, finishButton;
    private ImageButton nextButton;
    private ImageView[] indicators;
//    int color1 = this.getColor(this, R.color.accent);
//    int color2 = ContextCompat.getColor(this, R.color.primary);
//    int color3 = ContextCompat.getColor(this, R.color.primary_dark);

    //    int[] colorList = new int[]{color1, color2, color3};
    private int page = 0;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        sharedPref = new SharedPref(this);

        skipButton = findViewById(R.id.skip_button);
        finishButton = findViewById(R.id.finish_button);
        nextButton = findViewById(R.id.next_button);

        indicators = new ImageView[] {
                findViewById(R.id.intro_indicator_0),
                findViewById(R.id.intro_indicator_1),
                findViewById(R.id.intro_indicator_2),
        };

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        updateIndicators(page);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(OnboardingActivity.this, "Get started ...", Toast.LENGTH_LONG).show();
                getStarted();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(OnboardingActivity.this, "Get started ...", Toast.LENGTH_LONG).show();
                getStarted();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                page = i;
                updateIndicators(page);


                nextButton.setVisibility(i == 2 ? View.INVISIBLE : View.VISIBLE);
                nextButton.setClickable(i != 2);
                finishButton.setVisibility(i == 2 ? View.VISIBLE : View.INVISIBLE);
                finishButton.setClickable(i == 2);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



    }

    private void getStarted() {
        //do shared pref - onboarding
//        Intent intent = new Intent(this, DashboardActivity.class);
//        startActivity(intent);

        sharedPref.setSlideShown();
        Intent intent = new Intent(OnboardingActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        String titleArray[] = {"Categories", "Auto Change", "HD Wallpapers" };
        String descArray[] = {
                "Find wallpapers curated into simple categories",
                "Schedule automatic wallpaper changes for more customization",
                "Get easy access to HD wallpapers to personalize your device",
//                "Find and apply for projects/jobs in your field",
//                "Find and hire professionals for your project or job",
//                "Post a project/job and get bids"
        };
        int imageArray[] = {R.drawable.darling_slide_categories, R.drawable.darling_slide_schedule, R.drawable.darling_slide_hd  };

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);
            ImageView imageView = rootView.findViewById(R.id.imageView);
            TextView titleLabel = rootView.findViewById(R.id.title_label);
            TextView descLabel = rootView.findViewById(R.id.desc_label);

            titleLabel.setText(titleArray[getArguments().getInt(ARG_SECTION_NUMBER)]);
            descLabel.setText(descArray[getArguments().getInt(ARG_SECTION_NUMBER)]);
            Glide.with(this).load(imageArray[getArguments().getInt(ARG_SECTION_NUMBER)])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(new RequestOptions().fitCenter())
                    .into(imageView);


            return rootView;
        }
    }


    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }



}

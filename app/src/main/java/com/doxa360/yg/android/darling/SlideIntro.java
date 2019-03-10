package com.doxa360.yg.android.darling;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class SlideIntro extends AppIntro2 {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Wallpaper Demo App", "Beautify your screen with high quality wallpapers",
                R.drawable.outline_image_white_24dp, Color.parseColor("#e040fb")));
        addSlide(AppIntroFragment.newInstance("Explore", "Explore different categories of wallpapers to beautify your phone screen",
                R.drawable.outline_collections_white_24dp, Color.parseColor("#9C27B0")));
        addSlide(AppIntroFragment.newInstance("Daily", "Set daily wallpapers automatically",
                R.drawable.outline_event_white_24dp, Color.parseColor("#e040fb")));

        setProgressButtonEnabled(true);

        showSkipButton(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);

        setCustomTransformer(new ZoomOutPageTransformer());

    }

//    @Override
//    public void init(Bundle savedInstanceState) {
//
//        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_title_one), getString(R.string.slide_one_description),
//                R.drawable.closebyfriends, Color.parseColor("#FF9800")));
//        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_two_title), getString(R.string.slide_two_description),
//                R.drawable.nearbyhelp, Color.parseColor("#FF9800")));
//        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_three_title), getString(R.string.slide_three_description),
//                R.drawable.customers, Color.parseColor("#FF9800")));
//        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_four_title), getString(R.string.slide_four_description),
//                R.drawable.contacts, Color.parseColor("#FF9800")));
//
//        setProgressButtonEnabled(true);
//
//        // Turn vibration on and set intensity.
//        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
//
//        setCustomTransformer(new ZoomOutPageTransformer());
//    }

    private void goToActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        goToActivity(DashboardActivity.class);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        goToActivity(DashboardActivity.class);
    }

    //    @Override
//    public void onDonePressed() {
//        goToActivity(CreateAccountActivity.class);
//    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


}

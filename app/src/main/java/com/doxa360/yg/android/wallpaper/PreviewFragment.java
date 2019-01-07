package com.doxa360.yg.android.wallpaper;


import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.doxa360.yg.android.wallpaper.helpers.BlurImage;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewFragment extends Fragment {

    private ImageView mPreviewWallpaper, mBackgroundBlur;
    private WallpaperManager wallpaperManager;
    private Context mContext;
    private Bitmap bitmap;
    private ConstraintLayout mBackgroundLayout;
    private Button mSetButton;

    public PreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View) inflater.inflate(R.layout.fragment_preview, container, false);

        wallpaperManager = WallpaperManager.getInstance(mContext);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.appbg);
        mBackgroundLayout = rootView.findViewById(R.id.constraint_layout);
        mPreviewWallpaper = (ImageView) rootView.findViewById(R.id.wallpaper_preview);
        mPreviewWallpaper = (ImageView) rootView.findViewById(R.id.background_blur);
        mSetButton = rootView.findViewById(R.id.set_button);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "wallpaper set",Snackbar.LENGTH_LONG).setAction("undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,"undone",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
        setUpBackgroundColor();

        return rootView;
    }

    private void generatePalette() {

//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                setUpBackgroundColor();
//            }
//        });
    }

    private void setUpBackgroundColor() {
//        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
//        Palette.Swatch mutedSwatch = palette.getDarkVibrantSwatch();
//        if (vibrantSwatch != null) {
//            mBackgroundLayout.setBackgroundColor(vibrantSwatch.getRgb());
//            if (mutedSwatch != null) {
//                int[] colors = new int[] {vibrantSwatch.getRgb(), mutedSwatch.getRgb()};
//                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
//                mBackgroundLayout.setBackground(gradientDrawable);
//            }
//        }
//        Target target = new Target() {
//
//        }
        mBackgroundBlur.setImageBitmap(bitmap);

    }

    private void displayCurrentWallpaper() {
        Drawable currentWallpaperDrawable = wallpaperManager.getDrawable();
        mPreviewWallpaper.setImageDrawable(currentWallpaperDrawable);
    }

    private void setPhoneWallpaper(View view) {

        Snackbar.make(view, "New wallpaper", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

//        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.appbg);
//        if (wallpaperManager.isSetWallpaperAllowed())


        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}

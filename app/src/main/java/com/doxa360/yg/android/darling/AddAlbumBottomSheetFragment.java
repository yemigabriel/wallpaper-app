package com.doxa360.yg.android.darling;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;
import com.squareup.picasso.Picasso;
import com.thinkincode.utils.views.HorizontalFlowLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.doxa360.yg.android.darling.helpers.MyToolBox.isExternalStorageAvailable;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAlbumBottomSheetFragment extends BottomSheetDialogFragment {

    private static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10; //10MB
    private final String TAG = this.getClass().getSimpleName();
    private static final int CHOOSE_PHOTO_REQUEST = 1901;
    private TextInputEditText nameEditText;
    private TextInputLayout nameLayout;
    private TextView addPhoto;
    private HorizontalFlowLayout horizontalFlowLayout;
    private Button addAlbumButton;
    private Context context;
    private ArrayList<String> fileUri;
    private SharedPref sharedPref;
    private ArrayList<String> filePath;

    public static AddAlbumBottomSheetFragment getInstance() {
        return new AddAlbumBottomSheetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_album_bottom_sheet, container, false);

        sharedPref = new SharedPref(this.context);
        fileUri = new ArrayList<String>();
        filePath = new ArrayList<String>();

        nameEditText = rootView.findViewById(R.id.album_name);
        nameLayout = rootView.findViewById(R.id.album_name_layout);
        addPhoto = rootView.findViewById(R.id.add_photo);
        horizontalFlowLayout = rootView.findViewById(R.id.horizontal_flow_layout);
        addAlbumButton = rootView.findViewById(R.id.add_album_button);

        addAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().trim().length() < 1 )  {
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please type a name");
                } else if (filePath == null && filePath.size() == 0) {
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please tap the button below to add photos");
                }
                else {
                    Album album = new Album(nameEditText.getText().toString().trim(), fileUri.size(),
                            fileUri, filePath, new Date());
                    sharedPref.setAlbum(album);
                    Toast.makeText(context, "Album added", Toast.LENGTH_LONG).show();
                    //do update of prev fragment
                    getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, null);
                    getDialog().dismiss();
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });

        return rootView;
    }

    private void addPhoto() {
        //pick from gallery
//        Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent chooseImageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        chooseImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        chooseImageIntent.setType("image/*");
//        //Toast.makeText(getActivity(), "The size of your video must be less than 10MB", Toast.LENGTH_LONG).show();
//        startActivityForResult(chooseImageIntent, CHOOSE_PHOTO_REQUEST);

        // Create an intent.
        Intent openAlbumIntent = new Intent();
        // Only show images in the content chooser.
        // If you want to select all type data then openAlbumIntent.setType("*/*");
        // Must set type for the intent, otherwise there will throw android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.GET_CONTENT }
        openAlbumIntent.setType("image/*");
        // Set action, this action will invoke android os browse content app.
        openAlbumIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        openAlbumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Start the activity.
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO_REQUEST);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO_REQUEST){
                Uri mMediaUri = null;
                if (data == null){
                    Toast.makeText(context,"there was an error selecting your photo",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if (data.getData() != null){
                        mMediaUri = data.getData();
                        getActivity().grantUriPermission(getActivity().getPackageName(), mMediaUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        getActivity().grantUriPermission(getActivity().getPackageName(), mMediaUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        try {
                            context.getContentResolver().takePersistableUriPermission(mMediaUri, takeFlags);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }

                        int fileSize = 0;
                        InputStream inputStream = null;
                        try{
                            inputStream = context.getContentResolver().openInputStream(mMediaUri);
                            assert inputStream != null;
                            fileSize = inputStream.available();
                        } catch (IOException e){
                            Toast.makeText(context,"Error opening image. Please try again.",Toast.LENGTH_LONG).show();
                            return;
                        }
                        finally {
                            try{
                                assert inputStream != null;
                                inputStream.close();
                            } catch (IOException e){/*Intentionally blank*/ }
                        }
                        if (fileSize >= FILE_SIZE_LIMIT){
                            Toast.makeText(context,"The selected image is too large. Please choose another photo.",Toast.LENGTH_LONG).show();
                            return;
                        }

//                        this.fileUri.add(mMediaUri.toString());
//                        String filePath = MyToolBox.getRealPathFromUri(context, mMediaUri);
//                        this.filePath.add(filePath);
                        File photoFile = createFile("Albums");
                        byte[] fileBytes = getByteArrayFromFile(context, mMediaUri);

                        FileOutputStream fos = null;
                        try {
                            assert photoFile != null;
                            fos = new FileOutputStream(photoFile);
                            fos.write(fileBytes);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        this.fileUri.add(mMediaUri.toString());
                        String filePath = photoFile.getAbsolutePath();
                        this.filePath.add(filePath);


                    }
                     else if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();

                            getActivity().grantUriPermission(getActivity().getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            try {
                                context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }

                            File photoFile = createFile("Albums", i);
                            byte[] fileBytes = getByteArrayFromFile(context, uri);

                            FileOutputStream fos = null;
                            try {
                                assert photoFile != null;
                                fos = new FileOutputStream(photoFile);
                                fos.write(fileBytes);
                                fos.flush();
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            this.fileUri.add(uri.toString());
                            String filePath = photoFile.getAbsolutePath();
                            this.filePath.add(filePath);



                        }

                    }

                    horizontalFlowLayout.removeAllViews();
                    for (int i = 0; i < this.filePath.size(); i++) {
                        horizontalFlowLayout.addView(getPhotoIcon(this.filePath.get(i), i));
                    }
//                    for (String mediaUri : this.fileUri) {
//                        horizontalFlowLayout.addView(getPhotoIcon(mediaUri));
//                    }
                    Toast.makeText(context, horizontalFlowLayout.getChildCount() + " image(s) added", Toast.LENGTH_SHORT).show();
                }


//                fileBytes = getByteArrayFromFile(PostAdActivity.this, mMediaUri);
////                if(mediaFile==null) {
//                //create mediaFile
//                Log.e(TAG, "creating media file to write chosen image into");
//                mediaFile = createFile(MEDIA_TYPE_IMAGE);
//                Log.e(TAG, "media file succesfully written");
////                }
//                FileOutputStream fos = null;
//                try {
//                    fos = new FileOutputStream(mediaFile);
//                    fos.write(fileBytes);
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (MyToolBox.isNetworkAvailable(context)) {
//                        //TODO: do both here - upload file using mediaFile global variable ?
////                        uploadFile(mediaFile);
//                        allPhotos.add(mediaFile);
//                        if (allPhotos.size()>3) {
//                            Log.e(TAG, allPhotos.size() + " photos in list " + allPhotos.get(0).getName() + allPhotos.get(1).getName() + allPhotos.get(2).getName());
//                        }
////                        for (File photo:allPhotos) {
////                            ImageView adPhoto = new ImageView(PostAdActivity.this);
////                            adPhoto.setPadding(4,4,4,4);
////                            Picasso.with(PostAdActivity.this).load(photo).resize(64,64).centerCrop().into(adPhoto);
////                            mPhotoLayout.addView(adPhoto);
////                        }
//
////                        allPhotos.add(mediaFile);
//                        ImageView photo = new ImageView(PostAdActivity.this);
//                        photo.setPadding(4,4,4,4);
//                        Picasso.with(PostAdActivity.this).load(mediaFile).resize(64,64).centerCrop().into(photo);
//                        mPhotoLayout.addView(photo);
//
//                    } else {
//                        MyToolBox.AlertMessage(this, "Oops", "Network Error. Please check your connection");
//                    }
//                }
            }
        }
    }

    private FrameLayout getPhotoIcon(String uri, int id) {
        FrameLayout fl = new FrameLayout(context);
        fl.setId(id);
        // Create Layout Parameters for FrameLayout
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // Apply the Layout Parameters for FrameLayout
        fl.setLayoutParams(lp);
        // Apply 16 pixels padding each site of frame layout
        fl.setPadding(4,4,4,4);
        ImageView photo = new ImageView(context);
        FrameLayout.LayoutParams photoLp = new FrameLayout.LayoutParams(
                200, // Width in pixel
                200);
        photo.setLayoutParams(photoLp);
        photo.setPadding(4,4,4,4);
//        photo.setImageURI(Uri.parse(uri));
        Picasso.with(context).load(new File(uri)).resize(200,200).centerCrop().into(photo);

        ImageView deleteIcon = new ImageView(context);

        FrameLayout.LayoutParams deleteLp = new FrameLayout.LayoutParams(
                64, // Width in pixel
                64);
        deleteLp.gravity = Gravity.TOP|Gravity.START ;
        deleteIcon.setLayoutParams(deleteLp);
//
        deleteIcon.setBackground(getResources().getDrawable(R.drawable.onboarding_pager_round_icon));
        deleteIcon.setImageResource(R.drawable.ic_close_black_24dp);
        deleteIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent)));

//        Picasso.with(context).load(R.drawable.ic_close_black_24dp).resize(30,30).centerCrop().into(photo);

        fl.addView(photo);
        fl.addView(deleteIcon);

        fl.setClickable(true);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String mTitle = "Confirm";
                builder.setTitle(mTitle)
                        .setMessage("Are you sure you want to remove this image?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteImage(v.getId());
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return fl;
    }

    private void deleteImage(int id) {
        horizontalFlowLayout.removeViewAt(id);
//        File deleteFile = new File(filePath.get(id));
//        if(deleteFile.exists()) {
//            deleteFile.delete();
//        }
        filePath.remove(id);
    }

    private File createFile(String albumName, int loopIndex) {
        if(isExternalStorageAvailable()){
            File fileDir = new File( context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/Darling/Albums");//this.getString(R.string.app_name));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
//            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                    getString(R.string.app_name));
//            if(!mediaStorageDir.exists()){
//                if(!mediaStorageDir.mkdirs()){
//                    return null;
//                }
//            }
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
//            String path = fileDir.getPath() + File.separator;
//            File mediaFile = new File(path+"IMG_"+timestamp+".jpg");
//            File imageFile = new File(fileDir + "/IMG_" + timestamp + ".jpg");
//            if (!imageFile.exists()) {
//                try {
//                    imageFile.createNewFile();
////                            mProgressDialog.setMessage("Wrapping up ...");
////                            Toast.makeText(PreviewActivity.this, "just a second...", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

//            Toast.makeText(PostAdActivity.this,"File: "+Uri.fromFile(mediaFile),Toast.LENGTH_LONG).show();
            return new File(fileDir + "/IMG_" + timestamp + UUID.randomUUID().toString() + loopIndex + ".jpg"); //; new File(path+ albumName + "/IMG_"+timestamp+".jpg");
        }
        else{
            return null;
        }
    }

    private File createFile(String albumName) {
        if(isExternalStorageAvailable()){
            File fileDir = new File( context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/Darling/Albums");//this.getString(R.string.app_name));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
//            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                    getString(R.string.app_name));
//            if(!mediaStorageDir.exists()){
//                if(!mediaStorageDir.mkdirs()){
//                    return null;
//                }
//            }
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(now);
//            String path = fileDir.getPath() + File.separator;
//            File mediaFile = new File(path+"IMG_"+timestamp+".jpg");
//            File imageFile = new File(fileDir + "/IMG_" + timestamp + ".jpg");
//            if (!imageFile.exists()) {
//                try {
//                    imageFile.createNewFile();
////                            mProgressDialog.setMessage("Wrapping up ...");
////                            Toast.makeText(PreviewActivity.this, "just a second...", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

//            Toast.makeText(PostAdActivity.this,"File: "+Uri.fromFile(mediaFile),Toast.LENGTH_LONG).show();
            return new File(fileDir + "/IMG_" + timestamp + UUID.randomUUID().toString() + ".jpg"); //; new File(path+ albumName + "/IMG_"+timestamp+".jpg");
        }
        else{
            return null;
        }
    }

    private byte[] getByteArrayFromFile(Context context, Uri uri) {
        byte[] fileBytes = null;
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        Log.e(TAG, uri.getScheme());
        if (uri.getScheme().equals("content")) {
            try {
                inStream = context.getContentResolver().openInputStream(uri);
                outStream = new ByteArrayOutputStream();

                byte[] bytesFromFile = new byte[1024 * 1024]; // buffer size (1 MB)
                assert inStream != null;
                int bytesRead = inStream.read(bytesFromFile);
                while (bytesRead != -1) {
                    outStream.write(bytesFromFile, 0, bytesRead);
                    bytesRead = inStream.read(bytesFromFile);
                }

                fileBytes = outStream.toByteArray();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    assert inStream != null;
                    inStream.close();
                    assert outStream != null;
                    outStream.close();
                } catch (IOException e) { /*( Intentionally blank */ }
            }
        }
        return fileBytes;
    }

}

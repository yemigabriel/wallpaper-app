package com.doxa360.yg.android.darling;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.doxa360.yg.android.darling.helpers.MyToolBox;
import com.doxa360.yg.android.darling.helpers.SharedPref;
import com.doxa360.yg.android.darling.model.Album;
import com.doxa360.yg.android.darling.model.User;
import com.doxa360.yg.android.darling.service.ApiClient;
import com.doxa360.yg.android.darling.service.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private TextInputLayout firstNameLayout, lastNameLayout, emailLayout, passwordLayout ;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText ;
    private Spinner userTypeSpinner;
    private CheckBox termsCheckbox;
    private Button signUpButton, loginButton;
    private Context mContext;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private int selectedItem = 2;

    private SharedPref sharedPref;
    OnSignUpListener mCallback;


    public SignInFragment() {
        // Required empty public constructor
    }

    public interface OnSignUpListener {
        public void accountSignUp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPref = new SharedPref(mContext);

//        firstNameLayout = rootView.findViewById(R.id.firstNameInputLayout);
//        lastNameLayout = rootView.findViewById(R.id.lastNameInputLayout);
        emailLayout = rootView.findViewById(R.id.emailInputLayout);
        passwordLayout = rootView.findViewById(R.id.passwordInputLayout);
//        firstNameEditText = rootView.findViewById(R.id.firstNameEditText);
//        lastNameEditText = rootView.findViewById(R.id.lastNameEditText);
        emailEditText = rootView.findViewById(R.id.emailEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
//        userTypeSpinner = rootView.findViewById(R.id.user_type);
//        termsCheckbox = rootView.findViewById(R.id.terms);
        signUpButton = rootView.findViewById(R.id.signUpButton);
        loginButton = rootView.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.accountSignUp();
            }
        });
//        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedItem = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        return rootView;
    }

    private void validate() {

        if (!MyToolBox.isEmailValid(emailEditText.getText().toString().trim())) {
            emailLayout.setError("Please enter a valid email address");
        }
        else if (!MyToolBox.isMinimumCharacters(passwordEditText.getText().toString().trim(), 6)) {
            passwordLayout.setError("Password should be at least 6 characters");
        }
//        else if (!MyToolBox.isMinimumCharacters(lastNameEditText.getText().toString().trim(), 3)) {
//            lastNameLayout.setError("Last name should be at least 3 characters");
//        }
//        else if (!MyToolBox.isPasswordLengthValid(passwordEditText.getText().toString().trim(), 8)) {
//            passwordLayout.setError("Type a password with at least 8 characters");
//        }
//        else if (!MyToolBox.isPasswordValid(passwordEditText.getText().toString().trim(), false)) {
//            passwordLayout.setError("Your password must have an uppercase character and a number");
//        } else if (selectedItem == 2) {
//            Toast.makeText(mContext, "Please choose user type", Toast.LENGTH_LONG).show();
//        }
//        else if (!termsCheckbox.isChecked()) {
//            termsCheckbox.setError("Please accept terms and conditions before registering");
//        }
        else {
//            String name = mName.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
//            String firstName = firstNameEditText.getText().toString().trim();
//            String lastName = lastNameEditText.getText().toString().trim();
//            int terms = 1;
//            String[] userTypeArray = getResources().getStringArray(R.array.user_type);
//            String userType = userTypeArray[selectedItem];
            if (MyToolBox.isNetworkAvailable(mContext)) {
                signin(email, password); //firstName, lastName, userType, terms);
            } else {
                MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection.");
            }
        }

    }

    private void signin(final String email, final String password) { //, String firstName, String lastName, String userType, int terms) {
        this.progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Processing ...");
        progressDialog.show();
//        Call<User> call = mDutchApiInterface.signUpUser(user);
//        call.enqueue(new Callback<User>() {
        User user = new User(email, password);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WallpaperApp.SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface1 = retrofit.create(ApiInterface.class);
        Call<User> call = apiInterface1.signInUser(email, password);

//        Call<User> call = this.apiInterface.signUpUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, final Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Sign in successful", Toast.LENGTH_SHORT).show();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            if (response.body() != null) {
                                final int userId = response.body().getId();
                                final String userEmail = response.body().getEmail();
                                final String userName = response.body().getName();
                                final String userPassword = password;

                                sharedPref.setUserId(userId);
                                sharedPref.setUserName(userName);
                                sharedPref.setUserEmail(userEmail);
                                sharedPref.setUserPassword(userPassword);
                            }

                            syncOrRetrieve();
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    MyToolBox.AlertMessage(mContext, "Oops", "Wrong email and password combination");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                MyToolBox.AlertMessage(mContext, "Oops", "Network error. Please check your connection and try again.");

            }
        });



    }

    private void syncOrRetrieve() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Sign in successful")
                .setMessage("Would you like to sync albums on your device or retrieve synced albums?")
                .setPositiveButton("SYNC ALBUMS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // check for albums
                        syncAlbums();
                    }
                })
                .setNegativeButton("RETRIEVE ALBUMS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        retrieveAlbums();
                    }
                })
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
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

        try {
            mCallback = (OnSignUpListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSignUpListener");
        }
    }
}

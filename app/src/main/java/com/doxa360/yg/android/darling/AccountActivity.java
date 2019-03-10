package com.doxa360.yg.android.darling;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity
        implements SignInFragment.OnSignUpListener, SignUpFragment.OnSignInListener {

    final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        SignInFragment signInFragment = new SignInFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, signInFragment, TAG).commit();

    }

    @Override
    public void accountSignUp() {
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, signUpFragment, TAG).commit();
    }

    @Override
    public void accountSignIn() {
        SignInFragment signInFragment = new SignInFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, signInFragment, TAG).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

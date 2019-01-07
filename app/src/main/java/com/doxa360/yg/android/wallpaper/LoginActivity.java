package com.doxa360.yg.android.wallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.doxa360.yg.android.wallpaper.helpers.MyToolBox;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mNameLayout, mEmailLayout, mPasswordLayout;
    private EditText mName, mEmail, mPassword;
    private Button mButton, mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mName = findViewById(R.id.nameEditText);
        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.passwordEditText);

//        mNameLayout = findViewById(R.id.nameInputLayout);
        mEmailLayout = findViewById(R.id.emailInputLayout);
        mPasswordLayout = findViewById(R.id.passwordInputLayout);

        mButton = findViewById(R.id.loginButton);
        mSignUpButton = findViewById(R.id.signUpButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    private void validate() {

       if (!MyToolBox.isEmailValid(mEmail.getText().toString())) {
            mEmailLayout.setError("Please use a valid email address");
        }
        else if (!MyToolBox.isMinimumCharacters(mPassword.getText().toString(), 5)) {
            mPasswordLayout.setError("Type a password with at least 6 characters");
        }
        else {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            login();
        }

    }

    private void login() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void signup() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }


}

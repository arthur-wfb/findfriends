package com.ururu2909.findfriends.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ururu2909.findfriends.util.Constants;
import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.home.HomeActivity;
import com.ururu2909.findfriends.registration.RegistrationActivity;
import com.ururu2909.findfriends.util.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity implements LoginView{

    EditText login;
    EditText password;
    TextView authErrorAttention;
    TextView signUpButton;
    ProgressBar progressBar;
    Button signInButton;
    LoginPresenter presenter;
    SharedPreferences mSettings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        authErrorAttention = findViewById(R.id.authErrorAttention);
        progressBar = findViewById(R.id.progressBar);
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);
        signUpButton.setOnClickListener(r -> navigateToSignUpActivity());
        signInButton.setOnClickListener(r -> login());

        mSettings = SharedPreferencesManager.getInstance(this);
        presenter = new LoginPresenter(this, new LoginInteractor(), mSettings);
    }

    void login(){
        presenter.login(login.getText().toString(), password.getText().toString());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setError() {
        Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAuthError() {
        authErrorAttention.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoginError() {
        login.setError(getString(R.string.incorrect_login));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.incorrect_password));
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSignUpActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}

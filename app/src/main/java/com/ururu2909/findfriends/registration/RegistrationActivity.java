package com.ururu2909.findfriends.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.login.LoginActivity;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView {
    TextView login;
    TextView password;
    TextView confirmPassword;
    TextView signInButton;
    Button signUpButton;
    ProgressBar progressBar;
    RegistrationPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        progressBar = findViewById(R.id.progressBar);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);
        signInButton.setOnClickListener(R -> navigateToLogIn());
        signUpButton.setOnClickListener(r -> register());

        presenter = new RegistrationPresenter(this, new RegistrationInteractor());
    }

    private void register(){
        presenter.register(login.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
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
    public void setLoginError() {
        login.setError(getString(R.string.incorrect_login));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.incorrect_password));
    }

    @Override
    public void setLoginExistsError() {
        login.setError(getString(R.string.login_exists_attention));
    }

    @Override
    public void setConfirmPasswordError() {
        confirmPassword.setError(getString(R.string.different_passwords_attention));
    }

    @Override
    public void navigateToLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

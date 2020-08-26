package com.ururu2909.findfriends.login;

import android.content.SharedPreferences;

import com.ururu2909.findfriends.util.Validator;

public class LoginPresenter implements LoginInteractor.OnLoginFinishedListener {
    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private SharedPreferences mSettings;

    LoginPresenter(LoginView loginView, LoginInteractor loginInteractor, SharedPreferences mSettings) {
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
        this.mSettings = mSettings;
    }

    void login(String login, String password){
        if (loginView != null){
            if (!Validator.validateLogin(login)){
                loginView.setLoginError();
                return;
            }
            loginView.showProgress();
        }
        loginInteractor.login(login, password, this);
    }

    @Override
    public void onAuthError() {
        loginView.hideProgress();
        loginView.setAuthError();
    }

    @Override
    public void onError() {
        loginView.hideProgress();
        loginView.setError();
    }

    @Override
    public void onSuccess(String login, int avatar, String token) {
        loginView.hideProgress();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("login", login);
        editor.putInt("avatar", avatar);
        editor.putString("token", token);
        editor.apply();
        loginView.navigateToHome();
    }
}

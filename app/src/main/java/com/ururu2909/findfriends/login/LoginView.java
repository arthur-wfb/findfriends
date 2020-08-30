package com.ururu2909.findfriends.login;

public interface LoginView {
    void showProgress();

    void hideProgress();

    void setError();

    void setAuthError();

    void setLoginError();

    void navigateToHome();

    void navigateToSignUpActivity();
}

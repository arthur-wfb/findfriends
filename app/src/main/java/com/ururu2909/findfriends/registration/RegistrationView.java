package com.ururu2909.findfriends.registration;

public interface RegistrationView {
    void showProgress();

    void hideProgress();

    void setError();

    void setLoginError();

    void setPasswordError();

    void setLoginExistsError();

    void setConfirmPasswordError();

    void navigateToLogIn();
}

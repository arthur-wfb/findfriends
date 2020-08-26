package com.ururu2909.findfriends.registration;

import com.ururu2909.findfriends.util.Validator;

public class RegistrationPresenter implements RegistrationInteractor.OnRegistrationFinishedListener {
    private RegistrationView registrationView;
    private RegistrationInteractor registrationInteractor;

    RegistrationPresenter(RegistrationView registrationView, RegistrationInteractor registrationInteractor) {
        this.registrationView = registrationView;
        this.registrationInteractor = registrationInteractor;
    }

    void register(String login, String password, String confirmPassword){
        if (registrationView != null){
            if (!Validator.validateLogin(login)){
                registrationView.setLoginError();
                return;
            }
            if (!Validator.validatePassword(password)){
                registrationView.setPasswordError();
                return;
            }
            if (!password.equals(confirmPassword)){
                registrationView.setConfirmPasswordError();
                return;
            }
            registrationView.showProgress();
        }
        registrationInteractor.register(login, password, this);
    }

    @Override
    public void onLoginExistsError() {
        registrationView.hideProgress();
        registrationView.setLoginExistsError();
    }

    @Override
    public void onError() {
        registrationView.hideProgress();
        registrationView.setError();
    }

    @Override
    public void onSuccess() {
        registrationView.hideProgress();
        registrationView.navigateToLogIn();
    }
}

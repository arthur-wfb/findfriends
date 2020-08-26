package com.ururu2909.findfriends.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean validateLogin(String login){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]{1,33}$");
        Matcher matcher = pattern.matcher(login);
        return matcher.matches();
    }

    public static boolean validatePassword(String password){
        return password.length() >= 8 && password.length() <= 32;
    }
}

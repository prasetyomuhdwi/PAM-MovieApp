package net.prasetyomuhdwi.movieapps;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class Validation {
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPassword(CharSequence target) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])"
                + "(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        return !TextUtils.isEmpty(target) && p.matcher(target).matches();
    }

    public final static boolean isValidUsername(CharSequence target) {
        String regex = "^[a-zA-Z0-9_.]+$";
        Pattern p = Pattern.compile(regex);
        return !TextUtils.isEmpty(target) && p.matcher(target).matches();
    }

    public final static boolean isValidFullname(CharSequence target) {
        String regex = "^[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        return !TextUtils.isEmpty(target) && p.matcher(target).matches();
    }
}

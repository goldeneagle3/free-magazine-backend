package com.serbest.magazine.backend.common.validation;


import com.serbest.magazine.backend.exception.CustomApplicationException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class StringValidationCommon {

    private final static String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean patternMatchesEmail(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static void common_validateEmail(String email) {
        boolean isMatch = StringValidationCommon.patternMatchesEmail(email, regexPattern);
        if (!isMatch) {
            throw new IllegalArgumentException("Provide a valid email , please.");
        }
    }

    public static void common_validateStringLength(Integer min, Integer max, String field) {
        if (field.length() < min || field.length() > max){
            throw new IllegalArgumentException("Provide a valid input in the specified range.");
        }
    }
}

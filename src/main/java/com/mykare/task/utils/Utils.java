package com.mykare.task.utils;

import java.util.regex.Pattern;

public class Utils {
    
    public static Boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}

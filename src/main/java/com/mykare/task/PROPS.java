package com.mykare.task;

public class PROPS {
    public static final class Message {
        public static final String loginSuccess = "Login success";
        public static final String loginFailed = "Invalid Credentials";
        public static final String unauthorized = "Unauthorized request";
        public static final String logoutSuccess = "Logout success";
        public static final String emailAlreadyExists = "Email already exists";
        public static final String validEmail = "Not a valid email";
        public static final String emailNotFound = "Email not found";
        public static final String saveSuccess = "Record saved successfully";
        public static final String deleteSuccess = "Record deleted successfully";
        public static final String recordExists = "Record already exists";
    }

    public static final class ExternalApis {
        public static final String ipUrl = "https://api64.ipify.org?format=json";
        public static final String locationUrl = "http://ip-api.com/json/";
    }

    public static final class Roles {
        public static final String admin = "ADMIN";
        public static final String user = "USER";
    }

    public static final class TestCaseConstant {
        public static final String name = "John Doe";
        public static final String email = "test@gmail.com";
        public static final String wrongMail = "unknown@gmail.com";
        public static final String invalidMail = "invalid-email";
        public static final String password = "password";
        public static final String wrongPassword = "wrongPassword";
        public static final String hashedPassword = "hashedPassword";
        public static final String location = "USA";
        public static final String ipAddress = "192.168.1.1";
        public static final String token = "mocked-jwt-token";
    }

}

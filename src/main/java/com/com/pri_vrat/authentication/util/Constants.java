package com.com.pri_vrat.authentication.util;

public class Constants {
    public static class STATUS {
        public static String PENDING = "Pending";
        public static String DELETED = "Deleted";
        public static String APPROVED = "Approved";
        public static String COMPLETED = "Completed";
        public static String REJECTED = "Rejected";
    }
    /*  requestBody.add("client_secret", clientSecret);
            requestBody.add("realm", realm);
            requestBody.add("refresh_token", refreshToken);
            requestBody.add("client_id", clientId);*/

    public static class KEYCLOAK {
        public static final String CLIENT_SECRET = "client_secret";
        public static final String REALM = "realm";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String CLIENT_ID = "client_id";
        public static final String GRANT_TYPE = "grant_type";
        public static final String USER_NAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOG_APP_NAME = "LOG_APP_NAME";
        public static final String LOG_TENANT_ID = "LOG-TENANT-ID";
        public static final String LOG_USER_NAME = "LOG-USER-NAME";
        public static final String LOG_USER_TYPE = "LOG-USER-TYPE";
        public static final String ACCOUNT_DISABLED = "Account disabled";
        public static final String CONTENT_TYPE = "Content-Type";
    }

    public static class RESPONSE_CODE {
        public static String SUCCESS = "SUCCESS";
        public static String FAILED = "FAILED";
        public static String ERROR = "ERROR";
        public static String EXCEPTION = "EXCEPTION";
    }
}

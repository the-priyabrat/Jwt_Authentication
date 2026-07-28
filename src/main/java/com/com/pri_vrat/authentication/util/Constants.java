package com.com.pri_vrat.authentication.util;

public class Constants {
    public static class STATUS {
        public static final String PENDING = "Pending";
        public static final String DELETED = "Deleted";
        public static final String APPROVED = "Approved";
        public static final String COMPLETED = "Completed";
        public static final String REJECTED = "Rejected";
    }

    public static class API_KEY {
        public static final String ACTIVE = "Active";
        public static final String INACTIVE = "Inactive";
    }

    public static class EXCEPTION {
        public static final String PREFIX = "Failed for message {} at method {}";
    }

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
        public static final String DEFAULT_USER = "GUEST";
    }

    public static class RESPONSE_CODE {
        public static String SUCCESS = "SUCCESS";
        public static String FAILED = "FAILED";
        public static String ERROR = "ERROR";
        public static String EXCEPTION = "EXCEPTION";
    }

    public static class SCHEMA {
        public static final String DEFAULT_SCHEMA = "public";
    }
}

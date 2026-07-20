package com.com.pri_vrat.authentication.util;

public class Constants {
    public static class STATUS {
        public static String PENDING = "Pending";
        public static String DELETED = "Deleted";
        public static String APPROVED = "Approved";
        public static String COMPLETED = "Completed";
        public static String REJECTED = "Rejected";
    }
    public static class KEYCLOAK {
        public static String GRANT_TYPE = "password";
        public static String LOG_APP_NAME = "LOG_APP_NAME";
        public static String LOG_TENANT_ID = "LOG-TENANT-ID";
        public static String LOG_USER_NAME = "LOG-USER-NAME";
        public static String LOG_USER_TYPE = "LOG-USER-TYPE";
        public static String ACCOUNT_DISABLED= "Account disabled";
    }
    public static class RESPONSE_CODE {
        public static String SUCCESS = "SUCCESS";
        public static String FAILED = "FAILED";
        public static String ERROR = "ERROR";
        public static String EXCEPTION = "EXCEPTION";
    }
}

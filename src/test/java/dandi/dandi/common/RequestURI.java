package dandi.dandi.common;

public class RequestURI {

    public static final String LOGIN_REQUEST_URI = "/login/oauth/apple";
    public static final String TOKEN_REFRESH_REQUEST_URI = "/refresh";
    public static final String LOGOUT_REQUEST_URI = "/logout";

    public static final String MEMBER_INFO_URI = "/members";
    public static final String MEMBER_NICKNAME_URI = "/members/nickname";
    public static final String MEMBER_NICKNAME_DUPLICATION_CHECK_URI = "/members/nickname/duplication";
    public static final String MEMBER_NICKNAME_LOCATION = "/members/location";
    public static final String MEMBER_PROFILE_IMAGE_URI = "/members/profile-image";

    public static final String PUSH_NOTIFICATION_REQUEST_URI = "/push-notification";
    public static final String PUSH_NOTIFICATION_TIME_REQUEST_URI = "/push-notification/time";
    public static final String PUSH_NOTIFICATION_ALLOWANCE_REQUEST_URI = "/push-notification/allowance";
}

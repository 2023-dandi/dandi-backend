package dandi.dandi.common;

public class RequestURI {

    public static final String LOGIN_REQUEST_URI = "/auth/login/oauth/apple";
    public static final String TOKEN_REFRESH_REQUEST_URI = "/auth/refresh";
    public static final String LOGOUT_REQUEST_URI = "/auth/logout";

    public static final String MEMBER_INFO_URI = "/members";
    public static final String MEMBER_NICKNAME_URI = "/members/nickname";
    public static final String MEMBER_NICKNAME_DUPLICATION_CHECK_URI = "/members/nickname/duplication";
    public static final String MEMBER_NICKNAME_LOCATION = "/members/location";
    public static final String MEMBER_PROFILE_IMAGE_URI = "/members/profile-image";

    public static final String PUSH_NOTIFICATION_REQUEST_URI = "/push-notification";
    public static final String PUSH_NOTIFICATION_TIME_REQUEST_URI = "/push-notification/time";
    public static final String PUSH_NOTIFICATION_ALLOWANCE_REQUEST_URI = "/push-notification/allowance";

    public static final String POST_REGISTER_REQUEST_URI = "/posts";
    public static final String POST_IMAGE_REGISTER_REQUEST_URI = "/posts/images";
    public static final String POST_DETAILS_REQUEST_URI = "/posts";
    public static final String MY_POST_REQUEST_URI = "/posts/my";
    public static final String FEED_REQUEST_URI = "/posts/feed/weather";
}

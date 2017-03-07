package co.crossroadsapp.overwatch.utils;

/**
 * Created by sharmha on 3/4/16.
 */
public class Constants {

    public static final String STATUS_CAN_JOIN = "can_join";
    public static final String STATUS_NEW = "new";
    public static final String STATUS_FULL = "full";
    public static final String STATUS_OPEN = "open";

    public static final String ACTIVITY_RAID = "RAID";
    public static final String ACTIVITY_ARENA = "ARENA";
    public static final String ACTIVITY_CRUCIBLE = "CRUCIBLE";
    public static final String ACTIVITY_FEATURED = "FEATURED";
    public static final String ACTIVITY_STRIKES = "STRIKE";
    public static final String ACTIVITY_PATROL = "PATROL";

    public static final String LAUNCH_STATUS_UPCOMING = "upcoming";
    public static final String LAUNCH_STATUS_NOW = "now";

    public final static String GCM_SENDER_ID = "375926567407";

    public final static int LOGIN = 1;
    public final static int REGISTER = 2;

    public static final String TRAVELER_NOTIFICATION_INTENT = "co.crossroadsapp.overwatch.travelerfordestiny";

    // terms of services, legal and privacy policy url links
    public static final String LEGAL = "https://www.crossroadsapp.co/legal";
    public static final String LICENCE = "https://www.crossroadsapp.co/license"; //"http://68.140.240.194:3000/license"
    public static final String TERMS_OF_SERVICE = "http://w3.crossroadsapp.co/owterms";
    public static final String PRIVACY_POLICY = "http://w3.crossroadsapp.co/owprivacy";
    public static final String NONE = "(none)";
    public static final String PLAYSTATION = "PlayStation";
    public static final String XBOX = "Xbox";
    public static final String COOKIE_VALID_KEY = "isCookieValid";
    public static final int SIGNUP_ERROR = 1;
    public static final int LOGIN_ERROR = 2;
    public static final int INAPP = 3;
    public static final int ADDCONSOLE_ERROR = 3;

    public static String NOTIFICATION_INTENT_CHANNEL = "com.example.sharmha.notificationintent";
    public static int INTENT_ID = 9999;

    //firebase url
    public static String FIREBASE_DEV_URL = "https://overwatchapp-dev.firebaseio.com/";
    public static String FIREBASE_STAGING_URL = "https://overwatchapp-prod.firebaseio.com/";
    public static String FIREBASE_PROD_URL = "https://overwatchapp-live.firebaseio.com/";

    //network base url
    public static String NETWORK_DEV_BASE_URL = "https://overwatch-test-server.herokuapp.com/";
    public static String NETWORK_PROD_BASE_URL = "https://owlive.crossroadsapp.co/";
    //public static String NETWORK_PROD_BASE_URL = "https://overwatch-live.herokuapp.com/";
    public static String NETWORK_STAGING_URL = "https://overwatch-staging.herokuapp.com/";

    //app download links
    public static String DOWNLOAD_DEV_BUILD = "https://goo.gl/6vQpFn";
    public static String DOWNLOAD_PROD_BUILD = "https://goo.gl/GSLxIW";

    public static final int PROD = 0;
    public static final int STAGING = 1;
    public static final int DEV = 2;
    public static final int ENVIRONMENT = PROD;
    //public static final String PROD_DOMAIN = "overwatch-live.herokuapp.com";
    public static final String PROD_DOMAIN = "owlive.crossroadsapp.co";
    public static final String STAGING_DOMAIN = "overwatch-staging.herokuapp.com";
    public static final String DEV_DOMAIN = "overwatch-test-server.herokuapp.com";
    public static final String DOMAIN = (ENVIRONMENT == PROD ? PROD_DOMAIN : (ENVIRONMENT == STAGING ? STAGING_DOMAIN : DEV_DOMAIN));
    public static final String LOGIN_SIGNUP_SCREEN_KEY = "login_signup_screen_key";
    public static final int LOGIN_SCREEN_VALUE = 0;
    public static final int SIGNUP_SCREEN_VALUE = 1;
    public static final int ENTER_GAMETAG_NAME_VALUE = 2;
    public static final int ADD_LINKED_ACCOUNT_VALUE = 3;
    public static final int LOGIN_SCREEN_BACK_BUTTON = 9;

    //PSN verified keys
    public static String PSN_VERIFIED = "VERIFIED";
    public static String PSN_NOT_INITIATED = "NOT_INITIATED";
    public static String PSN_INITIATED = "INITIATED";
    public static String PSN_FAILED_INITIATED = "FAILED_INITIATION";
    public static String PSN_DELETED = "DELETED";
    public static String PSN_INVITED = "INVITED";

    public static String CONSOLEXBOXONE = "XBOXONE";
    public static String CONSOLEXBOX360 = "XBOX360";
    public static String CONSOLEPS4 = "PS4";
    public static String CONSOLEPS3 = "PS3";

    public static String CONSOLEXBOXONESTRG = "Xbox One";
    public static String CONSOLEXBOX360STRG = "Xbox 360";
    public static String CONSOLEPS4STRG = "PlayStation 4";
    public static String CONSOLEPS3STRG = "PlayStation 3";


    //clan id keys
    public static String CLAN_NOT_SET = "clan_id_not";
    public static String FREELANCER_GROUP = "clan_id_not_set";

    public static int USER_CHANNEL = 2;
    public static int EVENT_CHANNEL = 1;
    public static int EVENT_COMMENT_CHANNEL = 3;

    public static int PUBLIC_EVENT_FEED = 1;
    public static int EVENT_FEED = 2;

    //deeplink error types
    public static int EVENT_GRP_MISSING = 1;
    public static int EVENT_MISSING = 2;
    public static int EVENT_FULL = 3;
    public static int EVENT_CONSOLE_MISSING = 4;

    public static final int TIME_SECOND = 1000;
    public static final int TIME_MINUTE = 60 * 1000;
    public static final int TIME_HOUR = 60 * 60 * 1000;
    public static final int TIME_DAY = 24 * 60 * 60 * 1000;
    public static final int TIME_TWO_DAY = 2 * 24 * 60 * 60 * 1000;
    public static final int WEEK = 7 * 24 * 60 * 60 * 1000;

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String UNKNOWN_SOURCE = "unknown";
    public static final String ORGANIC_SOURCE = "organic";
    public static final String FACEBOOK_SOURCE = "facebook";
    public static final String BRANCH_SOURCE = "branch";
    public static final String APP_RESUME = "appResume";
    public static final String APP_INSTALL = "appInstall";
    public static final String APP_SIGNUP = "signupInit";
    public static final String APP_PUSHNOTIFICATION = "pushNotification";
    public static final String APP_INIT = "appInit";
    public static final String APP_ADCARD = "adCardInit";
    public static final String APP_EVENTSHARING = "eventSharing";
    public static final String APP_CURRENTTAB = "currentTabInit";
    public static final String APP_UPCOMINGTAB = "upcomingTabInit";
    public static String APP_SHOWPASSWORD = "showPassword";

    //urls
    public static String DEEP_LINK_IMAGE = "http://w3.crossroadsapp.co/overwatch/share/branch/v1/";
    public static String BUNGIE_ERROR = "LoginError";
    public static String BUNGIE_CONNECT_ERROR = "ConnectError";
    public static String BUNGIE_LEGACY_ERROR = "ConsoleError";

    public static String LEGACY_ERROR_TITLE = "Legacy Consoles";
    public static String LEGACY_ERROR_MSG = "In line with Rise of Iron, we now only support next-gen consoles. When you’ve upgraded your console, please come\n" +
            "back and join us!";

    //error types
    public static int GENERAL_ERROR = 1;
    public static int REPORT_COMMENT = 2;
    public static int REPORT_COMMENT_NEXT = 3;
    public static int GENERAL_LEAVE = 5;
    public static int GENERAL_KICK = 4;

    public static final String CONFIG_TOKEN_PROD = "003c2fff-9d24-4dbe-be76-6ab21574e2d9";//dev - "123"
    public static final String CONFIG_TOKEN_DEV = "123";
    public static final String CONFIG_TOKEN_KEY = "config_token";
    public static final String CONFIG_TOKEN = (ENVIRONMENT == PROD ? CONFIG_TOKEN_PROD : CONFIG_TOKEN_DEV);

    //getCurrentUser bungie
    public static String BUGIE_CURRENT_USER = "https://www.bungie.net/Platform/User/GetCurrentBungieAccount";

    public static String BUNGIE_PSN_LOGIN = "https://www.bungie.net/en/User/SignIn/Psnid";
    public static String BUNGIE_XBOX_LOGIN = "https://www.bungie.net/en/User/SignIn/Xuid";

    //api url
    public static String REPORT_COMMENT_URL = "api/v1/a/event/reportComment";
    public static String BUNGIE_RESPONSE_URL = "api/v1/gatewayResponse";
    public static String KICK_PLAYER_URL = "api/v1/a/event/kick";
    public static String CANCEL_PLAYER_URL = "api/v1/a/event/invite/cancel";
    public static String ACCEPT_EVENT_URL = "api/v1/a/event/invite/accept";


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "cookie";
    public static final String COOKIE_CAPITAL = "Cookie";
    public static final String COOKIES_HEADER = "Set-Cookie";
}

package co.crossroadsapp.overwatch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import co.crossroadsapp.overwatch.AddFinalActivity;
import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.data.ConsoleData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.NetworkEngine;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by sharmha on 2/23/16.
 */
public class Util {
    //To switch between production and development server links
    //where 1 points to development, 2 points to production and 3 points to Dev staging

    private static final String TAG = Util.class.getName();
    public static final String trimbleDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static SimpleDateFormat formatter = new SimpleDateFormat(trimbleDateFormat);

    public static String getNetworkUrl() {
        if (Constants.ENVIRONMENT == Constants.PROD) {
            return Constants.NETWORK_PROD_BASE_URL;
        } else if (Constants.ENVIRONMENT == Constants.STAGING) {
            return Constants.NETWORK_STAGING_URL;
        }
        return Constants.NETWORK_DEV_BASE_URL;
    }

    public static String getMixpanelID(Activity activity) {
        return Util.getDefaults("mixpanelToken", activity);
    }

    public static int softNavigationPresent(Activity c) {
        int hasSoftwareKeys = 0;
        if (c != null) {
            Display d = c.getWindowManager().getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = realHeight - displayHeight;
        }
        return hasSoftwareKeys;
    }

    public static String getFirebaseUrl(String clanId, String eventId, int channel) {
        String url;
        if (Constants.ENVIRONMENT == Constants.PROD) {
            url = Constants.FIREBASE_PROD_URL;
        } else if (Constants.ENVIRONMENT == Constants.STAGING) {
            url = Constants.FIREBASE_STAGING_URL;
        } else {
            url = Constants.FIREBASE_DEV_URL;
        }
        if (channel == Constants.EVENT_CHANNEL || channel == Constants.EVENT_COMMENT_CHANNEL) {
            if (eventId != null && (!eventId.equalsIgnoreCase("null"))) {
                if (channel == Constants.EVENT_COMMENT_CHANNEL) {
                    url = url + "comments/" + eventId;
                } else {
                    if (clanId != null && (!clanId.equalsIgnoreCase("null"))) {
                        url = url + "events/" + clanId + "/" + eventId;
                    }
                }
            } else {
                url = url + "events/" + clanId;
            }
        } else if (channel == Constants.USER_CHANNEL) {
            if (clanId != null && (!clanId.equalsIgnoreCase("null"))) {
                url = url + "users/" + clanId;
            }
        }
        return url;
    }

    public static String getAppDownloadLink() {
        if (Constants.ENVIRONMENT == Constants.STAGING) {
            return Constants.DOWNLOAD_PROD_BUILD;
        }
        return Constants.DOWNLOAD_DEV_BUILD;
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void getGCMToken(final Context c, final ControlManager mngr) {
        //final InstanceID instanceID = InstanceID.getInstance(c);
        //if (instanceID!=null) {
        new Thread() {
            public void run() {
                try {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    //String token = instanceID.getToken(Constants.GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    final String token1 = token;
                    ((Activity) c).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mngr.postGCMToken(token1, c);
                        }
                    });
                } catch (Exception e) {
                    //handle failure here
                    TravellerLog.w(this, "getGCMToken");
                }
            }
        }.start();
        //}
    }

    public static boolean checkUserObject(UserData ud) {
        if (ud != null) {
            if (ud.getUserId() == null || ud.getUser() == null || ud.getConsoleType() == null || ud.getClanId() == null || ud.getPsnVerify() == null || ud.getImageUrl() == null || ud.getMembershipId() == null || ud.getPsnId() == null) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static int dpToPx(int dp, Context c) {
        if (c != null) {
            DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }
        return 0;
    }

    public static String getApplicationVersionCode(Context c) {
        PackageInfo pInfo = null;
        String version = "APP_VERSION_NOT_DEFINED";
        //Context ctx = AppEngine.getInstance().getTrimbleContext().getContext();
//            if (ctx != null) {
        try {
            pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//            }
        if (pInfo != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(pInfo.versionName);
            version = sb.toString();
            version = version + "(" + pInfo.versionCode + ")";
        }
        return version;
    }

    private static String getDeviceID() {
        // get the AndroidId
//        String deviceID = getDeviceSHA256Hash(Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID));
//        return deviceID;

        return "ANDROID_ID_NOT_DEFINED";
    }

    public static void clearDefaults(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("user");
        editor.remove("password");
        editor.remove("csrf");
        editor.remove("cookie");
        editor.remove("opendrawer");
        CookieManager.getInstance().removeAllCookie();
//        CookieManager.getInstance().setCookie("https://www.bungie.net/", " ");
//        CookieManager.getInstance().setCookie("https://www.bungie.net/", " ");
        //editor.clear();
        editor.commit();
    }

    public static void clearLogoutDefaults(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("loggedin");
        CookieManager.getInstance().removeAllCookie();
//        CookieManager.getInstance().setCookie("https://www.bungie.net/", " ");
//        CookieManager.getInstance().setCookie("https://www.bungie.net/", " ");
        //editor.clear();
        editor.commit();
    }

    public static String changeTimeFormat(String args, String argsDate) throws Exception {
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat parseFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
        Date date = parseFormat.parse(argsDate + " " + args);
        return displayFormat.format(date);
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return (aTime.length() > 0 ? aTime : null);
    }

    public static void createNoNetworkDialogue(Context c) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                c);

        // set title
        alertDialogBuilder.setTitle("          Connection Failure");

        // set dialog message
        alertDialogBuilder
                .setMessage("             No network available")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static String createServerFormatedDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cal.getTime());
    }

    public static String convertUTCtoReadable(String utcDate) {
        try {
            DateTime date = new DateTime(utcDate);

            if (date.toLocalDate().isEqual(new LocalDate())) {
                return date.toLocalDateTime().toString("'Today at' h:mm a");
            } else if (date.toLocalDate().isEqual(new LocalDate().plusDays(1))) {
                return date.toLocalDateTime().toString("'Tomorrow at' h:mm a");
            } else if (date.toLocalDate().isBefore(new LocalDate().plusWeeks(1))) {
                return date.toLocalDateTime().toString("EEEE 'at' h:mm a");
            } else {
                return date.toLocalDateTime().toString("MMM d 'at' h:mm a");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String updateLastReceivedDate(final String lastDate, Resources res) {
        String mTemp = null;
        Calendar cal = getCalendar(lastDate);

        DateTime date = new DateTime(lastDate);
        long millis = date.getMillis();
        if (cal == null) {
            return null;
        }
        long timeDif = System.currentTimeMillis() - millis;

        if (timeDif < Constants.TIME_MINUTE) {
            int seconds = (int) timeDif / Constants.TIME_SECOND;
            mTemp = "now";
        } else if (timeDif >= Constants.TIME_MINUTE && timeDif < Constants.TIME_HOUR) {
            int minutes = (int) timeDif / Constants.TIME_MINUTE;
            mTemp = res.getQuantityString(R.plurals.minutes_time, minutes, minutes);
        } else if (timeDif >= Constants.TIME_HOUR && timeDif < Constants.TIME_DAY) {
            int hours = (int) timeDif / Constants.TIME_HOUR;
            mTemp = res.getQuantityString(R.plurals.hours_time, hours, hours);
        } else if (timeDif >= Constants.TIME_DAY) {
            //int days = (int) timeDif / Constants.TIME_DAY;
            int days = Days.daysBetween(date.toLocalDate(), new LocalDate()).getDays();
            mTemp = res.getQuantityString(R.plurals.days_time, days, days);
        }

        return mTemp;
    }

    public static String getErrorMessage(JSONObject jsonResponse) {
        if (jsonResponse != null) {
            try {

                if (jsonResponse.has("error")) {
                    //JSONObject jsonData = jsonResponse.optJSONObject("message");
                    String n = jsonResponse.getString("error");
                    return n;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Calendar getCalendar(String dateString) {
        try {
            Calendar cal1 = Calendar.getInstance();
            TimeZone tz = cal1.getTimeZone();
            long msFromEpochGmt = cal1.getTimeInMillis();
            long offsetFromUTC = tz.getOffset(msFromEpochGmt);
            Date date = formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setTimeInMillis(cal.getTimeInMillis() + offsetFromUTC);
            return cal;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateCurrEventOnTime(final String date) {
        String mTemp = null;
        Calendar cal = getCalendar(date);
        if (cal == null) {
            return false;
        }
        long timeDif = cal.getTimeInMillis() - System.currentTimeMillis();
        if (timeDif < 10 * 60 * 1000) {
            return true;
        }

        return false;
    }

    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String formattedTime = df.format(cal.getTime());
        return formattedTime;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static Date getCurrentCalendar() {
        Calendar c = Calendar.getInstance();
        Date currentDate = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        return currentDate;
    }

    public static void checkTimePicker(TextView date_display, TextView time_display, AddFinalActivity c) {
        if (date_display != null && time_display != null) {
            if (isPresentDay(c, date_display)) {
                if (!time_display.getText().toString().equalsIgnoreCase(c.getResources().getString(R.string.time_default))) {
                    try {
                        String t = time_display.getText().toString();
                        String t1 = Util.getCurrentTime();
                        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                        Date d1 = df.parse(t);
                        Date d2 = df.parse(t1);
                        if (d1.getTime() - d2.getTime() < 0) {
                            time_display.setText(getCurrentTime(d2.getHours(), d2.getMinutes(), c));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static String getCurrentTime(int h, int m, AddFinalActivity c) {
        Toast.makeText(c.getApplicationContext(), "Cannot set previous time for today.", Toast.LENGTH_SHORT).show();
        String t = updateTime(h, m);
        return t;
    }

    public static boolean isPresentDay(AddFinalActivity c, TextView date_display) {
        if (date_display != null) {
            if (date_display.getText().toString().equalsIgnoreCase(c.getResources().getString(R.string.date_default))) {
                return true;
            } else {
                try {
                    String s = date_display.getText().toString();
                    String z = getCurrentDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Date d1 = sdf.parse(s);
                    Date d2 = sdf.parse(z);
                    if (d1.getTime() - d2.getTime() == 0) {
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void picassoLoadIcon(Context c, ImageView eventIcon, String url, int height, int width, int avatar) {
        if (c != null && eventIcon != null) {
            if (url != null) {
                Picasso.with(c).load(url)
                        .resizeDimen(width, height)
                        .placeholder(avatar)
                        .into(eventIcon, new Callback() {
                            @Override
                            public void onSuccess() {
                                TravellerLog.w(TAG, "success");
                            }

                            @Override
                            public void onError() {
                                TravellerLog.w(TAG, "error");
                            }
                        });
            } else {
                eventIcon.setImageDrawable(c.getResources().getDrawable(avatar));
            }
        }
    }

    public static void picassoLoadImageWithoutMeasurement(Context c, ImageView eventIcon, String url, int avatar) {
        if (c != null && eventIcon != null) {
            if(avatar==199) {
                Picasso.with(c)
                        .load(url)
                        .fit().centerCrop()
                        .into(eventIcon, new Callback() {
                            @Override
                            public void onSuccess() {
                                TravellerLog.w(TAG, "success");
                            }

                            @Override
                            public void onError() {
                                TravellerLog.w(TAG, "error");
                            }
                        });
            } else {
                Picasso.with(c)
                        .load(url)
                        .placeholder(avatar)
                        .noFade().fit()
                        .into(eventIcon);
            }
        }
    }

    public static void picassoLoadIconPX(Context c, ImageView eventIcon, String url, int height, int width) {
        if (c != null && eventIcon != null) {
            if (url != null) {
                Picasso.with(c).load(url)
                        .resize(width, height)
                        .into(eventIcon, new Callback() {
                            @Override
                            public void onSuccess() {
                                TravellerLog.w(TAG, "success");
                            }

                            @Override
                            public void onError() {
                                TravellerLog.w(TAG, "error");
                            }
                        });
            }
        }
    }

    public static void showErrorMsg(final RelativeLayout errLayout, TextView errText, TextView errTitle, String errorText, String errorTitle) {
        if (errLayout != null) {
            errLayout.setVisibility(View.GONE);
            errLayout.setVisibility(View.VISIBLE);

            if (errorText != null && errText != null) {
                errText.setText(errorText);
            }

            if(errorTitle!=null && errTitle!=null) {
                errTitle.setText(errorTitle);
            }

            //put timer to make the error message gone after 5 seconds
            errLayout.postDelayed(new Runnable() {
                public void run() {
                    if (errLayout != null) {
                        errLayout.setVisibility(View.GONE);
                    }
                }
            }, 5000);
        }
    }

    public static ArrayList<String> removeListDuplicates(ArrayList<String> list) {
        if (list != null && !list.isEmpty()) {
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(list);
            list.clear();
            list.addAll(hashSet);
        }
        return list;
    }

    private static List<String> getDevicePlatforms(FragmentActivity activity) {
        if (activity != null) {
            String[] platforms = activity.getResources().getStringArray(R.array.platform_array_keys);
            if (platforms != null) {
                List<String> pl = new ArrayList<>();
                for (int i = 0; i < platforms.length; i++) {
                    pl.add(platforms[i]);
                }
                return new ArrayList(pl);
            }
        }
        return null;
    }

    public static ArrayList<String> getCorrectConsoleName(ArrayList<String> consoleType) {
        ArrayList<String> console = new ArrayList<>();
        for (String temp : consoleType) {
            switch (temp) {
                case "PS4":
                    console.add("PlayStation 4");
                    break;
                case "PS3":
                    console.add("PlayStation 3");
                    break;
                case "XBOXONE":
                    console.add("Xbox One");
                    break;
                case "XBOX360":
                    console.add("Xbox 360");
                    break;
                case "PC":
                    console.add("PC");
                    break;
            }
        }
        return console;
    }

    public static ArrayList<String> getRemConsoleName(ArrayList<String> consoleType) {
        ArrayList<String> list = new ArrayList<String>();
        if (consoleType.size() == 1) {
            for (String temp : consoleType) {
                switch (temp) {
                    case "PS4":
                        if (consoleType.size() == 1) {
                            list.add(Constants.CONSOLEXBOXONESTRG);
                            list.add(Constants.CONSOLEXBOX360STRG);
                        }
                        break;
                    case "PS3":
                        if (consoleType.size() == 1) {
                            list.add(Constants.CONSOLEPS4STRG);
                            list.add(Constants.CONSOLEXBOXONESTRG);
                            list.add(Constants.CONSOLEXBOX360STRG);
                        }
                        break;
                    case "XBOXONE":
                        if (consoleType.size() == 1) {
                            list.add(Constants.CONSOLEPS4STRG);
                            list.add(Constants.CONSOLEPS3STRG);
                        }
                        break;
                    case "XBOX360":
                        if (consoleType.size() == 1) {
                            list.add(Constants.CONSOLEXBOXONESTRG);
                            list.add(Constants.CONSOLEPS4STRG);
                            list.add(Constants.CONSOLEPS3STRG);
                        }
                        break;
                }
            }
        } else {
            if (consoleType.contains(Constants.CONSOLEPS4) && consoleType.contains(Constants.CONSOLEXBOX360)) {
                list.add(Constants.CONSOLEXBOXONESTRG);
            } else if (consoleType.contains(Constants.CONSOLEPS3) && consoleType.contains(Constants.CONSOLEXBOX360)) {
                list.add(Constants.CONSOLEXBOXONESTRG);
                list.add(Constants.CONSOLEPS4STRG);
            } else if (consoleType.contains(Constants.CONSOLEPS3) && consoleType.contains(Constants.CONSOLEXBOXONE)) {
                list.add(Constants.CONSOLEPS4STRG);
            }
        }
//        if (consoleType.equalsIgnoreCase("PS4")) {
//            list.add("Xbox One");
//            list.add("Xbox 360");
//        }
        return list;
    }

    public static void postTracking(Map obj, Context c, ControlManager cm, String key) {
        if (cm != null) {
            if (obj == null) {
                obj = new HashMap<String, String>();
            }
            RequestParams params = new RequestParams();
            params.put("trackingData", obj);
            params.put("trackingKey", key != null ? key : "UNKNOWN");
            cm.postTracking(params, c != null ? c : cm.getCurrentActivity());
        }
    }

    public static void roundCorner(TextView textView, Context mContext) {
        if (mContext != null) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(5);
            gd.setStroke(2, 0xFF52648b);
            gd.setColor(mContext.getResources().getColor(R.color.choose_platform_color));
            textView.setBackgroundDrawable(gd);
        }
    }

    public static Map<String, String> parseDeferredLink(String s) {
        String[] sub = s.split(Pattern.quote("://"));
        if (sub.length > 1) {
            String firstParam = sub[1];
            if (firstParam != null && !firstParam.isEmpty()) {
                String[] paramKeys = firstParam.split("[/]", 2);
                Map<String, String> json = new HashMap<String, String>();
                if (paramKeys.length > 1) {
                    json.put(paramKeys[0], paramKeys[1]);
                }
                return json;
            }
        }
        return null;
    }

    public static void setOrganicAppInstall(ControlManager cManager) {
        String s = Util.getDefaults("appInstall", cManager.getCurrentActivity());
        if (s == null || s.equalsIgnoreCase(Constants.UNKNOWN_SOURCE)) {
            Map<String, String> jsonOrganic = new HashMap<String, String>();
            jsonOrganic.put("ads", Constants.ORGANIC_SOURCE);
            Util.postTracking(jsonOrganic, cManager.getCurrentActivity(), cManager, Constants.APP_INSTALL);
            Util.setDefaults("appInstall", Constants.ORGANIC_SOURCE, cManager.getCurrentActivity());
        }
    }

    public static void createAlert(String title, String msg, String ok, Activity act) {
        AlertDialog.Builder builder;
        AlertDialog dialog;
        if (title != null && msg != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(act, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(act);
            }

            builder.setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with finish activity
                            dialog.dismiss();
                        }
                    });
            dialog = builder.create();
            dialog.show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static void updateCooieFromBackend(Context context, String cookies) {
        String[] comma = cookies.split(";");
        if (comma != null && comma.length > 0) {
            NetworkEngine.getmInstance(context).clear();
            for (int i = 0; i < comma.length; i++) {
                String[] cookie = comma[i].split("=");
                if (cookie != null && cookie.length > 1) {
                    BasicClientCookie newCookie = new BasicClientCookie(cookie[0], cookie[1]);
                    newCookie.setDomain(Constants.DOMAIN);
                    newCookie.setPath("/");
                    NetworkEngine.getmInstance(context).addCookies(newCookie);
                }
            }
        }
    }

    public static boolean ensureGameTag(UserData user) {
        List<ConsoleData> gameTags = user.getConsoles();
        return (gameTags != null && gameTags.size() > 0);
    }

    public static void setTextViewHTML(TextView title, String html) {
        if (title != null) {
            title.setMovementMethod(LinkMovementMethod.getInstance());
            if (Util.hasNougat()) {
                title.setText(Html.fromHtml(html, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));// for 24 api and more
            } else {
                title.setText(Html.fromHtml(html)); // or for older api
            }
        }
    }

    public static List<String> extractSelectedPlatform(FragmentActivity activity) {
        ArrayList<String> platforms = new ArrayList<>();
        List<String> devicePlatforms = getDevicePlatforms(activity);
        HashMap<String, String> _platformKeys = new HashMap<>();
        HashMap<String, String> platformKeyValues = new HashMap<>();
        final String[] dataKey = activity.getResources().getStringArray(R.array.platform_array_values);
        final String[] values = activity.getResources().getStringArray(R.array.platform_array_keys);
        if (dataKey != null && dataKey.length >= values.length) {
            for (int i = 0; i < dataKey.length; i++) {
                _platformKeys.put(dataKey[i], values[i]);
            }
        }

        UserData data = ControlManager.getmInstance().getUserData();
        if (data != null) {
            List<ConsoleData> consoles = data.getConsoles();
            String selectedConsole = data.getConsoleType();
            if (selectedConsole != null && consoles != null) {
                for (int i = 0; i < consoles.size(); i++) {
                    ConsoleData cd = consoles.get(i);
                    String elem = cd.getcType();
                    if (elem == null || selectedConsole.equalsIgnoreCase(elem)) {
//                        continue;
                    }
                    if(_platformKeys.containsKey(elem.toLowerCase())) {
                        platformKeyValues.put(elem.toLowerCase(), _platformKeys.get(elem.toLowerCase()));
                    }
                    if (cd.isPrimary()) {
                        platforms.add(0, elem);
                    } else {
                        platforms.add(elem);
                    }
                }
            }
        }
        platforms = getCorrectConsoleName(platforms);
        if (devicePlatforms != null && devicePlatforms.size() > platforms.size()) {
            platforms.add("Linked Accounts");
        }
        return platforms;
    }

    public static String[] buildConsoleList(String[] platformKeys, String[] platforms) {
        UserData data = ControlManager.getmInstance().getUserData();
        if (data != null) {
            List<String> tmp = new ArrayList<>();
            List<ConsoleData> consoles = data.getConsoles();
            if (consoles != null && platformKeys != null) {
                for (int i = 0; i < platformKeys.length; i++) {
                    boolean alreadyAdded = false;
                    for (int j = 0; j < consoles.size(); j++) {
                        ConsoleData cd = consoles.get(j);
                        String elem = cd.getcType();
                        if (elem == null || platformKeys[i].equalsIgnoreCase(elem)) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        tmp.add(platforms[i]);
                    }
                }
                String tmpArray[] = new String[tmp.size()];
                return tmp.toArray(tmpArray);
            }
        }
        return null;
    }

    public static int getUserProfileBorderColor() {
        UserData data = ControlManager.getmInstance().getUserData();
        if (data != null) {
            String selectedConsole = data.getConsoleType();
            if (selectedConsole != null) {
                switch (selectedConsole) {
                    case "PC": {
                        return R.color.login_color;
                    }
                    case "PS4": {
                        return R.color.user_profile_border_playstation;
                    }
                    case "XBOXONE":
                    case "XBOX360": {
                        return R.color.user_profile_border_xbox;
                    }
                    default: {
                        return R.color.login_color;
                    }
                }
            }
        }
        return R.color.login_color;
    }
}

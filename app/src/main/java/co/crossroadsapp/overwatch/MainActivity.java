package co.crossroadsapp.overwatch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.AppVersion;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.InvitationLoginData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.ConfigNetwork;
import co.crossroadsapp.overwatch.network.EventListNetwork;
import co.crossroadsapp.overwatch.network.GetVersion;
import co.crossroadsapp.overwatch.network.LoginNetwork;
import co.crossroadsapp.overwatch.network.LogoutNetwork;
import co.crossroadsapp.overwatch.network.OverwatchFetchUserProfileNetwork;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import co.crossroadsapp.overwatch.utils.Version;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends BaseActivity implements Observer {
    //private View register_layout;
    public UserData userData;
    Intent contentIntent;
    private String p;
    private String u;
    private ControlManager mManager;
    private RecyclerView horizontal_recycler_view;
    private ArrayList<EventData> horizontalList;
    private EventCardAdapter horizontalAdapter;
    private TextView privacyTerms;
    private String console;
    private TextView countText;
    private Runnable runnable;
    private Handler handler;
    private LinearLayoutManager horizontalLayoutManagaer;
    private InvitationLoginData invitationRp;
    private String cookies;
    private RelativeLayout topBar;
    private ImageView topBarBack;

    @Override
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.splash_loading);
        setTRansparentStatusBar();
        TravellerLog.w(this, "MainActivity.onCreate starts...");
        u = Util.getDefaults("user", getApplicationContext());
        p = Util.getDefaults("password", getApplicationContext());
        cookies = Util.getDefaults("cookie", getApplicationContext());
        console = Util.getDefaults("consoleType", getApplicationContext());

        Bundle b = getIntent().getExtras();

        if (b != null) {
            if (b != null && b.containsKey("invitation")) {
                invitationRp = (InvitationLoginData) b.getSerializable("invitation");
            }
        }

        userData = new UserData();

//        if(userData!=null && userData.getPsnId()!=null) {
//            u = userData.getPsnId();
//        }

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        userData = mManager.getUserData();

        // getting contentIntent from push notification click
        if (this.getIntent().hasExtra(Constants.TRAVELER_NOTIFICATION_INTENT)) {
            //tracking OS pushnotification initiation
            Map<String, Boolean> json = new HashMap<>();
            json.put("inApp", false);
            Util.postTracking(json, MainActivity.this, mManager, Constants.APP_PUSHNOTIFICATION);
            TravellerLog.w(this, "Push notification intent present");
            Intent messageIntent = (Intent) this.getIntent().getExtras().get(Constants.TRAVELER_NOTIFICATION_INTENT);
            if (messageIntent == null) {
                return;
            }
            contentIntent = null;
            if (messageIntent.getExtras() != null) {
                contentIntent = (Intent) messageIntent.getExtras().get(Constants.NOTIFICATION_INTENT_CHANNEL);
            }
        }

        //check android version for dev builds
        mManager.getAndroidVersion(this);
        mManager.getConfig(MainActivity.this);
        TravellerLog.w(this, "MainActivity.onCreate ends...");
    }

    protected InvitationLoginData getInvitationObject() {
        if (invitationRp != null) {
            return this.invitationRp;
        }
        return null;
    }

    public void showError(String err, String errorType) {
        hideProgressBar();
        if (err != null) {
            if (errorType != null && !errorType.isEmpty()) {
                if (errorType.equalsIgnoreCase(Constants.BUNGIE_ERROR)) {
                    Util.clearDefaults(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(),
                            MissingUser.class);
                    //intent.putExtra("id", username);
                    String errT = "We were unable to connect to your Bungie.net account via " + console + ". Please try again.";
                    intent.putExtra("error", errT);
                    startActivity(intent);
                } else if (errorType.equalsIgnoreCase(Constants.BUNGIE_LEGACY_ERROR)) {
                    // continue with delete
                    Util.clearDefaults(getApplicationContext());
                    showGenericError("LEGACY CONSOLES", getString(R.string.legacy_error), getString(R.string.ok_btn), null, Constants.GENERAL_ERROR, null, false);
                } else if (errorType.equalsIgnoreCase(Constants.BUNGIE_CONNECT_ERROR)) {
                    // continue with delete
                    RequestParams rp = new RequestParams();
                    if (u != null && !u.isEmpty()) {
                        rp.put("userName", u);
                    }
                    mManager.postLogout(MainActivity.this, rp);
                    Util.clearDefaults(getApplicationContext());
                }
            } else {
                //setErrText(err);
                //hideWebviews();
            }


//            if(!err.isEmpty()) {
//                Intent intent = new Intent(getApplicationContext(),
//                        MissingUser.class);
//                //intent.putExtra("id", username);
//                String errT = "We were unable to connect to your Bungie.net account via " + console + ". Please try again.";
//                intent.putExtra("error", errT);
//                startActivity(intent);
//            } else {
//                // continue with delete
//                RequestParams rp = new RequestParams();
//                rp.put("userName", u);
//                mManager.postLogout(MainActivity.this, rp);
//                //forwardAfterVersionCheck();
//            }
        } else {
            //setErrText(getString(R.string.server_error_tryagain));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTRansparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void forwardAfterVersionCheck() {
        //if (u != null && p!= null && console!=null && !u.isEmpty() && !p.isEmpty() && !console.isEmpty()) {
        String isCookieValid = Util.getDefaults(Constants.COOKIE_VALID_KEY, getApplicationContext());
        if (cookies == null && p != null) {
            showGenericError("CHANGES TO SIGN IN", "Good news! You can now sign in using your Xbox or PlayStation account (the same one you use for Bungie.net)", "OK", null, Constants.GENERAL_ERROR, null, false);
        }
        if (u != null && !u.isEmpty()) {
            // continue with delete
            RequestParams rp = new RequestParams();
            rp.put("userName", u);
            mManager.postLogout(MainActivity.this, rp);
        } else {
            Util.clearDefaults(getApplicationContext());
        }
        fetchProfile();
    }

    private void launchGametagFragment() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TravellerLog.w(this, "sign_in_btn.setOnClickListener");
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                intent.putExtra(Constants.LOGIN_SIGNUP_SCREEN_KEY, Constants.ENTER_GAMETAG_NAME_VALUE);
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });
    }

    private void launchMainLayout() {
        TravellerLog.w(this, "Show main activity layout as user data not available");
        setContentView(R.layout.activity_main);

        mManager.getPublicEventList(MainActivity.this);

        privacyTerms = (TextView) findViewById(R.id.privacy_terms);

        countText = (TextView) findViewById(R.id.player_count);

        TextView sign_up_btn = (TextView) findViewById(R.id.sign_up_btn);
        if (sign_up_btn != null) {
            sign_up_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TravellerLog.w(this, "sign_in_btn.setOnClickListener");
                            Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                            intent.putExtra(Constants.LOGIN_SIGNUP_SCREEN_KEY, Constants.SIGNUP_SCREEN_VALUE);
                            MainActivity.this.startActivityForResult(intent, 0);
                        }
                    });
                }
            });
        }

        TextView login_btn = (TextView) findViewById(R.id.login_btn);
        if (login_btn != null) {
            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TravellerLog.w(this, "sign_in_btn.setOnClickListener");
                            Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                            intent.putExtra(Constants.LOGIN_SIGNUP_SCREEN_KEY, Constants.LOGIN_SCREEN_VALUE);
                            MainActivity.this.startActivityForResult(intent, 0);
                        }
                    });
                }
            });
        }

        intializeLoginWebViews();

        Util.setTextViewHTML(privacyTerms, getString(R.string.terms_conditions));
        privacyTerms.setVisibility(View.GONE);

        topBar = (RelativeLayout) findViewById(R.id.top_header);
        topBarBack = (ImageView) findViewById(R.id.main_backbtn);

        topBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                intializeLoginWebViews();
            }
        });

        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        horizontalList = new ArrayList<EventData>();
        if (mManager.getEventListCurrent() != null) {
            horizontalList = mManager.getEventListCurrent();
        }
        horizontalAdapter = new EventCardAdapter(horizontalList, null, MainActivity.this, mManager, Constants.PUBLIC_EVENT_FEED);
        horizontalLayoutManagaer
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

        startSpinner();
    }

    private void fetchProfile() {
        OverwatchFetchUserProfileNetwork loginNetwork = new OverwatchFetchUserProfileNetwork(this);
        loginNetwork.addObserver(this);
        try {
            loginNetwork.fetchProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void intializeLoginWebViews() {
        /*intializeWebViews(webViewPS, mManager.getPSNLoginUrl()!=null?mManager.getPSNLoginUrl():Constants.BUNGIE_PSN_LOGIN);
        intializeWebViews(webViewXBOX, mManager.getXboxLoginUrl()!=null?mManager.getXboxLoginUrl():Constants.BUNGIE_XBOX_LOGIN);*/
    }

    private void intializeWebViews(WebView wb, String url) {
        if (wb != null) {
            wb.removeAllViews();
            wb.getSettings().setJavaScriptEnabled(true);
            wb.getSettings().setLoadWithOverviewMode(true);
            wb.getSettings().setUseWideViewPort(true);
            wb.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (url.equalsIgnoreCase("https://www.bungie.net/")) {
                        showBungieProgressBar();
                        //hideWebviews();
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    cookies = CookieManager.getInstance().getCookie(url);
                    String csrf;
                    String[] pair = cookies.split(";");
                    for (int i = 0; i < pair.length; i++) {
                        String temp = pair[i].substring(0, pair[i].indexOf('=')).trim();
                        if (temp.equalsIgnoreCase("bungled")) {
//                        webView.setVisibility(View.GONE);
                            csrf = pair[i].substring(pair[i].indexOf('=') + 1, pair[i].length());
                            Util.setDefaults("csrf", csrf, MainActivity.this);
                            Util.setDefaults("cookie", cookies, MainActivity.this);
                            //network call to get current user
                            mManager.getBungieCurrentUser(csrf, cookies, getApplicationContext());
                            return;
                        }
                    }
                }
            });
            if (url != null) {
                wb.loadUrl(url);
            }
        }
    }

    private void smoothScroll(final int position) {
        horizontal_recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0) {
                    horizontal_recycler_view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            horizontalLayoutManagaer.scrollToPositionWithOffset(position, 0);
                        }
                    });
                }
            }
        });

        horizontal_recycler_view.smoothScrollToPosition(position);
    }

    private void startSpinner() {
        if (horizontalAdapter != null && horizontal_recycler_view != null) {
            if (horizontalAdapter.elistLocal != null && horizontalAdapter.elistLocal.size() > 1) {
                final int speedScroll = 2000;
                handler = new Handler();
                runnable = new Runnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (count < horizontalAdapter.elistLocal.size()) {
                            //horizontal_recycler_view.smoothScrollToPosition(++count);
                            smoothScroll(++count);
                            handler.postDelayed(this, speedScroll);
                        } else {
                            count = 0;
                            horizontal_recycler_view.scrollToPosition(count);
                            handler.postDelayed(this, speedScroll);
                        }
                    }
                };

                handler.postDelayed(runnable, speedScroll);
            }
        }
    }

    private void stopSpinner() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        TravellerLog.w(this, "Registering ReceivefromBackpressService ");
        registerReceiver(ReceivefromBackpressService, new IntentFilter("backpress_flag"));
        startSpinner();
    }

    private BroadcastReceiver ReceivefromBackpressService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public void onStop() {
        stopSpinner();
        TravellerLog.w(this, "Unregistering ReceivefromBackpressService ");
        unregisterReceiver(ReceivefromBackpressService);
        hideProgressBar();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    public UserData getUserData() {
        if (userData != null) {
            return userData;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if(hideWebviews()) {
            super.onBackPressed();
            finish();
        }*/
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GetVersion) {
            TravellerLog.w(this, "Update observer for getversion network call response");
            AppVersion ver = (AppVersion) data;
            String currVer = Util.getApplicationVersionCode(this);
            String latestVer = ver.getVersion();
            Version currVersion = new Version(currVer);
            Version latestVersion = new Version(latestVer);
            if (latestVersion.compareTo(currVersion) > 0) {
                //mManager.getAndroidVersion(this);
            } else {
                forwardAfterVersionCheck();
            }
        } else if (observable instanceof OverwatchFetchUserProfileNetwork) {
            if (data != null && data instanceof UserData) {
                UserData user = (UserData) data;
                if( Util.ensureGameTag(user) )
                {
                    ControlManager.getmInstance().setUserdata(user);
                    launchListActivityFragment(user);
                }
                else {
                    launchGametagFragment();
                }
            }
            else {
                launchMainLayout();
            }
        } else if (observable instanceof LoginNetwork) {
            if (data != null) {
                TravellerLog.w(this, "Update observer for LoginNetwork network call response");
                UserData ud = (UserData) data;
                if (ud != null && ud.getUserId() != null) {
                    if ((ud.getAuthenticationId() == Constants.LOGIN)) {
                        launchListActivityFragment(ud);
                    } else {
                        setContentView(R.layout.activity_main);
                    }
                } else {
                    setContentView(R.layout.activity_main);
                }
            } else {
                TravellerLog.w(this, "Show main activity layout as user data not available from login response");
                setContentView(R.layout.activity_main);
            }
        } else if (observable instanceof LogoutNetwork) {
            launchMainLayout();
            showGenericError("CHANGES TO SIGN IN", "Your gamertag now replaces your Crossroads username when logging in.\n" +
                    "(your password is still the same)", "OK", null, Constants.GENERAL_ERROR, null, false);
        } else if (observable instanceof EventListNetwork) {
            if (data != null) {
                horizontalAdapter.elistLocal.clear();
                horizontalAdapter.addItem(mManager.getEventListCurrent(), null);
                horizontalAdapter.notifyDataSetChanged();
                startSpinner();
            }
        } else if (observable instanceof ConfigNetwork) {
            if (data != null) {
                if (mManager != null) {
                    mManager.parseAndSaveConfigUrls((JSONObject) data);
                }
                forwardAfterVersionCheck();
            }
        }
    }

    public void setUserCount(String userCount) {
        if (countText != null) {
            countText.setVisibility(View.VISIBLE);
            countText.setText(userCount);
        }
    }

    private void launchListActivityFragment(UserData ud) {
        //save in preferrence
        //Util.setDefaults("user", ud.getUserId(), getApplicationContext());
//                        Util.setDefaults("password", password, getApplicationContext());
        Util.setDefaults("consoleType", console, getApplicationContext());
        ud.setPassword(p);

        mManager = ControlManager.getmInstance();
        mManager.setUserdata(ud);
        Intent regIntent;

        //decide for activity
        //regIntent = mManager.decideToOpenActivity(contentIntent);

        regIntent = new Intent(getApplicationContext(),
                ListActivityFragment.class);
        if (contentIntent != null) {
            regIntent.putExtra("eventIntent", contentIntent);
        }

        //clear invitation req params
        if (invitationRp != null) {
            invitationRp.clearRp();
        }

        startActivity(regIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.LOGIN_SCREEN_BACK_BUTTON) {
            if( !Util.checkUserObject(ControlManager.getmInstance().getUserData()))
            {
                finish();
            }
        } else if (resultCode == Activity.RESULT_OK) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    launchListActivityFragment(ControlManager.getmInstance().getUserData());
                }
            });
        }
    }
}

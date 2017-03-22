package co.crossroadsapp.overwatch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.ConsoleData;
import co.crossroadsapp.overwatch.data.CurrentEventDataHolder;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.EventList;
import co.crossroadsapp.overwatch.data.GroupData;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.PushNotification;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.ActivityListNetwork;
import co.crossroadsapp.overwatch.network.ChangeCurrentConsoleNetwork;
import co.crossroadsapp.overwatch.network.EventByIdNetwork;
import co.crossroadsapp.overwatch.network.EventListNetwork;
import co.crossroadsapp.overwatch.network.EventRelationshipHandlerNetwork;
import co.crossroadsapp.overwatch.network.GroupListNetwork;
import co.crossroadsapp.overwatch.network.HelmetUpdateNetwork;
import co.crossroadsapp.overwatch.network.LogoutNetwork;
import co.crossroadsapp.overwatch.network.PrivacyLegalUpdateNetwork;
import co.crossroadsapp.overwatch.network.ResendBungieVerification;
import co.crossroadsapp.overwatch.utils.CircularImageView;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import co.crossroadsapp.overwatch.views.CloseableSpinner;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sharmha on 4/8/16.
 */
public class ListActivityFragment extends BaseActivity implements Observer, AdapterView.OnItemSelectedListener {
    UserData user;
    ControlManager mManager;
    private EventList eList;
    private ArrayList<EventData> eData;
    private ArrayList<EventData> fragmentCurrentEventList;
    private ArrayList<EventData> fragmentupcomingEventList;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    private CircularImageView userProfile;
    private CircularImageView userProfileDrawer;
    private TextView userNameDrawer;
    private DrawerLayout drawerLayout;
    private TextView logout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout notiBar;
    private TextView notiEventText;
    private TextView notiTopText;
    private TextView notiMessage;
    private ImageView notif_close;
    private RelativeLayout errLayout;
    private TextView errText;
    private ImageView close_err;
    private TextView createNewEventBtn;
    private TextView crash_report;
    private TextView showVersion;
    private RelativeLayout progress;
    private ValueEventListener listener;
    private TextView verify_logout;

    private Firebase refFirebase;

    private EventData pushEventObject;

    private RelativeLayout unverifiedUserScreen;
    private TextView verify_username;
    private TextView verify_user_bottom_text;
    protected ImageView appIcon;

    private GroupDrawerAdapter gpAct;
    private TextView inviteFrnds;
    private ValueEventListener userListener;
    private Firebase refUFirebase;
    private ImageView grpIcon;

    private WebView legalView;
    private TextView termsService;
    private TextView privacy;
    private TextView license;
    private TextView sendMsgAgain;
    private ArrayList<ActivityData> adActivityData;
    private ArrayAdapter<String> adapterConsole;
    private List<String> consoleItems;
    private ImageView imgConsole;
    private CloseableSpinner dropdown;
    private AlertDialog dialogPrivacy;
    private SwipeFrameLayout cardStackLayout;
    private String adCardPosition;
    private String showUnverifiedMsg;
    private ImageView down_arw_img;
    private TextView consoleText;
    private Bundle b;
    private TextView changePassword;
    private TextView changeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        //hideStatusBar();
        setTRansparentStatusBar();

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        b = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            b = getIntent().getExtras();
        }
        //user = b.getParcelable("userdata");
        if (mManager.getUserData() != null) {
            user = mManager.getUserData();
        } else if (b != null) {
            user = b.getParcelable("userdata");
        }

        showUnverifiedMsg = Util.getDefaults("showUnverifiedMsg", getApplicationContext());
        if (showUnverifiedMsg == null) {
            showUnverifiedUserMsg();
            Util.setDefaults("showUnverifiedMsg", "true", mManager.getCurrentActivity());
        }

        decidetoShowTutorial();
        //checkEventIntent();
//        if(b.containsKey("eventIntent")){
//            Intent localPushEventObj = (Intent)b.get("eventIntent");
//            if(localPushEventObj.hasExtra("message")) {
//                String payload = localPushEventObj.getStringExtra("message");
//                createPushEventObj(payload);
//            }
//            //Util.clearNotification(localPushEventObj.getExtras());
//            this.getIntent().removeExtra("eventIntent");
//        }

        //mManager.postGetActivityList(this);

        // get existing event list
        getExistingList();

        //mManager.getEventList(this);

        if (mManager.getCurrentGroupList() != null) {
            if (mManager.getCurrentGroupList().isEmpty()) {
                mManager.getGroupList(this);
            }
        } else {
            mManager.getGroupList(this);
        }


        Firebase.setAndroidContext(this);

        //register event listener
        //registerFirbase();

//        //register user listener
//        registerUserFirebase();

        unverifiedUserScreen = (RelativeLayout) findViewById(R.id.verify_fail);

        sendMsgAgain = (TextView) findViewById(R.id.send_msg_again);

        sendMsgAgain.setText(Html.fromHtml(getResources().getString(R.string.message_missing_tryagain)));

        //check user verification
        //checkUserPSNVerification();

        progress = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        userProfile = (CircularImageView) findViewById(R.id.userProfile);
        userProfileDrawer = (CircularImageView) findViewById(R.id.profile_avatar);
        userNameDrawer = (TextView) findViewById(R.id.profile_name);

        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        cardStackLayout = (SwipeFrameLayout) findViewById(R.id.notification_bar_layout);

        legalView = (WebView) findViewById(R.id.legal_web);

        appIcon = (ImageView) findViewById(R.id.badge_icon);
        updateUserProfileImage(user.getImageUrl());
        if (user != null) {
            if (user.getUser() != null) {
                userNameDrawer.setText(user.getPsnId());
            }
        }

        //hemlet update
        userProfileDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && user.getPsnVerify() != null) {
//                    if (!user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
//                        showUnverifiedUserMsg();
//                    } else {
                        findViewById(R.id.loadingImg).setVisibility(View.VISIBLE);
                        mManager.postHelmet(ListActivityFragment.this);
//                    }
                }
            }
        });

        fragmentCurrentEventList = new ArrayList<EventData>();
        fragmentupcomingEventList = new ArrayList<EventData>();
        adActivityData = new ArrayList<ActivityData>();

        drawerLayout = (DrawerLayout) findViewById(R.id.out);

        logout = (TextView) findViewById(R.id.logout_btn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, 0, 0) {//new ActionBarDrawerToggle(this, null,
            //null, 0, 0) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                changeProfileUsername("", false);
//                startCouchMarks(Constants.HOME_INTRODUCTION);
            }

            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                    if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {

                    }
                } else if (newState == DrawerLayout.STATE_SETTLING) {

                } else if (newState == DrawerLayout.STATE_IDLE) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
            }
        };

        // Set the drawer toggle as the DrawerListener
        this.drawerLayout.setDrawerListener(mDrawerToggle);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileDrawer(Gravity.LEFT);
            }
        });

        appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileDrawer(Gravity.RIGHT);
            }
        });

//        notiBar = (RelativeLayout) findViewById(R.id.notification_bar);
//        notiBarL = (FrameLayout) findViewById(R.id.notification_bar_layout);
//        notiEventText = (TextView) findViewById(R.id.noti_text);
//        notiTopText = (TextView) findViewById(R.id.noti_toptext);
//        notiMessage = (TextView) findViewById(R.id.noti_subtext);
//        notiMessage.setMovementMethod(new ScrollingMovementMethod());
//        notif_close = (ImageView) findViewById(R.id.noti_close);
//        notif_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                notiBar.setVisibility(View.GONE);
//            }
//        });

        errLayout = (RelativeLayout) findViewById(R.id.error_layout);
        errText = (TextView) findViewById(R.id.error_sub);
        close_err = (ImageView) findViewById(R.id.err_close);

        close_err.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errLayout.setVisibility(View.GONE);
            }
        });

        verify_logout = (TextView) findViewById(R.id.verify_logout);

        verify_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        sendMsgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                showProgress();
                mManager.resendBungieMsg(ListActivityFragment.this);
            }
        });

        createNewEventBtn = (TextView) findViewById(R.id.create_new_event);

        createNewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to create new event page
                Intent regIntent = new Intent(getApplicationContext(),
                        AddNewActivity.class);
                //regIntent.putExtra("userdata", user);
                startActivity(regIntent);
                finish();
            }
        });

        inviteFrnds = (TextView) findViewById(R.id.invite);
        imgConsole = (ImageView) findViewById(R.id.console_icon);
        down_arw_img = (ImageView) findViewById(R.id.down_arw_img);
        consoleText = (TextView) findViewById(R.id.consoletype_text);

        dropdown = (CloseableSpinner) findViewById(R.id.console_spinner);

        dropdown.setOnItemSelectedListener(this);
        // Set adapter for console selector
        setUpConsoleSpinner();

        changePassword = (TextView) findViewById(R.id.change_password);
        changeEmail = (TextView) findViewById(R.id.change_email);

//        adapterConsole = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, consoleItems) {
//
//            int FONT_STYLE = Typeface.BOLD;
//
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
//
//                ((TextView) v).setTypeface(Typeface.SANS_SERIF, FONT_STYLE);
//                ((TextView) v).setTextColor(
//                        getResources().getColorStateList(R.color.trimbe_white)
//                );
//                ((TextView) v).setGravity(Gravity.LEFT);
//
//                ((TextView) v).setPadding(Util.dpToPx(82, ListActivityFragment.this), 0, 0, 0);
//                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                ((TextView) v).setText(((TextView) v).getText());
//
//                if(((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG) || ((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOX360STRG)) {
//                    imgConsole.setImageResource(R.drawable.icon_xboxone_console);
//                } else {
//                    imgConsole.setImageResource(R.drawable.icon_psn_console);
//                }
//
//                return v;
//            }
//
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                return getCustomView(position, convertView, parent, consoleItems);
//            }
//        };
//        adapterConsole.setDropDownViewResource(R.layout.empty_layout);
//        dropdown.setAdapter(adapterConsole);
//        adapterConsole.notifyDataSetChanged();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(getApplicationContext(),
                        ChangePassword.class);
                startActivity(regIntent);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(getApplicationContext(),
                        ChangeEmail.class);
                startActivity(regIntent);
            }
        });

        inviteFrnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://crossrd.app.link/share";
                final Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                if (url != null) {
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
                    startActivity(Intent.createChooser(sharingIntent, "Share"));
                }
            }
        });
        crash_report = (TextView) findViewById(R.id.crash_btn);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), ListActivityFragment.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        showVersion = (TextView) findViewById(R.id.build_version);
        if (Util.getApplicationVersionCode(ListActivityFragment.this) != null) {
            showVersion.setText(Util.getApplicationVersionCode(ListActivityFragment.this));
        }

        termsService = (TextView) findViewById(R.id.legal1);
        privacy = (TextView) findViewById(R.id.legal2);
        license = (TextView) findViewById(R.id.legal3);

        termsService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLegalWebView(Constants.TERMS_OF_SERVICE);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLegalWebView(Constants.PRIVACY_POLICY);
            }
        });

        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLegalWebView(Constants.LICENCE);
            }
        });

//        showVersion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(legalView!=null) {
//                    closeProfileDrawer(Gravity.LEFT);
//                    legalView.setVisibility(View.VISIBLE);
//                    legalView.loadUrl(Constants.LEGAL);
//                }
//            }
//        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //track tab changes
                if (position == 0) {
                    //tracking current click
                    Map<String, String> json = new HashMap<String, String>();
                    Util.postTracking(json, ListActivityFragment.this, mManager, Constants.APP_CURRENTTAB);
                } else if (position == 1) {
                    //tracking upcoming click
                    Map<String, String> json = new HashMap<String, String>();
                    Util.postTracking(json, ListActivityFragment.this, mManager, Constants.APP_UPCOMINGTAB);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        crash_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(getApplicationContext(),
                        CrashReport.class);
                //regIntent.putExtra("userdata", user);
                startActivity(regIntent);
            }
        });

        gpAct = new GroupDrawerAdapter(this);

        checkClanSet();

        showNotifications();

        // check if just created new event to go to eventdetail
        if (b != null) {
            if (b.containsKey("gotoEventDetail")) {
                Intent i = new Intent(this, EventDetailActivity.class);
                startActivity(i);
            }
        }

        //check if app is opening from external deeplink
        checkIfExternalDeepLinkPresent();

        setGroupImageUrl();
    }

    private void decidetoShowTutorial() {
        if(user!=null && !user.getHasCompletedOnBoarding()) {
            Intent regIntent = new Intent(this,
                    TutorialActivity.class);
            startActivity(regIntent);
        }
    }

    private void checkPrivacyDialoge() {
        if (user != null) {
            if (user.getLegal() != null) {
                if (user.getLegal().getPrivacyNeedsUpdate() || user.getLegal().getTermsNeedsUpdate()) {
                    //alert pop-up dailogue
                    final TextView message = new TextView(this);
                    if (user.getLegal().getPrivacyNeedsUpdate() && user.getLegal().getTermsNeedsUpdate()) {
                        message.setText(Html.fromHtml((getString(R.string.dialog_message))));
                        setTextViewHTML(message, (getString(R.string.dialog_message)));
                    } else if (user.getLegal().getTermsNeedsUpdate()) {
                        message.setText(Html.fromHtml((getString(R.string.dialog_message_terms))));
                        setTextViewHTML(message, (getString(R.string.dialog_message_terms)));
                    } else if (user.getLegal().getPrivacyNeedsUpdate()) {
                        message.setText(Html.fromHtml((getString(R.string.dialog_message_privacy))));
                        setTextViewHTML(message, (getString(R.string.dialog_message_privacy)));
                    }

                    message.setPadding(95, 80, 95, 80);
                    //message.setMovementMethod(LinkMovementMethod.getInstance());
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                            .setTitle(R.string.dialog_title)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mManager.legalPrivacyDone(ListActivityFragment.this);
                                }
                            })
                            .setView(message);
                    dialogPrivacy = builder.create();
                    dialogPrivacy.show();
                }
            }
        }
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                dialogPrivacy.dismiss();
                // Do something with span.getURL() to handle the link click...
                legalView.setVisibility(View.VISIBLE);
                legalView.setWebViewClient(new WebViewClient());
                legalView.loadUrl(span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void setUpConsoleSpinner() {
        List<String> platforms = Util.extractSelectedPlatform(this);
        final String[] data = getResources().getStringArray(R.array.platform_array_keys);
        if (down_arw_img != null) {
            down_arw_img.setVisibility(View.VISIBLE);
        }
        updateUserProfileImageBorder();
        if (user != null) {
            updateUserProfileImage(user.getImageUrl());
            if (user.getUser() != null) {
                userNameDrawer.setText(user.getPsnId());
            }
        }
        adapterConsole = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, platforms) {
            int FONT_STYLE = Typeface.BOLD;

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(Typeface.SANS_SERIF, FONT_STYLE);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.trimble_white)
                );
                ((TextView) v).setGravity(Gravity.CENTER);

                ((TextView) v).setPadding(Util.dpToPx(0, ListActivityFragment.this), 0, 0, 0);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ((TextView) v).setText(((TextView) v).getText());
                if (imgConsole != null) {
                    imgConsole.setImageResource(R.mipmap.img_icon_platform);
                }
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getCustomView(position, convertView, parent, getItem(position));
            }
        };
        adapterConsole.setDropDownViewResource(R.layout.empty_layout);
        dropdown.setAdapter(adapterConsole);
    }

    private void updateConsoleListUserDrawer1() {
        //final ArrayList<String> consoleItems = new ArrayList<String>();
        consoleItems = Util.getCorrectConsoleName(mManager.getConsoleList());
        //final ArrayList<String> consoleItems = mManager.getConsoleList();
        if (needToAdd(consoleItems)) {
            //consoleItems.add("Add Console");
        }
        if (consoleItems.size() > 1) {
            down_arw_img.setVisibility(View.VISIBLE);
            adapterConsole = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, consoleItems) {
                int FONT_STYLE = Typeface.BOLD;

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    ((TextView) v).setTypeface(Typeface.SANS_SERIF, FONT_STYLE);
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.trimble_white)
                    );
                    ((TextView) v).setGravity(Gravity.CENTER);

                    ((TextView) v).setPadding(Util.dpToPx(0, ListActivityFragment.this), 0, 0, 0);
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    ((TextView) v).setText(((TextView) v).getText());

                    if (((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG) || ((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOX360STRG)) {
                        imgConsole.setImageResource(R.drawable.icon_xboxone_consolex);
                    } else {
                        imgConsole.setImageResource(R.drawable.icon_psn_consolex);
                    }

                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    return getCustomView(position, convertView, parent, getItem(position));
                }
            };
            adapterConsole.setDropDownViewResource(R.layout.empty_layout);
            dropdown.setAdapter(adapterConsole);
            adapterConsole.notifyDataSetChanged();
        } else {
            if (consoleItems.get(0).equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG)) {
                imgConsole.setImageResource(R.drawable.icon_xboxone_consolex);
            } else if (consoleItems.get(0).equalsIgnoreCase(Constants.CONSOLEPS4STRG)) {
                imgConsole.setImageResource(R.drawable.icon_psn_consolex);
            }
            down_arw_img.setVisibility(View.GONE);
            consoleText.setText(consoleItems.get(0).toString());
        }
    }

    protected void setAdCardPosition(String adAct) {
        adCardPosition = adAct;
    }

    private boolean needToAdd(List<String> consoleItems) {
        if (consoleItems.size() == 1) {
            return true;
        } else {
            int x = 0;
            for (int i = 0; i < consoleItems.size(); i++) {
                if (consoleItems.get(i).equalsIgnoreCase(Constants.CONSOLEPS4STRG) || consoleItems.get(i).equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG)) {
                    x++;
                }
            }
            if (x < 2) {
                return true;
            }
        }
        return false;
    }

    private String getConsoleType(String item) {
        String value=null;
        switch (item) {
            case "PlayStation 4":
                value = Constants.CONSOLEPS4;
                break;
            case "Xbox One":
                value = Constants.CONSOLEXBOXONE;
                break;
            default:
        }
        return value;
    }

    //console selector spinner
    private View getCustomView(int position, View convertView, ViewGroup parent, final String item) {
        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout.console_selction_view, parent, false);
        ImageView addSymbol = (ImageView) row.findViewById(R.id.console_img);
        CardView card = (CardView) row.findViewById(R.id.console_card);
        final String itemValue = getConsoleType(item);

        if (position == 0) {
            row.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
            card.setVisibility(View.GONE);
        } else {
            row.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Util.dpToPx(50, ListActivityFragment.this)));
            card.setVisibility(View.VISIBLE);
        }
        if (item.equalsIgnoreCase("Linked Accounts")) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dropdown != null) {
                        Activity activity = (Activity) v.getContext();
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dropdown.onDetachedFromWindow();
                                //start new activity for event
                                Intent regIntent = new Intent(ListActivityFragment.this,
                                        LoginScreen.class);
                                regIntent.putExtra(Constants.LOGIN_SIGNUP_SCREEN_KEY, Constants.ADD_LINKED_ACCOUNT_VALUE);
                                //regIntent.putExtra("userdata", user);
                                ListActivityFragment.this.startActivityForResult(regIntent, 0);
                                closeProfileDrawer(Gravity.LEFT);
                            }
                        });
                    }
                }
            });
        } else {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dropdown != null) {
                        Activity activity = (Activity) v.getContext();
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dropdown.onDetachedFromWindow();
                                //start new activity for event
                                changeToOtherConsole(itemValue!=null?itemValue:item);
                            }
                        });
                    }
                }
            });
        }

        TextView label = (TextView) row.findViewById(R.id.add_console_text);
        if (item != null) {
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            label.setText(item);
        }

        return row;
    }

    private void checkIfExternalDeepLinkPresent() {
        if (mManager != null) {
            if (mManager.getDeepLinkEvent() != null) {
                RequestParams param = new RequestParams();
                param.add("id", mManager.getDeepLinkEvent());
                mManager.postEventById(ListActivityFragment.this, param);
            }
        }
    }

    private void updateExternalDeepLink(EventData data) {
        if (mManager != null) {
            if (checkIfConsolePresent(data.getConsoleType())) {
                GroupData gp = mManager.getGroupObj(data.getClanId());
                if (gp != null) {
                    if (data.getPlayerData().size() == data.getMaxPlayer()) {
                        showDeeplinkError(Constants.EVENT_FULL, null, null, null);
                    } else {
                        checkToChangeConsoleGrp(data.getConsoleType() != null ? data.getConsoleType() : null, data.getClanId() != null ? data.getClanId() : null);
                        startEventDetail(data);
                    }
                } else {
                    showDeeplinkError(Constants.EVENT_GRP_MISSING, data.getClanName() != null ? data.getClanName() : "", mManager.getDeepLinkActivityName() != null ? mManager.getDeepLinkActivityName() : "", data.getClanId());
                }
            } else {
                showDeeplinkError(Constants.EVENT_CONSOLE_MISSING, data.getClanName() != null ? data.getClanName() : "", data.getConsoleType() != null ? data.getConsoleType() : "", null);
            }
            mManager.setDeepLinkEvent(null, null);
        }
    }

    private boolean checkIfConsolePresent(String consoleType) {
        if (mManager.getUserData() != null && mManager.getUserData().getConsoles() != null) {
            List<ConsoleData> localCon = mManager.getUserData().getConsoles();
            for (int i = 0; i < localCon.size(); i++) {
                if (consoleType.equalsIgnoreCase(localCon.get(i).getcType())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateUserProfileImageBorder() {
        int borderColor = Util.getUserProfileBorderColor();
        if( Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            if (userProfile != null) {
                userProfile.setBorderColor(getColor(borderColor));
            }
            if (userProfileDrawer != null) {
                userProfileDrawer.setBorderColor(getColor(borderColor));
            }
        }
        else
        {
            if (userProfile != null) {
                userProfile.setBorderColor(getResources().getColor(borderColor));
            }
            if (userProfileDrawer != null) {
                userProfileDrawer.setBorderColor(getResources().getColor(borderColor));
            }
        }
    }

    private void updateUserProfileImage(String url) {
        if ((url != null) && url.length() > 0) {
            if (user != null) {
                Picasso.with(this).load(url).resizeDimen(R.dimen.player_profile_drawer_width, R.dimen.player_profile_drawer_hgt)
                        .centerCrop().placeholder(R.drawable.default_profile).into(userProfile);
                Picasso.with(this).load(url).resizeDimen(R.dimen.player_profile_drawer_width, R.dimen.player_profile_drawer_hgt)
                        .centerCrop().placeholder(R.drawable.default_profile).into(userProfileDrawer);
            }
        }
    }

    private void checkEventIntent() {
        if (pushEventObject == null) {
            if (this.getIntent().hasExtra("eventIntent")) {
                Intent cIntent = (Intent) this.getIntent().getExtras().get("eventIntent");
                String contentIntent = cIntent.getExtras().get("message").toString();
                createPushEventObj(contentIntent);
                //remove intent
                this.getIntent().removeExtra("eventIntent");
            }
        }
    }

    private void showLogoutDialog() {
        // alert dailogue
        new AlertDialog.Builder(ListActivityFragment.this)
                .setMessage(getResources().getString(R.string.logout_popup_msg))
                .setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeProfileDrawer(Gravity.LEFT);
                        showProgress();
                        // continue with delete
                        logout();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_btn), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void logout() {
        RequestParams rp = new RequestParams();
        if (user != null && user.getUser() != null) {
            rp.put("userName", user.getPsnId());
        }
        mManager.postLogout(ListActivityFragment.this, rp);
    }

    private void showLegalWebView(final String service) {
        if (legalView != null) {
            closeProfileDrawer(Gravity.LEFT);
            legalView.setVisibility(View.VISIBLE);
            legalView.setWebViewClient(new WebViewClient());
            legalView.loadUrl(service);
        }
    }

    private void checkUserPSNVerification() {
        if (user != null) {
            if (user.getPsnVerify() != null) {
                if (!user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
                    //register user listener
                    registerUserFirebase();
//                    verify_username = (TextView) findViewById(R.id.top_text);
//                    verify_user_bottom_text = (TextView) findViewById(R.id.verify_bottom_text);
//                    verify_user_bottom_text.setText(Html.fromHtml((getString(R.string.verify_accnt_bottomtext))));
//                    verify_user_bottom_text.setMovementMethod(LinkMovementMethod.getInstance());
//                    verify_username.setText("Hi " + user.getUser() + "!");
//                    unverifiedUserScreen.setVisibility(View.VISIBLE);
                } else {
                    if (unverifiedUserScreen != null) {
                        unverifiedUserScreen.setVisibility(View.GONE);
                        checkClanSet();
                    }
                    updateUserProfileImageBorder();
                    updateUserProfileImage(user.getImageUrl() != null ? user.getImageUrl() : null);
                    unregisterUserFirebase();
                }
            }
        }
    }

    @Override
    protected void goToDetail(String id, String console, String clanId) {
        if (id != null) {
            if (mManager != null) {
                //check and change if current active console or grp is different
                checkToChangeConsoleGrp(console, clanId);

                getEventById(id);
            }
        }
    }

    protected void getEventById(String id) {
        if ((id != null) && (!id.equalsIgnoreCase("null"))) {
            RequestParams param = new RequestParams();
            param.add("id", id);
            mManager.postEventById(this, param);
        }
    }

    private void checkToChangeConsoleGrp(String console, String clanId) {
        if (mManager != null && user != null) {
            //check and change if current active console is different
            if (console != null) {
                if (user.getConsoleType() != null) {
                    if (!console.equalsIgnoreCase(user.getConsoleType()) && checkIfConsolePresent(console)) {
                        changeToOtherConsole(console);
                    }
                }
            }
            //check and change if current active group is different
            if (clanId != null) {
                if (mManager.getGroupObj(clanId) != null) {
                    if (!clanId.equalsIgnoreCase(user.getClanId())) {
                        GroupData grp = mManager.getGroupObj(clanId);
                        RequestParams params = new RequestParams();
                        if (user.getUserId() != null) {
                            params.add("id", user.getUserId());
                        }
                        params.add("clanId", grp.getGroupId());
                        params.add("clanName", grp.getGroupName());
                        params.add("clanImage", grp.getGroupImageUrl());
                        mManager.postSetGroup(ListActivityFragment.this, params);
                    }
                }
            }
        }
    }

    private void checkClanSet() {
        String openD = Util.getDefaults("opendrawer", this);
        if(openD!=null && openD.equalsIgnoreCase("true")) {
            openProfileDrawer(Gravity.RIGHT);
            Util.setDefaults("opendrawer", "false", this);
        }
//        if (user != null) {
//            if (user.getPsnVerify() != null) {
//                if (user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
//                    if (user.getClanId() != null) {
//                        if (user.getAuthenticationId() == Constants.REGISTER) {
//                            openProfileDrawer(Gravity.RIGHT);
//                        }
//                        setGroupImageUrl();
//                    }
//                }
//            }
//        }
    }

    private void setGroupImageUrl() {
        if (user != null && user.getClanId() != null) {
            GroupData gd = mManager.getGroupObj(user.getClanId());
            if (gd != null) {
                String imageUrl = gd.getGroupImageUrl();
                if (imageUrl != null) {
                    setgrpIcon(imageUrl);
                }
            }
        }
    }

    private void getExistingList() {
        if (mManager != null) {
            if (mManager.getEventListCurrent() != null && (!mManager.getEventListCurrent().isEmpty())) {
                if ((mManager.getAdsActivityList() != null) && (!mManager.getAdsActivityList().isEmpty())) {
                    adActivityData = mManager.getAdsActivityList();
                }
                createUpcomingCurrentList(mManager.getEventListCurrent());
            }
        }
    }

    private void registerFirbase() {
        setupEventListener();
        if (mManager != null) {
            if (mManager.getUserData() != null) {
                this.user = mManager.getUserData();
            }
        }
        if (user != null && user.getClanId() != null) {
            refFirebase = new Firebase(Util.getFirebaseUrl(this.user.getClanId(), null, Constants.EVENT_CHANNEL));
            if (listener != null) {
                refFirebase.addValueEventListener(listener);
            }
        }
    }

    private void unregisterFirebase() {
        if (refFirebase != null && listener != null) {
            refFirebase.removeEventListener(listener);
        }
    }

    private void registerUserFirebase() {
        if (user.getUserId() != null) {
            setupUserListener();
            refUFirebase = new Firebase(Util.getFirebaseUrl(this.user.getUserId(), null, Constants.USER_CHANNEL));
            if (userListener != null) {
                refUFirebase.addValueEventListener(userListener);
            }
        }
    }

    private void unregisterUserFirebase() {
        if (userListener != null) {
            if (refUFirebase != null) {
                refUFirebase.removeEventListener(userListener);
            }
        }
    }

    private void setupUserListener() {
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null) {
                    UserData ud = new UserData();
                    String psnV = null;
                    if (snapshot.hasChild("value")) {
                        JSONObject userObj = new JSONObject((HashMap) snapshot.getValue());
                        ud.toJson(userObj);
                        if (ud.getUserId() != null) {
                            mManager.setUserdata(ud);
                            user = ud;
                            updateUserProfileImageBorder();
                            updateUserProfileImage(user.getImageUrl() != null ? user.getImageUrl() : null);
//                            if(ud.getPsnVerify()!=null) {
//                                psnV = ud.getPsnVerify();
//                            }
//                            if (psnV != null) {
//                                if (psnV.equalsIgnoreCase(Constants.PSN_VERIFIED)) {
//                                    if (mManager != null && mManager.getUserData() != null) {
//                                        UserData u = mManager.getUserData();
//                                        u.setPsnVerify(psnV);
//                                    }
//                                    //checkUserPSNVerification();
//                                } else if(psnV.equalsIgnoreCase(Constants.PSN_DELETED)){
//                                    Util.clearDefaults(ListActivityFragment.this.getApplicationContext());
//                                    afterLogout();
//                                }
//                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        };
    }

    private void afterLogout() {
        Intent regIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        finish();
        startActivity(regIntent);
    }

    private void setupEventListener() {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mManager.getEventList(ListActivityFragment.this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        };
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public void showProgress() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgress() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    private void createPushEventObj(String payload) {
        pushEventObject = new EventData();
        try {
            JSONObject jsonObj = new JSONObject(payload);
            if (jsonObj.has("eventId")) {
                String id = ((jsonObj.get("eventId") == null) ? "null" : jsonObj.get("eventId").toString());//jsonObj.get("eventId");
                if (mManager != null) {
                    if ((id != null) && (!id.equalsIgnoreCase("null"))) {
                        if (mManager.getEventObj(id) != null) {
                            pushEventObject = mManager.getEventObj(id);
                        } else {
                            RequestParams param = new RequestParams();
                            param.add("id", id);
                            mManager.postEventById(this, param);
                        }
                    }
                }
            } else {
                pushEventObject.toJson(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchPushEventDetail() {

        startEventDetail(pushEventObject);
    }

    private void startEventDetail(EventData eData) {
        if (eData != null) {
            if (eData.getActivityData() != null) {
                if (eData.getActivityData().getActivitySubtype() != null) {
                    CurrentEventDataHolder ins = CurrentEventDataHolder.getInstance();
                    ins.setData(eData);
                    //launch eventdetailactivity
                    //start new activity for event
                    Intent regIntent = new Intent(this,
                            EventDetailActivity.class);
                    //regIntent.putExtra("userdata", user);
                    startActivity(regIntent);
                    //finish();
                }
            }
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

    private void openProfileDrawer(int gravity) {
        if (gravity == Gravity.RIGHT || gravity == Gravity.LEFT) {
            if (gravity == Gravity.RIGHT && gpAct != null) {
                gpAct.setSelectedGroup();
                mManager.getGroupList(this);
            } else if (gravity == Gravity.LEFT && gpAct != null) {
                setUpConsoleSpinner();
            }
            this.drawerLayout.openDrawer(gravity);
        }
    }

    private void closeProfileDrawer(int gravity) {
        if (gravity == Gravity.RIGHT || gravity == Gravity.LEFT) {
            if (this.drawerLayout.isDrawerOpen(gravity)) {
                this.drawerLayout.closeDrawer(gravity);
                if (gravity == Gravity.RIGHT) {
                    //mManager.getGroupList(this);
                }
            }
        }
    }

    protected void closeAllDrawers() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                closeProfileDrawer(Gravity.LEFT);
            } else if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                closeProfileDrawer(Gravity.RIGHT);
            }
        }
    }

    public void showError(String err) {
        hideProgress();
        findViewById(R.id.loadingImg).setVisibility(View.GONE);
        closeProfileDrawer(Gravity.RIGHT);
        closeProfileDrawer(Gravity.LEFT);
        if (mManager != null && mManager.getDeepLinkEvent() != null) {
            showDeeplinkError(Constants.EVENT_MISSING, mManager.getDeepLinkActivityName(), null, null);
            mManager.setDeepLinkEvent(null, null);
        } else {
            //show timed error message
            //setErrText(err);
        }

//        errLayout.setVisibility(View.GONE);
//        errLayout.setVisibility(View.VISIBLE);
//        errText.setText(err);
//        //timer for error msg visibility
//        errLayout.postDelayed(new Runnable() {
//            public void run() {
//                if(errLayout!=null) {
//                    errLayout.setVisibility(View.GONE);
//                }
//            }
//        }, 5000);
    }

    public void showError(LoginError err) {
        hideProgress();
        findViewById(R.id.loadingImg).setVisibility(View.GONE);
        closeProfileDrawer(Gravity.RIGHT);
        closeProfileDrawer(Gravity.LEFT);
        if (mManager != null && mManager.getDeepLinkEvent() != null) {
            showDeeplinkError(Constants.EVENT_MISSING, mManager.getDeepLinkActivityName(), null, null);
            mManager.setDeepLinkEvent(null, null);
        } else {
            //show timed error message
            setErrText(err);
        }

//        errLayout.setVisibility(View.GONE);
//        errLayout.setVisibility(View.VISIBLE);
//        errText.setText(err);
//        //timer for error msg visibility
//        errLayout.postDelayed(new Runnable() {
//            public void run() {
//                if(errLayout!=null) {
//                    errLayout.setVisibility(View.GONE);
//                }
//            }
//        }, 5000);
    }

    @Override
    public void onResume() {
        checkPrivacyDialoge();
        super.onResume();
        //get latest event list
        if (mManager != null) {
            mManager.setCurrentActivity(this);
            setUpConsoleSpinner();
            getExistingList();
            mManager.getEventList(this);
        }
        registerFirbase();
        registerUserFirebase();
//        if (user!=null && user.getPsnVerify()!=null) {
//            if (!user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
//                //register user listener
//                registerUserFirebase();
//            }
//        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            b = getIntent().getExtras();
            if (b != null && b.containsKey("reportIssue")) {
                //pagerAdapter.
                showGenericError(getString(R.string.report_submitted_header), getString(R.string.report_submitted), "OK", null, Constants.GENERAL_ERROR, null, false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //remove deeplink reference
        if (mManager != null) {
            mManager.setDeepLinkEvent(null, null);
        }
        unregisterFirebase();
        unregisterUserFirebase();
    }

    @Override
    public void onStart() {
        super.onStart();
        //registerReceiver(ReceivefromDeeplink, new IntentFilter("deeplink_flag"));
        registerReceiver(ReceivefromService, new IntentFilter("subtype_flag"));
    }

    private BroadcastReceiver ReceivefromDeeplink = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String eId = intent.getStringExtra("eventId");
            RequestParams param = new RequestParams();
            param.add("id", eId);
            mManager.postEventById(ListActivityFragment.this, param);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        unregisterFirebase();
        //unregisterUserFirebase();
        //unregisterReceiver(ReceivefromDeeplink);
        unregisterReceiver(ReceivefromService);
    }

    private BroadcastReceiver ReceivefromService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TravellerLog.w(this, "ReceivefromService: ");
//            if (mManager.getCurrentActivity() == ListActivityFragment.this) {
//                String subtype = intent.getStringExtra("subtype");
//                boolean playerMsg = intent.getBooleanExtra("playerMessage", false);
//                String msg = intent.getStringExtra("message");
//                notiEventText.setText(subtype);
//                if (playerMsg) {
//                    notiTopText.setText("FIRETEAM MESSAGE");
//                    notiMessage.setVisibility(View.VISIBLE);
//                    notiMessage.setText(msg);
//                } else {
//                    notiEventText.setText(msg);
//                    notiTopText.setText(subtype.toUpperCase());
//                    notiMessage.setVisibility(View.GONE);
//                    mManager.getEventList(ListActivityFragment.this);
//                }
//                notiBar.setVisibility(View.VISIBLE);
//                //put timer to make the notification message gone after 5 seconds
////                notiBar.postDelayed(new Runnable() {
////                    public void run() {
////                        if(notiBar!=null) {
////                            notiBar.setVisibility(View.GONE);
////                        }
////                    }
////                }, 7000);
//            }
        }
    };

    public void setgrpIcon(String groupImageUrl) {
//        if (appIcon != null) {
//            if (groupImageUrl != null) {
//                Util.picassoLoadIcon(this, appIcon, groupImageUrl, R.dimen.group_icon_list_hgt, R.dimen.group_icon_list_width, R.drawable.img_logo_badge_group);
//                appIcon.invalidate();
//            }
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            String console = parent.getItemAtPosition(position).toString();
            if (console != null) {
                switch (console) {
                    case "PlayStation 3":
                        console = "PS3";
                        break;
                    case "PlayStation 4":
                        console = "PS4";
                        break;
                    case "Xbox One":
                        console = "XBOXONE";
                        break;
                    case "Xbox 360":
                        console = "XBOX360";
                        break;

                }
            }
            if (console != null) {
                changeToOtherConsole(console);
                closeProfileDrawer(Gravity.LEFT);
            }
        }
    }

    private void changeToOtherConsole(String console) {
        showProgressBar();
        RequestParams rp_console = new RequestParams();
        rp_console.add("consoleType", console);
        mManager.changeToOtherConsole(ListActivityFragment.this, rp_console);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class PagerAdapter extends FragmentPagerAdapter {
        String tabTitles[] = new String[]{"CURRENT", "UPCOMING"};
        Context context;
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new BlankFragment(ListActivityFragment.this, fragmentCurrentEventList, adActivityData);
                case 1:
                    return new BlankFragment(ListActivityFragment.this, fragmentupcomingEventList, null);
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            //registeredFragments.put(position, fragment);
            return fragment;
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(ListActivityFragment.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setTypeface(Typeface.SANS_SERIF, 1);
            tv.setPadding(0, 25, Util.dpToPx(21, ListActivityFragment.this), 0);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }

    private void updateCurrentFrag() {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
        if (page != null) {
            ((BlankFragment) page).updateCurrListAdapter(fragmentCurrentEventList, adActivityData);
        }

        page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
        if (page != null) {
            ((BlankFragment) page).updateCurrListAdapter(fragmentupcomingEventList, null);
        }

        //
//        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
//        // based on the current position you can then cast the page to the correct
//        // class and call the method:
//        if (viewPager.getCurrentItem() == 0 && page != null) {
//            ((BlankFragment)page).updateCurrListAdapter(fragmentCurrentEventList);
//        }else if (viewPager.getCurrentItem() == 1 && page != null) {
//            ((BlankFragment)page).updateCurrListAdapter(fragmentupcomingEventList);
//        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof EventListNetwork) {
            if (data != null) {
                if(data instanceof LoginError) {
                    showError((LoginError) data);
                } else {
                    //                   if(data instanceof EventListNetwork) {
                    //                       EventList eList = ((EventListNetwork) data).getEventList();
                    eData = new ArrayList<EventData>();

                    adActivityData = new ArrayList<ActivityData>();

                    eData = mManager.getEventListCurrent();
                    //                       ActivityList adList = ((EventListNetwork) data).getActList();
                    adActivityData = mManager.getAdsActivityList();
                    //                   }
//                    eList = (EventList) data;
//                    if (eData != null) {
//                        eData.clear();
//                    }
//                    eData = eList.getEventList();
                    createUpcomingCurrentList(eData);
                    //Checking if current launch happened due to push notification click
                    checkEventIntent();
                    if (pushEventObject != null) {
                        launchPushEventDetail();
                        pushEventObject = null;
                    }
                    updateCurrentFrag();
                }
            }
        } else if (observable instanceof EventRelationshipHandlerNetwork) {
            if (data != null) {
                if (data instanceof LoginError) {
                    showError((LoginError) data);
                } else {
                    EventData ed = new EventData();
                    ed = (EventData) data;
                    if (this.eData != null) {
                        for (int i = 0; i < this.eData.size(); i++) {
                            if (ed.getEventId().equalsIgnoreCase(this.eData.get(i).getEventId())) {
                                if (ed.getMaxPlayer() > 0) {
                                    this.eData.remove(i);
                                    this.eData.add(i, ed);
                                } else {
                                    this.eData.remove(i);
                                }
                                createUpcomingCurrentList(eData);
                                updateCurrentFrag();
                                break;
                            }
                        }
                    }
                    hideProgress();
                    //to update all lists in the background
                    //mManager.getEventList(ListActivityFragment.this);
                }
            }
        } else if (observable instanceof GroupListNetwork) {
            hideProgress();
            if (data != null) {
                if (data instanceof LoginError) {
                    showError((LoginError) data);
                } else {
                    setGroupImageUrl();
                    if (data instanceof UserData) {
                        unregisterFirebase();
                        registerFirbase();
                        mManager.getEventList(this);
                        hideProgress();
                        closeProfileDrawer(Gravity.RIGHT);
                        //mManager.getGroupList(this);
                        //gpAct.setSelectedGroup();
                    } else if (data instanceof GroupData) {
                        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                            gpAct.updateGrpData(data);
                        }
                        //setGroupImageUrl();
                    } else {
                        //setGroupImageUrl();
                        if (gpAct != null) {
                            gpAct.update(data);
                        }
//                        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//                            gpAct.update(data);
//                        }
                    }
                }
            }
        } else if (observable instanceof ResendBungieVerification) {
            hideProgress();
            Toast.makeText(this, "Verification Message Sent to Your Account",
                    Toast.LENGTH_LONG).show();
        } else if (observable instanceof EventByIdNetwork) {
            hideProgressBar();
            if (data != null) {
                if (data instanceof LoginError) {
                    showError((LoginError) data);
                } else {
                    if (pushEventObject != null) {
                        pushEventObject = new EventData();
                        pushEventObject = (EventData) data;
                        startEventDetail(pushEventObject);
                        pushEventObject = null;
                    } else if (mManager.getDeepLinkEvent() != null && (!mManager.getDeepLinkEvent().isEmpty())) {
                        updateExternalDeepLink((EventData) data);
                    } else {
                        startEventDetail((EventData) data);
                    }
                }
            }
        } else if (observable instanceof LogoutNetwork) {
            if(data instanceof LoginError) {
                showError((LoginError) data);
            } else {
                afterLogout();
            }
        } else if (observable instanceof HelmetUpdateNetwork) {
            findViewById(R.id.loadingImg).setVisibility(View.GONE);
            updateUserProfileImageBorder();
            if (data != null) {
                if(data instanceof LoginError) {
                    showError((LoginError) data);
                } else {
                    user = mManager.getUserData();
                    updateUserProfileImage(user.getImageUrl());
                }
            }
        } else if (observable instanceof ChangeCurrentConsoleNetwork) {
            hideProgressBar();
            if (data != null) {
                if(data instanceof LoginError) {
                    showError((LoginError) data);
                } else {
                    user = mManager.getUserData();
                }
            }
            //changeUserProfileBorderColor();
            updateUserProfileImageBorder();
            setUpConsoleSpinner();
            mManager.getEventList(this);
            mManager.getGroupList(this);
        } else if (observable instanceof PrivacyLegalUpdateNetwork) {
            if (dialogPrivacy != null) {
                dialogPrivacy.dismiss();
            }
        } else if (observable instanceof ActivityListNetwork) {
            hideProgressBar();
            if (mManager.getCurrentActivityList() != null && !mManager.getCurrentActivityList().isEmpty()) {
                //start new activity for add event creation
                Intent regIntent = new Intent(ListActivityFragment.this,
                        AddFinalActivity.class);
                //regIntent.putExtra("userdata", user);
                regIntent.putExtra("adcard", true);
                regIntent.putExtra("adCardId", adCardPosition);
                startActivity(regIntent);
                finish();
            }
        }
    }

    private void changeUserProfileBorderColor() {
        if (user != null) {
            if (user.getConsoleType() != null) {
                if (user.getConsoles().size() > 1) {
                    switch (user.getConsoleType()) {
                        case "PS3":
                            userProfileDrawer.setBorderColor(getResources().getColor(R.color.user_profile_border_playstation));
                            userProfile.setBorderColor(getResources().getColor(R.color.user_profile_border_playstation));
                            break;
                        case "PS4":
                            userProfileDrawer.setBorderColor(getResources().getColor(R.color.user_profile_border_playstation));
                            userProfile.setBorderColor(getResources().getColor(R.color.user_profile_border_playstation));
                            break;
                        case "XBOX360":
                            userProfileDrawer.setBorderColor(getResources().getColor(R.color.user_profile_border_xbox));
                            userProfile.setBorderColor(getResources().getColor(R.color.user_profile_border_xbox));
                            break;
                        case "XBOXONE":
                            userProfileDrawer.setBorderColor(getResources().getColor(R.color.user_profile_border_xbox));
                            userProfile.setBorderColor(getResources().getColor(R.color.user_profile_border_xbox));
                            break;
                    }
                }
            }
        }
    }

    private void createUpcomingCurrentList(ArrayList<EventData> currentEventList) {
        fragmentCurrentEventList = new ArrayList<EventData>();
        fragmentupcomingEventList = new ArrayList<EventData>();
        if (currentEventList != null) {
            if (currentEventList.size() > 0) {
                for (int i = 0; i < currentEventList.size(); i++) {
                    if (currentEventList.get(i).getLaunchEventStatus() != null) {
                        if (currentEventList.get(i).getLaunchEventStatus().equalsIgnoreCase(Constants.LAUNCH_STATUS_UPCOMING)) {
                            fragmentupcomingEventList.add(currentEventList.get(i));
                        } else {
                            fragmentCurrentEventList.add(currentEventList.get(i));
                        }
                    }
                }
            }
        }
    }

    private boolean checkDrawerOpen() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT) || drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (legalView.getVisibility() == View.VISIBLE) {
            if (user.getLegal().getPrivacyNeedsUpdate()) {
                legalView.setVisibility(View.GONE);
                checkPrivacyDialoge();
            } else {
                legalView.setVisibility(View.GONE);
                openProfileDrawer(Gravity.LEFT);
            }
        } else if (checkDrawerOpen()) {
            closeAllDrawers();
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    SwipeStackAdapter adapter;
    //View view;
    SwipeDeck cardStack;
    int n = 0;

    public void showNotifications() {
//        if(c instanceof ListActivityFragment) {
//            view = ((ListActivityFragment)c).getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
//        } else if (c instanceof EventDetailActivity) {
//            view = ((EventDetailActivity)c).getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
//            //if(eventNotiList==null) {
//            eventNotiList = new ArrayList<PushNotification>();
//            //}
//            // filter event related message notification
//            if(((EventDetailActivity)c).currEvent!=null) {
//                getEventNotification(((EventDetailActivity)c).currEvent);
//            }
//        }
        //cardStack = null;
        //cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        if (notiList != null && (!notiList.isEmpty())) {
            if (cardStackLayout != null) {
                cardStackLayout.setVisibility(View.VISIBLE);
            }
        }

        ArrayList<PushNotification> localNoti = new ArrayList<PushNotification>();
        if (n == 0) {
            if (notiList != null && (!notiList.isEmpty())) {
                localNoti.add(notiList.get(notiList.size() - 1));
                n++;
            }
        } else {
            if (notiList != null && (!notiList.isEmpty())) {
                localNoti = notiList;
            }
        }
        if (adapter != null) {
            adapter = null;
        }
        if (notiList == null) {
            notiList = new ArrayList<PushNotification>();
        }
        adapter = new SwipeStackAdapter(localNoti, this);
        cardStack.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                checkAndRemoveNoti(ListActivityFragment.this, position, adapter);
                //notiList.remove(position);

            }

            @Override
            public void cardSwipedRight(int position) {
                checkAndRemoveNoti(ListActivityFragment.this, position, adapter);
            }

            @Override
            public void cardsDepleted() {
                if (notiList.size() > 0) {
                    showNotifications();
                } else {
                    cardStackLayout.setVisibility(View.GONE);
                }
                //removeNotifyLayout();
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
    }
}

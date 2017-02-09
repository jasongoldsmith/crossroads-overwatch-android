package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.PushNotification;
import co.crossroadsapp.overwatch.utils.CircularImageView;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseActivity extends FragmentActivity {

    protected RelativeLayout errLayout;
    protected TextView errText;
    private RelativeLayout progress;
    private RelativeLayout deeplinkError;
    private ControlManager mManager = ControlManager.getmInstance();
    protected static ArrayList<PushNotification> notiList;
    protected ArrayList<PushNotification> eventNotiList=new ArrayList<PushNotification>();
    private boolean appBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(ReceivefromService, new IntentFilter("subtype_flag"));
        // todo handle uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                TravellerLog.e("Error"+ Thread.currentThread().getStackTrace()[2],paramThrowable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(appBackground) {
            //tracking resume
            Map<String, String> json = new HashMap<String, String>();
            Util.postTracking(json, null, mManager, Constants.APP_RESUME);
            appBackground = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if(!componentInfo.getPackageName().equalsIgnoreCase(getApplicationContext().getPackageName())){
            appBackground = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ReceivefromService);
    }

    protected void setErrText(String errorText) {
        if(errText==null ){
            errText = (TextView) findViewById(R.id.error_sub);
        }

        if (errLayout==null) {
            errLayout = (RelativeLayout) findViewById(R.id.parent_error_layout);
        }

        // show timed error message
        if(errorText!=null && !errorText.isEmpty()) {
            Util.showErrorMsg(errLayout, errText, errorText);
        }

//        if(errText!=null && errLayout!=null) {
//            errLayout.setVisibility(View.GONE);
//            errLayout.setVisibility(View.VISIBLE);
//            errText.setText(errorText);
//
//            //put timer to make the error message gone after 5 seconds
//            errLayout.postDelayed(new Runnable() {
//                public void run() {
//                    if(errLayout!=null) {
//                        errLayout.setVisibility(View.GONE);
//                    }
//                }
//            }, 5000);
//        }
    }

    public void closeView(View view) {
        if(errLayout!=null) {
            errLayout.setVisibility(View.GONE);
        }
    }

    public void showUnverifiedUserMsg() {
//        final RelativeLayout unverifiedMsg = (RelativeLayout) findViewById(R.id.new_user_msg);
//        CircularImageView userHeader = (CircularImageView) findViewById(R.id.unverified_player);
//        TextView username = (TextView) findViewById(R.id.username_text);
//        ImageView closeBtn = (ImageView) findViewById(R.id.close_btn);
//        CardView verifyBungieBtn = (CardView) findViewById(R.id.verify_btn);
//        TextView notNow = (TextView) findViewById(R.id.not_now);
//        TextView unverifiedBungieText = (TextView) findViewById(R.id.bungie_text);
//
//        UserData userData = mManager.getUserData();
//
//        unverifiedMsg.setVisibility(View.VISIBLE);
//
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                unverifiedMsg.setVisibility(View.GONE);
//                Util.setDefaults("showUnverifiedMsg", "true", mManager.getCurrentActivity());
//            }
//        });
//
//        notNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                unverifiedMsg.setVisibility(View.GONE);
//                Util.setDefaults("showUnverifiedMsg", "true", mManager.getCurrentActivity());
//            }
//        });
//
//        if(userData!=null) {
//            if(userData.getPsnId()!=null) {
//                if(userData.getClanTag()!=null && !userData.getClanTag().isEmpty()) {
//                    username.setText(userData.getPsnId() + " [" + userData.getClanTag() + "]");
//                } else {
//                    username.setText(userData.getPsnId());
//                }
//            }
//
//            if(userData.getImageUrl()!=null) {
//                Util.picassoLoadIcon(mManager.getCurrentActivity(), userHeader, userData.getImageUrl(), R.dimen.activity_profile_icon_hgt, R.dimen.activity_profile_icon_width, R.drawable.profile_image);
//            }
//        }
//
//        unverifiedBungieText.setText(Html.fromHtml((getString(R.string.unverified_bungie_text))));
//        unverifiedBungieText.setMovementMethod(LinkMovementMethod.getInstance());
//
//        unverifiedBungieText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.setDefaults("showUnverifiedMsg", "true", mManager.getCurrentActivity());
//                unverifiedMsg.setVisibility(View.GONE);
//            }
//        });
//
//        verifyBungieBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.setDefaults("showUnverifiedMsg", "true", mManager.getCurrentActivity());
//                unverifiedMsg.setVisibility(View.GONE);
//                String uri = "http://www.bungie.net";
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                mManager.getCurrentActivity().startActivity(browserIntent);
//            }
//        });
    }

    public void showGenericError(String header, String msg, String firstBtnText, String secondBtnText, final int errorType, final RequestParams rp, final boolean keyboardOpen) {
        final RelativeLayout deeplinkErrorG = (RelativeLayout) findViewById(R.id.deeplink_error);
        TextView errMsgG = (TextView) findViewById(R.id.msg);
        TextView btnTextG = (TextView) findViewById(R.id.btn_text);
        CardView btnG = (CardView) findViewById(R.id.add_btn);
        TextView titleG = (TextView) findViewById(R.id.eyesup_text);
        deeplinkErrorG.setVisibility(View.VISIBLE);
        ImageView closeG= (ImageView) findViewById(R.id.close);
        TextView noBtn = (TextView) findViewById(R.id.no_thanks);
        showHidePopupBackground(View.VISIBLE);

        if(keyboardOpen) {
            if(mManager!=null && mManager.getCurrentActivity()!=null)
            ((EventDetailActivity)mManager.getCurrentActivity()).hideKeyboard();
        }

        if(errorType == Constants.GENERAL_ERROR) {
            noBtn.setVisibility(View.GONE);
        } else {
            noBtn.setVisibility(View.VISIBLE);
            noBtn.setText("Cancel");
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deeplinkErrorG.setVisibility(View.GONE);
                    showHidePopupBackground(View.GONE);
                }
            });
        }

        deeplinkErrorG.setVisibility(View.GONE);
        deeplinkErrorG.setVisibility(View.VISIBLE);
        closeG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deeplinkErrorG.setVisibility(View.GONE);
                        showHidePopupBackground(View.GONE);
                        if(mManager!=null && mManager.getCurrentActivity()!=null && mManager.getCurrentActivity() instanceof CrashReport) {
                            ((CrashReport) mManager.getCurrentActivity()).finish();
                        }
                        if(keyboardOpen) {
                            if(mManager!=null && mManager.getCurrentActivity()!=null)
                                ((EventDetailActivity)mManager.getCurrentActivity()).showKeyboard();
                        }
                    }
                });
            }
        });

        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(errorType==Constants.REPORT_COMMENT) {
                    if(mManager!=null && mManager.getCurrentActivity()!=null && mManager.getCurrentActivity() instanceof EventDetailActivity) {
                        showProgressBar();
                        mManager.postCommentReporting(rp!=null?rp:null);
                        mManager.postCommentReporting(null);
                        deeplinkErrorG.setVisibility(View.GONE);
                    }
                } else if(errorType==Constants.REPORT_COMMENT_NEXT){
                    // go to create new event page
                    Intent regIntent = new Intent(getApplicationContext(),
                            CrashReport.class);
                    regIntent.putExtra("reportIssue", true);
                    regIntent.putExtra("requestParams", rp);
                    startActivity(regIntent);
                    deeplinkErrorG.setVisibility(View.GONE);
                    showHidePopupBackground(View.GONE);
                } else {
                    deeplinkErrorG.setVisibility(View.GONE);
                    showHidePopupBackground(View.GONE);
                    if(mManager!=null && mManager.getCurrentActivity()!=null && mManager.getCurrentActivity() instanceof CrashReport) {
                        ((CrashReport) mManager.getCurrentActivity()).finish();
                    }
                    if(keyboardOpen) {
                        if(mManager!=null && mManager.getCurrentActivity()!=null)
                            ((EventDetailActivity)mManager.getCurrentActivity()).showKeyboard();
                    }
                }
            }
        });
        if (header != null && !header.isEmpty()) {
            titleG.setText(header);
        }
        if (msg != null && !msg.isEmpty()) {
            errMsgG.setText(msg);
        }
        if (firstBtnText != null && !firstBtnText.isEmpty()) {
            btnTextG.setAllCaps(true);
            //btnTextG.setTextSize(Util.dpToPx(16, this));
            btnTextG.setText(firstBtnText);
            btnTextG.setPadding(10, 10, 10, 10);
            //btnTextG.setPadding(Util.dpToPx(88, this), Util.dpToPx(12, this),Util.dpToPx(88, this),Util.dpToPx(12, this));
        }
    }

    void showHidePopupBackground(final int visibility)
    {
        Activity activity = (Activity) mManager.getCurrentActivity();
        if( activity != null ) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View error_layout = findViewById(R.id.parent_error_layout);
                    if (error_layout != null) {
                        error_layout.setVisibility(visibility);
                        error_layout.invalidate();
                    }
                    if( visibility == View.GONE )
                    {
                        hideProgressBar();
                    }
                }
            });
        }
    }

    public void showDeeplinkError(int eventFull, final String deepLinkEvent, String deepLinkName, final String clanId) {
        deeplinkError = (RelativeLayout) findViewById(R.id.deeplink_error);
        TextView errMsg = (TextView) findViewById(R.id.msg);
        TextView btnText = (TextView) findViewById(R.id.btn_text);
        CardView btn = (CardView) findViewById(R.id.add_btn);
        deeplinkError.setVisibility(View.VISIBLE);
        TextView noBtn = (TextView) findViewById(R.id.no_thanks);
        ImageView close= (ImageView) findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deeplinkError.setVisibility(View.GONE);
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deeplinkError.setVisibility(View.GONE);
            }
        });
        switch(eventFull) {
            case 2:
                errMsg.setText("Sorry, that " +deepLinkEvent+ " is no longer available. Would you like to add one of your own?");
                btnText.setText("ADD THIS ACTIVITY");
                btnText.setPadding(10, 10, 10, 10);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // go to create new event page
                        Intent regIntent = new Intent(getApplicationContext(),
                                AddNewActivity.class);
                        //regIntent.putExtra("userdata", mManager.getUserData());
                        startActivity(regIntent);
                    }
                });
                break;
            case 1:
                errMsg.setText("You’ll need to be in the " +deepLinkEvent+ " group to join " + deepLinkName+ ". Request to join?");
                btnText.setText("VIEW GROUP ON BUNGIE.NET");
                btnText.setPadding(10, 10, 10, 10);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = "http://www.bungie.net";
                        if(clanId!=null && !clanId.isEmpty()) {
                            uri = "http://www.bungie.net/en/clan/"+clanId;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        mManager.getCurrentActivity().startActivity(browserIntent);
                    }
                });
                break;
            case 3:
                errMsg.setText(getResources().getString(R.string.deeplink_full_err));
                btnText.setText("YES");
                btnText.setPadding(10, 10, 10, 10);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // go to create new event page
                        Intent regIntent = new Intent(getApplicationContext(),
                                AddNewActivity.class);
                        //regIntent.putExtra("userdata", mManager.getUserData());
                        startActivity(regIntent);
                    }
                });
                break;
            case 4:
                errMsg.setText("You'll need a "+deepLinkName+" linked to your Bungie account to join that activity from "+deepLinkEvent+ ".");
                btnText.setText(getString(R.string.ok_btn));
                btnText.setPadding(10, 10, 10, 10);
                noBtn.setText("");
                btnText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to create new event page
                    Intent regIntent = new Intent(getApplicationContext(),
                            UpdateConsoleActivity.class);
                    //regIntent.putExtra("userdata", mManager.getUserData());
                    startActivity(regIntent);
                }
                });
                break;
        }
    }

    public void hideDeeplinkError() {
        if (deeplinkError != null) {
            deeplinkError.setVisibility(View.GONE);
        }
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private ArrayList<PushNotification> data;
        private Context context;

        public SwipeStackAdapter(ArrayList<PushNotification> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if(v == null) {
//                Display display = getWindowManager().getDefaultDisplay();
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                // normally use a viewholder
                v = inflater.inflate(R.layout.base_notification_card, null);
            }
                if(data!=null && data.get(position)!=null) {
                    final String id = data.get(position).geteId();
                    String name = data.get(position).geteName();
                    String msg = data.get(position).getMessage();
                    boolean typeM = data.get(position).getTypeMessage();

                    final String console = data.get(position).getEventConsole();
                    final String grpId = data.get(position).getEventClanId();
                    String grpName = data.get(position).getEventClanName();
                    String grpImage = data.get(position).getEventClanImageUrl();
                    String msngrConsoleId = data.get(position).getMessengerConsoleId();
                    String msngrImage = data.get(position).getMessengerImageUrl();

                    //CardView card = (CardView) v.findViewById(R.id.base_test_card);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (id != null) {
                                //tracking OS pushnotification initiation
                                Map<String, Boolean> json = new HashMap<>();
                                json.put("inApp", true);
                                Util.postTracking(json, null, mManager, Constants.APP_PUSHNOTIFICATION);
                                goToDetail(id, console, grpId);
                            }
                        }
                    });
                    TextView notiEventText = (TextView) v.findViewById(R.id.noti_text);
                    TextView notiTopText = (TextView) v.findViewById(R.id.noti_toptext);
                    TextView notiTopSubText = (TextView) v.findViewById(R.id.noti_topsubtext);
                    ImageView grpImageView = (ImageView) v.findViewById(R.id.noti_icon);
                    CircularImageView msngrImageView = (CircularImageView) v.findViewById(R.id.player_msg_pic);

                    if (typeM) {
                        if(msngrConsoleId!=null) {
                            notiTopText.setText(msngrConsoleId);
                        }
//                        if(msngrImage!=null) {
//                            Picasso.with(getApplicationContext())
//                                    .load(msngrImage)
//                                    .placeholder(R.drawable.icon_alert)
//                                    .fit().centerCrop()
//                                    .into(msngrImageView);
//                        }
//                        grpImageView.setVisibility(View.GONE);
//                        msngrImageView.setVisibility(View.VISIBLE);
                        notiTopSubText.setVisibility(View.GONE);
                        if(msg!=null) {
                            notiEventText.setText(data.get(position).getMessage());
                        }
                    } else {
//                        grpImageView.setVisibility(View.VISIBLE);
//                        msngrImageView.setVisibility(View.GONE);
                        if (name!=null && !name.isEmpty()) {
                            notiTopSubText.setVisibility(View.VISIBLE);
                            notiTopSubText.setText(name.toUpperCase());
                        }
                        if (console!=null && grpName!=null) {
                            String first = console +": "+ grpName;
                            notiTopText.setText(first);
                        }
//                        if(grpImage!=null && (!grpImage.isEmpty())) {
//                            if(getApplicationContext()!=null) {
//                                Picasso.with(getApplicationContext())
//                                        .load(grpImage)
//                                        .placeholder(R.drawable.icon_alert)
//                                        .fit().centerCrop()
//                                        .into(grpImageView);
//                            }
//                        }
                    }
                    if(msg!=null) {
                        notiEventText.setText(data.get(position).getMessage());
                    }
                }
            //}
            return v;
        }
    }

    protected void goToDetail(String s, String console, String clanId) {

    }

    private BroadcastReceiver ReceivefromService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //          if (mManager.getCurrentActivity() == ListActivityFragment) {
            PushNotification noti = new PushNotification();
            if(intent.hasExtra("subtype")) {
                String subtype = intent.getStringExtra("subtype");
                noti.seteName(subtype);
            }
            if(intent.hasExtra("playerMessage")) {
                boolean playerMsg = intent.getBooleanExtra("playerMessage", false);
                noti.setTypeMessage(playerMsg);
            }
            if(intent.hasExtra("eventId")) {
                String eventId = intent.getStringExtra("eventId");
                noti.seteId(eventId);
            }
            if(intent.hasExtra("eventUpdated")) {
                String eventUpdated = intent.getStringExtra("eventUpdated");
                noti.seteUpdated(eventUpdated);
            }
            if(intent.hasExtra("message")) {
                String msg = intent.getStringExtra("message");
                noti.setMessage(msg);
            }
            if(intent.hasExtra("eventClanId")) {
                String eventClanId = intent.getStringExtra("eventClanId");
                noti.setEventClanId(eventClanId);
            }
            if(intent.hasExtra("eventClanName")) {
                String eventClanName = intent.getStringExtra("eventClanName");
                noti.setEventClanName(eventClanName);
            }
            if(intent.hasExtra("eventClanImageUrl")) {
                String eventClanImageUrl = intent.getStringExtra("eventClanImageUrl");
                noti.setEventClanImageUrl(eventClanImageUrl);
            }
            if(intent.hasExtra("eventConsole")) {
                String eventConsole = intent.getStringExtra("eventConsole");
                noti.setEventConsole(eventConsole);
            }
            if(intent.hasExtra("messengerConsoleId")) {
                String messengerConsoleId = intent.getStringExtra("messengerConsoleId");
                noti.setMessengerConsoleId(messengerConsoleId);
            }
            if(intent.hasExtra("messengerImageUrl")) {
                String messengerImageUrl = intent.getStringExtra("messengerImageUrl");
                noti.setMessengerImageUrl(messengerImageUrl);
            }

            if (notiList == null) {
                notiList = new ArrayList<PushNotification>();
            }
            notiList.add(noti);
            //
            Context act = mManager.getCurrentActivity();
            if (act instanceof EventDetailActivity) {
                if(noti.getTypeMessage()) {
                    ((EventDetailActivity) act).showNotifications();
                }
            } else if (act instanceof ListActivityFragment) {
                ((ListActivityFragment) act).showNotifications();
            }
        }
    };

    protected void checkAndRemoveNoti(Context c, int position, SwipeStackAdapter adapter) {
        if(c!=null) {
            if(position>=0) {
                if (c instanceof ListActivityFragment) {
                    if (notiList != null && notiList.get(position) != null) {
                        notiList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (eventNotiList != null && eventNotiList.get(position) != null) {
                        if (eventNotiList.get(position) != null) {
                            notiList.remove(eventNotiList.get(position));
                            //removeEventNotiFromNotiList(eventNotiList.get(position).geteId());
                            eventNotiList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    protected void removeNotiById(String Id) {
        if(notiList!=null) {
            for (int y=0; y<notiList.size();y++) {
                if (notiList.get(y).geteId()!=null) {
                    if(notiList.get(y).geteId().equalsIgnoreCase(Id)) {
                        notiList.remove(y);
                        y--;
                    }
                }
            }
        }
    }

    protected void getEventNotification(EventData currEvent) {
        if(currEvent.getEventId()!=null) {
            String id = currEvent.getEventId();
            if (notiList!=null){
                for (int i=0; i<notiList.size(); i++) {
                    if (notiList.get(i)!=null && notiList.get(i).geteId()!=null) {
                        if(notiList.get(i).geteId().equalsIgnoreCase(id)) {
                            if(notiList.get(i).getTypeMessage()){
                                eventNotiList.add(notiList.get(i));
                            } else {
                                notiList.remove(i);
                                i--;
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeNotifyLayout() {

    }

    protected void showProgressBar() {
        if(progress == null) {
            progress = (RelativeLayout) findViewById(R.id.progress_base_layout);
        }

        if(progress!=null) {
            hideProgressBar();
            progress.setVisibility(View.VISIBLE);
        }
    }

    protected void showBungieProgressBar() {
        if(progress == null) {
            progress = (RelativeLayout) findViewById(R.id.progress_base_layout);
        }
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            progress.setBackgroundDrawable( getResources().getDrawable(R.drawable.bungie_error_background) );
        } else {
            progress.setBackground( getResources().getDrawable(R.drawable.bungie_error_background));
        }

        if(progress!=null) {
            hideProgressBar();
            progress.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgressBar() {
        if(progress!=null) {
            progress.setVisibility(View.GONE);
        }
    }
}

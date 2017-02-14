package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.ActivityList;
import co.crossroadsapp.overwatch.data.AppVersion;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.EventList;
import co.crossroadsapp.overwatch.data.GroupData;
import co.crossroadsapp.overwatch.data.GroupList;
import co.crossroadsapp.overwatch.data.InvitationLoginData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.ActivityListNetwork;
import co.crossroadsapp.overwatch.network.AddCommentNetwork;
import co.crossroadsapp.overwatch.network.AddNewConsoleNetwork;
import co.crossroadsapp.overwatch.network.BungieMessageNetwork;
import co.crossroadsapp.overwatch.network.BungieUserNetwork;
import co.crossroadsapp.overwatch.network.ChangeCurrentConsoleNetwork;
import co.crossroadsapp.overwatch.network.ConfigNetwork;
import co.crossroadsapp.overwatch.network.EventByIdNetwork;
import co.crossroadsapp.overwatch.network.EventListNetwork;
import co.crossroadsapp.overwatch.network.EventRelationshipHandlerNetwork;
import co.crossroadsapp.overwatch.network.EventSendMessageNetwork;
import co.crossroadsapp.overwatch.network.ForgotPasswordNetwork;
import co.crossroadsapp.overwatch.network.GetVersion;
import co.crossroadsapp.overwatch.network.GroupListNetwork;
import co.crossroadsapp.overwatch.network.HelmetUpdateNetwork;
import co.crossroadsapp.overwatch.network.InvitePlayerNetwork;
import co.crossroadsapp.overwatch.network.LoginNetwork;
import co.crossroadsapp.overwatch.network.LogoutNetwork;
import co.crossroadsapp.overwatch.network.PrivacyLegalUpdateNetwork;
import co.crossroadsapp.overwatch.network.ReportCommentNetwork;
import co.crossroadsapp.overwatch.network.ReportCrashNetwork;
import co.crossroadsapp.overwatch.network.ResendBungieVerification;
import co.crossroadsapp.overwatch.network.TrackingNetwork;
import co.crossroadsapp.overwatch.network.VerifyConsoleIDNetwork;
import co.crossroadsapp.overwatch.network.postGcmNetwork;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.ErrorShowDialog;
import co.crossroadsapp.overwatch.utils.TravellerDialogueHelper;
import co.crossroadsapp.overwatch.utils.Util;
import co.crossroadsapp.overwatch.utils.Version;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.mixpanel.android.mpmetrics.MPConfig;
import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by sharmha on 2/29/16.
 */
public class ControlManager implements Observer {

    private EventListNetwork eventListNtwrk;
    private EventRelationshipHandlerNetwork eventRelationshipNtwrk;
    private ActivityListNetwork activityListNetwork;
    private postGcmNetwork gcmTokenNetwork;
    private LoginNetwork loginNetwork;
    private LogoutNetwork logoutNetwork;
    private ResendBungieVerification resendBungieMsg;
    private GetVersion getVersionNetwork;
    private EventSendMessageNetwork eventSendMsgNetwork;
    private ReportCrashNetwork crashReportNetwork;
    private UserData user;

    private Version checkVersion;

    private Activity mCurrentAct;

    private static final String TAG = ControlManager.class.getSimpleName();

    private EventList eList;
    private GroupList gList;
    private ArrayList<EventData> eData;
    private ArrayList<GroupData> gData;
    private ArrayList<ActivityData> activityList;
    private ArrayList<ActivityData> raidActivityList;
    private ArrayList<ActivityData> crucibleActivityList;

    private static ControlManager mInstance;
    private ForgotPasswordNetwork forgotPasswordNetwork;
    private GroupListNetwork groupListNtwrk;
    private VerifyConsoleIDNetwork verifyConsoleNetwork;
    private EventByIdNetwork eventById;
    private HelmetUpdateNetwork helmetUpdateNetwork;
    private String deepLinkEvent;
    private ArrayList<ActivityData> adActivityData;
    private ArrayList<String> consoleList;
    private AddNewConsoleNetwork addConsoleNetwork;
    private ChangeCurrentConsoleNetwork changeCurrentConsoleNetwork;
    private String deepLinkActivityName;
    private PrivacyLegalUpdateNetwork legalPrivacyNetwork;
    private AddCommentNetwork addCommentsNetwork;
    private TrackingNetwork trackingNetwork;
    private AsyncHttpClient client;
    private Boolean showFullEvent;
    private ReportCommentNetwork reportCommentNetwork;
    private InvitePlayerNetwork invitePlayersNetwork;
    private BungieUserNetwork bugieGetUser;
    private ConfigNetwork getConfigNetwork;
    private String bungieCurrentUserUrl;
    private String psnURL;
    private String xboxURL;
    private BungieMessageNetwork bungieMsgNtwrk;

    public ControlManager() {
    }

    public static ControlManager getmInstance() {
        if (mInstance==null) {
            mInstance = new ControlManager();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    public void setUserdata(UserData ud) {
        if(user!=null && user.getUser()!=null){

        }else {
            user = new UserData();
        }
        user = ud;
    }

    public UserData getUserData(){
        return user;
    }

    public ArrayList<EventData> getEventListCurrent() {
        if (eData!= null && !eData.isEmpty()) {
            return eData;
        }
        return null;
    }

    public void setEventList(EventList el) {
        if(eData==null) {
            eData = new ArrayList<EventData>();
        } else {
            eData.clear();
        }
        eData = el.getEventList();
    }

    public void setadList(ActivityList al) {
        if(adActivityData==null) {
            adActivityData = new ArrayList<ActivityData>();
        }
        adActivityData = al.getActivityList();
    }

    public ArrayList<ActivityData> getAdsActivityList() {
        if (adActivityData!= null && !adActivityData.isEmpty()) {
            return adActivityData;
        }
        return null;
    }

    public void getEventList(ListActivityFragment activity) {
        try {
            if(activity!=null) {
                eventListNtwrk = new EventListNetwork(activity);
                eventListNtwrk.addObserver(activity);
                //eventListNtwrk.addObserver(this);
                eventListNtwrk.getEvents(Constants.EVENT_FEED);
                //todo commenting out get android version for google release
                getAndroidVersion(activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resendBungieMsg(ListActivityFragment activity) {
        try {
        resendBungieMsg = new ResendBungieVerification(activity);
        resendBungieMsg.addObserver(activity);
        resendBungieMsg.resendBungieMsgVerify();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    }

    public void getEventList() {
        try {
            eventListNtwrk = new EventListNetwork(mCurrentAct);
            eventListNtwrk.addObserver(this);
            eventListNtwrk.getEvents(Constants.EVENT_FEED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPublicEventList(MainActivity mainActivity) {
        try {
            eventListNtwrk = new EventListNetwork(mCurrentAct);
            eventListNtwrk.addObserver(this);
            if(mainActivity !=null) {
                eventListNtwrk.addObserver(mainActivity);
            }
            eventListNtwrk.getEvents(Constants.PUBLIC_EVENT_FEED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getGroupList(ListActivityFragment act) {
        try {
            if (act != null) {
                groupListNtwrk = new GroupListNetwork(act);
                groupListNtwrk.addObserver(this);
                groupListNtwrk.addObserver(act);
            } else {
                groupListNtwrk = new GroupListNetwork(mCurrentAct);
                groupListNtwrk.addObserver(this);
            }
            groupListNtwrk.getGroups();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postSetGroup(ListActivityFragment act, RequestParams params) {
        try{
        groupListNtwrk = new GroupListNetwork(act);
            groupListNtwrk.addObserver(this);
        groupListNtwrk.addObserver(act);
        groupListNtwrk.postSelectGroup(params);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    }

    public void postMuteNoti(ListActivityFragment act, RequestParams params) {
        try{
            if(groupListNtwrk==null) {
                groupListNtwrk = new GroupListNetwork(act);
            }
            groupListNtwrk.addObserver(act);
            groupListNtwrk.postMuteNotification(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public EventData getEventObj(String eId) {
        if(eData!=null && (!eData.isEmpty())) {
            for (int i=0; i<eData.size(); i++) {
                if (eData.get(i)!=null) {
                    if(eData.get(i).getEventId()!=null) {
                        if(eData.get(i).getEventId().equalsIgnoreCase(eId)) {
                            EventData event = new EventData();
                            event = eData.get(i);
                            return event;
                        }
                    }
                }
            }
        }

        return null;
    }

    public void postJoinEvent(Activity activity, RequestParams params) {
        try {
            eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
            if (activity instanceof EventDetailActivity){
                eventRelationshipNtwrk.addObserver((EventDetailActivity)activity);
            } else if (activity instanceof ListActivityFragment){
                eventRelationshipNtwrk.addObserver((ListActivityFragment)activity);
            }
            eventRelationshipNtwrk.addObserver(this);
            eventRelationshipNtwrk.postJoin(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postEventMessage(EventDetailActivity ea, String msg, String id, String eventId){

        try {
            eventSendMsgNetwork = new EventSendMessageNetwork(ea);
            eventSendMsgNetwork.addObserver(ea);
            RequestParams rp = new RequestParams();
            rp.put("id", id);
            rp.put("message", msg);
            rp.put("eId", eventId);
            eventSendMsgNetwork.postEventMsg(rp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postGetActivityList(Activity c, RequestParams params) {
        try {
            if(c instanceof AddNewActivity) {
                activityListNetwork = new ActivityListNetwork((AddNewActivity)c);
                //activityListNetwork.addObserver(this);
                activityListNetwork.addObserver((AddNewActivity)c);
            } else if (c instanceof ListActivityFragment) {
                activityListNetwork = new ActivityListNetwork((ListActivityFragment)c);
                //activityListNetwork.addObserver(this);
                activityListNetwork.addObserver((ListActivityFragment)c);
            }
            if(activityList!=null) {
                activityList.clear();
            }
            activityListNetwork.postGetActivityList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void postGetActivityList(ListActivityFragment c) {
//        try {
//            activityListNetwork = new ActivityListNetwork(c);
//            activityListNetwork.addObserver(this);
//            activityListNetwork.postGetActivityList();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void postUnJoinEvent(ListActivityFragment activity, RequestParams params) {
        try {
            eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
            eventRelationshipNtwrk.addObserver(activity);
            eventRelationshipNtwrk.addObserver(this);
            eventRelationshipNtwrk.postUnJoin(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postUnJoinEvent(EventDetailActivity activity, RequestParams params) {
        try {
            eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
            eventRelationshipNtwrk.addObserver(activity);
            eventRelationshipNtwrk.addObserver(this);
            eventRelationshipNtwrk.postUnJoin(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ActivityData> getCustomActivityList(String tempActivityName) {
        if (this.activityList!= null) {
            raidActivityList = new ArrayList<ActivityData>();

            if(tempActivityName.equalsIgnoreCase(Constants.ACTIVITY_FEATURED)) {
                for (int i = 0; i < activityList.size(); i++) {
                    if (activityList.get(i).getActivityFeature()) {
                        raidActivityList.add(activityList.get(i));
                    }
                }
            } else {
                for (int i = 0; i < activityList.size(); i++) {
                    if (activityList.get(i).getActivityType().equalsIgnoreCase(tempActivityName)) {
                        raidActivityList.add(activityList.get(i));
                    }
                }
            }
            return this.raidActivityList;
        }
        return null;
    }

    public ArrayList<ActivityData> getCheckpointActivityList(String subtype, String diff) {
        if (this.activityList!= null) {
            ArrayList<ActivityData> checkpointActivityList = new ArrayList<ActivityData>();
            for (int i=0; i<activityList.size();i++) {
                if(diff!=null) {
                    if (activityList.get(i).getActivitySubtype().equalsIgnoreCase(subtype) && activityList.get(i).getActivityDifficulty().equalsIgnoreCase(diff)) {
                        checkpointActivityList.add(activityList.get(i));
                    }
                }else {
                    if (activityList.get(i).getActivitySubtype().equalsIgnoreCase(subtype)) {
                        checkpointActivityList.add(activityList.get(i));
                    }
                }
            }
            return checkpointActivityList;
        }

        return null;
    }

    public ArrayList<ActivityData> getCurrentActivityList() {
        return activityList;
    }

    public void postLogin(Activity activity, RequestParams params, int postId) {
        try {
            loginNetwork = new LoginNetwork(activity);
            loginNetwork.addObserver(this);
            if (postId== Constants.LOGIN) {
                if (activity instanceof LoginActivity) {
                    loginNetwork.addObserver((LoginActivity) activity);
                } else if (activity instanceof MainActivity) {
                    loginNetwork.addObserver((MainActivity) activity);
                }
                loginNetwork.doSignup(params);
            } else if(postId == Constants.REGISTER) {
                loginNetwork.addObserver((RegisterActivity)activity);
                loginNetwork.doRegister(params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void verifyBungieId(ConsoleSelectionActivity activity, RequestParams params) {
        try {
            verifyConsoleNetwork = new VerifyConsoleIDNetwork(activity);
            verifyConsoleNetwork.addObserver(activity);
            verifyConsoleNetwork.doVerifyConsoleId(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postResetPassword(ForgotLoginActivity activity, RequestParams params) {
        try {
            forgotPasswordNetwork = new ForgotPasswordNetwork(activity);
            forgotPasswordNetwork.addObserver(activity);
            forgotPasswordNetwork.doResetPassword(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postLogout(ListActivityFragment act, RequestParams params) {
        try {
            logoutNetwork = new LogoutNetwork(act);
            //logoutNetwork.addObserver(this);
            logoutNetwork.addObserver(act);
            logoutNetwork.doLogout(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postLogout(MainActivity act, RequestParams params) {
        try {
            logoutNetwork = new LogoutNetwork(act);
            //logoutNetwork.addObserver(this);
            logoutNetwork.addObserver(act);
            logoutNetwork.doLogout(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postHelmet(ListActivityFragment act) {
        try {
            helmetUpdateNetwork = new HelmetUpdateNetwork(act);
            helmetUpdateNetwork.addObserver(act);
            helmetUpdateNetwork.getHelmet();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postEventById(Activity listActivityFragment, RequestParams param) {
        try {
            eventById = new EventByIdNetwork(listActivityFragment);
            if(listActivityFragment instanceof ListActivityFragment) {
                eventById.addObserver((ListActivityFragment)listActivityFragment);
            } else if(listActivityFragment instanceof EventDetailActivity) {
                eventById.addObserver((EventDetailActivity)listActivityFragment);
            }
            eventById.getEventById(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showErrorDialogue(String err) {
//        if(err==null){
//            //err = "Request failed. Please wait a few seconds and refresh.";
//        }
        //if(err!=null) {
            if (this.mCurrentAct != null) {
                if (mCurrentAct instanceof SplashActivity) {
                    ((SplashActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof LoginActivity) {
                    ((LoginActivity) mCurrentAct).showError(err, null);
                } else if (mCurrentAct instanceof RegisterActivity) {
                    ((RegisterActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof ListActivityFragment) {
                    ((ListActivityFragment) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof AddFinalActivity) {
                    ((AddFinalActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof EventDetailActivity) {
                    ((EventDetailActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof ForgotLoginActivity) {
                    ((ForgotLoginActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof ConsoleSelectionActivity) {
                    ((ConsoleSelectionActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof ChangePassword) {
                    ((ChangePassword) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof MainActivity) {
                    ((MainActivity) mCurrentAct).showError(err, null);
                } else if (mCurrentAct instanceof UpdateConsoleActivity) {
                    ((UpdateConsoleActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof AddFinalActivity) {
                    ((AddFinalActivity) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof CrashReport) {
                    ((CrashReport) mCurrentAct).showError(err);
                } else if (mCurrentAct instanceof ChangeEmail) {
                    ((ChangeEmail) mCurrentAct).showError(err);
                }
            }
        //}
    }

    public void getAndroidVersion(ListActivityFragment activity) {
//        getVersionNetwork = new GetVersion(activity);
//        getVersionNetwork.addObserver(this);
//        try {
//            getVersionNetwork.getAppVer();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void getAndroidVersion(MainActivity activity) {
//        getVersionNetwork = new GetVersion(activity);
//        getVersionNetwork.addObserver(activity);
//        getVersionNetwork.addObserver(this);
//        try {
//            getVersionNetwork.getAppVer();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public Intent decideToOpenActivity(Intent contentIntent) {

        Intent regIntent;
        regIntent = new Intent(mCurrentAct.getApplicationContext(),
                ListActivityFragment.class);
        if (contentIntent != null ) {
            regIntent.putExtra("eventIntent", contentIntent);
        }
//        if (contentIntent != null ) {
//            regIntent = new Intent(mCurrentAct.getApplicationContext(),
//                    ListActivityFragment.class);
//            regIntent.putExtra("eventIntent", contentIntent);
//        } else {
//            if(user.getPsnVerify()!=null && user.getPsnVerify().equalsIgnoreCase(Constants.PSN_VERIFIED)) {
//                if(user.getClanId()!=null && user.getClanId().equalsIgnoreCase(Constants.CLAN_NOT_SET)) {
//                    regIntent = new Intent(mCurrentAct.getApplicationContext(),
//                            ListActivityFragment.class);
//                } else {
//                    if(this.eData!=null && (!this.eData.isEmpty())) {
//                        regIntent = new Intent(mCurrentAct.getApplicationContext(),
//                                ListActivityFragment.class);
//                    } else {
//                        regIntent = new Intent(mCurrentAct.getApplicationContext(),
//                                CreateNewEvent.class);
//                    }
//                }
//            } else {
//                regIntent = new Intent(mCurrentAct.getApplicationContext(),
//                        CreateNewEvent.class);
//            }
//        }
        return regIntent;
    }

    public void setCurrentActivity(Activity act) {
        this.mCurrentAct = act;
    }

    public Context getCurrentActivity() {
        if (this.mCurrentAct!=null) {
            return this.mCurrentAct;
        }
        return null;
    }
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof EventListNetwork) {
            eData = new ArrayList<EventData>();
            if (data instanceof EventListNetwork) {
                EventList eList = ((EventListNetwork) data).getEventList();
                eData = eList.getEventList();
                ActivityList adList = ((EventListNetwork) data).getActList();
                adActivityData = adList.getActivityList();
            }else {
                EventList eList = (EventList) data;
                eData = eList.getEventList();
            }
        } else if (observable instanceof LogoutNetwork){
            //mCurrentAct.finish();
        } else if (observable instanceof EventRelationshipHandlerNetwork) {
            EventData ed = (EventData) data;
            boolean eventExist=true;
            if (eData!= null) {
                for (int i=0; i<eData.size();i++) {
                    if (ed.getEventId().equalsIgnoreCase(eData.get(i).getEventId())) {
                        eventExist = false;
                        if (ed.getMaxPlayer()>0) {
                            eData.remove(i);
                            eData.add(i, ed);
                        } else {
                            eData.remove(i);
                        }
                        break;
                    }
                }
                if(eventExist) {
                    eData.add(ed);
                }
            }
        } else if (observable instanceof ActivityListNetwork) {
            updateActivityList(data!=null?data:null);
        } else if(observable instanceof GetVersion) {
            AppVersion ver = (AppVersion) data;
            if (this.mCurrentAct!=null) {
                String currVer = Util.getApplicationVersionCode(this.mCurrentAct);
                String latestVer = ver.getVersion();
                Version currVersion = new Version(currVer);
                Version latestVersion = new Version(latestVer);
                if (latestVersion.compareTo(currVersion)>0){
                    AlertDialog.Builder builder = TravellerDialogueHelper.createConfirmDialogBuilder(this.mCurrentAct, "New Version Available", "A new version of Crossroads is available for download", "Download", "Later", null);

                    builder.setPositiveButton(R.string.download_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Util.getAppDownloadLink()));
                            mCurrentAct.startActivity(browserIntent);
                        }
                    }).setNegativeButton(R.string.later_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if(mCurrentAct instanceof MainActivity) {
                                getAndroidVersion((MainActivity) mCurrentAct);
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    ErrorShowDialog.show(dialog);
                }

            }
        } else if(observable instanceof GroupListNetwork) {
            if(data instanceof UserData) {
                setUserdata((UserData) data);
            } else if(data instanceof GroupData) {
                //
            } else {
                if(gData!=null) {
                    gData.clear();
                } else {
                    gData = new ArrayList<GroupData>();
                }
                gData = (ArrayList<GroupData>) data;
            }
        } else if(observable instanceof LoginNetwork) {
            if (data!=null) {
                getEventList();
                getGroupList(null);
            }
        } else if(observable instanceof BungieUserNetwork) {
            if(data!=null) {
                try {
                    String platform = getCurrentPlatform();
                    if(platform!=null) {
                        try {
                            HashMap<String,Object> map =
                                    new ObjectMapper().readValue(data.toString(), HashMap.class);
                            RequestParams rp = new RequestParams();
                            rp.put("bungieResponse", map);
                            rp.put("consoleType", platform);
                            rp.put("bungieURL", getBungieCurrentUserUrl()!=null?getBungieCurrentUserUrl():Constants.BUGIE_CURRENT_USER);
                            if(mCurrentAct instanceof MainActivity) {
                                loginNetwork = new LoginNetwork(mCurrentAct);
                                InvitationLoginData notificationObj = ((MainActivity) mCurrentAct).getInvitationObject();
                                if(notificationObj!=null) {
                                    rp.put("invitation", notificationObj.getRp());
                                }
                                loginNetwork.addObserver(this);
                                loginNetwork.addObserver(((MainActivity) mCurrentAct));
                                loginNetwork.doSignup(rp);
                            }
                        } catch (JsonGenerationException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//        } else if(observable instanceof BungieMessageNetwork) {
//            if(data!=null) {
//                try {
//                    RequestParams rp = new RequestParams();
//                    if(((BungieMessageNetwork) observable).getId()!=null) {
//                        rp.put("responseType", "sendBungieMessage");
//                        HashMap<String,Object> map = new ObjectMapper().readValue(data.toString(), HashMap.class);
//                        rp.put("gatewayResponse", map);
//                        Map<String, String> map2 = new HashMap();
//                        map2.put("pendingEventInvitationId", ((BungieMessageNetwork) observable).getId());
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void updateActivityList(Object data) {
        if(data!=null) {
            activityList = new ArrayList<ActivityData>();
            ActivityList al = (ActivityList) data;
            activityList = al.getActivityList();
        }
    }

    public ArrayList<GroupData> getCurrentGroupList() {

        if(gData!=null) {
            return gData;
        }
        return null;
    }

    public GroupData getGroupObj(String id) {
        if (gData!=null) {
            for (int i =0; i<gData.size(); i++) {
                if(gData.get(i)!=null) {
                    if (gData.get(i).getGroupId()!=null) {
                        if (gData.get(i).getGroupId().equalsIgnoreCase(id)) {
                            return gData.get(i);
                        }
                    }
                }
            }
        }
        return null;
    }

    public void postCreateEvent(String activityId, String creator_id, int minP, int maxP, String dateTime, Context activity) {
        ArrayList<String> players = new ArrayList<String>();
        players.add(creator_id);

        RequestParams rp = new RequestParams();
        rp.put("eType", activityId);
        rp.put("minPlayers", minP);
        rp.put("maxPlayers", maxP);
        rp.put("creator", creator_id);
        rp.put("players", players);
        rp.put("launchDate", dateTime);

        try {
            eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
            eventRelationshipNtwrk.addObserver(this);
            if(activity instanceof AddFinalActivity) {
                eventRelationshipNtwrk.addObserver((AddFinalActivity)activity);
            }
            eventRelationshipNtwrk.postCreateEvent(rp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postGCMToken(String token, Context context) {

        RequestParams rp = new RequestParams();
        rp.put("deviceToken", token);

        gcmTokenNetwork = new postGcmNetwork(context);
        try {
            gcmTokenNetwork.postGcmToken(rp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postTracking(RequestParams rp, Context context) {
        trackingNetwork = new TrackingNetwork(context);
        try {
            if(context instanceof SplashActivity)
            trackingNetwork.addObserver((SplashActivity)context);
            trackingNetwork.postTracking(rp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void postCrash(CrashReport c, RequestParams requestParams, int report_type) {
        try {
            crashReportNetwork = new ReportCrashNetwork(c);
            crashReportNetwork.addObserver(c);
            crashReportNetwork.doCrashReport(requestParams, report_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void postChangePassword(ChangePassword activity, RequestParams params) {
        try {
            forgotPasswordNetwork = new ForgotPasswordNetwork(activity);
            forgotPasswordNetwork.addObserver(activity);
            forgotPasswordNetwork.doChangePassword(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postComments(EventDetailActivity activity, RequestParams params) {
        try {
            addCommentsNetwork = new AddCommentNetwork(activity);
            addCommentsNetwork.addObserver(activity);
            addCommentsNetwork.postComments(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDeepLinkEvent(String deepLinkEvent, String actName) {
        this.deepLinkEvent = deepLinkEvent;
        this.deepLinkActivityName = actName;
    }

    public String getDeepLinkEvent() {
        return deepLinkEvent;
    }

    public String getDeepLinkActivityName() {
        return deepLinkActivityName;
    }

    public ArrayList<String> getConsoleList() {
        consoleList = new ArrayList<>();
        if(user!=null) {
            if(user.getConsoleType()!=null) {
                consoleList.add(user.getConsoleType());
                for (int n = 0; n < user.getConsoles().size(); n++) {
                    if (!user.getConsoles().get(n).getcType().equalsIgnoreCase(user.getConsoleType())) {
                        consoleList.add(user.getConsoles().get(n).getcType());
                    }
                }
            }
        }
        return consoleList;
    }

    public void addOtherConsole(UpdateConsoleActivity activity, RequestParams rp_console) {
        try {
            addConsoleNetwork = new AddNewConsoleNetwork(activity);
            addConsoleNetwork.addObserver(activity);
            addConsoleNetwork.doAddConsole(rp_console);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeToOtherConsole(ListActivityFragment activity, RequestParams rp_console) {
        try {
            changeCurrentConsoleNetwork = new ChangeCurrentConsoleNetwork(activity);
            changeCurrentConsoleNetwork.addObserver(activity);
            changeCurrentConsoleNetwork.doChangeConsole(rp_console);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void invitePlayers(EventDetailActivity activity, RequestParams rp) {
        try {
            invitePlayersNetwork = new InvitePlayerNetwork(activity);
            invitePlayersNetwork.addObserver(activity);
            invitePlayersNetwork.postInvitedList(rp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ActivityData getAdsActivity(String adcardEventId) {
        for (int n=0;n<adActivityData.size();n++) {
            if (adActivityData.get(n).getId().equalsIgnoreCase(adcardEventId)) {
                return adActivityData.get(n);
            }
        }
        return null;
    }

    public void legalPrivacyDone(ListActivityFragment act) {
        try {
            legalPrivacyNetwork = new PrivacyLegalUpdateNetwork(act);
            legalPrivacyNetwork.addObserver(act);
            legalPrivacyNetwork.postTermsPrivacyDone();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AsyncHttpClient getBungieClient(String csrf, String cookies) {
            AsyncHttpClient clientT = new AsyncHttpClient();
            clientT.setTimeout(30000);
            if(csrf!=null) {
                clientT.addHeader("x-csrf", csrf);
            }
            if(cookies!=null) {
                clientT.addHeader("Cookie", cookies);
            }
            clientT.addHeader("x-api-key", "f091c8d36c3c4a17b559c21cd489bec0");
        return clientT;
    }

    public void setClient(Context c) {
        client = new AsyncHttpClient();
        client.setTimeout(30000);
        ConnectivityManager connManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        client.addHeader(Constants.CONFIG_TOKEN_KEY, Constants.CONFIG_TOKEN);
        client.addHeader("$wifi", String.valueOf(mWifi.isConnected()));
        client.addHeader("$screen_dpi", String.valueOf(metrics.densityDpi));
        client.addHeader("$screen_height", String.valueOf(metrics.heightPixels));
        client.addHeader("$screen_width", String.valueOf(metrics.widthPixels));
        client.addHeader("$lib_version", MPConfig.VERSION);
        client.addHeader("$os", "Android");
        client.addHeader("$os_version", Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE);
        client.addHeader("$manufacturer", Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER);
        client.addHeader("$brand", Build.BRAND == null ? "UNKNOWN" : Build.BRAND);
        client.addHeader("$model", Build.MODEL == null ? "UNKNOWN" : Build.MODEL);
        try {
            if (c != null && c.getPackageManager() != null) {
                if (c.getPackageName() != null) {
                    PackageInfo pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
                    String version = pInfo.versionName;
                    if (version != null) {
                        client.addHeader("$app_version", version);
                        client.addHeader("$app_version_code", Integer.toString(pInfo.versionCode));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        client.addHeader("x-fbooksdk", "facebook-android-sdk:[4,5)");
        client.addHeader("x-fbasesdk", "firebase-9.2.0");
        client.addHeader("x-mpsdk", "mixpanel-android:4.+");
        client.addHeader("x-branchsdk", "branch-library:1.+");
        client.addHeader("x-fabricsdk", "answers:1.3.8@aar");

        try {
            try {
                final int servicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(c);
                switch (servicesAvailable) {
                    case ConnectionResult.SUCCESS:
                        client.addHeader("$google_play_services", "available");
                        break;
                    case ConnectionResult.SERVICE_MISSING:
                        client.addHeader("$google_play_services", "missing");
                        break;
                    case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                        client.addHeader("$google_play_services", "out of date");
                        break;
                    case ConnectionResult.SERVICE_DISABLED:
                        client.addHeader("$google_play_services", "disabled");
                        break;
                    case ConnectionResult.SERVICE_INVALID:
                        client.addHeader("$google_play_services", "invalid");
                        break;
                }
            } catch (RuntimeException e) {
                // Turns out even checking for the service will cause explosions
                // unless we've set up meta-data
                client.addHeader("$google_play_services", "not configured");
            }

        } catch (NoClassDefFoundError e) {
            client.addHeader("$google_play_services", "not included");
        }

        NfcManager managerNfc = (NfcManager) c.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = managerNfc.getDefaultAdapter();
        if (adapter != null) {
            client.addHeader("$has_nfc", String.valueOf(adapter.isEnabled()));
        }

        TelephonyManager manager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
        client.addHeader("$carrier", manager.getNetworkOperator()!=null?manager.getNetworkOperator():"UNKNOWN");
    }

    public void addClientHeader(String headerKey, String headerValue) {
        if(client!=null) {
            client.addHeader(headerKey, headerValue);
        }
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    public void setShowFullEvent(Boolean showFullEvent) {
        this.showFullEvent = showFullEvent;
    }

    public boolean getshowFullEvent() {
        if(showFullEvent!=null) {
            return showFullEvent;
        }
        return false;
    }

    public void postCommentReporting(RequestParams requestParams) {
        try {
            reportCommentNetwork = new ReportCommentNetwork(mCurrentAct);
            reportCommentNetwork.addObserver((EventDetailActivity)mCurrentAct);
            reportCommentNetwork.doCommentReporting(requestParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBungieCurrentUser(String csrf, String cookies, Context applicationContext) {
        try {
            bugieGetUser = new BungieUserNetwork(csrf, cookies, applicationContext, getBungieCurrentUserUrl()!=null?getBungieCurrentUserUrl():Constants.BUGIE_CURRENT_USER);
            bugieGetUser.addObserver(this);
            bugieGetUser.getBungieCurrentUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentPlatform() {
        String c = Util.getDefaults("cookie", mCurrentAct);
        String[] pair = c.split(";");
        for(int i=0; i<pair.length;i++) {
            String temp = pair[i].substring(0, pair[i].indexOf('=')).trim();
            if(temp.equalsIgnoreCase("bunglesony")) {
                return Constants.CONSOLEPS4;
            } else if(temp.equalsIgnoreCase("bunglemsa")) {
                return Constants.CONSOLEXBOXONE;
            }
        }
        return null;
    }

    public void getConfig(MainActivity c) {
        try {
        getConfigNetwork = new ConfigNetwork(c);
        getConfigNetwork.addObserver(c);
        getConfigNetwork.getConfig();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBungieCurrentUserUrl() {
        if (bungieCurrentUserUrl!=null && !bungieCurrentUserUrl.isEmpty()) {
            return bungieCurrentUserUrl;
        }
        return Util.getDefaults("playerDetailsURL", mCurrentAct);
    }

    protected String getPSNLoginUrl() {
        if (psnURL!=null && !psnURL.isEmpty()) {
            return psnURL;
        }
        return Util.getDefaults("psnLoginURL", mCurrentAct);
    }

    protected String getXboxLoginUrl() {
        if (xboxURL!=null && !xboxURL.isEmpty()) {
            return xboxURL;
        }
        return Util.getDefaults("xboxLoginURL", mCurrentAct);
    }

    public void parseAndSaveConfigUrls(JSONObject data) {
        try {
            if(data.has("playerDetailsURL") && !data.isNull("playerDetailsURL")) {
                if(!data.getString("playerDetailsURL").isEmpty()) {
                    bungieCurrentUserUrl = data.getString("playerDetailsURL");
                    Util.setDefaults("playerDetailsURL", bungieCurrentUserUrl, mCurrentAct);
                }
            }
            if(data.has("psnLoginURL") && !data.isNull("psnLoginURL")) {
                if(!data.getString("psnLoginURL").isEmpty()) {
                    psnURL = data.getString("psnLoginURL");
                    Util.setDefaults("psnLoginURL", psnURL, mCurrentAct);
                }
            }
            if(data.has("xboxLoginURL") && !data.isNull("xboxLoginURL")) {
                if(!data.getString("xboxLoginURL").isEmpty()) {
                    xboxURL = data.getString("xboxLoginURL");
                    Util.setDefaults("xboxLoginURL", xboxURL, mCurrentAct);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserFromNetwork(MainActivity mainActivity, RequestParams rp) {
        try {
        LoginNetwork getUserNtwrk = new LoginNetwork(mainActivity);
        getUserNtwrk.addObserver(this);
        getUserNtwrk.addObserver(mainActivity);
        getUserNtwrk.getUser(rp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendInviteBungieMsg(String msgUrl, String body, Context applicationContext, String id) {
        try {
            String csrf = Util.getDefaults("csrf", applicationContext);
            String cookies = Util.getDefaults("cookie", applicationContext);

            if(csrf!=null && !csrf.isEmpty() && cookies!=null && !cookies.isEmpty()) {
                ByteArrayEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                bungieMsgNtwrk = new BungieMessageNetwork(csrf, cookies, applicationContext, msgUrl);
                bungieMsgNtwrk.addObserver(this);
                bungieMsgNtwrk.postBungieMsg(entity, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postAcceptInvite(EventDetailActivity activity, RequestParams rp) {
        eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
        eventRelationshipNtwrk.addObserver(activity);
        eventRelationshipNtwrk.addObserver(this);
        eventRelationshipNtwrk.postEventPlayerRelation(rp, Constants.ACCEPT_EVENT_URL);
    }

    public void postKickPlayer(EventDetailActivity activity, RequestParams requestParams) {
        eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
        eventRelationshipNtwrk.addObserver(activity);
        eventRelationshipNtwrk.addObserver(this);
        eventRelationshipNtwrk.postEventPlayerRelation(requestParams, Constants.KICK_PLAYER_URL);
    }

    public void postCancelPlayer(EventDetailActivity activity, RequestParams requestParams) {
        eventRelationshipNtwrk = new EventRelationshipHandlerNetwork(activity);
        eventRelationshipNtwrk.addObserver(activity);
        eventRelationshipNtwrk.addObserver(this);
        eventRelationshipNtwrk.postEventPlayerRelation(requestParams, Constants.CANCEL_PLAYER_URL);
    }

    public void postChangeEmail(ChangeEmail activity, RequestParams params) {
        try {
            forgotPasswordNetwork = new ForgotPasswordNetwork(activity);
            forgotPasswordNetwork.addObserver(activity);
            forgotPasswordNetwork.doChangeEmail(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

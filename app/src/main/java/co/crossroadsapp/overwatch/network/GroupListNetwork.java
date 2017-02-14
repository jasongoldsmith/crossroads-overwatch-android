package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.data.GroupData;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 5/19/16.
 */
public class GroupListNetwork extends Observable {

    private Context mContext;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/account/group/list";
    private String selectedGroupUrl = "api/v1/a/user/updateGroup";
    private String groupMuteUrl = "api/v1/a/account/group/mute";
    private ArrayList<GroupData> groupList;

    //private EventList eventList;
    private ControlManager mManager;

    public GroupListNetwork(Context c) {
        //listA = act;
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);

        groupList = new ArrayList<GroupData>();
//        if (eventList != null) {
//
//        } else {
//            eventList = new EventList();
//        }
    }

    public void getGroups() throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        parseGroupList(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    mManager.showErrorDialogue(statusCode + " - server failed");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(errorResponse);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

    private void dispatchError(JSONObject errorResponse) {
        LoginError error = new LoginError();
        error.toJson(errorResponse);
        setChanged();
        notifyObservers(error);
    }

    private void parseGroupList(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonobject = response.getJSONObject(i);
            if (jsonobject.length()>0) {
                GroupData eData = new GroupData();
                eData.toJson(jsonobject);
                groupList.add(eData);
            }
        }
        setChanged();
        notifyObservers(this.groupList);
    }

    private void parseUserObj(JSONObject response) {
        UserData user = new UserData();
        //todo debug change
//        try {
            user.toJson(response);
            //todo changing current controlmanager user obj and later userobject will change and only one parser will do that
            if (mManager.getUserData()!=null) {
                UserData u = mManager.getUserData();
                if(user!=null && user.getClanId()!=null) {
                    u.setClanId(user.getClanId());
                }
            }

        setChanged();
        notifyObservers(user);
    }

    private void parseGrpData(JSONObject response) {
        if (response!=null) {
            GroupData eData = new GroupData();
            eData.toJson(response);
            setChanged();
            notifyObservers(eData);
        }
    }

    public void postSelectGroup(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(selectedGroupUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    parseUserObj(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    mManager.showErrorDialogue(statusCode + " - server failed for create event");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(errorResponse);
                }
            });
        } else {
            Util.createNoNetworkDialogue(mContext);
        }
    }



    public void postMuteNotification(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(groupMuteUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    parseGrpData(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    mManager.showErrorDialogue(statusCode + " - server failed for create event");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(errorResponse);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

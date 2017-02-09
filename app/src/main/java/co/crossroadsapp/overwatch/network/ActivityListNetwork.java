package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.ActivityList;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 3/8/16.
 */
public class ActivityListNetwork extends Observable {
    private NetworkEngine ntwrk;
    private String url = "api/v1/activity/list";
    private Context mContext;
    private ControlManager mManager;

    private ActivityList activityList;

    public ActivityListNetwork(Context c){
        mContext = c;
        ntwrk = NetworkEngine.getmInstance(c);
        mManager = ControlManager.getmInstance();

        if (activityList != null) {

        } else {
            activityList = new ActivityList();
        }
    }

    public void postGetActivityList(RequestParams rp) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.get(url, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
//                    Toast.makeText(mContext, "List server call Success",
//                            Toast.LENGTH_SHORT).show();
                    TravellerLog.w(this, "onSuccess response:" + response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
//                    Toast.makeText(mContext, "List server call Success",
//                            Toast.LENGTH_SHORT).show();

                    parseActivityList(response);
                    TravellerLog.w(this, "onSuccess response:" + response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                    Toast.makeText(mContext, "List error from server  - " + statusCode + " ",
//                            Toast.LENGTH_LONG).show();
                    //mManager.showErrorDialogue(statusCode + " - server failed");
                    TravellerLog.w(this, "onFailure errorResponse:" + errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        Toast.makeText(mContext, "List error from server  - " + statusCode + " " + errorResponse.getString("message"),
//                                Toast.LENGTH_LONG).show();
                    //mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));
                    TravellerLog.w(this, "onFailure errorResponse:" + errorResponse);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

    private void parseActivityList(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = response.getJSONObject(i);
                ActivityData eData = new ActivityData();
                eData.toJson(jsonobject);
                this.activityList.appendActivityList(eData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mManager.updateActivityList(this.activityList);
        setChanged();
        notifyObservers(this.activityList);
    }
}

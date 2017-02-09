package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 8/19/16.
 */
public class TrackingNetwork extends Observable {

    private final Context mContext;
    private final ControlManager mManager;
    private NetworkEngine ntwrk;
    private String url = "api/v2/mixpanel/track";

    public TrackingNetwork(Context c) {
        mContext = c;
        ntwrk = NetworkEngine.getmInstance(c);
        mManager = ControlManager.getmInstance();
    }

    public void postTracking(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response.has("trackingKey") && !response.isNull("trackingKey")) {
                        try {
                            if(response.get("trackingKey").toString().equalsIgnoreCase(Constants.APP_INSTALL)) {
                                setChanged();
                                notifyObservers();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    mManager.showErrorDialogue(null);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

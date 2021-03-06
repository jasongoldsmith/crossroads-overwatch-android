package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 7/8/16.
 */
public class HelmetUpdateNetwork extends Observable {

    private Context mContext;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/account/updateHelmet";

    private ControlManager mManager;

    public HelmetUpdateNetwork(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void getHelmet() throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    parseUserResponse(response);
//                    try {
//                        String url = null;
//                        if (response != null) {
//                            if (response.has("helmetUrl")) {
//                                url = response.getString("helmetUrl");
//                            }
//                        }
//                        setChanged();
//                        notifyObservers(url);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

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

    private void parseUserResponse(JSONObject response) {
        //parse response
        UserData user = new UserData();
        if(response!=null) {
            user.toJson(response);
        }
        if(user!=null) {
            if((user.getClanId()!=null) && (!user.getClanId().isEmpty())) {
                mManager.setUserdata(user);
            }
        }
        setChanged();
        notifyObservers(user);
    }

    private void dispatchError(JSONObject errorResponse) {
        LoginError error = new LoginError();
        error.toJson(errorResponse);
        setChanged();
        notifyObservers(error);
    }
}

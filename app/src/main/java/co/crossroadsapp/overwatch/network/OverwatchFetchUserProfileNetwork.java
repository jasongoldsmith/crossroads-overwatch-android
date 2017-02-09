package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
/**
 * Created by karagdi on 1/20/17.
 */
public class OverwatchFetchUserProfileNetwork extends Observable {
    private Context _context;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/user/self";

    private ControlManager mManager;

    public OverwatchFetchUserProfileNetwork(Context c) {
        this._context = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void fetchProfile() throws JSONException {
        if (Util.isNetworkAvailable(this._context)) {
            ntwrk.get(url, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TravellerLog.w(this, "onSuccess response: " + response);
                    parseSignUpUser(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    setChanged();
                    notifyObservers();
                }
            });
        }else {
            Util.createNoNetworkDialogue(this._context);
        }
    }

    private void parseSignUpUser(JSONObject response) {
        UserData user = new UserData();
        user.toJson(response);
        //user.setImageUrl("https://s3-us-west-2.amazonaws.com/frienduel-assets/metaimages/icn_avatar.png");
        //user.setAuthenticationId(Constants.LOGIN);
        setChanged();
        notifyObservers(user);
    }
}

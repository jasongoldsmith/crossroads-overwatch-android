package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 10/26/16.
 */
public class BungieUserNetwork extends Observable {

    private Context mContext;
    private NetworkEngine ntwrk;

    private ControlManager mManager;

    public BungieUserNetwork(String csrf, String cookies, Context c, String url) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
        ntwrk.updateBugnieBaseUrlAndHeader(csrf, cookies, url);
    }

    public void getBungieCurrentUser() throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.get(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //set cookie validation
                    Util.setDefaults(Constants.COOKIE_VALID_KEY, "true", mContext);
                    setChanged();
                    notifyObservers(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //set cookie validation
                    Util.setDefaults(Constants.COOKIE_VALID_KEY, "false", mContext);
                    mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

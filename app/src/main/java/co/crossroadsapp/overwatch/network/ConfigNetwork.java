package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 11/2/16.
 */
public class ConfigNetwork extends Observable {
    private Context mContext;
    private NetworkEngine ntwrk;
    private String url = "api/v1/config";
    private ControlManager mManager;

    public ConfigNetwork(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void getConfig() throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            String[] headers = {Constants.CONFIG_TOKEN_KEY, Constants.CONFIG_TOKEN};
            ntwrk.get(url, headers, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    try {
                        mManager.showErrorDialogue(Util.getErrorMessage(errorResponse.getJSONObject(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));

                    setChanged();
                    notifyObservers(errorResponse);
                }
            });
        } else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

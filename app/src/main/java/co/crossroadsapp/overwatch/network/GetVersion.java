package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.data.AppVersion;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 3/18/16.
 */
public class GetVersion extends Observable {

    private NetworkEngine ntwrk;
    private String url = "api/v1/appVersion/android";
    private Context mContext;

    public GetVersion(Context c) {
        mContext = c;
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void getAppVer() throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
//                    Toast.makeText(mContext, "Lis server call Success",
//                            Toast.LENGTH_SHORT).show();
                    parseVersion(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
//                    Toast.makeText(mContext, "List server call Success",
//                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                    Toast.makeText(mContext, "List error from server  - " + statusCode + " ",
//                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        Toast.makeText(mContext, "List error from server  - " + statusCode + " " + " ",
//                                Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

    private void parseVersion(JSONObject response) {
        AppVersion av = new AppVersion();
        av.toJson(response);
        setChanged();
        notifyObservers(av);
    }

}

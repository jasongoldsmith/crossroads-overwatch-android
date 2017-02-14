package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Observable;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by sharmha on 4/1/16.
 */
public class ReportCrashNetwork extends Observable {

    private final ControlManager mManager;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/report/create";
    private String urlUnsigned = "api/v2/report/create";
    private Context mContext;

    public ReportCrashNetwork(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void doCrashReport(RequestParams params, int type) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            String localUrl = urlUnsigned;
            if(mManager!=null && mManager.getUserData()!=null && mManager.getUserData().getUserId()!=null) {
                localUrl = url;
            }
            if(type == Constants.REPORT_COMMENT_NEXT) {
                localUrl = Constants.REPORT_COMMENT_URL;
            }
            ntwrk.post(localUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers();
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

    public void doCrashReport(String email, String userMsg) throws JSONException, UnsupportedEncodingException {
        if (Util.isNetworkAvailable(this.mContext)) {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email", email);
            jsonParams.put("description", userMsg);
            StringEntity entity = new StringEntity(jsonParams.toString());

            ntwrk.post(this.mContext, urlUnsigned, entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers();
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
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(errorResponse);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

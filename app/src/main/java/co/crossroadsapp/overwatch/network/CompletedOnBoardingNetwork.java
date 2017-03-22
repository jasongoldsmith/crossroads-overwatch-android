package co.crossroadsapp.overwatch.network;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Observable;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 3/22/17.
 */

public class CompletedOnBoardingNetwork extends Observable {
    private Context mContext;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/user/completeOnBoarding";
    private ControlManager mManager;

    public CompletedOnBoardingNetwork(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void postOnBoardingCompleted(RequestParams params) {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

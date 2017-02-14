package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.core.BattletagAlreadyTakenException;
import co.crossroadsapp.overwatch.core.InvalidEmailProvided;
import co.crossroadsapp.overwatch.core.NoUserFoundException;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.core.TrimbleException;
import co.crossroadsapp.overwatch.data.GeneralServerError;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 5/13/16.
 */
public class ForgotPasswordNetwork extends Observable {
    private Context mContext;
    private NetworkEngine ntwrk;
    private String url = "api/v1/auth/request/resetPassword";
    private String changePswrdUrl = "api/v1/a/user/updatePassword";
    private ControlManager mManager;
    private String changeEmailUrl = "api/v1/a/user/updateEmail";

    public ForgotPasswordNetwork(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void doResetPassword(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    Toast.makeText(mContext, "Signup error from server  - " + statusCode,
//                            Toast.LENGTH_LONG).show();
                    mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

    public void doChangePassword(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(changePswrdUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers();
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

    public void doChangeEmail(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(changeEmailUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    setChanged();
                    notifyObservers();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(errorResponse);
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }
}

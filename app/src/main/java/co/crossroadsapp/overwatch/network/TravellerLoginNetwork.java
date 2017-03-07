package co.crossroadsapp.overwatch.network;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Observable;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.core.BattletagAlreadyTakenException;
import co.crossroadsapp.overwatch.core.GeneralErrorException;
import co.crossroadsapp.overwatch.core.InvalidEmailProvided;
import co.crossroadsapp.overwatch.core.NoUserFoundException;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.core.TrimbleException;
import co.crossroadsapp.overwatch.data.GeneralServerError;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by karagdi on 1/25/17.
 */

public class TravellerLoginNetwork extends Observable {
    private Context _context;
    private NetworkEngine ntwrk;
    private String url = "api/v1/auth/signIn";
    private ControlManager mManager;

    public TravellerLoginNetwork(Context c) {
        this._context = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void doLogin(final String email, String password) throws JSONException, UnsupportedEncodingException {
        if (Util.isNetworkAvailable(this._context)) {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email", email);
            jsonParams.put("password", password);
            StringEntity entity = new StringEntity(jsonParams.toString());
            ntwrk.post(this._context, url, entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TravellerLog.w(this, "onSuccess response: " + response);
                    postGcm();
                    parseSignUpUser(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(statusCode, email, errorResponse);
                }
            });
        } else {
            Util.createNoNetworkDialogue(this._context);
        }
    }

    private void dispatchError(int statusCode, String email, JSONObject errorResponse) {
        OverwatchLoginException exception = null;
        LoginError error = new LoginError();
        error.toJson(errorResponse);
        if (statusCode == TrimbleException.TRIMBLE_BAD_REQUEST) {
            GeneralServerError er = error.getGeneralServerError();
            if (er != null && er.getCode() == GeneralServerError.INVALID_MAIL_PROVIDED) {
                exception = new InvalidEmailProvided(statusCode, error.getDescription(), error.getGeneralServerError());
            } else if (er != null && er.getCode() == GeneralServerError.NO_USER_FOUND_WITH_THE_EMAIL) {
                exception = new NoUserFoundException(statusCode, error.getDescription(), error.getGeneralServerError());
            } else if (er != null && er.getCode() == GeneralServerError.ALREADY_TAKEN) {
                exception = new BattletagAlreadyTakenException(statusCode, error.getDescription(), error.getGeneralServerError());
            } else {
                exception = new GeneralErrorException(statusCode, error.getDescription(), error.getGeneralServerError());
            }
            exception.setUserTag(email);
        }
        setChanged();
        notifyObservers(exception);
    }

    private void postGcm() {
        //post gcm token
        Util.getGCMToken(this._context, mManager);
    }

    private void parseSignUpUser(JSONObject response) {
        UserData user = new UserData();
        user.toJson(response);
        //user.setUser("Dimitare");
        //user.setImageUrl("https://s3-us-west-2.amazonaws.com/frienduel-assets/metaimages/icn_avatar.png");
        //user.setAuthenticationId(Constants.LOGIN);
        setChanged();
        notifyObservers(user);
    }
}

package co.crossroadsapp.overwatch.network;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Observable;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.core.AddConsoleException;
import co.crossroadsapp.overwatch.core.BattletagAlreadyTakenException;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.core.TrimbleException;
import co.crossroadsapp.overwatch.data.GeneralServerError;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by karagdi on 1/25/17.
 */
public class AddConsoleNetwork extends Observable {
    private Context _context;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/user/addConsole";
    private ControlManager _manager;

    public AddConsoleNetwork(Context c) {
        this._context = c;
        this._manager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void addConsole(String consoleType, final String consoleId) throws JSONException, UnsupportedEncodingException {
        if (Util.isNetworkAvailable(this._context)) {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("consoleType", consoleType);
            jsonParams.put("consoleId", consoleId);
            StringEntity entity = new StringEntity(jsonParams.toString());
            ntwrk.post(this._context, url, entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TravellerLog.w(this, "onSuccess response: " + response);
                    parseConsoleResponse(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    TravellerLog.w(this, "onFailure errorResponse: " + errorResponse);
                    dispatchError(statusCode, consoleId, errorResponse);
                }
            });
        }
    }

    private void dispatchError(int statusCode, String consoleId, JSONObject errorResponse) {
        OverwatchLoginException exception = null;
        LoginError error = new LoginError();
        error.toJson(errorResponse);
        if (statusCode == TrimbleException.TRIMBLE_BAD_REQUEST) {
            GeneralServerError er = error.getGeneralServerError();
            if (er != null && er.getCode() == GeneralServerError.ALREADY_TAKEN) {
                exception = new AddConsoleException(statusCode, error.getDescription(), error.getGeneralServerError());
            } else {
                exception = new BattletagAlreadyTakenException(statusCode, error.getDescription(), error.getGeneralServerError());
            }
            exception.setUserTag(consoleId);
        }
        setChanged();
        notifyObservers(exception);
    }

    private void parseConsoleResponse(JSONObject response) {
        UserData user = new UserData();
        user.toJson(response);
        //user.setUser("Dimitare");
        //user.setImageUrl("https://s3-us-west-2.amazonaws.com/frienduel-assets/metaimages/icn_avatar.png");
        //user.setAuthenticationId(Constants.LOGIN);
        setChanged();
        notifyObservers(user);
    }
}

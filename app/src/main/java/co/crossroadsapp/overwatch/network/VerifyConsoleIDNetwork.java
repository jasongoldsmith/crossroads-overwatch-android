package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.data.ConsoleData;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 6/3/16.
 */
public class VerifyConsoleIDNetwork extends Observable {

    private Context mContext;
    private NetworkEngine ntwrk;
    private String url = "api/v1/auth/checkBungieAccount";
    private ControlManager mManager;

    public VerifyConsoleIDNetwork(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void doVerifyConsoleId(RequestParams params) throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    parseConsoleResponse(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

    private void parseConsoleResponse(JSONObject response) {
        try {
            //parse response
            ConsoleData cData = new ConsoleData();
            if(response!=null) {
                if(response.has("consoleId")) {
                    cData.setcId(response.getString("consoleId"));
                }
                if(response.has("bungieMemberShipId")) {
                    cData.setMembershipId(response.getString("bungieMemberShipId"));
                }
                if(response.has("consoleType")) {
                    cData.setcType(response.getString("consoleType"));
                }
            }
            setChanged();
            notifyObservers(cData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

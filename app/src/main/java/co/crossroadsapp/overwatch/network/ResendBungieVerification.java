package co.crossroadsapp.overwatch.network;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.utils.Util;
import cz.msebera.android.httpclient.Header;

/**
 * Created by sharmha on 5/31/16.
 */
public class ResendBungieVerification extends Observable {

    private final ControlManager mManager;
    private NetworkEngine ntwrk;
    private String url = "api/v1/a/account/group/resendBungieMessage";
    private Context mContext;

    public ResendBungieVerification(Context c) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
    }

    public void resendBungieMsgVerify() throws JSONException {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
//                    Toast.makeText(mContext, "Lis server call Success",
//                            Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(mContext, "List error from server  - " + statusCode + " " + " ",
//                                Toast.LENGTH_LONG).show();
                    mManager.showErrorDialogue(Util.getErrorMessage(errorResponse));
                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

}

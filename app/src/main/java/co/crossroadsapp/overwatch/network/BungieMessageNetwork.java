package co.crossroadsapp.overwatch.network;

import android.content.Context;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by sharmha on 11/4/16.
 */
public class BungieMessageNetwork extends Observable {

    private Context mContext;
    private NetworkEngine ntwrk;
    private ControlManager mManager;
    protected String id=null;

    public BungieMessageNetwork(String csrf, String cookies, Context c, String url) {
        mContext = c;
        mManager = ControlManager.getmInstance();
        ntwrk = NetworkEngine.getmInstance(c);
        ntwrk.updateBugnieBaseUrlAndHeader(csrf, cookies, url);
    }

    public void postBungieMsg(ByteArrayEntity rp, String eId) {
        if (Util.isNetworkAvailable(mContext)) {
            id = eId;
            ntwrk.post(mContext, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    createPostForBungieResponse(response);
//                    setChanged();
//                    notifyObservers(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });
        }else {
            Util.createNoNetworkDialogue(mContext);
        }
    }

    private void createPostForBungieResponse(JSONObject response) {
        try {
            RequestParams rp = new RequestParams();
            if(id!=null) {
                rp.put("responseType", "sendBungieMessage");
                HashMap<String,Object> map = new ObjectMapper().readValue(response.toString(), HashMap.class);
                rp.put("gatewayResponse", map);
                Map<String, String> map2 = new HashMap<String, String>();
                map2.put("pendingEventInvitationId", id);
                rp.put("responseParams", map2);
                postBungieResponse(rp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postBungieResponse(RequestParams rp) {
        if (Util.isNetworkAvailable(mContext)) {
            ntwrk = NetworkEngine.getmInstance(mContext);
            ntwrk.post(Constants.BUNGIE_RESPONSE_URL, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    setChanged();
//                    notifyObservers();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });
//        }else {
//            Util.createNoNetworkDialogue(mContext);
        }
    }

}

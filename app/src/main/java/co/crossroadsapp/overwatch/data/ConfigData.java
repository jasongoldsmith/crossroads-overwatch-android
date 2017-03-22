
package co.crossroadsapp.overwatch.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import co.crossroadsapp.overwatch.utils.Util;

public class ConfigData {

    @SerializedName("mixpanelToken")
    @Expose
    private String mixpanelToken;
    @SerializedName("onBoardingScreens")
    @Expose
    private OnBoardingScreens onBoardingScreens;
    @SerializedName("httpStatusCode")
    @Expose
    private Integer httpStatusCode;

    public String getMixpanelToken() {
        return mixpanelToken;
    }

    public void setMixpanelToken(String mixpanelToken) {
        this.mixpanelToken = mixpanelToken;
    }

    public OnBoardingScreens getOnBoardingScreens() {
        return onBoardingScreens;
    }

    public void setOnBoardingScreens(OnBoardingScreens onBoardingScreens) {
        this.onBoardingScreens = onBoardingScreens;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void parseData(JSONObject data) {
        String token = null;
        try {
            if(data.has("mixpanelToken") && !data.isNull("mixpanelToken")) {
                if (!data.getString("mixpanelToken").isEmpty()) {
                    token = data.getString("mixpanelToken");
                    setMixpanelToken(token);
                }
            }
            if(data.has("onBoardingScreens") && !data.isNull("onBoardingScreens")) {
                JSONObject bs = data.getJSONObject("onBoardingScreens");
                OnBoardingScreens obs = new OnBoardingScreens();
                obs.parseonbaordingData(bs);
                setOnBoardingScreens(obs);
            }
            if(data.has("httpStatusCode") && !data.isNull("httpStatusCode")) {
                 setHttpStatusCode(data.getInt("httpStatusCode"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 7/29/16.
 */
public class AdCardData {
    private boolean isAdCard=false;
    private String adCardBaseUrl=null;
    private String adCardImagePath=null;
    private String adCardHeader=null;
    private String adCardSubHeader=null;

    public void setIsAdCard(boolean isAd) {
        isAdCard = isAd;
    }

    public boolean getIsAdCard() {
        return isAdCard;
    }

    public void setAdCardBaseUrl(String adCardBaseUrl) {
        this.adCardBaseUrl = adCardBaseUrl;
    }

    public String getAdCardBaseUrl() {
        return adCardBaseUrl;
    }

    public void setAdCardHeader(String adCardHeader) {
        this.adCardHeader = adCardHeader;
    }

    public String getAdCardHeader() {
        return adCardHeader;
    }

    public String getAdCardImagePath() {
        return adCardImagePath;
    }

    public void setAdCardImagePath(String adCardImagePath) {
        this.adCardImagePath = adCardImagePath;
    }

    public void setAdCardSubHeader(String adCardSubHeader) {
        this.adCardSubHeader = adCardSubHeader;
    }

    public String getAdCardSubHeader() {
        return adCardSubHeader;
    }

    public void toJson(JSONObject actData) {
        try {
            if (actData.has("isAdCard")){
                setIsAdCard(actData.getBoolean("isAdCard"));
            }
            if (actData.has("adCardBaseUrl")) {
                setAdCardBaseUrl(actData.getString("adCardBaseUrl"));
            }
            if (actData.has("adCardImagePath")) {
                setAdCardImagePath(actData.getString("adCardImagePath"));
            }
            if (actData.has("adCardHeader")) {
                setAdCardHeader(actData.getString("adCardHeader"));
            }
            if (actData.has("adCardSubHeader")) {
                setAdCardSubHeader(actData.getString("adCardSubHeader"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

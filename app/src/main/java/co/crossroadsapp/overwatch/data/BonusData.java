package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 8/10/16.
 */
public class BonusData {

    private String aBonusName=null;
    private String aBonusInfo=null;
    private boolean isActive=false;
    private String id=null;

    private void setaBonusName(String name) {
        this.aBonusName = name;
    }

    public String getaBonusName() {
        return aBonusName;
    }

    private void setaBonusInfo(String info) {
        this.aBonusInfo = info;
    }

    public String getaBonusInfo() {
        return aBonusInfo;
    }

    private void setisActive(boolean active) {
        this.isActive = active;
    }

    public boolean getisActive() {
        return isActive;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void toJson(JSONObject actData) {
        try {
            if (actData.has("aBonusName") && !actData.isNull("aBonusName")){
                setaBonusName(actData.getString("aBonusName"));
            }
            if (actData.has("aBonusInfo") && !actData.isNull("aBonusInfo")) {
                setaBonusInfo(actData.getString("aBonusInfo"));
            }
            if (actData.has("isActive") && !actData.isNull("isActive")) {
                setisActive(actData.getBoolean("isActive"));
            }
            if (actData.has("_id") && !actData.isNull("_id")) {
                setId(actData.getString("_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

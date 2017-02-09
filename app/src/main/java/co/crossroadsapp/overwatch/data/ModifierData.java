package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 8/10/16.
 */
public class ModifierData {

    private String aModifierName=null;
    private String aModifierInfo=null;
    private boolean isActive=false;
    private String id=null;

    private void setaModifierName(String name) {
        this.aModifierName = name;
    }

    public String getaModifierName() {
        return aModifierName;
    }

    private void setaModifierInfo(String info) {
        this.aModifierInfo = info;
    }

    public String getaModifierInfo() {
        return aModifierInfo;
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
            if (actData.has("aModifierName") && !actData.isNull("aModifierName")){
                setaModifierName(actData.getString("aModifierName"));
            }
            if (actData.has("aModifierInfo") && !actData.isNull("aModifierInfo")) {
                setaModifierInfo(actData.getString("aModifierInfo"));
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

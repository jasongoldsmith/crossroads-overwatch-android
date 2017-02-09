package co.crossroadsapp.overwatch.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sharmha on 2/28/16.
 */
public class ActivityData {
    private String id;
    private String activityType;
    private String activitySubtype;
    private int activityLight;
    private int minPlayer;
    private int maxPlayer;
    private String activityCheckpoint;
    private String activityDifficulty;
    private String activityIconUrl;
    private int activityLevel;
    private boolean activityFeature;
    private AdCardData adCardData;
    private ArrayList<ModifierData> modifierList;
    private ArrayList<BonusData> bonusList;
    private String aDescription;
    private String aLocation;
    private String aImageBaseUrl=null;
    private String aImagePath=null;
    private String tag;
    private String aFeedMode;

    public ActivityData () {
        adCardData = new AdCardData();
        modifierList = new ArrayList<ModifierData>();
        bonusList = new ArrayList<BonusData>();
    }

    public void setAdCardData(AdCardData ad) {
        adCardData = ad;
    }

    public AdCardData getAdCardData() {
        return adCardData;
    }

    public void setTag(String t) {
        tag = t;
    }

    public String getTag() {
        return tag;
    }

    public void setId(String aId) {
        id = aId;
    }

    public String getId() {
        return this.id;
    }

    public void setActivityType(String aType) {
        activityType = aType;
    }

    public String getActivityType() {
        return this.activityType;
    }

    public void setActivityFeature(boolean aFeature) {
        activityFeature = aFeature;
    }

    public boolean getActivityFeature() {
        return this.activityFeature;
    }

    public void setActivitySubtype(String aSubtype) {
        activitySubtype = aSubtype;
    }

    public String getActivitySubtype() {
        return this.activitySubtype;
    }

    public void setActivityLight(int aLight) {
        activityLight = aLight;
    }

    public int getActivityLight() {
        return this.activityLight;
    }

    public void setActivityCheckpoint(String aCheckpoint) {
        activityCheckpoint = aCheckpoint;
    }

    public String getActivityCheckpoint() {
        return this.activityCheckpoint;
    }

    public void setActivityIconUrl(String aIconUrl) {
        activityIconUrl = aIconUrl;
    }

    public String getActivityIconUrl() {
        return this.activityIconUrl;
    }

    public String getaImagePath() {
        return aImagePath;
    }

    public void setActivityDifficulty(String aDifficulty) {
        activityDifficulty = aDifficulty;
    }

    public String getActivityDifficulty() {
        return this.activityDifficulty;
    }

    public void setMinPlayer(int min) {
        minPlayer = min;
    }

    public int getMinPlayer() {
        return this.minPlayer;
    }

    public void setMaxPlayer(int max) {
        maxPlayer = max;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    public void setActivityLevel(int level) {
        activityLevel = level;
    }

    public int getActivityLevel() {
        return this.activityLevel;
    }

    public void setaDescription(String desc) {
        aDescription = desc;
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaLocation(String loc) {
        aLocation = loc;
    }

    public String getaLocation() {
        return aLocation;
    }

    private void setaFeedMode(String mode) {
        aFeedMode = mode;
    }

    public String getaFeedMode() {
        return aFeedMode;
    }

    public ArrayList<ModifierData> getModifierList() {
        return modifierList;
    }

    public ArrayList<BonusData> getBonusList() {
        return bonusList;
    }

    public void toJson(JSONObject actData) {
        try {
            if(actData!=null) {
                setId(actData.getString("_id"));
                setActivityType(actData.getString("aType"));
                setActivitySubtype(actData.getString("aSubType"));
                setMinPlayer(actData.getInt("minPlayers"));
                setMaxPlayer(actData.getInt("maxPlayers"));
                if (!actData.isNull("aCheckpoint")) {
                    setActivityCheckpoint(actData.getString("aCheckpoint"));
                }
                if(actData.has("aDifficulty") && !actData.isNull("aDifficulty")) {
                    setActivityDifficulty(actData.getString("aDifficulty"));
                }
                if (!actData.isNull("aIconUrl")) {
                    setActivityIconUrl(actData.getString("aIconUrl"));
                } else {
                    setActivityIconUrl(null);
                }
                if(actData.has("aLight") && !actData.isNull("aLight")) {
                    setActivityLight(actData.getInt("aLight"));
                }
                if (!actData.isNull("aLevel")) {
                    setActivityLevel(actData.getInt("aLevel"));
                }
                if (!actData.isNull("isFeatured")) {
                    setActivityFeature(actData.getBoolean("isFeatured"));
                }

                if (actData.has("adCard")) {
                    JSONObject jsonobjectAd = actData.optJSONObject("adCard");
                    AdCardData adcard = new AdCardData();
                    adcard.toJson(jsonobjectAd);
                    setAdCardData(adcard);
                }

                if (actData.has("tag") && !actData.isNull("tag")) {
                    setTag(actData.getString("tag"));
                }

                if (actData.has("aFeedMode") && !actData.isNull("aFeedMode")) {
                    setaFeedMode(actData.getString("aFeedMode"));
                }

                if(actData.has("aDescription") && !actData.isNull("aDescription")) {
                    setaDescription(actData.getString("aDescription"));
                }

                if(actData.has("aLocation") && !actData.isNull("aLocation")) {
                    JSONObject jsonobjectLoc = actData.optJSONObject("aLocation");
                    if (jsonobjectLoc.has("aSubLocation") && !jsonobjectLoc.isNull("aSubLocation")) {
                        setaLocation(jsonobjectLoc.getString("aSubLocation"));
                    }
                }

                if (actData.has("aImage") && !actData.isNull("aImage")) {
                    JSONObject jsonobjectIm = actData.optJSONObject("aImage");
                    if (jsonobjectIm.has("aImageBaseUrl") && !jsonobjectIm.isNull("aImageBaseUrl")) {
                        aImageBaseUrl = jsonobjectIm.getString("aImageBaseUrl");
                        if (jsonobjectIm.has("aImageImagePath") && !jsonobjectIm.isNull("aImageImagePath")) {
                            aImagePath = aImageBaseUrl + jsonobjectIm.getString("aImageImagePath");
                        }
                    }
                }

                if (actData.has("aModifiers") && !actData.isNull("aModifiers")) {
                    JSONArray jsonArrM = actData.optJSONArray("aModifiers");
                    for (int i = 0; i < jsonArrM.length(); i++) {
                        JSONObject jsonobjectM = jsonArrM.getJSONObject(i);
                        ModifierData mData = new ModifierData();
                        mData.toJson(jsonobjectM);
                        modifierList.add(mData);
                    }
                }

                if (actData.has("aBonus") && !actData.isNull("aBonus")) {
                    JSONArray jsonArrB = actData.optJSONArray("aBonus");
                    for (int i = 0; i < jsonArrB.length(); i++) {
                        JSONObject jsonobjectB = jsonArrB.getJSONObject(i);
                        BonusData mData = new BonusData();
                        mData.toJson(jsonobjectB);
                        bonusList.add(mData);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

package co.crossroadsapp.overwatch.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 2/29/16.
 */
public class CreatorData extends PlayerData {
    public CreatorData() {
        super();
    }

    public void toJson(JSONObject creator) {
        try {
            if(creator!=null) {
                if (creator.has("_id") && !creator.isNull("_id")) {
                    setPlayerId(creator.getString("_id"));
                }
                if (creator.has("userName") && !creator.isNull("userName")) {
                    setUsername(creator.getString("userName"));
                }
                if (creator.has("consoles") && !creator.isNull("consoles")) {
                    JSONArray conArray = creator.optJSONArray("consoles");
                    if (conArray != null) {
                        for (int i = 0; i < conArray.length(); i++) {
                            JSONObject conData = (JSONObject) conArray.get(i);
                            if (conData.has("isPrimary")) {
                                if (conData.getBoolean("isPrimary")) {
                                    if (conData.has("consoleId") && !conData.isNull("consoleId")) {
                                        String id = conData.getString("consoleId");
                                        setPsnId(id);
                                    }
                                    if (conData.has("clanTag") && !conData.isNull("clanTag")) {
                                        String clanTag = conData.getString("clanTag");
                                        setClanTag(clanTag);
                                    }
                                }
                            }
                        }
                    }
                }

                //setPsnId(creator.getString("psnId"));
                if (creator.has("imageUrl") && !creator.isNull("imageUrl")) {
                    setPlayerImageUrl(creator.getString("imageUrl"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

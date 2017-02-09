package co.crossroadsapp.overwatch.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 8/16/16.
 */
public class CommentData extends PlayerData {
    private String comment=null;
    private String id=null;
    private String created=null;
    private boolean isReported=false;

    public CommentData() {
        super();
    }

    private void setComment(String text) {
        comment = text;
    }

    public String getComment() {
        return comment;
    }

    private void setReported(boolean reported) {
        this.isReported = reported;
    }

    public boolean getReported() {
        return isReported;
    }

    private void setId(String _id) {
        id = _id;
    }

    public String getId() {
        return id;
    }

    private void setCreated(String time) {
        created = time;
    }

    public String getCreated() {
        return created;
    }

    public void toJson(JSONObject data) {
        try {
            if(data.has("user") && !data.isNull("user")) {
                JSONObject creator = data.optJSONObject("user");
                if(creator.has("_id") && !creator.isNull("_id")) {
                    setPlayerId(creator.getString("_id"));
                }
                if(creator.has("userName") && !creator.isNull("userName")) {
                    setUsername(creator.getString("userName"));
                }
                if(creator.has("consoles") && !creator.isNull("consoles")) {
                    JSONArray conArray = creator.optJSONArray("consoles");
                    if (conArray != null) {
                        for (int i = 0; i < conArray.length(); i++) {
                            JSONObject conData = (JSONObject) conArray.get(i);
                            if (conData.has("isPrimary") && !conData.isNull("isPrimary")) {
                                if (conData.getBoolean("isPrimary")) {
                                    if (conData.has("consoleId")) {
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
                if(creator.has("imageUrl") && !creator.isNull("imageUrl")) {
                    setPlayerImageUrl(creator.getString("imageUrl"));
                }
            }
            if(data.has("text") && !data.isNull("text")) {
                setComment(data.getString("text"));
            }

            if(data.has("isReported") && !data.isNull("isReported")) {
                setReported(data.getBoolean("isReported"));
            }

            if(data.has("_id") && !data.isNull("_id")) {
                setId(data.getString("_id"));
            }

            if(data.has("created") && !data.isNull("created")) {
                setCreated(data.getString("created"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

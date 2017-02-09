package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 5/19/16.
 */
public class GroupData {

    private String groupId=null;
    private String groupName=null;
    private int memberCount=0;
    private boolean clanEnabled=false;
    private String groupImageUrl=null;
    public boolean isSelected=false;
    private int eventCount=0;
    private boolean muteNotification=false;


    public void setGroupId(String id) {
        groupId = id;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupName(String name) {
        groupName = name;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setEventCount(int count) {
        eventCount = count;
    }

    public int getEventCount() {
        return this.eventCount;
    }

    public void setMemberCount(int count) {
        memberCount = count;
    }

    public int getMemberCount() {
        return this.memberCount;
    }

    public void setGroupImageUrl(String image) {
        groupImageUrl = image;
    }

    public String getGroupImageUrl() {
        if(this.groupImageUrl!=null) {
            return this.groupImageUrl;
        }
        return null;
    }

    public void setGroupClanEnabled(boolean clan) {
        clanEnabled = clan;
    }

    public boolean getGroupselected() {
        return this.isSelected;
    }

    public void setMuteNotification(boolean mute) {
        this.muteNotification = mute;
    }

    public boolean getMuteNotification() {
        return muteNotification;
    }

    public void setGroupSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getGroupClanEnabled() {
        return this.clanEnabled;
    }

    public void toJson(JSONObject jsonobject) {
        if (jsonobject!= null) {
            try {
                if (!jsonobject.isNull("groupId")) {
                    if (jsonobject.has("groupId")) {
                        setGroupId(jsonobject.getString("groupId"));
                    }
                }
                if (jsonobject.has("groupName")) {
                    setGroupName(jsonobject.getString("groupName"));
                }
                if(jsonobject.has("muteNotification")) {
                    setMuteNotification(jsonobject.getBoolean("muteNotification"));
                }
                if (jsonobject.has("memberCount")) {
                    setMemberCount(jsonobject.getInt("memberCount"));
                }
                if (jsonobject.has("eventCount")) {
                    setEventCount(jsonobject.getInt("eventCount"));
                }
                if (jsonobject.has("clanEnabled")) {
                    setGroupClanEnabled(jsonobject.getBoolean("clanEnabled"));
                }
                if (jsonobject.has("avatarPath")) {
                    setGroupImageUrl(jsonobject.getString("avatarPath"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

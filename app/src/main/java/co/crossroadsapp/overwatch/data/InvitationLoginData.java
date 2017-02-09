package co.crossroadsapp.overwatch.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sharmha on 10/18/16.
 */
public class InvitationLoginData implements Serializable {
    private HashMap<String, Object> invitation;
    private Map<String, Object> invitationRP;

    public InvitationLoginData(String invitee, String eId, String link) {
        invitation = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>(Arrays.asList(invitee.split(",")));
        invitation.put("eId", eId);
        invitation.put("invitees", list);
        invitation.put("invitationLink", link);
        //invitationRP.put("invitation", invitation);
    }

    public Map<String, Object> getRp() {
        return invitation;
    }

    public boolean isInvitePresent() {
        if(invitation!=null) {
            return true;
        }
        return false;
    }

    public void clearRp() {
        if(invitation!=null) {
            invitation = null;
        }
    }
}

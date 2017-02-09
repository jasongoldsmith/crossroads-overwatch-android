package co.crossroadsapp.overwatch.data;

import java.util.ArrayList;

/**
 * Created by sharmha on 5/26/16.
 */
public class GroupList {

    private ArrayList<GroupData> groupList;

    public GroupList() {
        groupList = new ArrayList<GroupData>();
    }

    public void appendGroupList(GroupData eData) {
        if (groupList != null) {
            groupList.add(eData);
        }
    }

    public ArrayList<GroupData> getGroupList() {
        if (this.groupList != null) {
            return this.groupList;
        }
        return null;
    }
}

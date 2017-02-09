package co.crossroadsapp.overwatch.data;

import java.util.ArrayList;

/**
 * Created by sharmha on 3/9/16.
 */
public class ActivityList {
    private ArrayList<ActivityData> activityList;

    public ActivityList() {
        activityList = new ArrayList<ActivityData>();
    }

    public void appendActivityList(ActivityData eData) {
        if (activityList != null) {
            activityList.add(eData);
        }
    }

    public ArrayList<ActivityData> getActivityList() {
        if (this.activityList != null) {
            return this.activityList;
        }
        return null;
    }
}

package co.crossroadsapp.overwatch.data;

import java.util.ArrayList;

/**
 * Created by sharmha on 2/29/16.
 */
public class EventList {
    private ArrayList<EventData> eventList;

    public EventList() {
        eventList = new ArrayList<EventData>();
    }

    public void appendEventList(EventData eData) {
        if (eventList != null) {
            eventList.add(eData);
        }
    }

    public ArrayList<EventData> getEventList() {
        if (this.eventList != null) {
            return this.eventList;
        }
        return null;
    }
}

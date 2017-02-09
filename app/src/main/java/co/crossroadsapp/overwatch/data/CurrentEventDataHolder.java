package co.crossroadsapp.overwatch.data;

/**
 * Created by sharmha on 3/30/16.
 */
public class CurrentEventDataHolder {

    private EventData data;
    private boolean joinVisible;
    public EventData getData() {return data;}
    public void setData(EventData data) {this.data = data;}

    public void setJoinVisible(boolean join){
        this.joinVisible = join;
    }
    public boolean getJoinVisible() {
        return joinVisible;
    }

    private static final CurrentEventDataHolder holder = new CurrentEventDataHolder();
    public static CurrentEventDataHolder getInstance() {return holder;}
}

package co.crossroadsapp.overwatch.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sharmha on 6/3/16.
 */
public class ConsoleData implements Parcelable {
    private String cType= null;
    private String cId = null;
    private String membershipId = null;
    private String verifyStatus = null;
    private boolean isPrimary=false;
    private String clanTag=null;

    public ConsoleData() {

    }

    protected ConsoleData(Parcel in) {
        cType = in.readString();
        cId = in.readString();
        membershipId = in.readString();
        verifyStatus = in.readString();
        isPrimary = in.readByte() != 0;
        clanTag = in.readString();
    }

    public static final Creator<ConsoleData> CREATOR = new Creator<ConsoleData>() {
        @Override
        public ConsoleData createFromParcel(Parcel in) {
            return new ConsoleData(in);
        }

        @Override
        public ConsoleData[] newArray(int size) {
            return new ConsoleData[size];
        }
    };

    public void setcId(String id) {
        this.cId = id;
    }

    public String getcId() {
        return cId;
    }

    public void setcType(String type) {
        this.cType = type;
    }

    public String getcType() {
        return cType;
    }

    public void setMembershipId(String mId) {
        this.membershipId = mId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setVerifyStatus(String status) {
        this.verifyStatus = status;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setClanTag(String tag) {
        this.clanTag = tag;
    }

    public String getClanTag() {
        return clanTag;
    }

    public void setPrimary(boolean primary) {
        this.isPrimary = primary;
    }
    public boolean isPrimary() {
        return isPrimary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cType);
        dest.writeString(cId);
        dest.writeString(membershipId);
        dest.writeString(verifyStatus);
        dest.writeByte((byte) (isPrimary ? 1 : 0));
        dest.writeString(clanTag);
    }
}

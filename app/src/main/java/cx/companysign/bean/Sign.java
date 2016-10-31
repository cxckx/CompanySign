package cx.companysign.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cxcxk on 2016/10/13.
 */
public class Sign implements Parcelable {
    private Integer SignId;
    private Integer Signsign;
    private String signDate;
    private Integer signUser;
    private String longitude;
    private String latitude;
    private String signAddress;
    private String radius;

    public Integer getSignId() {
        return SignId;
    }

    public void setSignId(Integer signId) {
        SignId = signId;
    }

    public Integer getSignsign() {
        return Signsign;
    }

    public void setSignsign(Integer signsign) {
        Signsign = signsign;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public Integer getSignUser() {
        return signUser;
    }

    public void setSignUser(Integer signUser) {
        this.signUser = signUser;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SignId);
        dest.writeInt(Signsign);
        dest.writeString(signDate);
        dest.writeInt(signUser);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(signAddress);
        dest.writeString(radius);
    }

    public static final Parcelable.Creator<Sign> CREATOR = new Creator() {
        @Override
        public Sign createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Sign p = new Sign();
            p.setSignId(source.readInt());
            p.setSignsign(source.readInt());
            p.setSignDate(source.readString());
            p.setSignUser(source.readInt());
            p.setLongitude(source.readString());
            p.setLatitude(source.readString());
            p.setSignAddress(source.readString());
            p.setRadius(source.readString());
            return p;
        }

        @Override
        public Sign[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Sign[size];
        }
    };
}

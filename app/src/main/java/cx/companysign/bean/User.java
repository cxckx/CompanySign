package cx.companysign.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class User implements Parcelable{
    private Integer userId;
    private String userName;
    private String userPhone;
    private Integer userSex;
    private Long userBirthday;
    private Integer userCompanyId;
    private String userIconHeader;
    private String userBranch;
    private String userPartCompany;
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public Integer getUserSex() {
        return userSex;
    }
    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }
    public Long getUserBirthday() {
        return userBirthday;
    }
    public void setUserBirthday(Long userBirthday) {
        this.userBirthday = userBirthday;
    }
    public Integer getUserCompanyId() {
        return userCompanyId;
    }
    public void setUserCompanyId(Integer userCompanyId) {
        this.userCompanyId = userCompanyId;
    }
    public String getUserIconHeader() {
        return userIconHeader;
    }
    public void setUserIconHeader(String userIconHeader) {
        this.userIconHeader = userIconHeader;
    }
    public String getUserBranch() {
        return userBranch;
    }
    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }
    public String getUserPartCompany() {
        return userPartCompany;
    }
    public void setUserPartCompany(String userPartCompany) {
        this.userPartCompany = userPartCompany;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userPhone);
        dest.writeInt(userSex);
        dest.writeLong(userBirthday);
        dest.writeInt(userCompanyId);
        dest.writeString(userIconHeader);
        dest.writeString(userBranch);
        dest.writeString(userPartCompany);

    }

    public static final Parcelable.Creator<User> CREATOR = new Creator(){
                @Override
               public User createFromParcel(Parcel source) {
                        // TODO Auto-generated method stub
                        // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
                        User p = new User();
                        p.setUserId(source.readInt());
                    p.setUserName(source.readString());
                    p.setUserPhone(source.readString());
                    p.setUserSex(source.readInt());
                    p.setUserBirthday(source.readLong());
                    p.setUserCompanyId(source.readInt());
                    p.setUserIconHeader(source.readString());
                    p.setUserBranch(source.readString());
                        p.setUserPartCompany(source.readString());
                        return p;
                    }

                     @Override
                     public User[] newArray(int size) {
                         // TODO Auto-generated method stub
                         return new User[size];
                 }
             };

    public static User UserEntityToUser(UserEntity user){
        User entity = new User();
        entity.setUserBirthday(user.getUserBirthday());
        entity.setUserBranch(user.getUserBranch());
        entity.setUserCompanyId(user.getUserCompanyId());
        entity.setUserId(user.getUserId());
        entity.setUserIconHeader(user.getUserIconHeader());
        entity.setUserName(user.getUserName());
        entity.setUserPartCompany(user.getUserPartCompany());
        entity.setUserPhone(user.getUserPhone());
        entity.setUserSex(user.getUserSex());
        return entity;
    }
}

package cx.companysign.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by cxcxk on 2016/10/27.
 */
@Entity
public class UserEntity {
    @Id(autoincrement = true)
    private Long id;
    private Integer userId;
    private String userName;
    @Unique
    private String userPhone;

    private Integer userSex;
    private Long userBirthday;
    private Integer userCompanyId;
    private String userIconHeader;
    private String userBranch;
    private String userPartCompany;
    @Generated(hash = 1055408845)
    public UserEntity(Long id, Integer userId, String userName, String userPhone,
            Integer userSex, Long userBirthday, Integer userCompanyId,
            String userIconHeader, String userBranch, String userPartCompany) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userSex = userSex;
        this.userBirthday = userBirthday;
        this.userCompanyId = userCompanyId;
        this.userIconHeader = userIconHeader;
        this.userBranch = userBranch;
        this.userPartCompany = userPartCompany;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getUserId() {
        return this.userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPhone() {
        return this.userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public Integer getUserSex() {
        return this.userSex;
    }
    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }
    public Long getUserBirthday() {
        return this.userBirthday;
    }
    public void setUserBirthday(Long userBirthday) {
        this.userBirthday = userBirthday;
    }
    public Integer getUserCompanyId() {
        return this.userCompanyId;
    }
    public void setUserCompanyId(Integer userCompanyId) {
        this.userCompanyId = userCompanyId;
    }
    public String getUserIconHeader() {
        return this.userIconHeader;
    }
    public void setUserIconHeader(String userIconHeader) {
        this.userIconHeader = userIconHeader;
    }
    public String getUserBranch() {
        return this.userBranch;
    }
    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }
    public String getUserPartCompany() {
        return this.userPartCompany;
    }
    public void setUserPartCompany(String userPartCompany) {
        this.userPartCompany = userPartCompany;
    }
   
    public static UserEntity UserToUserEntity(User user){
         UserEntity entity = new UserEntity();
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

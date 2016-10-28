package cx.companysign.bean;

/**
 * Created by cxcxk on 2016/10/12.
 */
public class Branch {
    private Integer branchId;
    private String branchName;
    private Integer companyId;
    public Integer getBranchId() {
        return branchId;
    }
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}

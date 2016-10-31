package cx.companysign.utils;

import cx.companysign.bean.User;

/**
 * Created by cxcxk on 2016/10/10.
 */
public interface Receiver {

    int ALL_CONTRACT = 0;
    int PARTCOMPANY_CONTRACT = 1;
    int BRANCH_CONTRACT = 2;

    void translateUser(User user, String pwd);

    void refreshContract();

    void showWhat(int type);
}

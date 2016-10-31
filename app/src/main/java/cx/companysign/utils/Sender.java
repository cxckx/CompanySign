package cx.companysign.utils;

import java.util.ArrayList;
import java.util.List;

import cx.companysign.bean.User;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class Sender {
    private List<Receiver> receivers = new ArrayList<>();
    private static Sender sender;

    private Sender() {

    }

    public static Sender getInstance() {
        if (sender == null) {
            sender = new Sender();
        }

        return sender;
    }

    public void register(Receiver receiver) {
        receivers.add(receiver);
    }

    public void unRegister(Receiver receiver) {
        receivers.remove(receiver);
    }

    public void translateUser(User user, String pwd) {
        for (Receiver receiver : receivers) {
            receiver.translateUser(user, pwd);
        }
    }

    public void refreshContract() {
        for (Receiver receiver : receivers) {
            receiver.refreshContract();
        }
    }

    public void showWhat(int type) {
        for (Receiver receiver : receivers) {
            receiver.showWhat(type);
        }
    }
}

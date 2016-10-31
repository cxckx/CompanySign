package cx.companysign.utils;

/**
 * Created by cxcxk on 2016/10/27.
 */
public class ListenerSingleUtils {

    private AbsListener mAbsListener;
    private static Object sObject = new Object();
    private static ListenerSingleUtils sListenerSingleUtils;

    private ListenerSingleUtils() {
    }

    ;

    public static ListenerSingleUtils getInstance() {
        if (sListenerSingleUtils == null) {
            synchronized (sObject) {
                if (sListenerSingleUtils == null) {
                    sListenerSingleUtils = new ListenerSingleUtils();
                }
            }
        }


        return sListenerSingleUtils;
    }

    public AbsListener getListener() {
        return mAbsListener;
    }

    public void setListener(AbsListener listener) {
        this.mAbsListener = listener;
    }

    public void listener(String password, String phone) {
        if (this.mAbsListener != null) {
            mAbsListener.listenerUpdate(password, phone);
        }
    }
}

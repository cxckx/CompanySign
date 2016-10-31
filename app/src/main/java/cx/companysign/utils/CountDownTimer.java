package cx.companysign.utils;

/**
 * Created by cxcxk on 2016/2/27.
 */
public class CountDownTimer extends android.os.CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        notify.notifyProgress(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        notify.notifyFinish();
    }

    public CountDownTimer(long millisInFuture, long countDownInterval, Notify notify) {
        super(millisInFuture, countDownInterval);
        this.notify = notify;
    }

    private Notify notify;
}

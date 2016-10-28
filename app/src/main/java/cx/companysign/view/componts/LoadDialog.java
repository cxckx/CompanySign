package cx.companysign.view.componts;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import cx.companysign.R;

/**
 * Created by cxcxk on 2016/10/24.
 */
public class LoadDialog extends Dialog {
    public LoadDialog(Context context) {
        super(context);
    }

    public LoadDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private String message;

        private ProgressBar bar;
        private boolean cancleable = true;
        private boolean touchOutSide = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }



        public Builder setCancleable(boolean cancleable){
            this.cancleable = cancleable;
            return this;
        }

        public Builder setCancleableOnTouchOutSide(boolean cancleable){
            this.touchOutSide = cancleable;
            return this;
        }



        public LoadDialog create() {

            final LoadDialog dialog = new LoadDialog(context, R.style.Dialog);
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.load__progressbar_layout,null);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(touchOutSide);
            dialog.setCancelable(cancleable);

            return dialog;
        }

    }

}

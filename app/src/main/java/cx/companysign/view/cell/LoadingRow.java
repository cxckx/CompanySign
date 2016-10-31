package cx.companysign.view.cell;

import android.content.Context;

import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;


/**
 * Created by cxcxk on 2016/4/15.
 */
public class LoadingRow extends RelativeLayout {

    ProgressBar progressBar;

    public LoadingRow(Context context) {
        super(context);
        init(context);
    }

    public LoadingRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        progressBar = new ProgressBar(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(progressBar, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(), 32), MeasureSpec.EXACTLY));
    }
}

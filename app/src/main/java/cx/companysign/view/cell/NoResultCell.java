package cx.companysign.view.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;


/**
 * Created by cxcxk on 2016/4/22.
 */
public class NoResultCell extends RelativeLayout {

    TextView view;

    public NoResultCell(Context context) {
        super(context);
        init(context);
    }

    public NoResultCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoResultCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        view = new TextView(context);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);

        addView(view, params);
    }

    public void setText(String text){
        view.setText(text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(), 40),MeasureSpec.EXACTLY));
    }
}

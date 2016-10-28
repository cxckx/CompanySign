package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;

import cx.companysign.R;

/**
 * Created by cxcxk on 2016/10/15.
 */
public class SortLabelView extends LinearLayout {
    TextView text;
    public SortLabelView(Context context) {
        super(context);
        init(context);
    }

    public SortLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SortLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setOrientation(VERTICAL);

        text = new TextView(context);
        text.setTextColor(Color.WHITE);

        setBackgroundColor(Color.parseColor("#e0e0e0"));

        addView(text, LayoutHelper.createLinearLayout(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, AndroidUtils.dip2px(context, 16), AndroidUtils.dip2px(context, 4), 0, AndroidUtils.dip2px(context, 4)));
    }

    public void setText(String text){
        this.text.setText(text);
    }
}


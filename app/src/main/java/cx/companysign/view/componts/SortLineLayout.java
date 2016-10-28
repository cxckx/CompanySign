package cx.companysign.view.componts;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

import cx.companysign.R;

/**
 * Created by cxcxk on 2016/10/15.
 */
public class SortLineLayout extends LinearLayout {
    private List<TextView> textViewList = new ArrayList<>();
    private Context mContext;
    public SortLineLayout(Context context) {
        super(context);
        init(context);
    }

    public SortLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SortLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        setOrientation(VERTICAL);
        mContext = context;
        setPadding(AndroidUtils.dip2px(context, 8), 0, AndroidUtils.dip2px(context, 8), 0);
        setBackgroundColor(Color.parseColor("#55e0e0e0"));
    }
    int selectIndex = -1;

    public void setSelect(int position){
        if(getChildCount() == 0) return;
        TextView view = (TextView) getChildAt(position);
        if(selectIndex != -1){
            TextView textView = (TextView) getChildAt(selectIndex);
            textView.setTextColor(Color.BLACK);
        }
        view.setTextColor(getResources().getColor(R.color.colorAccent));
        selectIndex = position;
    }

    public void clearLabel(){
        this.removeAllViews();
    }

    public void addLable(final List<String> list){
        for (String key:list){
            TextView textView = new TextView(mContext);
            textView.setClickable(true);
            textView.setText(key);
            textView.setTextColor(Color.BLACK);
            textView.setTag(getChildCount());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(AndroidUtils.dip2px(mContext, 4));
            this.addView(textView, LayoutHelper.createLinearLayout(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 1f));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelect(Integer.parseInt(v.getTag().toString()));
                    if (listener != null){
                        listener.onItemClick(Integer.parseInt(v.getTag().toString()));
                    }
                }
            });
        }
    }

    private OnItemClickListener listener;

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
       void onItemClick(int position);
    }
}

package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;


/**
 * Created by cxcxk on 2016/4/21.
 */
public class TextCell extends RelativeLayout {

    TextView view;
    private static Paint paint;

    public TextCell(Context context) {
        super(context);
        init(context);
    }

    public TextCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setBackgroundColor(Color.WHITE);
        view = new TextView(context);
        view.setTextColor(16);
        view.setTextColor(Color.GRAY);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_VERTICAL);
        params.leftMargin = AndroidUtils.dip2px(context, 32);
        addView(view, params);

        if(paint == null){
            paint = new Paint();
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#e0e0e0"));
        }
    }

    public void setText(String text,boolean isNeedDivider){
        view.setText(text);
        setWillNotDraw(!isNeedDivider);
    }

    public String getText(){
        return  view.getText().toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine(AndroidUtils.dip2px(getContext(), 16),canvas.getHeight()-1,canvas.getWidth()-AndroidUtils.dip2px(getContext(), 16),canvas.getHeight()-1,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(),40),MeasureSpec.EXACTLY));
    }
}

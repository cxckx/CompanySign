package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

;import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.view.componts.DefaultHeadPortraitView;

import cx.companysign.R;

/**
 * Created by cxcxk on 2016/4/21.
 */
public class TextImageCell extends LinearLayout {
    CircleTextCell circleTextCell;
    TextView view;
    ImageView imageView;
    private static Paint paint;



    public TextImageCell(Context context) {
        super(context);
        init(context);
    }

    public TextImageCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextImageCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        setOrientation(HORIZONTAL);
        circleTextCell = new CircleTextCell(context);


        view = new TextView(context);
        view.setTextSize(AndroidUtils.dip2px(context, 8));
        view.setTextColor(Color.BLACK);

        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.leftMargin = AndroidUtils.dip2px(context,16);
        params.rightMargin = AndroidUtils.dip2px(context,16);


        addView(circleTextCell, params);

        LayoutParams params1 = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
        params1.gravity = Gravity.CENTER;

        addView(view, params1);

        addView(imageView,params);

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

    public void setShowText(String text){
        circleTextCell.setShowText(text);
    }

    public void setColor(int index){
        circleTextCell.setColorIndex(index);
    }

    public void setOpen(boolean open){
        if (open){
            imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }else {
            imageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
            canvas.drawLine(AndroidUtils.dip2px(getContext(), 16),canvas.getHeight()-1,canvas.getWidth()-AndroidUtils.dip2px(getContext(), 16),canvas.getHeight()-1,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(),56),MeasureSpec.EXACTLY));
    }
}

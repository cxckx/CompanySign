package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;

import cx.companysign.R;


/**
 * Created by cxcxk on 2016/4/21.
 */
public class CircleTextCell extends View {

    private static Paint textPaint, circlePaint;
    String text = "";


    int colors[] = {R.color.organce, R.color.blue, R.color.green};
    int mCurrentColor = R.color.blue;


    public CircleTextCell(Context context) {
        super(context);
        init(context);
    }

    public CircleTextCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleTextCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setDither(true);
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            textPaint.setColor(Color.WHITE);
            textPaint.setStrokeWidth(1);
            textPaint.setTextSize(AndroidUtils.dip2px(context, 15));
        }

        if (circlePaint == null) {
            circlePaint = new Paint();
            circlePaint.setDither(true);
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(getResources().getColor(mCurrentColor));
        }


    }

    public void setColorIndex(int index) {
        if (index >= 0 && index < colors.length) {
            mCurrentColor = colors[index];
        }
        circlePaint.setColor(getResources().getColor(mCurrentColor));
        invalidate();
    }

    public void setShowText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getHeight() * 3 / 8, circlePaint);
        float width = textPaint.measureText(text);
        float height = textPaint.getFontMetrics().ascent + textPaint.getFontMetrics().descent;
        canvas.drawText(text, (canvas.getWidth() - width) / 2, (canvas.getHeight() - height) / 2, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(), 56), MeasureSpec.EXACTLY), heightMeasureSpec);
    }
}

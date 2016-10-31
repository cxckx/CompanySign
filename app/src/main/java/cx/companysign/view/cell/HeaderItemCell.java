package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;

import cx.companysign.R;


/**
 * Created by cxcxk on 2016/8/1.
 */
public class HeaderItemCell extends View {

    private static Paint rectPaint;
    private static Paint textPaint;
    private String text = "";

    public HeaderItemCell(Context context) {
        super(context);
        init();
    }

    public HeaderItemCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderItemCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //setPadding(AndroidUtils.dip2px(getContext(),16),AndroidUtils.dip2px(getContext(),16),0,0);
        if (rectPaint == null) {
            rectPaint = new Paint();
            rectPaint.setColor(getResources().getColor(R.color.colorPrimary));
            rectPaint.setAntiAlias(true);
            rectPaint.setDither(true);
            rectPaint.setStyle(Paint.Style.FILL);
        }
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setAntiAlias(true);
            textPaint.setDither(true);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setStrokeCap(Paint.Cap.ROUND);
            textPaint.setStrokeJoin(Paint.Join.ROUND);
            textPaint.setStrokeWidth(1.5f);
            float density = getResources().getDisplayMetrics().density;
            textPaint.setTextSize(12 * density);
        }
        setWillNotDraw(false);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    int color;

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        textPaint.setTextSize(canvas.getWidth() / 3);
        rectPaint.setColor(color);
        Rect rect = new Rect();
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getWidth() / 2, canvas.getWidth() / 2 - AndroidUtils.dip2px(getContext(), 10), rectPaint);

        float size = textPaint.measureText(text.substring(0, 1));
        float height = textPaint.getFontMetrics().ascent + textPaint.getFontMetrics().descent;
        canvas.drawText(text.substring(0, 1), (canvas.getWidth() - size) / 2, (canvas.getHeight() - height) / 2, textPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = AndroidUtils.dip2px(getContext(), 72);
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);// 60,480
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);


        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = AndroidUtils.dip2px(getContext(), 72);
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}

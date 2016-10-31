package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;

/**
 * Created by cxcxk on 2016/10/24.
 */
public class ImageTextCell extends View {

    private static Paint imagePaint, textPaint;
    int resId;
    CharSequence text;

    public ImageTextCell(Context context) {
        super(context);
        init(context);
    }

    public ImageTextCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageTextCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (imagePaint == null) {
            imagePaint = new Paint();
            imagePaint.setAntiAlias(true);
            imagePaint.setDither(true);
            imagePaint.setColor(Color.parseColor("#e0e0e0"));
        }
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setDither(true);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(getResources().getDisplayMetrics().density * 16);
            textPaint.setStrokeWidth(1.5f);
        }
        invalidate();
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(@DrawableRes int resId) {
        this.resId = resId;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        //bitmap = Bitmap.createScaledBitmap(bitmap,AndroidUtils.dip2px(getContext(),50),canvas.getHeight()-AndroidUtils.dip2px(getContext(), 32),false);
        canvas.drawBitmap(bitmap, AndroidUtils.dip2px(getContext(), 16), AndroidUtils.dip2px(getContext(), 16), imagePaint);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        canvas.drawText(text.toString(), bitmap.getWidth() + AndroidUtils.dip2px(getContext(), 48), canvas.getHeight() / 2 - (textPaint.ascent() + textPaint.descent()) / 2, textPaint);
        canvas.drawLine(AndroidUtils.dip2px(getContext(), 16), canvas.getHeight() - 1, canvas.getWidth() - AndroidUtils.dip2px(getContext(), 16), canvas.getHeight() - 1, imagePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(), 56), MeasureSpec.EXACTLY));
    }
}

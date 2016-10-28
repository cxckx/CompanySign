package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;

import cx.companysign.view.componts.ImageViewPlus;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class ContractImageItemCell extends LinearLayout {

    ImageViewPlus  cell;
    TextView name;
    Paint linePaint;

    public ContractImageItemCell(Context context) {
        super(context);
        init(context);
    }

    public ContractImageItemCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContractImageItemCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        setOrientation(HORIZONTAL);

        cell = new ImageViewPlus(context);
        //cell.initParam(R.drawable.icon,true);
        //cell.setBackgroundResource(R.drawable.circle_shape);
        LayoutParams params = LayoutHelper.createLinearLayout(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER);
        params.leftMargin = AndroidUtils.dip2px(context, 16);
        params.topMargin = AndroidUtils.dip2px(context,0);
        addView(cell, params);

        //cell.setText("陈晓");
        params = LayoutHelper.createLinearLayout(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT);
        params.leftMargin = AndroidUtils.dip2px(context, 16);
        params.topMargin = AndroidUtils.dip2px(context, 16);
        name = new TextView(context);
        name.setTextSize(6 * getResources().getDisplayMetrics().density);
        name.setTextColor(Color.BLACK);
        //name.setText("陈晓");

        addView(name, params);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(Color.parseColor("#e0e0e0"));

        //setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(),72),MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(name.getX(), canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1, linePaint);
    }

    public void setText(String name) {
       this.name.setText(name);
       setWillNotDraw(false);
    }

    public void setImage(Bitmap bitmap){
        cell.setImageBitmap(bitmap);
    }
}

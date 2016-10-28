package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;


/**
 * Created by cxcxk on 2016/4/6.
 */
public class EtcParentCell extends RelativeLayout {

    boolean right,top,end;
    int margin,numColoums = 3;// numColoums 与 GridView 每一行item对应

    static Paint paint;
    int colorLine;

    public EtcParentCell(Context context) {
        super(context);
        init();
    }

    public EtcParentCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EtcParentCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void  init(){
        if(paint == null){
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
        }
        setBackgroundColor(Color.WHITE);
    }

    public void setBorderColor(int color){
        this.colorLine = color;
        paint.setColor(colorLine);
        setWillNotDraw(false);
    }

    /**
     * 处理每个位置的边框重叠
     * @param position
     */
    public void setPosition(int position){
        this.right = (position%numColoums == numColoums-1);
        this.top = position/numColoums == 0;
        setWillNotDraw(false);
    }
    public void setNumColoums(int numColoums){
        this.numColoums = numColoums;
        setWillNotDraw(false);
    }

    /**
     * 设置外边距 为适配宽度
     * @param margin
     */
    public void setMargin(int margin){
        this.margin = margin;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(MeasureSpec.makeMeasureSpec((int) AndroidUtils.getGridItemWidth(getContext(), numColoums, margin), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) AndroidUtils.getGridItemWidth(getContext(), numColoums, margin), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0.1f,0,0.1f,canvas.getHeight()-0.1f,paint);//左
        if(right||end){
            canvas.drawLine(canvas.getWidth()-0.1f,0,canvas.getWidth()-0.1f,canvas.getHeight(),paint);//右
        }
        if(top){
            canvas.drawLine(0,0,canvas.getWidth(),0,paint);//上
        }
        canvas.drawLine(0, canvas.getHeight() - 0.1f, canvas.getWidth(), canvas.getHeight() - 0.1f, paint);//下
    }

    public void setEnd(boolean end) {
        this.end = end;
        setWillNotDraw(false);
    }
}

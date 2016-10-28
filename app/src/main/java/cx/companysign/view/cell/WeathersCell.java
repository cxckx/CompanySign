package cx.companysign.view.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cx.companysign.bean.WeatherEntity;


/**
 * Created by cxcxk on 2016/4/14.
 */
public class WeathersCell extends LinearLayout {

    WeatherCell cell[] = new WeatherCell[4];

    public WeathersCell(Context context) {
        super(context);
        init(context);
    }

    public WeathersCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeathersCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){

        setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
        for (int i = 0;i<cell.length;i++){
            cell[i] = new WeatherCell(context);
            cell[i].setEnd(i == cell.length - 1);
            addView(cell[i], params);
        }

    }

    public void setMargin(int margin){
        for (int i = 0;i<cell.length;i++){
            cell[i].setMargin(margin);
        }
    }

    public void setnumColums(int num){
        for (int i = 0;i<cell.length;i++){
            cell[i].setNumColoums(num);
            cell[i].setPosition(i);
        }
    }

    public void setDays(WeatherEntity[]days){
        for (int i = 0;i<days.length;i++){
            cell[i].setDayAndNight("日", "夜");
            cell[i].setTemp(days[i].getTemp());
            cell[i].setTime(days[i].getWeek());
            cell[i].setWind(days[i].getWind());
            cell[i].setDayAndNightImage(days[i].getDayImage(),days[i].getNightImage());
        }
    }
}

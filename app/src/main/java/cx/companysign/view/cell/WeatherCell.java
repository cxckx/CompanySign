package cx.companysign.view.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by cxcxk on 2016/4/14.
 */
public class WeatherCell extends EtcParentCell {

    TextView week, day, night, temp, wind;
    ImageView dayImage, nightImage;


    public WeatherCell(Context context) {
        super(context);
        init(context);
    }

    public WeatherCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeatherCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LinearLayout layoutAll = new LinearLayout(context);
        layoutAll.setOrientation(LinearLayout.VERTICAL);


        week = new TextView(context);
        week.setTextSize(20);
        week.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        layoutAll.addView(week, params);

        day = new TextView(context);
        day.setTextSize(12);
        day.setTextColor(Color.BLACK);

        night = new TextView(context);
        night.setTextSize(12);
        night.setTextColor(Color.BLACK);

        dayImage = new ImageView(context);

        nightImage = new ImageView(context);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;

        params1.rightMargin = AndroidUtils.dip2px(context, 4);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(day, params1);

        layout.addView(dayImage, params1);
        layout.addView(night, params1);
        layout.addView(nightImage, params1);

        layoutAll.addView(layout, params);

        temp = new TextView(context);
        temp.setTextSize(16);
        temp.setTextColor(Color.BLACK);
        layoutAll.addView(temp, params);

        wind = new TextView(context);
        wind.setTextColor(Color.BLACK);

        layoutAll.addView(wind, params);
        RelativeLayout.LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.addRule(CENTER_IN_PARENT);
        addView(layoutAll, params2);
    }


    public void setTime(String time) {
        this.week.setText(time);
    }

    public void setDayAndNight(String day, String night) {
        this.day.setText(day);
        this.night.setText(night);
    }

    public void setTemp(String temp) {
        this.temp.setText(temp);
    }

    public void setWind(String wind) {
        this.wind.setText(wind);
    }

    public void setDayAndNightImage(String dayImage, String nightImage) {
        Picasso.with(getContext())
                .load(dayImage)
                .into(this.dayImage);
        Picasso.with(getContext())
                .load(nightImage)
                .into(this.nightImage);

    }


}

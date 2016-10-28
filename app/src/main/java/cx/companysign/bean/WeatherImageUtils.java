package cx.companysign.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxcxk on 2016/04/12.
 */
public class WeatherImageUtils {

    public static Map<String,Drawable> map = new HashMap<>();




    public static String getPollutionLevel(int pm){
       if(pm <= 50){
           return "优";
       }else if(pm > 50 && pm<=100){
           return "良";
       }else if(pm > 100 && pm <= 150){
           return "轻度污染";
       }else if(pm > 150 && pm <= 200){
           return "中度污染";
       }else if(pm > 200 && pm <= 300){
           return "重度污染";
       }
        return "严重污染";
    }
}

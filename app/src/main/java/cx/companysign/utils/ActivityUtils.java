package cx.companysign.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxcxk on 2016/10/26.
 */
public class ActivityUtils {

    private static  List<Activity> mActivities = new ArrayList<>();

    public static void addActiviy(Activity activity){
        mActivities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activity.finish();
        mActivities.remove(activity);
    }

    public static  void finishAll(){
         for (Activity activity:mActivities){
             if(!activity.isFinishing()){
                 activity.onBackPressed();
             }

         }

        mActivities.clear();
    }

    public static void finishAll2() {
        for (Activity activity : mActivities) {
            if (activity != null) {
                activity.finish();
            }

        }

        mActivities.clear();
    }
}

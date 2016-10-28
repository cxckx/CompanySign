package cx.companysign;


import com.baidu.mapapi.SDKInitializer;
import com.example.cxcxk.android_my_library.AppApplication;

import cn.smssdk.SMSSDK;
import cx.companysign.utils.LocationService;

/**
 * Created by cxcxk on 2016/10/9.
 */
public class CompanySignApp extends AppApplication {

    public static LocationService service;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        service = new LocationService(getApplicationContext());
        SMSSDK.initSDK(this, "17f36c0efc296", "878ed52afaded4a86f5adc66566c226f");

    }

}

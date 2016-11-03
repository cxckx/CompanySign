package cx.companysign.utils;

import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.utils.WebServiceUtil;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class WebServiceDataNetOperator extends NetDataOperater<String> {
    @Override
    protected String onNetWork(Attribute attribute) {

        WebServiceUtil serviceUtil = new WebServiceUtil(attribute, mHandler);
        serviceUtil.setIsDotNet(false);
        return serviceUtil.GetString(attribute.getUrl(), attribute.getNameSpace(), attribute.getMethodName(), attribute.getParams());
    }
}

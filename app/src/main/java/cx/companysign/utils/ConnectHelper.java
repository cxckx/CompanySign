package cx.companysign.utils;

import com.example.cxcxk.android_my_library.utils.NetDataOperater;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class ConnectHelper {

    public static String URL = "http://15k914i506.iok.la:12286/CompanyWebService/CorePort?wsdl";
    public static String NAMESPACE = "http://core/";


    public static class METHOD {
        public static String LOGIN = "login";
        public static String GETEMPLOYEE = "getEmployee";
        public static String GETUSERBYCOMPANY = "getUserByCompany";
        public static String GETPARTCOMPANY = "getPartCompany";
        public static String GETBRANCH = "getBranch";
        public static String GETUSERBYBRANCHNAME = "getUserByBranchName";
        public static String GETUSERBYPARTCOMPANYNAME = "getUserByPartCompanyName";
        public static String GETSIGN = "getSign";
        public static String GETSIGNOUT = "getSignOut";
        public static String INSERTSIGNIN = "insertSignIn";
        public static String INSERTSIGNOUT = "insertSignOut";
        public static String GETSIGNOUTBYDATE = "getSignOutByDate";
        public static String ISSIGN = "isSign";
        public static String ISSIGNOUT = "isSignOut";
        public static String UPDATEPWD = "updatePwd";
        public static String ISHAVEPHONE = "isHavePhone";
        public static String AUTHOR = "author";
        public static String ISAUTHOR = "isAuthor";
        public static String LOGINOUT = "loginOut";
        public static String GETPWD = "XMLPD";
        public static String UPDATEINFO = "updateInfo";
    }

    public static NetDataOperater.Attribute createAttribute() {
        NetDataOperater.Attribute attribute = new NetDataOperater.Attribute();
        attribute.setUrl(ConnectHelper.URL);
        attribute.setNameSpace(ConnectHelper.NAMESPACE);
        return attribute;
    }

    public static NetDataOperater.Attribute createWeatherAttr() {
        NetDataOperater.Attribute attribute = new NetDataOperater.Attribute();
        String url = "http://api.map.baidu.com/telematics/v3/weather";
        attribute.setUrl(url);
        Map<String, String> map = new HashMap<>();
        map.put("ak", "p47C5ZP0pAtyOZfx3DfgMb0G4TVMKlpD");
        map.put("output", "json");
        map.put("mcode", "04:4D:1F:2C:55:C2:31:9A:AB:56:F6:45:87:D5:33:0D:BE:B6:58:E6;cx.companysign");
        attribute.setParams(map);
        return attribute;
    }
}

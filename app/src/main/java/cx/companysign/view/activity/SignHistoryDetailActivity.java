package cx.companysign.view.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.bean.Sign;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.WebServiceDataNetOperator;

/**
 * Created by cxcxk on 2016/10/14.
 */
public class SignHistoryDetailActivity extends BaseActivity{

    ActionBar actionBar;
    MapView signMap,signOutMap;
    TextView signView,signOutView;
    BitmapDescriptor mCurrentMarker;
    NetDataOperater operater;
    Sign sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_history_detail_layout);
        ActivityUtils.addActiviy(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("历史详情");
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                }
            }
        });
        signMap = (MapView) findViewById(R.id.sign_map);
        signOutMap = (MapView) findViewById(R.id.signout_map);
        signOutView = (TextView) findViewById(R.id.signout_location);
        signView = (TextView) findViewById(R.id.sign_location);
        sign = getIntent().getParcelableExtra("sign");
        signView.setText(sign.getSignDate() + "\n" + sign.getSignAddress());

        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_openmap_mark);

        BaiduMap map = signMap.getMap();

        initMap(map,sign);

        operater = new WebServiceDataNetOperator();
        NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
        attribute.setMethodName(ConnectHelper.METHOD.GETSIGNOUTBYDATE);
        Map<String,String > mapHash = new HashMap<>();
        mapHash.put("arg0",sign.getSignUser()+"");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = format.parse(sign.getSignDate());
            mapHash.put("arg1",new SimpleDateFormat("yyyy-MM-dd").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        attribute.setParams(mapHash);

        operater.request(attribute, "1");

        attribute.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if( o.toString() == "") return ;
                if (o.toString().equals("null")) {
                    signOutMap.setVisibility(View.GONE);
                    signOutView.setText("未签退");
                    signOutView.setGravity(Gravity.CENTER);
                    signOutView.setTextSize(AndroidUtils.dip2px(SignHistoryDetailActivity.this, 8));
                } else {
                    BaiduMap map = signOutMap.getMap();
                    Sign sign = new Sign();
                    JSONObject object = JSON.parseObject(o.toString());
                    sign.setLatitude(object.getString("longitude"));
                    sign.setLongitude(object.getString("latitude"));
                    sign.setSignDate(object.getString("signDate"));
                    sign.setSignId(object.getInteger("signId"));
                    sign.setSignUser(object.getInteger("signUser"));
                    sign.setSignsign(object.getInteger("signsign"));
                    sign.setSignAddress(object.getString("signAddress"));
                    sign.setRadius(object.getString("radius"));
                    initMap(map, sign);
                    signOutView.setText(sign.getSignDate() + "\n" + sign.getSignAddress());
                }
            }

            @Override
            public void OnError(String s) {

            }

            @Override
            public void OnProgress(int i) {

            }
        });

    }

    private void initMap(BaiduMap map,Sign sign){
        double signLongitude = Double.parseDouble(sign.getLongitude());
        double signLatitude = Double.parseDouble(sign.getLatitude());
        map.setMyLocationEnabled(true);
        map.setIndoorEnable(true);
        LatLng cenpt = new LatLng(signLongitude,signLatitude);

        MarkerOptions markerOptions = new MarkerOptions().icon(mCurrentMarker).position(cenpt);
        //获取添加的 marker 这样便于后续的操作
        Marker marker = (Marker) map.addOverlay(markerOptions);

        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(Float.parseFloat(sign.getRadius()))
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(signLatitude)
                .longitude(signLongitude).build();
        map.setMyLocationData(locData);

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        map.setMapStatus(mMapStatusUpdate);
    }
}

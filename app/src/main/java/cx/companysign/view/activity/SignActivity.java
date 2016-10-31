package cx.companysign.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.CompanySignApp;
import cx.companysign.R;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.LocationService;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.componts.CalendarView;

/**
 * Created by cxcxk on 2016/10/13.
 */
public class SignActivity extends BaseActivity {

    private LocationService service;
    private BDLocationListener locationListener;
    private MapBaseIndoorMapInfo mMapBaseIndoorMapInfo;
    ActionBar actionBar;
    CalendarView calendarView;
    MapView mapView;
    BaiduMap map;
    private boolean isFirstLoc = true;
    BitmapDescriptor mCurrentMarker;
    String address;
    float radius;
    TextView addressView;
    double longitude, latitude;
    NetDataOperater operator;
    Button signBtn;
    int isSign = 0;
    NetDataOperater.Attribute attr;
    boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signlayout);
        ActivityUtils.addActiviy(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("签到/签退");
        actionBar.setSubTitle("请按照签到流程进行");
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.addMenu(0, R.drawable.ic_date_range_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                } else if (i == 0) {
                    Intent intent = new Intent(SignActivity.this, SignHistoryActivity.class);
                    intent.putExtra("userid", getIntent().getIntExtra("userid", 0));
                    startActivity(intent);
                }
            }
        });

        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        calendarView = (CalendarView) findViewById(R.id.calendar);

        calendarView.setClickable(false);

        mapView = (MapView) findViewById(R.id.map);
        map = mapView.getMap();


        map.setIndoorEnable(true); // 打开室内图
        map.setMyLocationEnabled(true);
        map.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b) {
                    // 进入室内图
                    // 通过获取回调参数 mapBaseIndoorMapInfo 来获取室内图信息，包含楼层信息，室内ID等
                    mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
                } else {
                    // 移除室内图
                }
            }
        });
        operator = new WebServiceDataNetOperator();
        signBtn = (Button) findViewById(R.id.qiandao);

        isSign = getIntent().getIntExtra("issign", -1);
        List<String> list = new ArrayList<>();
        list.add(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        calendarView.setOptionalDate(list);
        if (isSign == 0) {
            signBtn.setText("签退");
            List<String> list1 = new ArrayList<>();
            list1.add(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            calendarView.setSelectedDates(list1);
        } else if (isSign == -1) {

            signBtn.setText("签到");
        }
        attr = ConnectHelper.createAttribute();
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isSign == -1) {
                    attr.setMethodName(ConnectHelper.METHOD.INSERTSIGNIN);
                } else if (isSign == 0) {
                    attr.setMethodName(ConnectHelper.METHOD.INSERTSIGNOUT);
                }


                Map<String, String> map = new HashMap<>();
                map.put("arg0", getIntent().getIntExtra("userid", 0) + "");
                map.put("arg1", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                map.put("arg2", address);
                map.put("arg3", longitude + "");
                map.put("arg4", latitude + "");
                map.put("arg5", radius + "");
                attr.setParams(map);
                operator.request(attr, "1");
            }
        });

        attr.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if (o.toString() == "") return;
                if (Boolean.parseBoolean(o.toString())) {
                    isChanged = true;
                    if (isSign == -1) {
                        List<String> list = new ArrayList<>();
                        list.add(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                        calendarView.setSelectedDates(list);
                        Toast.makeText(SignActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        signBtn.setText("签退");
                        isSign = 0;
                    } else {
                        Toast.makeText(SignActivity.this, "签退成功", Toast.LENGTH_SHORT).show();
                        isSign = 1;
                        finish();
                    }

                }
            }

            @Override
            public void OnError(String s) {
                isSign = -1;
                Toast.makeText(SignActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnProgress(int i) {
                signBtn.setText("正在上传数据...");
            }
        });
        map.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,
                mCurrentMarker));
        service = CompanySignApp.service;
        service.setLocationOption(service.getDefaultLocationClientOption());

        locationListener = new BDLocationListener() {
            private String lastFloor = null;

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // map view 销毁后不在处理新接收的位置
                if (bdLocation == null || mapView == null) {
                    return;
                }
                address = bdLocation.getAddrStr() + bdLocation.getLocationDescribe();
                longitude = bdLocation.getLongitude();
                latitude = bdLocation.getLatitude();
                String bid = bdLocation.getBuildingID();
                if (bid != null && mMapBaseIndoorMapInfo != null) {
                    Log.i("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo.getID());
                    if (bid.equals(mMapBaseIndoorMapInfo.getID())) {// 校验是否满足室内定位模式开启条件
                        // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                        String floor = bdLocation.getFloor().toUpperCase();// 楼层
                        //Log.i("indoor", "floor = " + floor + " position = " + mFloorListAdapter.getPosition(floor));
                        Log.i("indoor", "radius = " + bdLocation.getRadius() + " type = " + bdLocation.getNetworkLocationType());
                        address += floor + "层";
                        boolean needUpdateFloor = true;
                        if (lastFloor == null) {
                            lastFloor = floor;
                        } else {
                            if (lastFloor.equals(floor)) {
                                needUpdateFloor = false;
                            } else {
                                lastFloor = floor;
                            }
                        }
                        if (needUpdateFloor) {// 切换楼层
                            map.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());

                        }

                        if (!bdLocation.isIndoorLocMode()) {
                            service.startIndoor();// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                            Log.i("indoor", "start indoormod");
                        }
                    }
                }
                radius = bdLocation.getRadius();
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(radius)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                map.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng cenpt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
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
                addressView.setText("位置: " + address);
            }
        };
        service.registerListener(locationListener);
        addressView = (TextView) findViewById(R.id.address);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isSign", isSign);
        //intent.putExtra("isRequest",isChanged);
        setResult(0, intent);
        super.onBackPressed();


    }

    @Override
    protected void onStart() {
        super.onStart();
        service.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        service.unregisterListener(locationListener);
        service.stop();
        map.setMyLocationEnabled(false);
        mapView.onDestroy();
        if (!operator.isRunFinish()) {
            operator.cancleAllRequest();
        }
    }
}

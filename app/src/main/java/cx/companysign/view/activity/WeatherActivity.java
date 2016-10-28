package cx.companysign.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.JsonNetDataOperator;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.CompanySignApp;
import cx.companysign.R;
import cx.companysign.bean.WeatherEntity;
import cx.companysign.bean.WeatherImageUtils;
import cx.companysign.bean.WeatherIndex;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.LocationService;
import cx.companysign.view.cell.WeathersCell;
import cx.companysign.view.componts.AppDialog;

/**
 * Created by cxcxk on 2016/4/12.
 */
public class WeatherActivity extends BaseActivity {

    TextView locationView,temp,mWeather,pm_25;
    WeathersCell cell;
    private LocationService locationService;
    public MyLocationListenner myListener = new MyLocationListenner();

    private boolean isFirstLoc = true;
    RecyclerView recyclerView;

    List<WeatherIndex> indexes = new ArrayList<>();
    RecycleAdapter adapter;
    ProgressDialog dialog ;

    NetDataOperater operater;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000&&data != null){
            String city = data.getStringExtra("city");
            final NetDataOperater.Attribute attribute = ConnectHelper.createWeatherAttr();
            Map<String,String> map = attribute.getParams();
            if(map ==null) map = new HashMap<>();
            map.put("location", city + "市");
            operater.request(attribute, "1");
            dialog.setMessage("正在加载...");
            dialog.show();
            attribute.setNetWork(new INetWork() {
                @Override
                public void OnCompleted(Object o) {
                    if (o.toString().equals("")) return ;
                    parseJson(o.toString());
                    dialog.dismiss();
                }

                @Override
                public void OnError(String s) {
                    AppDialog dialog1 = new AppDialog.Builder(WeatherActivity.this)
                            .setTitle("异常")
                            .setMessage("网络异常")
                            .setCancleableOnTouchOutSide(false)
                            .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    operater.request(attribute,"1");
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    dialog1.show();
                }

                @Override
                public void OnProgress(int i) {

                }
            });


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fresco.initialize(this);
        setContentView(R.layout.weather_layout);
        ActivityUtils.addActiviy(this);
        final ActionBar actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setTitle("天气查询");
        actionBar.addMenu(0, R.drawable.ic_add_white_24dp);

        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int index) {
                if (index == 0) {
                    Intent intent = new Intent(WeatherActivity.this, CityListActivity.class);
                    startActivityForResult(intent, 1000);
                } else if (index == -1) {
                    onBackPressed();
                }
            }
        });

        dialog = new ProgressDialog(this);

        dialog.setCanceledOnTouchOutside(false);

        locationView = (TextView) findViewById(R.id.city);
        temp = (TextView) findViewById(R.id.now_temp);

        mWeather = (TextView) findViewById(R.id.weather);
        pm_25 = (TextView) findViewById(R.id.pm_25);
        cell = (WeathersCell) findViewById(R.id.weathers);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cell.getLayoutParams();
        cell.setMargin(params.leftMargin);
        cell.setnumColums(4);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleAdapter();
        recyclerView.setAdapter(adapter);

        operater = new JsonNetDataOperator();
        locationService = CompanySignApp.service;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(myListener);
        //注册监听

        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        dialog.setMessage("正在定位...");
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        /**
         * 启动定位
         */

        // -----------location config ------------

    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                final NetDataOperater.Attribute attribute = ConnectHelper.createWeatherAttr();
                Map<String,String> map = attribute.getParams();
                if(map ==null) map = new HashMap<>();
                map.put("location", location.getLongitude() + "," + location.getLatitude());
                operater.request(attribute, "1");
                dialog.setMessage("正在加载...");
                attribute.setNetWork(new INetWork() {
                    @Override
                    public void OnCompleted(Object o) {
                        if(o.toString().equals("")) return ;
                        parseJson(o.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void OnError(String s) {
                        AppDialog dialog1 = new AppDialog.Builder(WeatherActivity.this)
                                .setTitle("异常")
                                .setMessage("网络异常")
                                .setCancleableOnTouchOutSide(false)
                                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        operater.request(attribute,"1");
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog1.show();
                    }

                    @Override
                    public void OnProgress(int i) {

                    }
                });

            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * RecycleView的适配器
     */
    class RecycleAdapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(WeatherActivity.this)
                    .inflate(R.layout.weather_index_item_layout,null);
            ViewHolder holder = new ViewHolder(view);
            holder.content = (TextView) holder.itemView.findViewById(R.id.content);
            holder.status = (TextView) holder.itemView.findViewById(R.id.status);
            holder.title = (TextView) holder.itemView.findViewById(R.id.title);

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(indexes.get(position).getTitle());
            holder.status.setText(indexes.get(position).getTips()+":  ");
            holder.content.setText(indexes.get(position).getDes());
        }

        @Override
        public int getItemCount() {
            return indexes.size();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,status,content;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationService.stop(); //停止定位服务
    }

    public void parseJson(String json){


        final List<WeatherEntity> entityList = new ArrayList<>();
        JSONObject allJson = JSON.parseObject(json);
        String status = allJson.getString("status");
        if(!TextUtils.isEmpty(status)&&status.equals("success")){
            indexes.clear();
            JSONArray results = allJson.getJSONArray("results");
            JSONObject oneObject = results.getJSONObject(0);
            if(oneObject != null){
                final String pm25 = results.getJSONObject(0).getString("pm25");
                final String city = results.getJSONObject(0).getString("currentCity");
                JSONArray weather_data = results.getJSONObject(0).getJSONArray("weather_data");
                final String weather = weather_data.getJSONObject(0).getString("weather");
                final String nowTemp = weather_data.getJSONObject(0).getString("date");
                for (int i = 0;i<weather_data.size();i++){
                    WeatherEntity entity = new WeatherEntity();
                    JSONObject object = weather_data.getJSONObject(i);
                    entity.setWind(object.getString("wind"));
                    entity.setDayImage(object.getString("dayPictureUrl"));
                    entity.setNightImage(object.getString("nightPictureUrl"));
                    entity.setTemp(object.getString("temperature"));
                    if(i != 0){
                        entity.setWeek(object.getString("date"));
                    }else {
                        entity.setWeek(object.getString("date").substring(0,object.getString("date").indexOf(" ")));
                    }
                    entityList.add(entity);
                }

                JSONArray index = results.getJSONObject(0).getJSONArray("index");
                for (int i = 0;i<index.size();i++){
                    JSONObject object = index.getJSONObject(i);
                    WeatherIndex index1 = new WeatherIndex();
                    index1.setTitle(object.getString("title")+":  "+object.getString("zs"));
                    index1.setDes(object.getString("des"));
                    index1.setTips(object.getString("tipt"));
                    indexes.add(index1);
                }

                        locationView.setText(city);
                        pm_25.setText("PM2.5: " + pm25+"  "+(!pm25.equals("")? WeatherImageUtils.getPollutionLevel(Integer.parseInt(pm25)):"未知"));
                        mWeather.setText(weather);
                        temp.setText(nowTemp.substring(nowTemp.lastIndexOf("：") + 1, nowTemp.length() - 1));
                        cell.setDays(entityList.toArray(new WeatherEntity[entityList.size()]));
                        adapter.notifyDataSetChanged();

         }
        }else {

                    AppDialog dialog = new AppDialog.Builder(WeatherActivity.this)
                            .setTitle("天气预报")
                            .setMessage("天气查询失败")
                            .setCancleableOnTouchOutSide(false)
                            .setCancleable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .create();
                    dialog.show();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(myListener); //注销掉监听
    }
}

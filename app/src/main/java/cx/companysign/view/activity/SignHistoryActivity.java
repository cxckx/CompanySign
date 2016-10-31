package cx.companysign.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.bean.Sign;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.componts.AppDialog;
import cx.companysign.view.componts.CalendarView;
import cx.companysign.view.componts.LoadDialog;

/**
 * Created by cxcxk on 2016/10/13.
 */
public class SignHistoryActivity extends BaseActivity {

    ActionBar actionBar;
    TextView textSelectMonthView, signedDaysView;
    ImageButton nextBtn, lastBtn;
    CalendarView calendar;
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    NetDataOperater operater;
    List<Sign> signs = new ArrayList<>();
    LoadDialog mLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_history_layout);
        ActivityUtils.addActiviy(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("签到/签退历史");

        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);

        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                }
            }
        });
        calendar = (CalendarView) findViewById(R.id.calendar);
        //calendar.setClickable(false);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        textSelectMonthView = (TextView) findViewById(R.id.txt_select_month);
        textSelectMonthView.setText(calendar.getDate());
        nextBtn = (ImageButton) findViewById(R.id.img_select_next_month);
        lastBtn = (ImageButton) findViewById(R.id.img_select_last_month);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setNextMonth();
                textSelectMonthView.setText(calendar.getDate());
                signedDaysView.setText("本月已签到天数: " + getSignDays() + "天");
            }
        });
        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setLastMonth();
                textSelectMonthView.setText(calendar.getDate());
                signedDaysView.setText("本月已签到天数: " + getSignDays() + "天");
            }
        });

        mLoadDialog = new LoadDialog.Builder(this)
                .setCancleableOnTouchOutSide(false)
                .create();
        operater = new WebServiceDataNetOperator();
        final NetDataOperater.Attribute attr = ConnectHelper.createAttribute();
        attr.setMethodName(ConnectHelper.METHOD.GETSIGN);
        Map<String, String> map = new HashMap<>();
        map.put("arg0", getIntent().getIntExtra("userid", 0) + "");
        attr.setParams(map);
        operater.request(attr, "1");
        mLoadDialog.show();
        attr.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if (o.toString() == "") return;
                mLoadDialog.dismiss();
                JSONArray array = JSON.parseArray(o.toString());
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < array.size(); i++) {
                    Sign sign = new Sign();
                    JSONObject object = array.getJSONObject(i);
                    sign.setLatitude(object.getString("longitude"));
                    sign.setLongitude(object.getString("latitude"));
                    sign.setSignDate(object.getString("signDate"));
                    sign.setSignId(object.getInteger("signId"));
                    sign.setSignUser(object.getInteger("signUser"));
                    sign.setSignsign(object.getInteger("signsign"));
                    sign.setSignAddress(object.getString("signAddress"));
                    sign.setRadius(object.getString("radius"));
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sign.getSignDate());
                        String dateString = format.format(date);
                        list.add(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    signs.add(sign);
                }
                calendar.setOptionalDate(list);
                calendar.setSelectedDates(list);
                signedDaysView.setText("本月已签到天数: " + getSignDays() + "天");
            }

            @Override
            public void OnError(String s) {
                AppDialog dialog1 = new AppDialog.Builder(SignHistoryActivity.this)
                        .setTitle("异常")
                        .setMessage("网络异常")
                        .setCancleableOnTouchOutSide(false)
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                operater.request(attr, "1");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mLoadDialog.dismiss();
                                finish();
                            }
                        }).create();
                dialog1.show();
            }

            @Override
            public void OnProgress(int i) {

            }
        });

        calendar.setOnClickDate(new CalendarView.OnClickListener() {
            @Override
            public void onClickDateListener(int year, int month, int day) {
                String date = year + "" + month + "" + day;
                for (Sign sign : signs) {
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sign.getSignDate());
                        if (date.equals(format.format(date1))) {
                            Intent intent = new Intent(SignHistoryActivity.this, SignHistoryDetailActivity.class);
                            intent.putExtra("sign", sign);
                            startActivity(intent);
                            break;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        signedDaysView = (TextView) findViewById(R.id.signed_days);


    }

    private int getSignDays() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int signNum = 0;
        for (Sign sign : signs) {
            try {
                Date dateNow = new SimpleDateFormat("yyyy-MM").parse(calendar.getDate());
                Date date = format.parse(sign.getSignDate());
                if (date.getYear() == dateNow.getYear()) {
                    if (date.getMonth() == dateNow.getMonth()) {
                        signNum++;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return signNum;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!operater.isRunFinish()) {
            operater.cancleAllRequest();
        }
    }
}

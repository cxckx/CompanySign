package cx.companysign.view.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.TabLayout.TabLayout;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.adapter.FragmentViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.bean.User;
import cx.companysign.bean.UserEntity;
import cx.companysign.dao.DBInterface;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.Receiver;
import cx.companysign.utils.Sender;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.cell.ClickableTextView;
import cx.companysign.view.componts.AppDialog;
import cx.companysign.view.fragment.ContractFragment;
import cx.companysign.view.fragment.MineFragment;
import cx.companysign.view.service.ListenerService;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class MainUIActivty extends BaseActivity {

    WebServiceDataNetOperator operator;
    TabLayout tabLayout;
    ViewPager pager;
    User user;
    ActionBar actionBar;
    MineFragment mineFragment;
    ContractFragment fragment;
    PopupWindow popupWindow;
    ProgressDialog dialogP;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui_layout);
        ActivityUtils.addActiviy(this);
        initPopWindow();
        actionBar = (ActionBar) findViewById(R.id.main_actionbar);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.addMenu(0, R.drawable.ic_refresh_white_24dp);
        actionBar.addMenu(1, R.drawable.ic_more_vert_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == 0) {
                    request();
                    Sender.getInstance().refreshContract();
                } else if (i == 1) {
                    popupWindow.showAsDropDown(actionBar, actionBar.getWidth() - 200, 0);

                }
            }
        });

        dialogP= new ProgressDialog(this);
        dialogP.setMessage("正在获取个人信息,请稍后...");
        dialogP.setCanceledOnTouchOutside(false);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.addTab().setText("联系人").setTextColor(Color.parseColor("#e0e0e0"))
                .setTextColorSelect(Color.parseColor("#FFFFFF")).setImage(R.drawable.ic_contacts_white_24dp).setting();
        tabLayout.addTab().setText("我的").setTextColor(Color.parseColor("#e0e0e0"))
                .setTextColorSelect(Color.parseColor("#FFFFFF")).setImage(R.drawable.ic_person_white_24dp).setting();
        tabLayout.select(0);
        tabLayout.setSelect(new TabLayout.OnTabSelect() {
            @Override
            public void onTabSelect(int i) {
                if (i == 1) {
                    Sender.getInstance().translateUser(user,getIntent().getStringExtra("pwd"));
                }
            }
        });



        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout.bindViewPager(pager);
        fragment= new ContractFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment);
        mineFragment = new MineFragment();
        fragments.add(mineFragment);
        Sender.getInstance().register(mineFragment);
        Sender.getInstance().register(fragment);
        pager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));

        operator = new WebServiceDataNetOperator();
        UserEntity entity  = DBInterface.instance().getUser( getIntent().getStringExtra("phone"));
        if(entity == null){
            request();
        }else {
            actionBar.setTitle(entity.getUserName());
            user = User.UserEntityToUser(entity);

        }


        Intent intent = new Intent(this, ListenerService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ListenerService.MyBinder myBinder = (ListenerService.MyBinder) service;
            ListenerService service1 = myBinder.getService();
            service1.listener(getIntent().getStringExtra("pwd"), getIntent().getStringExtra("phone"));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initPopWindow(){
        final View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_layout, null, false);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, 250, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        //popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 这里是位置显示方式,在屏幕的左侧
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        final ClickableTextView text1 = (ClickableTextView) popupWindow_view.findViewById(R.id.text1);
        final ClickableTextView text2 = (ClickableTextView) popupWindow_view.findViewById(R.id.text2);
        final ClickableTextView text3 = (ClickableTextView) popupWindow_view.findViewById(R.id.text3);
        text1.setiOnClick(new ClickableTextView.IOnClick() {
            @Override
            public void click(String text) {
                popupWindow.dismiss();
                text1.setTextColor(Color.parseColor("#000000"));
                text2.setTextColor(Color.parseColor("#a0a0a0"));
                text3.setTextColor(Color.parseColor("#a0a0a0"));
                Sender.getInstance().showWhat(Receiver.BRANCH_CONTRACT);
            }
        });
        text2.setiOnClick(new ClickableTextView.IOnClick() {
            @Override
            public void click(String text) {
                popupWindow.dismiss();
                text1.setTextColor(Color.parseColor("#a0a0a0"));
                text2.setTextColor(Color.parseColor("#000000"));
                text3.setTextColor(Color.parseColor("#a0a0a0"));
                Sender.getInstance().showWhat(Receiver.PARTCOMPANY_CONTRACT);
            }
        });
        text3.setiOnClick(new ClickableTextView.IOnClick() {
            @Override
            public void click(String text) {
                popupWindow.dismiss();
                text1.setTextColor(Color.parseColor("#a0a0a0"));
                text2.setTextColor(Color.parseColor("#a0a0a0"));
                text3.setTextColor(Color.parseColor("#000000"));
                Sender.getInstance().showWhat(Receiver.ALL_CONTRACT);
            }
        });

    }

    private void request(){
        final NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
        attribute.setMethodName(ConnectHelper.METHOD.GETEMPLOYEE);
        Map<String,String> map = new HashMap<>();
        map.put("arg0", getIntent().getStringExtra("phone"));
        attribute.setParams(map);
        operator.request(attribute, "1");

        dialogP.show();
        attribute.setNetWork(new INetWork<String>() {
            @Override
            public void OnCompleted(String s) {
                if (s.equals("")) return;
                dialogP.dismiss();
                JSONObject object = JSON.parseObject(s);
                user = new User();
                user.setUserBirthday(object.getLong("userBirthday"));
                user.setUserBranch(object.getString("userBranch"));
                user.setUserCompanyId(object.getInteger("userCompanyId"));
                user.setUserIconHeader(object.getString("userIconHeader"));
                user.setUserId(object.getInteger("userId"));
                user.setUserName(object.getString("userName"));
                user.setUserPartCompany(object.getString("userPartCompany"));
                user.setUserPhone(object.getString("userPhone"));
                user.setUserSex(object.getInteger("userSex"));
                actionBar.setTitle(user.getUserName());

                Sender.getInstance().translateUser(user, getIntent().getStringExtra("pwd"));
            }

            @Override
            public void OnError(String s) {
                dialogP.dismiss();
                AppDialog dialog1 = new AppDialog.Builder(MainUIActivty.this)
                        .setTitle("异常")
                        .setMessage("网络异常")
                        .setCancleableOnTouchOutSide(false)
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogP.show();
                                dialog.dismiss();
                                operator.request(attribute, "1");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialogP.dismiss();
                            }
                        }).create();
                dialog1.show();

            }

            @Override
            public void OnProgress(int i) {

            }
        });
    }
    boolean isBacking = false;
    @Override
    public void onBackPressed() {

        if (!dialogP.isShowing()){
            if (isBacking) return ;
            isBacking = true;
            final NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
            attribute.setMethodName(ConnectHelper.METHOD.LOGINOUT);
            Map<String,String> map = new HashMap<>();
            map.put("arg0", getIntent().getStringExtra("phone"));
            attribute.setParams(map);
            operator.request(attribute, "a");
            attribute.setNetWork(new INetWork() {
                @Override
                public void OnCompleted(Object o) {
                    if(o.toString().equals("")) return ;
                    if(Boolean.parseBoolean(o.toString()))

                        MainUIActivty.super.onBackPressed();
                }

                @Override
                public void OnError(String s) {
                    isBacking = false;
                    AppDialog dialog = new AppDialog.Builder(MainUIActivty.this)
                            .setTitle("Waring")
                            .setMessage("网络异常或是服务器异常")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }

                @Override
                public void OnProgress(int i) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {

        if (!operator.isRunFinish()) {
            operator.cancleAllRequest();
        }
        unbindService(mServiceConnection);

        super.onDestroy();
        Sender.getInstance().unRegister(mineFragment);
        Sender.getInstance().unRegister(fragment);
    }
}

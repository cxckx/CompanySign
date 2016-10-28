package cx.companysign.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;

import java.util.HashMap;
import java.util.Map;

import cx.companysign.utils.AbsListener;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.DecodeUtils;
import cx.companysign.utils.ListenerSingleUtils;
import cx.companysign.utils.WebServiceDataNetOperator;

/**
 * Created by cxcxk on 2016/10/26.
 */
public class ListenerService extends Service implements AbsListener{

    MyBinder mMyBinder;
    NetDataOperater operater = new WebServiceDataNetOperator();
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            NetDataOperater.Attribute attribute = (NetDataOperater.Attribute) msg.obj;
            operater.request(attribute,"1");
            return true;
        }
    });


    @Override
    public void onCreate() {
        super.onCreate();
        ListenerSingleUtils.getInstance().setListener(this);
        mMyBinder = new MyBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    @Override
    public void listenerUpdate(String password, String phone) {
        if(!operater.isRunFinish()){
            operater.cancleAllRequest();
        }

        listener(password, phone);
    }

    public class MyBinder extends Binder{

        public ListenerService getService(){
            return ListenerService.this;
        }
    }

    public void listener(final String password,final String phone){

        final NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
        attribute.setMethodName(ConnectHelper.METHOD.GETPWD);
        Map<String,String> map = new HashMap<>();
        map.put("arg0",phone);
        attribute.setParams(map);

        operater.request(attribute, "1");


        attribute.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                Message message =  mHandler.obtainMessage();
                message.obj = attribute;
                if(o.toString().equals("")){

                    mHandler.sendMessageDelayed(message,1000);
                    return ;
                }

                String pwd = DecodeUtils.decrypt(o.toString(),phone);
                Log.i("TAGGG",pwd+"----"+password);
                if(!pwd.equals(password)){
                    Toast.makeText(ListenerService.this,"您的账号密码已被修改,如若非本人操作请尽快修改密码",Toast.LENGTH_LONG).show();
                    ActivityUtils.finishAll();
                }else {
                    mHandler.sendMessageDelayed(message,2000);
                }
            }

            @Override
            public void OnError(String s) {
                Message message =  mHandler.obtainMessage();
                message.obj = attribute;
                mHandler.sendMessageDelayed(message,2000);
            }

            @Override
            public void OnProgress(int i) {

            }
        });
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(!operater.isRunFinish()){
            operater.cancleAllRequest();
        }
        mHandler.removeCallbacksAndMessages(null);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

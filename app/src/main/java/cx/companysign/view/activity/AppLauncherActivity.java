package cx.companysign.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;

import java.util.HashMap;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.DeviceUuidFactory;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.componts.AppDialog;

/**
 * Created by cxcxk on 2016/10/24.
 */
public class AppLauncherActivity extends AppCompatActivity {

    NetDataOperater operater;
     ProgressDialog dialogP;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.app_launcher_layout);
        ActivityUtils.addActiviy(this);
        operater = new WebServiceDataNetOperator();
        final NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
        attribute.setMethodName(ConnectHelper.METHOD.ISAUTHOR);
        Map<String, String> map = new HashMap<>();
        map.put("arg0", new DeviceUuidFactory(this).getDeviceUuid().toString());
        attribute.setParams(map);
        dialogP = new ProgressDialog(this);
        dialogP.setMessage("正在进行授权验证...");
        dialogP.setCanceledOnTouchOutside(false);
        AnimationSet set = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.app_launcher_anim);
        findViewById(R.id.image).startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialogP.show();
                operater.request(attribute, "1");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        attribute.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if (o.toString().equals("")) return;
                dialogP.dismiss();
                if (Boolean.parseBoolean(o.toString())) {
                    /**
                     * 确认已授权
                     */
                    Intent intent = new Intent(AppLauncherActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    /**
                     * 设备未授权,请求授权
                     */

                    AppDialog dialog = new AppDialog.Builder(AppLauncherActivity.this)
                            .setTitle("提示")
                            .setCancleableOnTouchOutSide(false)
                            .setMessage("该设备未授权,是否授权")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    View view = LayoutInflater.from(AppLauncherActivity.this)
                                            .inflate(R.layout.input_dialog_layout, null);
                                    final EditText editText = (EditText) view.findViewById(R.id.input);
                                    AppDialog inputDialog = new AppDialog.Builder(AppLauncherActivity.this)
                                            .setTitle("识别码")
                                            .setContentView(view)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    final ProgressDialog dialog1 = new ProgressDialog(AppLauncherActivity.this);
                                                    dialog1.setMessage("正在授权...");
                                                    dialog1.setCanceledOnTouchOutside(false);
                                                    dialog1.show();
                                                    NetDataOperater.Attribute attribute1 = ConnectHelper.createAttribute();
                                                    attribute1.setMethodName(ConnectHelper.METHOD.AUTHOR);
                                                    Map<String, String> map1 = new HashMap<String, String>();
                                                    map1.put("arg0", new DeviceUuidFactory(AppLauncherActivity.this).getDeviceUuid().toString());
                                                    map1.put("arg1", Build.MODEL);
                                                    map1.put("arg2", editText.getText().toString());
                                                    attribute1.setParams(map1);
                                                    operater.request(attribute1, "2");
                                                    attribute1.setNetWork(new INetWork() {
                                                        @Override
                                                        public void OnCompleted(Object o) {
                                                            if (o.toString().equals("")) return;
                                                            dialog1.dismiss();
                                                            if (Boolean.parseBoolean(o.toString())) {
                                                                Toast.makeText(AppLauncherActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(AppLauncherActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                AppDialog dialog2 = new AppDialog.Builder(AppLauncherActivity.this)
                                                                        .setMessage("授权失败")
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
                                                                dialog2.show();

                                                            }
                                                        }

                                                        @Override
                                                        public void OnError(String s) {
                                                             dialog1.dismiss();
                                                            AppDialog dialog2 = new AppDialog.Builder(AppLauncherActivity.this)
                                                                    .setTitle("提示")
                                                                    .setMessage("授权失败")
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
                                                            dialog2.show();

                                                        }

                                                        @Override
                                                        public void OnProgress(int i) {

                                                        }
                                                    });
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    inputDialog.show();

                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }
            }

            @Override
            public void OnError(String s) {
                dialogP.dismiss();
                AppDialog dialog = new AppDialog.Builder(AppLauncherActivity.this)
                        .setTitle("提示")
                        .setCancleableOnTouchOutSide(false)
                        .setMessage("验证失败,是否重新验证")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialogP.show();
                                operater.request(attribute, "1");
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void OnProgress(int i) {

            }
        });
    }


}

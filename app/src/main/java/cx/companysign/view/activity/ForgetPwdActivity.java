package cx.companysign.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cx.companysign.R;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.CountDownTimer;
import cx.companysign.utils.DecodeUtils;
import cx.companysign.utils.Notify;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.componts.AppDialog;

/**
 * Created by cxcxk on 2016/10/16.
 */
public class ForgetPwdActivity extends BaseActivity implements Notify {

    ActionBar actionBar;
    EditText phone, checkNumEdit;
    String code;
    private CountDownTimer countDownTimer;
    Button checkNumBtn, checkPhoneBtn;
    NetDataOperater operater;
    private long millsFuture = 60 * 1000;
    private long countDownInterval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwd_layout);
        operater = new WebServiceDataNetOperator();
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("验证手机号");
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);

        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                } else if (i == 0) {
                    code = checkNumEdit.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        checkNumEdit.setError("请输入验证码");
                    } else {
                        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        SMSSDK.submitVerificationCode("86", phone.getText().toString(), code);
                    }

                }
            }
        });
        phone = (EditText) findViewById(R.id.phone);
        checkNumBtn = (Button) findViewById(R.id.get_check);
        checkNumEdit = (EditText) findViewById(R.id.check_num);
        checkNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPhone(phone.getText().toString())) {
                    countDownTimer.start();
                    checkNumBtn.setText("60秒后重新发送");
                    checkNumBtn.setEnabled(false);
                    SMSSDK.getVerificationCode("86", phone.getText().toString());
                }

            }
        });
        countDownTimer = new CountDownTimer(millsFuture, countDownInterval, this);

        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgetPwdActivity.this, "验证成功", Toast.LENGTH_SHORT)
                                        .show();
                                countDownTimer.cancel();
                                if (!getIntent().getBooleanExtra("type", false)) {
                                    Intent intent = new Intent(ForgetPwdActivity.this, ModifyPwdActivity.class);
                                    intent.putExtra("phone", phone.getText().toString());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
                                    attribute.setMethodName(ConnectHelper.METHOD.GETPWD);
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("arg0", phone.getText().toString());
                                    attribute.setParams(map);
                                    operater.request(attribute, "2");
                                    attribute.setNetWork(new INetWork() {
                                        @Override
                                        public void OnCompleted(Object o) {
                                            if (o.toString() == "") return;
                                            AppDialog appDialog = new AppDialog.Builder(ForgetPwdActivity.this)
                                                    .setTitle("密码")
                                                    .setMessage(DecodeUtils.decrypt(o.toString(), phone.getText().toString()))
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
                                            appDialog.show();
                                        }

                                        @Override
                                        public void OnError(String s) {
                                            Toast.makeText(ForgetPwdActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void OnProgress(int i) {

                                        }
                                    });
                                }

                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        boolean smart = (Boolean) data;
                        if (smart) {
                            //通过智能验证
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ForgetPwdActivity.this, "智能验证成功", Toast.LENGTH_SHORT)
                                            .show();
                                    countDownTimer.cancel();
                                    if (!getIntent().getBooleanExtra("type", false)) {
                                        Intent intent = new Intent(ForgetPwdActivity.this, ModifyPwdActivity.class);
                                        intent.putExtra("phone", phone.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
                                        attribute.setMethodName(ConnectHelper.METHOD.GETPWD);
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("arg0", phone.getText().toString());
                                        attribute.setParams(map);
                                        operater.request(attribute, "2");
                                        attribute.setNetWork(new INetWork() {
                                            @Override
                                            public void OnCompleted(Object o) {
                                                if (o.toString() == "") return;
                                                AppDialog appDialog = new AppDialog.Builder(ForgetPwdActivity.this)
                                                        .setTitle("密码")
                                                        .setMessage(DecodeUtils.decrypt(o.toString(), phone.getText().toString()))
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
                                                appDialog.show();
                                            }

                                            @Override
                                            public void OnError(String s) {
                                                Toast.makeText(ForgetPwdActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void OnProgress(int i) {

                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

        checkPhoneBtn = (Button) findViewById(R.id.check_phone);
        checkPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (checkPhone(phone.getText().toString())) {

                    NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
                    attribute.setMethodName(ConnectHelper.METHOD.ISHAVEPHONE);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("arg0", phone.getText().toString());
                    attribute.setParams(map);
                    final ProgressDialog dialog = new ProgressDialog(ForgetPwdActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("正在验证...");
                    dialog.show();
                    operater.request(attribute, "1");
                    attribute.setNetWork(new INetWork() {
                        @Override
                        public void OnCompleted(Object o) {
                            if (o.toString() == "") return;
                            dialog.dismiss();
                            if (Boolean.parseBoolean(o.toString())) {
                                actionBar.addMenu(0, R.drawable.ic_check_white_24dp);
                                Toast.makeText(ForgetPwdActivity.this, "验证成功", Toast.LENGTH_SHORT);
                                findViewById(R.id.check_num_layout).setVisibility(View.VISIBLE);
                                checkPhoneBtn.setEnabled(false);
                                checkPhoneBtn.setText("已验证手机号");
                                phone.setEnabled(false);
                            } else {
                                Toast.makeText(ForgetPwdActivity.this, "无效手机号", Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void OnError(String s) {
                            dialog.dismiss();
                            Toast.makeText(ForgetPwdActivity.this, "验证失败", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void OnProgress(int i) {

                        }
                    });
                } else {
                    phone.setError("手机号无效");
                }
            }
        });
    }

    @Override
    public void notifyFinish() {
        checkNumBtn.setEnabled(true);
        checkNumBtn.setText("重新发送");
    }

    @Override
    public void notifyProgress(long mills) {
        checkNumBtn.setText(mills / 1000 + "秒后重新发送");
    }

    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!operater.isRunFinish()) {
            operater.cancleAllRequest();
        }
    }


}


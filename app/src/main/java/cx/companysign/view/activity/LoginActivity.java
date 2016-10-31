package cx.companysign.view.activity;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.util.HashMap;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.componts.AppDialog;

;

public class LoginActivity extends BaseActivity {

    EditText phoneEdit, passwordEdit;
    Button btnLogin;
    TextView forgetPwdView, modifyPwdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityUtils.addActiviy(this);
        ActionBar actionBar = (ActionBar) findViewById(R.id.main_actionbar);
        actionBar.setBackGroundColor(getResources().getColor(com.example.cxcxk.android_my_library.R.color.colorPrimary));
        actionBar.setTitle("登录");
        phoneEdit = (EditText) findViewById(R.id.edit_phone);
        passwordEdit = (EditText) findViewById(R.id.edit_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (phoneHandler(phoneEdit.getText().toString()) && passwordHandler(passwordEdit.getText().toString())) {
                    login();
                }

            }
        });
        forgetPwdView = (TextView) findViewById(R.id.forget_pwd);
        forgetPwdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
            }
        });

        modifyPwdView = (TextView) findViewById(R.id.modify_pwd);
        modifyPwdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                intent.putExtra("type", false);
                startActivity(intent);
            }
        });
    }

    private void login() {
        final NetDataOperater operater = new WebServiceDataNetOperator();
        final NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
        final Map<String, String> map = new HashMap<>();
        map.put("arg0", phoneEdit.getText().toString());
        map.put("arg1", passwordEdit.getText().toString());
        attribute.setMethodName(ConnectHelper.METHOD.LOGIN);
        attribute.setParams(map);
        final ProgressDialog dialogP = new ProgressDialog(LoginActivity.this);
        dialogP.setMessage("正在登录,请稍候...");
        dialogP.setCancelable(false);
        dialogP.setCanceledOnTouchOutside(false);
        dialogP.show();
        operater.request(attribute, "1");
        attribute.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if (o.toString().equals("")) return;
                int isSuccess = Integer.parseInt(o.toString());
                if (isSuccess == 1) {
                    dialogP.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Intent intent = new Intent(LoginActivity.this, MainUIActivty.class);
                            intent.putExtra("phone", phoneEdit.getText().toString());
                            intent.putExtra("pwd", passwordEdit.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (isSuccess == 0) {
                    dialogP.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog dialog1 = new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("登录失败")
                                    .create();
                            dialog1.show();
                        }
                    });
                } else if (isSuccess == 3) {
                    dialogP.dismiss();
                    AppDialog dialog1 = new AppDialog.Builder(LoginActivity.this)
                            .setTitle("警告")
                            .setMessage("您的账号已在别处登录\n若非本人操作请立刻修改密码")
                            .setCancleableOnTouchOutSide(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog1.show();
                }
            }

            @Override
            public void OnError(String s) {
                dialogP.dismiss();
                AppDialog dialog1 = new AppDialog.Builder(LoginActivity.this)
                        .setTitle("异常")
                        .setMessage("网络异常")
                        .setCancleableOnTouchOutSide(false)
                        .setPositiveButton("继续登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialogP.show();
                                operater.request(attribute, "1");
                            }
                        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
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

    private boolean phoneHandler(String phone) {
        if (TextUtils.isEmpty(phone)) {
            phoneEdit.setError("请输入手机号");
            return false;
        } else if (phone.length() != 11) {
            phoneEdit.setError("无效手机号");
            return false;
        }

        return true;
    }

    private boolean passwordHandler(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("请输入密码");
            return false;
        } else if (password.length() < 6 || password.length() > 16) {
            passwordEdit.setError("请输入6~16位的密码");
            return false;
        }

        return true;
    }
}

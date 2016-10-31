package cx.companysign.view.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import java.util.HashMap;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.WebServiceDataNetOperator;

/**
 * Created by cxcxk on 2016/10/16.
 */
public class ModifyPwdActivity extends BaseActivity {
    NetDataOperater operater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_pwd_layout);

        final EditText text = (EditText) findViewById(R.id.pwd);
        ActionBar actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.addMenu(0, R.drawable.ic_check_white_24dp);
        actionBar.setTitle("新密码");
        operater = new WebServiceDataNetOperator();
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                } else if (i == 0) {
                    if (text.getText().length() == 0) {
                        text.setError("请输入密码");
                        return;
                    } else if (text.getText().length() < 6 || text.getText().length() > 16) {
                        text.setError("请输入6-16位的密码");
                        return;
                    }
                    NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
                    attribute.setMethodName(ConnectHelper.METHOD.UPDATEPWD);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("arg0", getIntent().getStringExtra("phone"));
                    map.put("arg1", text.getText().toString());
                    attribute.setParams(map);
                    operater.request(attribute, "1");
                    attribute.setNetWork(new INetWork() {
                        @Override
                        public void OnCompleted(Object o) {
                            if (o.toString() == "") return;
                            if (Boolean.parseBoolean(o.toString())) {
                                Toast.makeText(ModifyPwdActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ModifyPwdActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void OnError(String s) {
                            Toast.makeText(ModifyPwdActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnProgress(int i) {

                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!operater.isRunFinish()) {
            operater.cancleAllRequest();
        }
    }
}

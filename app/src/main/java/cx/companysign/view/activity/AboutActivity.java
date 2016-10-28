package cx.companysign.view.activity;

import android.os.Bundle;

import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;

import cx.companysign.R;
import cx.companysign.utils.ActivityUtils;

/**
 * Created by cxcxk on 2016/10/12.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        ActivityUtils.addActiviy(this);
        ActionBar actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if(i == -1){
                    onBackPressed();
                }
            }
        });
        actionBar.setTitle("关于XIAO");

    }
}

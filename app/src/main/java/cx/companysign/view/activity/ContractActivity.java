package cx.companysign.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.componts.ProblemLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.bean.NameHeader;
import cx.companysign.bean.User;
import cx.companysign.bean.UserEntity;
import cx.companysign.dao.DBInterface;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.Receiver;
import cx.companysign.utils.SortListAdapter;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.cell.ContractImageItemCell;
import cx.companysign.view.cell.ContractItemCell;
import cx.companysign.view.componts.AppDialog;
import cx.companysign.view.componts.SortLineLayout;

public class ContractActivity extends BaseActivity {

    ListView listView;
    ActionBar actionBar;
    String title = "";
    int type;
    NetDataOperater operator;
    NetDataOperater.Attribute attribute;
    List<User> users = new ArrayList<>();
    ProblemLayout problemLayout;
    ProgressDialog dialog;
    SortListAdapter sortListAdapter;
    SortLineLayout sortLineLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        ActivityUtils.addActiviy(this);
        title = getIntent().getStringExtra("extra");
        operator = new WebServiceDataNetOperator();

        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle(title);

        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                }
            }
        });
        sortLineLayout = (SortLineLayout) findViewById(R.id.sort_layout);
        problemLayout = (ProblemLayout) findViewById(R.id.problem_layout);
        problemLayout.setText("你所查之空值空如也,点击刷新");
        listView = (ListView) findViewById(R.id.contract_list);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        type = getIntent().getIntExtra("type",-1);
        if (type > 0){
            List<UserEntity> entities = null;
            attribute = ConnectHelper.createAttribute();
            if(type == Receiver.BRANCH_CONTRACT){
                attribute.setMethodName(ConnectHelper.METHOD.GETUSERBYBRANCHNAME);
                entities = DBInterface.instance().loadUserbyBranch(title);

            }else if(type == Receiver.PARTCOMPANY_CONTRACT){
                attribute.setMethodName(ConnectHelper.METHOD.GETUSERBYPARTCOMPANYNAME);
                entities = DBInterface.instance().loadUserbyBranch(title);
            }
            Map<String,String> map = new HashMap<>();
            map.put("arg0", title);
            attribute.setParams(map);

            if(entities == null || entities.size() == 0){
                dialog = new ProgressDialog(this);
                dialog.setMessage("正在获取数据...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                operator.request(attribute,"1");
            }else {
                for (UserEntity entity:entities){
                    users.add(User.UserEntityToUser(entity));
                }
                sortListAdapter = new MySortAdapter(ContractActivity.this,UserToNameHeader());
                sortLineLayout.addLable(sortListAdapter.getWhichChars());
                sortLineLayout.setSelect(0);
                listView.setAdapter(sortListAdapter);
            }

        }

        attribute.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if ( o.toString() == "") return;
                users.clear();
                dialog.dismiss();
                String json = o.toString();
                JSONArray array = JSON.parseArray(json);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    User user = new User();
                    user.setUserBirthday(object.getLong("userBirthday"));
                    user.setUserBranch(object.getString("userBranch"));
                    user.setUserCompanyId(object.getInteger("userCompanyId"));
                    user.setUserIconHeader(object.getString("userIconHeader"));
                    user.setUserId(object.getInteger("userId"));
                    user.setUserName(object.getString("userName"));
                    user.setUserPartCompany(object.getString("userPartCompany"));
                    user.setUserPhone(object.getString("userPhone"));
                    user.setUserSex(object.getInteger("userSex"));
                    users.add(user);
                }
                if (users.size() == 0) {
                    problemLayout.setVisibility(View.VISIBLE);
                } else {
                    problemLayout.setVisibility(View.GONE);
                }
                sortListAdapter = new MySortAdapter(ContractActivity.this,UserToNameHeader());
                sortLineLayout.addLable(sortListAdapter.getWhichChars());
                sortLineLayout.setSelect(0);
                listView.setAdapter(sortListAdapter);

            }

            @Override
            public void OnError(String s) {
                dialog.dismiss();
                AppDialog dialog = new AppDialog.Builder(ContractActivity.this)
                        .setTitle("结果")
                        .setMessage("获取数据失败，请检查网络连接")
                        .create();
                dialog.show();
            }

            @Override
            public void OnProgress(int i) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContractActivity.this, UserInfoActivity.class);
                // Log.i("TAGG",users.get(position).getUserBranch());
                String content = sortListAdapter.getContentOnList(position);
                int index = 0;
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserName().equals(content)) {
                        index = i;
                        break;
                    }
                }
                intent.putExtra("user", users.get(index));
                startActivity(intent);
            }
        });

        problemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator.isRunFinish()) {
                    dialog.show();

                    operator.request(attribute, "1");
                }

            }
        });


        sortLineLayout.setItemClickListener(new SortLineLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                sortLineLayout.setSelect(position);
                String headerChar = sortListAdapter.getWhichChars().get(position);
                int index = sortListAdapter.getPositionOnList(headerChar);
                listView.setSelection(index);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (sortListAdapter != null) {
                    if (sortListAdapter.getLineType(listView.getFirstVisiblePosition()) == SortListAdapter.Type.TYPE_LABLE) {
                        String headerChar = sortListAdapter.getContentOnList(listView.getFirstVisiblePosition());
                        int index = sortListAdapter.getWhichChars().indexOf(headerChar);
                        sortLineLayout.setSelect(index);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });


    }

    private List<NameHeader> UserToNameHeader(){
        List<NameHeader> list = new ArrayList<>();
        for (User user:users){
            NameHeader header = new NameHeader();
            header.setHeader(user.getUserIconHeader());
            header.setName(user.getUserName());
            list.add(header);
        }

        return list;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {


            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null){
                    convertView = new ContractItemCell(ContractActivity.this);
                }
                ContractItemCell cell = (ContractItemCell) convertView;
                cell.setText(users.get(position).getUserName());
            return convertView;
        }
    }

    class MySortAdapter extends SortListAdapter{


        public MySortAdapter(Context context, List<NameHeader> SortContents) {
            super(context, SortContents);
        }

        @Override
        public View contentView(NameHeader content,int position, View convertView, ViewGroup parent) {
            int viewType =  getItemViewType(position);
            if(viewType == 2){
                if(convertView == null){
                    convertView = new ContractItemCell(ContractActivity.this);
                }
                ContractItemCell cell = (ContractItemCell) convertView;
                cell.setText(content.getName());
            }else if (viewType == 1){
                if(convertView == null){
                    convertView = new ContractImageItemCell(ContractActivity.this);
                }
                ContractImageItemCell cell = (ContractImageItemCell) convertView;
                byte[] photoimg = new byte[0];
                photoimg = Base64.decode(content.getHeader(),Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(photoimg, 0, photoimg.length);
                cell.setImage(bitmap);
                cell.setText(content.getName());


            }
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(operator != null){
            if(!operator.isRunFinish()){
                operator.cancleAllRequest();
            }
        }
    }
}

package cx.companysign.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.cell.TextBigCell;
import com.example.cxcxk.android_my_library.view.componts.ProblemLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.bean.Branch;
import cx.companysign.bean.NameHeader;
import cx.companysign.bean.PartCompany;
import cx.companysign.bean.User;
import cx.companysign.bean.UserEntity;
import cx.companysign.dao.DBInterface;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.Receiver;
import cx.companysign.utils.SortListAdapter;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.activity.ContractActivity;
import cx.companysign.view.activity.UserInfoActivity;
import cx.companysign.view.cell.ContractImageItemCell;
import cx.companysign.view.cell.ContractItemCell;
import cx.companysign.view.componts.SortLineLayout;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class ContractFragment extends Fragment implements Receiver{


    ListView listView;
    NetDataOperater operater ;
    NetDataOperater.Attribute attribute;
    List<User> users = new ArrayList<>();
    List<Branch> branches=new ArrayList<>();
    List<PartCompany> partCompanies = new ArrayList<>();
    String methodName="";
    int methodType = Receiver.ALL_CONTRACT;
    ProblemLayout problemLayout;
    SortLineLayout sortLineLayout;
    SortListAdapter sortListAdapter;
     ProgressDialog dialogP;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listView = new ListView(getContext());
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        operater = new WebServiceDataNetOperator();
        attribute = ConnectHelper.createAttribute();

        methodName = ConnectHelper.METHOD.GETUSERBYCOMPANY;
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.addView(listView, LayoutHelper.createRelativeLayout(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        problemLayout = new ProblemLayout(getContext());

        problemLayout.setVisibility(View.GONE);
        layout.addView(problemLayout, LayoutHelper.createRelativeLayout(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT));

        sortLineLayout = new SortLineLayout(getContext());
        RelativeLayout.LayoutParams params = LayoutHelper.createRelativeLayout(LayoutHelper.WRAP_CONTENT,LayoutHelper.MATCH_PARENT);
        params.topMargin = AndroidUtils.dip2px(getContext(),16);
        params.bottomMargin = AndroidUtils.dip2px(getContext(),16);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout.addView(sortLineLayout, params);
        dialogP = new ProgressDialog(getContext());
        dialogP.setMessage("正在获取数据...");
        dialogP.setCanceledOnTouchOutside(false);
        if(methodType == ALL_CONTRACT){
            if(DBInterface.instance().getUserCount() == 0){
                request();
            }else {
                List<UserEntity> entities = DBInterface.instance().loadAllUser();
                for (UserEntity entity:entities){
                    users.add(User.UserEntityToUser(entity));
                }
                if (users.size() == 0) {
                    problemLayout.setText("你所查之空值空如也,点击右上角刷新");
                    problemLayout.setVisibility(View.VISIBLE);
                } else {
                    problemLayout.setVisibility(View.GONE);
                }
                sortListAdapter = new MySortAdapter(getContext(), UserToNameHeader());
                sortLineLayout.clearLabel();
                sortLineLayout.addLable(sortListAdapter.getWhichChars());
                sortLineLayout.setSelect(0);
                listView.setAdapter(sortListAdapter);
            }
        }

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        problemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
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

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (methodType != Receiver.ALL_CONTRACT) return;
                if (sortListAdapter != null) {
                    if (sortListAdapter.getLineType(listView.getFirstVisiblePosition()) == SortListAdapter.Type.TYPE_LABLE) {
                        String headerChar = sortListAdapter.getContentOnList(listView.getFirstVisiblePosition());
                        int index = sortListAdapter.getWhichChars().indexOf(headerChar);
                        sortLineLayout.setSelect(index);
                    }
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (methodType == Receiver.ALL_CONTRACT) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
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
                } else if (methodType == Receiver.BRANCH_CONTRACT) {
                    Intent intent = new Intent(getActivity(), ContractActivity.class);
                    // Log.i("TAGG",users.get(position).getUserBranch());
                    intent.putExtra("extra", branches.get(position).getBranchName());
                    intent.putExtra("type", methodType);

                    startActivity(intent);
                } else if (methodType == Receiver.PARTCOMPANY_CONTRACT) {
                    Intent intent = new Intent(getActivity(), ContractActivity.class);
                    // Log.i("TAGG",users.get(position).getUserBranch());
                    intent.putExtra("extra", partCompanies.get(position).getPartCompanyName());
                    intent.putExtra("type", methodType);
                    startActivity(intent);
                }
            }
        });
    }

    public void request(){
        attribute.setMethodName(methodName);
        Map<String,String> map = new HashMap<>();
        map.put("arg0", "1");
        attribute.setParams(map);
        operater.request(attribute, "1");

        dialogP.show();
        attribute.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if (o.toString() == "") return;
                if (methodType == Receiver.ALL_CONTRACT) {
                    users.clear();
                    List<UserEntity> entities = new ArrayList<UserEntity>();
                    dialogP.dismiss();
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
                        entities.add(UserEntity.UserToUserEntity(user));
                    }
                    if (users.size() == 0) {
                        problemLayout.setText("你所查之空值空如也,点击右上角刷新");
                        problemLayout.setVisibility(View.VISIBLE);
                    } else {
                        problemLayout.setVisibility(View.GONE);
                    }
                    DBInterface.instance().insertOrUpdateUser(entities);
                } else if (methodType == Receiver.BRANCH_CONTRACT) {
                    branches.clear();
                    dialogP.dismiss();
                    String json = o.toString();
                    JSONArray array = JSON.parseArray(json);
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Branch branch = new Branch();
                        branch.setBranchId(object.getInteger("branchId"));
                        branch.setBranchName(object.getString("branchName"));
                        branch.setCompanyId(object.getInteger("companyId"));
                        branches.add(branch);
                    }
                    if (branches.size() == 0) {
                        problemLayout.setVisibility(View.VISIBLE);
                    } else {
                        problemLayout.setVisibility(View.GONE);
                    }


                } else if (methodType == Receiver.PARTCOMPANY_CONTRACT) {

                    partCompanies.clear();
                    dialogP.dismiss();
                    String json = o.toString();
                    JSONArray array = JSON.parseArray(json);
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        PartCompany partCompany = new PartCompany();
                        partCompany.setPartCompanyId(object.getInteger("partCompanyId"));
                        partCompany.setPartCompanyAddress(object.getString("partCompanyAddress"));
                        partCompany.setPartCompanyCompanyId(object.getInteger("partCompanyCompanyId"));
                        partCompany.setPartCompanyName(object.getString("partCompanyName"));
                        partCompanies.add(partCompany);
                    }
                    if (partCompanies.size() == 0) {
                        problemLayout.setVisibility(View.VISIBLE);
                    } else {
                        problemLayout.setVisibility(View.GONE);
                    }

                }
                if (methodType != Receiver.ALL_CONTRACT) {
                    listView.setAdapter(new MyAdapter());
                } else {
                    sortListAdapter = new MySortAdapter(getContext(), UserToNameHeader());
                    sortLineLayout.clearLabel();
                    sortLineLayout.addLable(sortListAdapter.getWhichChars());
                    sortLineLayout.setSelect(0);
                    listView.setAdapter(sortListAdapter);
                }
            }

            @Override
            public void OnError(String s) {
               /* AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("获取数据失败，请检查网络连接")
                        .create();
                dialog.show();*/
                dialogP.dismiss();
                users.clear();
                branches.clear();
                partCompanies.clear();

                if (methodType != Receiver.ALL_CONTRACT) {
                    listView.setAdapter(new MyAdapter());
                } else {
                    sortListAdapter = new MySortAdapter(getContext(), UserToNameHeader());
                    sortLineLayout.clearLabel();
                    sortLineLayout.addLable(sortListAdapter.getWhichChars());
                    sortLineLayout.setSelect(0);
                    listView.setAdapter(sortListAdapter);
                }

                problemLayout.setVisibility(View.VISIBLE);
                problemLayout.setText("获取数据失败...点击刷新");
            }

            @Override
            public void OnProgress(int i) {
                dialogP.setMessage("正在获取数据..." + " " + i + "%");
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!operater.isRunFinish()){
            operater.cancleAllRequest();
        }
    }

    @Override
    public void translateUser(User user,String pwd) {

    }

    @Override
    public void refreshContract() {
        if(listView != null){
            DBInterface.instance().clear();
            request();
        }
    }

    @Override
    public void showWhat(int type) {
        methodType = type;
        if(sortLineLayout == null) return ;

        if(type == Receiver.ALL_CONTRACT){
            methodName = ConnectHelper.METHOD.GETUSERBYCOMPANY;
            sortLineLayout.setVisibility(View.VISIBLE);
        }else if(type == Receiver.BRANCH_CONTRACT){
            methodName = ConnectHelper.METHOD.GETBRANCH;
            sortLineLayout.setVisibility(View.GONE);
        }else if(type == Receiver.PARTCOMPANY_CONTRACT){
            methodName =  ConnectHelper.METHOD.GETPARTCOMPANY;
            sortLineLayout.setVisibility(View.GONE);
        }
        if(methodType == ALL_CONTRACT){
            if(DBInterface.instance().getUserCount() == 0){
                request();
            }else {
                users.clear();
                List<UserEntity> entities = DBInterface.instance().loadAllUser();
                for (UserEntity entity:entities){
                    users.add(User.UserEntityToUser(entity));
                }
                if (users.size() == 0) {
                    problemLayout.setText("你所查之空值空如也,点击右上角刷新");
                    problemLayout.setVisibility(View.VISIBLE);
                } else {
                    problemLayout.setVisibility(View.GONE);
                }
                sortListAdapter = new MySortAdapter(getContext(), UserToNameHeader());
                sortLineLayout.clearLabel();
                sortLineLayout.addLable(sortListAdapter.getWhichChars());
                sortLineLayout.setSelect(0);
                listView.setAdapter(sortListAdapter);
            }
        }else {
            request();
        }
    }

    class MyAdapter extends BaseAdapter{



        @Override
        public int getCount() {
            switch (methodType){
                case Receiver.ALL_CONTRACT:
                    return users.size();

                case Receiver.BRANCH_CONTRACT:
                    return branches.size();
                case Receiver.PARTCOMPANY_CONTRACT:
                    return partCompanies.size();
            }
            return 0;
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
            if(methodType == Receiver.ALL_CONTRACT){
                if(convertView == null){
                    convertView = new ContractItemCell(getContext());
                }
                ContractItemCell cell = (ContractItemCell) convertView;
                cell.setText(users.get(position).getUserName());
            }else if(methodType == Receiver.PARTCOMPANY_CONTRACT){
                if(convertView == null){
                    convertView = new TextBigCell(getContext());
                }
                TextBigCell cell = (TextBigCell) convertView;
                cell.setText(partCompanies.get(position).getPartCompanyName(),true);
            }else if(methodType == Receiver.BRANCH_CONTRACT){
                if(convertView == null){
                    convertView = new TextBigCell(getContext());
                }
                TextBigCell cell = (TextBigCell) convertView;
                cell.setText(branches.get(position).getBranchName(),true);
            }
            return convertView;
        }
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
    class MySortAdapter extends SortListAdapter{


        public MySortAdapter(Context context, List<NameHeader> SortContents) {
            super(context, SortContents);
        }



        @Override
        public View contentView(NameHeader content,int position, View convertView, ViewGroup parent) {
            int viewType =  getItemViewType(position);
            if(viewType == 2){
                if(convertView == null){
                    convertView = new ContractItemCell(getContext());
                }
                ContractItemCell cell = (ContractItemCell) convertView;
                cell.setText(content.getName());
            }else if (viewType == 1){
                if(convertView == null){
                    convertView = new ContractImageItemCell(getContext());
                }
                ContractImageItemCell cell = (ContractImageItemCell) convertView;
                byte[] photoimg = new byte[0];
                photoimg = Base64.decode(content.getHeader(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(photoimg, 0, photoimg.length);
                cell.setImage(bitmap);
                cell.setText(content.getName());


            }

            return convertView;
        }
    }


}

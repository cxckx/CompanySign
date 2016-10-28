package cx.companysign.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.ConnectUtils;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.componts.DefaultHeadPortraitView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.bean.User;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.DeviceUuidFactory;
import cx.companysign.utils.Receiver;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.activity.AboutActivity;
import cx.companysign.view.activity.LoginActivity;
import cx.companysign.view.activity.RegisterActivity;
import cx.companysign.view.activity.SignActivity;
import cx.companysign.view.activity.SignHistoryActivity;
import cx.companysign.view.activity.WeatherActivity;
import cx.companysign.view.cell.ImageTextCell;
import cx.companysign.view.componts.AppDialog;
import cx.companysign.view.componts.LoadDialog;

/**
 * Created by cxcxk on 2016/10/10.
 */
public class MineFragment extends Fragment implements Receiver {


    DefaultHeadPortraitView cell;

    TextView branch,partCompany;
    ListView mineListView;
    User user = new User();
    NetDataOperater operater;
    NetDataOperater.Attribute attributeSign,attributeSignOut;
    int isSign = -1;
    boolean isRequest = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mine_f_layout,container,false);
        cell = (DefaultHeadPortraitView) view.findViewById(R.id.header_cell);
        branch = (TextView) view.findViewById(R.id.branch);
        partCompany = (TextView) view.findViewById(R.id.part_company);
        cell.setColor(getResources().getColor(R.color.green));
        mineListView = (ListView) view.findViewById(R.id.mine_list);
        mineListView.setDivider(null);
        mineListView.setDividerHeight(0);

        operater = new WebServiceDataNetOperator();
        attributeSign = ConnectHelper.createAttribute();
        attributeSignOut = ConnectHelper.createAttribute();
        attributeSign.setMethodName(ConnectHelper.METHOD.ISSIGN);
        attributeSignOut.setMethodName(ConnectHelper.METHOD.ISSIGNOUT);

        attributeSign.setNetWork(new INetWork<String>() {
            @Override
            public void OnCompleted(String s) {
                if (s.toString() == "") return;
                isRequest = false;
                if (Boolean.parseBoolean(s)) {
                    isSign = 0;
                    operater.request(attributeSignOut, "2");
                } else {
                    dialog.dismiss();
                    isSign = -1;
                }
            }

            @Override
            public void OnError(String s) {
                MineFragment.this.dialog.dismiss();
                AppDialog dialog1 = new AppDialog.Builder(getContext())
                        .setTitle("异常")
                        .setMessage("网络异常")
                        .setCancleableOnTouchOutSide(false)
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MineFragment.this.dialog.show();
                                operater.request(attributeSign,"1");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }).create();
                dialog1.show();
            }

            @Override
            public void OnProgress(int i) {

            }
        });
        attributeSignOut.setNetWork(new INetWork() {
            @Override
            public void OnCompleted(Object o) {
                if (o.toString() == "") return;
                isRequest = false;
                if (Boolean.parseBoolean(o.toString())) {
                    isSign = 1;
                } else {
                    isSign = 0;
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void OnError(String s) {
                dialog.dismiss();
                AppDialog dialog1 = new AppDialog.Builder(getContext())
                        .setTitle("异常")
                        .setMessage("网络异常")
                        .setCancleableOnTouchOutSide(false)
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MineFragment.this.dialog.show();
                                operater.request(attributeSign,"1");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MineFragment.this.dialog.dismiss();

                            }
                        }).create();
                dialog1.show();
            }

            @Override
            public void OnProgress(int i) {

            }
        });

        dialog = new LoadDialog.Builder(getContext()).
                setCancleableOnTouchOutSide(false).
                create();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mineListView.setAdapter(new MyAdapter());


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 5) {
                    Intent intent = new Intent(getContext(), AboutActivity.class);
                    startActivity(intent);
                } else if (position == 0) {

                    if(isSign == -1 || isSign == 0){
                        Intent intent = new Intent(getContext(), SignActivity.class);
                        intent.putExtra("userid",user.getUserId());
                        intent.putExtra("issign",isSign);
                        startActivityForResult(intent, 0);
                    }else if(isSign == 1){
                        Toast.makeText(getContext(),"今天的签到流程已完成,请明天再来签到",Toast.LENGTH_SHORT).show();
                    }



                }else if(position == 1){
                     Intent intent = new Intent(getContext(), SignHistoryActivity.class);
                    intent.putExtra("userid",user.getUserId());
                    startActivity(intent);
                }else if(position == 2){
                    Intent intent = new Intent(getContext(), WeatherActivity.class);
                    startActivity(intent);
                }else if(position == 3){
                    final NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
                    attribute.setMethodName(ConnectHelper.METHOD.LOGINOUT);
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("arg0", user.getUserPhone());
                    attribute.setParams(map);
                    operater.request(attribute, "a");
                    attribute.setNetWork(new INetWork() {
                        @Override
                        public void OnCompleted(Object o) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void OnError(String s) {
                            AppDialog dialog = new AppDialog.Builder(getActivity())
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

                }else if(position == 6){
                    AppDialog dialog = new AppDialog.Builder(getContext())
                            .setTitle("授权码")
                            .setMessage(new DeviceUuidFactory(getContext()).getDeviceUuid().toString())
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }else if(position == 4){
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    intent.putExtra("user",user);
                    intent.putExtra("pwd",pwd);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!operater.isRunFinish()){
            operater.cancleAllRequest();
        }
    }
    String pwd;
    @Override
    public void translateUser(User user,String pwd) {
        this.user = user;
        this.pwd = pwd;
        if(user == null) return ;
        if(isRequest && ConnectUtils.isConnect()){
            Map<String,String> map = new HashMap<>();
            map.put("arg0", user.getUserId().toString());
            map.put("arg1", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            attributeSign.setParams(map);
            attributeSignOut.setParams(map);
            dialog.show();
            operater.request(attributeSign, "1");
        }


        cell.setText(user.getUserName().substring(0,1));
        branch.setText(user.getUserBranch());
        partCompany.setText(user.getUserPartCompany());

    }

    @Override
    public void setArguments(Bundle args) {

    }

    String [] items = {"签到/签退","历史签到","天气预报","切换账号","个人信息","关于","授权码"};
    int [] itemsRes = {R.drawable.sign_dpi_24dp,R.drawable.history_sign_24dp,R.drawable.weather,R.drawable.qiehuan,R.drawable.myinfo,R.drawable.about,R.drawable.shouquanma};
    @Override
    public void refreshContract() {

    }
    LoadDialog dialog;
    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*dialog.show();
        operater.request(attributeSign, "1");*/
        isSign =data.getIntExtra("isSign", -1);
        //isRequest = data.getBooleanExtra("isRequest",false);

    }

    @Override
    public void showWhat(int type) {
        /**
         * 不作处理
         */
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
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
                convertView = new ImageTextCell(getContext());
            }
            ImageTextCell cell = (ImageTextCell) convertView;
            cell.setResId(itemsRes[position]);
            cell.setText(items[position]);
            return convertView;
        }
    }
}

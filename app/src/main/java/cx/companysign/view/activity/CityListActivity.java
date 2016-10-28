package cx.companysign.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.ConnectUtils;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.actionbar.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.CityUtils;
import cx.companysign.view.cell.LoadingRow;
import cx.companysign.view.cell.NoResultCell;
import cx.companysign.view.cell.TextCell;
import cx.companysign.view.cell.TextImageCell;

/**
 * Created by cxcxk on 2016/4/15.
 */
public class CityListActivity extends BaseActivity implements View.OnClickListener{

    public static int LOADING = 0;
    public static int LOADED = 1;
    public static int UNLOAD = 2;

    ListView listView;
    TextView beijing,shanghai,tianjin,chongqing,aomen,xianggang;
    List<String> provinces = new ArrayList<>();
    Map<String,List<String>> citys = new HashMap<>();
    ListAdapter adapter;
    ListPopWindowAdapter listPopWindowAdapter;
    Handler handler  = new Handler();

    SearchView searchView;

    ListPopupWindow popupWindow;
    boolean isNoResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.city_list_layout);
        ActivityUtils.addActiviy(this);
        final ActionBar actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setTitle("城市列表");

        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int index) {
                if (index == -1) {
                    onBackPressed();
                }
            }
        });

        popupWindow = new ListPopupWindow(this);
        listPopWindowAdapter = new ListPopWindowAdapter();
        popupWindow.setAdapter(listPopWindowAdapter);
        int width = getResources().getDisplayMetrics().widthPixels;

        popupWindow.setWidth(width- AndroidUtils.dip2px(this, 32));
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                popupWindow.dismiss();
                TextCell cell = (TextCell) arg1;
                Intent intent = new Intent();
                intent.putExtra("city", cell.getText());
                setResult(1000, intent);
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.city_province);
        beijing = (TextView) findViewById(R.id.beijing);
        shanghai = (TextView) findViewById(R.id.shanghai);
        tianjin = (TextView) findViewById(R.id.tianjin);
        chongqing = (TextView) findViewById(R.id.chongqing);
        aomen = (TextView) findViewById(R.id.aomen);
        xianggang = (TextView) findViewById(R.id.xianggang);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setHint("请输入关键词");
        popupWindow.setAnchorView(searchView);
        searchView.setSearchInputListener(new SearchView.SearchInputListener() {
            @Override
            public void afterTextChanged(String s) {
                List<String> city = new ArrayList<String>();
                for (List<String> list: CityUtils.citys.values()){
                    for (String str:list){
                        if(!TextUtils.isEmpty(s)&&str.contains(s)){
                            city.add(str);
                        }
                    }
                }
                if (!TextUtils.isEmpty(s)&&city.size() == 0){
                    isNoResult = true;
                    city.add("未查找到结果");
                }else {
                    isNoResult = false;
                }
                listPopWindowAdapter.bindData(city);
                if(TextUtils.isEmpty(s)){
                    popupWindow.dismiss();
                }else {
                    popupWindow.show();
                }
            }
        });

        beijing.setOnClickListener(this);
        shanghai.setOnClickListener(this);
        tianjin.setOnClickListener(this);
        chongqing.setOnClickListener(this);
        aomen.setOnClickListener(this);
        xianggang.setOnClickListener(this);

        listView.setDivider(null);
        listView.setDividerHeight(0);

        adapter = new ListAdapter();
        listView.setAdapter(adapter);
        isLoading = LOADING;
        update(-1);

        if (ConnectUtils.isConnect()) {
            //通过工具类调用WebService接口

            provinces.addAll(CityUtils.prvince.keySet());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoading = UNLOAD;
                    update(0);
                }
            },2000);




        }else {
            Toast.makeText(CityListActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (isLoading == LOADED && position > mPosition && position <= citys.get(provinces.get(mPosition)).size() + mPosition) {


                    Intent intent = new Intent();
                    intent.putExtra("city", citys.get(provinces.get(mPosition)).get(position - mPosition - 1));
                    setResult(1000, intent);
                    finish();
                    return;
                }

                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (position < mPosition || mPosition < 0) {
                    mPosition = position;
                } else if (position > mPosition) {
                    List<String> city = citys.get(provinces.get(mPosition));
                    int size = 0;
                    if (city == null) {
                        size = 1;
                    } else {
                        size = city.size();
                    }
                    mPosition = position - size;
                } else if (position == mPosition) {
                    isLoading = UNLOAD;
                    update(mPosition);
                    mPosition = -1;
                    return;
                }

                isLoading = UNLOAD;
                if (isLoading == UNLOAD) {
                    isLoading = LOADING;
                    update(mPosition);
                    final List<String> list = citys.get(provinces.get(mPosition));
                    if (list == null) {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                citys.put(provinces.get(mPosition), CityUtils.citys.get(provinces.get(mPosition)));
                                isLoading = LOADED;
                                update(mPosition);
                            }
                        },2000);


                    } else {
                        isLoading = LOADED;
                        update(mPosition);
                    }
                }

            }
        });


    }

    int mPosition = -1;//当前点击的行

    int count = 0;
    int loadingRow = -1;
    int  isLoading = UNLOAD;



    private void update(final int position){

        count = 0;
       if(isLoading == LOADING) {
           loadingRow = position+1;
           count += provinces.size()+1;
       }else if(isLoading == LOADED){
           List<String> list = citys.get(provinces.get(position));

           int size = 0;
           if (list != null){
               size = list.size();
           }else {
               list = new ArrayList<>();
               citys.put(provinces.get(position),list);
           }
           count+=provinces.size()+size;
       }else if(isLoading == UNLOAD){
           count+=provinces.size();
       }

        adapter.notifyDataSetChanged();
        listView.setSelection(position);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if(v.getId() == beijing.getId()){

            intent.putExtra("city", "北京");

        }else if(v.getId() == tianjin.getId()){

            intent.putExtra("city", "天津");

        }else if(v.getId() == shanghai.getId()){

            intent.putExtra("city", "上海");

        }else if(v.getId() == chongqing.getId()){

            intent.putExtra("city", "重庆");

        }else if(v.getId() == aomen.getId()){

            intent.putExtra("city", "澳门");

        }else if(v.getId() == xianggang.getId()){

            intent.putExtra("city", "香港");

        }
        setResult(1000,intent);
        finish();
    }

    class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return count;
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
        public int getViewTypeCount() {
            return 3;
        }




        @Override
        public int getItemViewType(int position) {
             if(position == loadingRow && isLoading == LOADING){
                 return 0;
             }else if(isLoading == LOADED && (position > mPosition&&position<=citys.get(provinces.get(mPosition)).size()+mPosition)){
                 return 2;
             }
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             int viewType = getItemViewType(position);
             if (viewType == 0) {
                    if(convertView == null){
                        convertView = new LoadingRow(CityListActivity.this);
                    }
                   LoadingRow row = (LoadingRow) convertView;
             }else if(viewType == 1){
                 if(convertView == null){
                     convertView = new TextImageCell(CityListActivity.this);
                 }
                 TextImageCell view = (TextImageCell) convertView;


                view.setOpen(position == mPosition);

                 int size = 0;
                 if(isLoading == LOADED){
                     List<String> list = citys.get(provinces.get(mPosition));
                     if(list != null){
                         size = list.size();
                     }
                 }else if(isLoading == LOADING){
                     size = 1;
                 }

                if(position > mPosition+size){
                    String show = CityUtils.prvince.get(provinces.get(position-size));

                    view.setShowText(show);
                    view.setText(provinces.get(position-size),true);
                }else {
                    String show = CityUtils.prvince.get(provinces.get(position));
                    view.setShowText(show);
                    view.setText(provinces.get(position),true);
                }
             }else if(viewType == 2){
                 if(convertView == null){
                     convertView = new TextCell(CityListActivity.this);
                 }
                 TextCell view = (TextCell) convertView;
                 String city =  citys.get(provinces.get(mPosition)).get(position-mPosition-1);
                 view.setText(city,true);
             }


            return convertView;
        }
    }


    class ListPopWindowAdapter extends BaseAdapter{

        List<String> city = new ArrayList<>();


        public void bindData(List<String> city){
            this.city = city;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return city.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public boolean isEnabled(int position) {
            if(isNoResult){
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if(isNoResult){
                return 0;
            }
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);
            if(viewType == 0){
                if (convertView == null){
                    convertView = new NoResultCell(CityListActivity.this);
                }
                NoResultCell cell = (NoResultCell) convertView;
                cell.setText(city.get(position));
            }else if(viewType == 1){
                if(convertView == null){
                    convertView = new TextCell(CityListActivity.this);
                }
                TextCell cell = (TextCell) convertView;

                cell.setText(city.get(position),true);
            }
            return convertView;
        }
    }

}

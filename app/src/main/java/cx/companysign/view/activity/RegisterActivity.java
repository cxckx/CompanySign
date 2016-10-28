package cx.companysign.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.INetWork;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.componts.DateSelectView;
import com.example.cxcxk.android_my_library.view.componts.DefaultHeadPortraitView;
import com.example.cxcxk.android_my_library.view.componts.ImageLauncher;
import com.example.cxcxk.android_my_library.view.componts.SexSelectLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cx.companysign.R;
import cx.companysign.bean.User;
import cx.companysign.utils.ActivityUtils;
import cx.companysign.utils.ConnectHelper;
import cx.companysign.utils.WebServiceDataNetOperator;
import cx.companysign.view.componts.AppDialog;
import cx.companysign.view.componts.ImageViewPlus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cxcxk on 2016/5/29.
 */
public class RegisterActivity extends BaseActivity {

    ActionBar actionBar;
    EditText phoneEdt,nameEdit;

    DateSelectView dateSelectView;
    SexSelectLayout sexSelectLayout;
    SimpleDateFormat format;
    String mDate;
    NetDataOperater<String> mDataOperater;
   FrameLayout mFrameLayout;
    Bitmap mBitmap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ActivityUtils.addActiviy(this);
        mFrameLayout = (FrameLayout) findViewById(R.id.header_container);
        format = new SimpleDateFormat("yyyy-MM-dd");
        final User user = getIntent().getParcelableExtra("user");
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        dateSelectView = (DateSelectView) findViewById(R.id.date_select);
        sexSelectLayout = (SexSelectLayout) findViewById(R.id.sex_layout);
        phoneEdt = (EditText) findViewById(R.id.phone);
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        nameEdit = (EditText) findViewById(R.id.name);
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setTitle("修改个人信息");
        actionBar.addMenu(0, R.drawable.ic_check_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(final int index) {
                if (index == -1) {
                     onBackPressed();
                }else if(index == 0){
                    InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    bitmapToString(new PhotoHandler() {
                        @Override
                        public void handler(final String photo) {
                            final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                            dialog.setMessage("正在上传...");
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            if (phoneEdt.getText().toString().equals(user.getUserPhone())) {
                                NetDataOperater.Attribute attribute1 = ConnectHelper.createAttribute();
                                attribute1.setMethodName(ConnectHelper.METHOD.UPDATEINFO);
                                Map<String, String> map1 = new HashMap<String, String>();
                                map1.put("arg0", nameEdit.getText().toString());
                                map1.put("arg1", phoneEdt.getText().toString());
                                map1.put("arg2", user.getUserPhone());
                                map1.put("arg3", photo);

                                attribute1.setParams(map1);

                                mDataOperater.request(attribute1, "2");
                                attribute1.setNetWork(new INetWork() {
                                    @Override
                                    public void OnCompleted(Object o) {
                                        if (o.toString().equals("")) return;
                                        dialog.dismiss();
                                        if (Boolean.parseBoolean(o.toString())) {
                                            Toast.makeText(RegisterActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            ActivityUtils.finishAll2();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void OnError(String s) {
                                        dialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "修改失败,网络异常", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void OnProgress(int i) {

                                    }
                                });


                            } else {
                                NetDataOperater.Attribute attribute = ConnectHelper.createAttribute();
                                attribute.setMethodName(ConnectHelper.METHOD.ISHAVEPHONE);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("arg0", phoneEdt.getText().toString());
                                attribute.setParams(map);
                                mDataOperater.request(attribute, "1");
                                attribute.setNetWork(new INetWork<String>() {
                                    @Override
                                    public void OnCompleted(String s) {
                                        if (s.equals("")) {
                                            return;
                                        }
                                        dialog.dismiss();
                                        if (Boolean.parseBoolean(s)) {
                                            Toast.makeText(RegisterActivity.this, "手机号已存在", Toast.LENGTH_SHORT).show();
                                        } else {
                                            NetDataOperater.Attribute attribute1 = ConnectHelper.createAttribute();
                                            attribute1.setMethodName(ConnectHelper.METHOD.UPDATEINFO);
                                            Map<String, String> map1 = new HashMap<String, String>();
                                            map1.put("arg0", nameEdit.getText().toString());
                                            map1.put("arg1", phoneEdt.getText().toString());
                                            map1.put("arg2", user.getUserPhone());
                                            map1.put("arg3", photo);

                                            attribute1.setParams(map1);

                                            mDataOperater.request(attribute1, "2");
                                            attribute1.setNetWork(new INetWork() {
                                                @Override
                                                public void OnCompleted(Object o) {
                                                    if (o.toString().equals("")) return;

                                                    if (Boolean.parseBoolean(o.toString())) {

                                                        Toast.makeText(RegisterActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        ActivityUtils.finishAll2();

                                                        //ListenerSingleUtils.getInstance().listener(getIntent().getStringExtra("pwd"), phoneEdt.getText().toString());
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void OnError(String s) {
                                                    Toast.makeText(RegisterActivity.this, "修改失败,网络异常", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void OnProgress(int i) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void OnError(String s) {
                                        dialog.dismiss();
                                        AppDialog dialog = new AppDialog.Builder(RegisterActivity.this)
                                                .setTitle("提示")
                                                .setMessage("网络异常")
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

                            }
                        }
                    });



                }
            }
        });
        mDataOperater = new WebServiceDataNetOperator();

        if(user.getUserIconHeader().equals("")){
            DefaultHeadPortraitView cell = new  DefaultHeadPortraitView(this);
            cell.setColor(getResources().getColor(R.color.colorPrimaryDark));
            cell.setText(user.getUserName().substring(0, 1));
            mFrameLayout.addView(cell, LayoutHelper.createFrameLayout(AndroidUtils.dip2px(this, 100), AndroidUtils.dip2px(this, 100), Gravity.CENTER));
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setItems(new String[]{"拍照", "相册选取"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if(which == 0){//拍照
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                                        startActivityForResult(intent, 1);
                                    }else{//相册选取
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    /* 开启Pictures画面Type设定为image */
                                        intent.setType("image/*");
                                    /* 取得相片后返回本画面 */
                                        startActivityForResult(intent, 1);
                                    }
                                }
                            }).create();
                    dialog.show();
                }
            });
        }else {
            ImageViewPlus imageViewPlus = new ImageViewPlus(this);
            byte []photoimg = Base64.decode(user.getUserIconHeader(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoimg, 0, photoimg.length);
            imageViewPlus.setImageBitmap(bitmap);
            mFrameLayout.addView(imageViewPlus, LayoutHelper.createFrameLayout(AndroidUtils.dip2px(this, 100), AndroidUtils.dip2px(this, 100), Gravity.CENTER));
            imageViewPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setItems(new String[]{"拍照", "相册选取"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (which == 0) {//拍照
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                                        startActivityForResult(intent, 1);
                                    } else {//相册选取
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    /* 开启Pictures画面Type设定为image */
                                        intent.setType("image/*");
                                    /* 取得相片后返回本画面 */
                                        startActivityForResult(intent, 1);
                                    }
                                }
                            }).create();
                    dialog.show();
                }
            });
        }

        phoneEdt.setText(user.getUserPhone());
        nameEdit.setText(user.getUserName());
        mDate = format.format(new Date(user.getUserBirthday()));
        dateSelectView.setDate(mDate);
        sexSelectLayout.setSelected(1-user.getUserSex());
        sexSelectLayout.setTexts("男", "女");
        sexSelectLayout.setClickable(false);
        /*dateSelectView.setOnClick(new IOnClick() {
            @Override
            public void onClick() {
                final DatePicker picker = new DatePicker(RegisterActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                        .setView(picker)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDate = picker.getYear() + "-" + (picker.getMonth() + 1) + "-" + picker.getDayOfMonth();
                                *//*try {
                                    timeBirth = new SimpleDateFormat("yyyy-MM-dd").parse(mDate).getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*//*
                                dateSelectView.setDate(mDate);
                            }
                        }).create();
                dialog.show();
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mDataOperater.isRunFinish()){
            mDataOperater.cancleAllRequest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if (data != null) {
                //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                Uri mImageCaptureUri = data.getData();
                //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                if (mImageCaptureUri != null) {
                    Bitmap image;
                    try {
                        //这个方法是根据Uri获取Bitmap图片的静态方法
                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        if (image != null) {
                            mFrameLayout.removeAllViews();
                            ImageLauncher launcher = new ImageLauncher(RegisterActivity.this);
                            launcher.initParam(R.drawable.icon,true);
                            launcher.load(new BitmapDrawable(getResources(), image));
                            mFrameLayout.addView(launcher, LayoutHelper.createFrameLayout(AndroidUtils.dip2px(this, 100), AndroidUtils.dip2px(this, 100), Gravity.CENTER));
                            mBitmap = image;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                        Bitmap image = extras.getParcelable("data");
                        if (image != null) {
                            mFrameLayout.removeAllViews();
                            ImageLauncher launcher = new ImageLauncher(RegisterActivity.this);
                            launcher.initParam(R.drawable.icon,true);
                            launcher.load(new BitmapDrawable(getResources(),image));
                            mFrameLayout.addView(launcher, LayoutHelper.createFrameLayout(AndroidUtils.dip2px(this, 100), AndroidUtils.dip2px(this, 100),Gravity.CENTER));
                            mBitmap = image;
                        }
                    }
                }

            }
        }
    }

    public void bitmapToString(final PhotoHandler h){
        Observable.just(mBitmap)
                .map(new Func1<Bitmap, String>() {
                    @Override
                    public String call(Bitmap bitmap) {
                        if (bitmap == null) return null;
                        String photo = null;
                        ByteArrayOutputStream baos = null;
                        try {
                            baos  = new ByteArrayOutputStream();

                            //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);


                            byte[] buffers = baos.toByteArray();
                            System.out.println("图片的大小：" + buffers.length);

                            //将图片的字节流数据加密成base64字符输出
                            photo = Base64.encodeToString(buffers, 0, buffers.length, Base64.DEFAULT);


                        }finally {
                            if(baos != null)
                                try {
                                    baos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }

                            return photo;
                        }
                    }

                    ).

                    observeOn(AndroidSchedulers.mainThread()

                    )
                            .

                    subscribeOn(Schedulers.newThread()

                    )
                            .

                    subscribe(new Action1<String>() {
                                  @Override
                                  public void call(String s) {
                                      if (h != null) {
                                          h.handler(s);
                                      }
                                  }
                              }

                    );


                }

        public interface PhotoHandler{
       void handler(String s);
    }
}

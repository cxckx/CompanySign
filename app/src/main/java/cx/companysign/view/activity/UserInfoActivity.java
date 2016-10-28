package cx.companysign.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cx.companysign.R;
import cx.companysign.bean.User;
import cx.companysign.utils.ActivityUtils;

public class UserInfoActivity extends AppCompatActivity {

    private User user;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActivityUtils.addActiviy(this);
        user = getIntent().getParcelableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout coordinatorLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        coordinatorLayout.setTitle(user.getUserName());
        coordinatorLayout.setExpandedTitleTypeface(Typeface.SERIF);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 打电话
                 */
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + user.getUserPhone());
                intent.setData(data);
                int c = ContextCompat.checkSelfPermission(UserInfoActivity.this, "android.permission.CALL_PHONE");
                if (c == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }

            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RecycleViewAdapter());
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Bitmap bitmap = null;
        if(user.getUserIconHeader().equals("")){
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.back_info);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            byte[] photoimg = new byte[0];
            photoimg =  Base64.decode(user.getUserIconHeader(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(photoimg, 0, photoimg.length);
        }

       /* if(!bitmap.isRecycled()){
            bitmap.recycle();
        }*/
        Palette.from(bitmap)
                .generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch s1 = palette.getVibrantSwatch();

                Palette.Swatch s2 = palette.getDarkVibrantSwatch();

                Palette.Swatch s3 = palette.getLightVibrantSwatch();

                Palette.Swatch s4 = palette.getMutedSwatch();

                Palette.Swatch s5 = palette.getDarkMutedSwatch();

                Palette.Swatch s6 = palette.getLightMutedSwatch();

                if (s1 != null) {
                    int redValue = Color.red(s1.getRgb());
                    int blueValue = Color.blue(s1.getRgb());
                    int greenValue = Color.green(s1.getRgb());
                    int color = Color.rgb(Math.abs(redValue - 255), Math.abs(greenValue - 255), Math.abs(blueValue - 255));
                    coordinatorLayout.setExpandedTitleColor(color);
                    return;
                }

                if (s2 != null) {

                    coordinatorLayout.setExpandedTitleColor(s2.getTitleTextColor());

                }

                if (s3 != null) {

                    coordinatorLayout.setExpandedTitleColor(s3.getTitleTextColor());

                }

                if (s4 != null) {

                    coordinatorLayout.setExpandedTitleColor(s4.getTitleTextColor());

                }

                if (s5 != null) {

                    coordinatorLayout.setExpandedTitleColor(s5.getTitleTextColor());

                }

                if (s6 != null) {

                    coordinatorLayout.setExpandedTitleColor(s6.getTitleTextColor());

                }
            }
        });
        //bitmap = BitmapUtils.blurBitmap(this,bitmap);
        imageView.setImageBitmap(bitmap);
    }


    class RecycleViewAdapter extends RecyclerView.Adapter<ViewHolders>{


        @Override
        public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.card_layout,null);
            ViewHolders holder = new ViewHolders(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolders holder, int position) {
            String s = "";
             if(position == 0){
                 s = "手机号: "+user.getUserPhone();
             }else if(position == 1){
                 s = "性别: "+ (user.getUserSex() == 1 ? "男":"女")+"  年龄: "+(new Date().getYear()-new Date(user.getUserBirthday()).getYear());
             }else if(position == 2){
                 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                 s = "出生日期: "+(format.format(new Date(user.getUserBirthday())));
             }else if(position == 3){
                 s = "部门: "+ user.getUserBranch();
             }else if(position == 4){
                 s = "所在分公司: "+user.getUserPartCompany();
             }
             holder.view.setText(s);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
    class ViewHolders extends RecyclerView.ViewHolder{

        public TextView view;
        public ViewHolders(View itemView) {
            super(itemView);
            view = (TextView) itemView.findViewById(R.id.content_text);
        }
    }
}

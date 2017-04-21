package com.example.qy.q233;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

/**
 * Created by Xu Yining on 2017/4/14.
 */

public class StartActivity extends Activity {

//    private ViewPager viewPager;
//
//    /**装分页显示的view的数组*/
//    private ArrayList<View> pageViews;
//    private ImageView imageView;
//
//    /**将小圆点的图片用数组表示*/
//    private ImageView[] imageViews;
//
//    //包裹滑动图片的LinearLayout
//    private ViewGroup viewPics;
//
//    //包裹小圆点的LinearLayout
//    private ViewGroup viewPoints;
    ViewFlipper viewFlipper = null;
    float startX;

    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        ((MyApp) getApplication()).setSharedPreferences(sp);

        Button login = (Button) findViewById(R.id.start_bt_login);
        Button signup = (Button) findViewById(R.id.start_bt_signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(StartActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login.setOnTouchListener(new TouchDark());
        signup.setOnTouchListener(new TouchDark());

        init();

//        LayoutInflater inflater = getLayoutInflater();
//        pageViews = new ArrayList<View>();
//        pageViews.add(inflater.inflate(R.layout.viewpager_page1, null));
//        pageViews.add(inflater.inflate(R.layout.viewpager_page2, null));
//        //创建imageviews数组，大小是要显示的图片的数量
//        imageViews = new ImageView[pageViews.size()];
//        //从指定的XML文件加载视图
//        viewPics = (ViewGroup) inflater.inflate(R.layout.start, null);
//        //实例化小圆点的linearLayout和viewpager
//        viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup);
//        viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
//        //添加小圆点的图片
//        for(int i=0;i<pageViews.size();i++){
//            imageView = new ImageView(StartActivity.this);
//            //设置小圆点imageview的参数
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));//创建一个宽高均为20 的布局
//            imageView.setPadding(20, 0, 20, 0);
//            //将小圆点layout添加到数组中
//            imageViews[i] = imageView;



//            //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
//            if(i==0){
//                imageViews[i].setBackgroundResource(R.drawable.background);
//            }else{
//                imageViews[i].setBackgroundResource(R.drawable.backgroungacc);
//            }
//
//            //将imageviews添加到小圆点视图组
//            viewPoints.addView(imageViews[i]);
//        }
//        //显示滑动图片的视图
//        setContentView(viewPics);
//        //设置viewpager的适配器和监听事件
//        viewPager.setAdapter(new GuidePageAdapter());
//        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }


    private void init() {
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:

                if (event.getX() > startX) { // 向右滑动
                    viewFlipper.setInAnimation(this, R.anim.in_leftright);
                    viewFlipper.setOutAnimation(this, R.anim.out_leftright);
                    viewFlipper.showNext();
                } else if (event.getX() < startX) { // 向左滑动
                    viewFlipper.setInAnimation(this, R.anim.in_rightleft);
                    viewFlipper.setOutAnimation(this, R.anim.out_rightleft);
                    viewFlipper.showPrevious();
                }
                break;
        }

        return super.onTouchEvent(event);
    }


}

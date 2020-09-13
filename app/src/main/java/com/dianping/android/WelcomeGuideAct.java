package com.dianping.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideAct extends AppCompatActivity implements View.OnClickListener{

    ViewPager welcomePager;

    List<View> pagerList;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        welcomePager = (ViewPager) findViewById(R.id.welcome_page);

        btn = (Button) findViewById(R.id.welcome_btn);
        btn.setOnClickListener(this);
        initPager();
    }

    private void initPager() {
        pagerList = new ArrayList<>();

        ImageView view_01 = new ImageView(this);
        view_01.setImageResource(R.drawable.guide_01);
        pagerList.add(view_01);

        ImageView view_02 = new ImageView(this);
        view_02.setImageResource(R.drawable.guide_02);
        pagerList.add(view_02);

        ImageView view_03 = new ImageView(this);
        view_03.setImageResource(R.drawable.guide_03);
        pagerList.add(view_03);

        welcomePager.setAdapter(new MyAdapter());

        welcomePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页卡被选中是被调用，Animation is not necessarily complete.所以加载按钮的速度要比onPageScrolled块
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(WelcomeGuideAct.this,MainActivity.class);
        startActivity(intent);
    }

    private class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) pagerList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pagerList.get(position));
        }
    }
}
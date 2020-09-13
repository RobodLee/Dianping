package com.dianping.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dianping.android.utils.SharedUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeStartAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);

        //使用Handle方法实现跳转
//        new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                startActivity(new Intent(WelcomeStartAct.this,MainActivity.class));
//            }
//        }.sendEmptyMessageDelayed(0,1000);

        //使用Timer方法实现跳转
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (SharedUtils.getSharedBoolean()) {
                    startActivity(new Intent(WelcomeStartAct.this,WelcomeGuideAct.class));
                    SharedUtils.setShareBoolean();
                } else {
                    startActivity(new Intent(WelcomeStartAct.this,MainActivity.class));
                }
                finish();
            }
        },1000);
    }
}
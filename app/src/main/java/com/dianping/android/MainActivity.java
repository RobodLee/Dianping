package com.dianping.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dianping.android.fragment.FragmentHome;
import com.dianping.android.fragment.FragmentMy;
import com.dianping.android.fragment.FragmentSearch;
import com.dianping.android.fragment.FragmentTuan;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    RadioGroup group;

    RadioButton home;     //首页按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = (RadioGroup) findViewById(R.id.main_bottom_tabs);
        home = (RadioButton) findViewById(R.id.main_home);
        group.setOnCheckedChangeListener(this);
        home.setChecked(true);
        replaceFragment(new FragmentHome(),false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.main_home:
                replaceFragment(new FragmentHome(),true);
                break;
            case R.id.main_tuan:
                replaceFragment(new FragmentTuan(),true);
                break;
            case R.id.main_search:
                replaceFragment(new FragmentSearch(),true);
                break;
            case R.id.main_my:
                replaceFragment(new FragmentMy(),true);
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment,boolean isInit) {
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_content,fragment);
        //if (isInit) {
            transaction.addToBackStack(null);
        //}
        transaction.commit();
    }

}

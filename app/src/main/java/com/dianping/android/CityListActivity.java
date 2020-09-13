package com.dianping.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianping.android.constants.Constants;
import com.dianping.android.enity.City;
import com.dianping.android.utils.HttpUtil;
import com.dianping.android.utils.Utility;
import com.dianping.android.view.SiderBar;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityListActivity extends AppCompatActivity {

    private TextView backButton;
    private ListView cityListView;
    private SiderBar siderBar;

    private List<City> cities;  //城市列表

    private static List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);

        //左上角返回按钮
        backButton = (TextView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //SiderBar的初始化以及点击事件
        siderBar = (SiderBar) findViewById(R.id.city_list_siderbar);
        siderBar.setOnTouchingLetterChangedListener(new SiderBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int index = findIndex(s);
                if (index != -1) {
                    cityListView.setSelection(index);
                }
            }
        });

        //ListView及其适配器的初始化
        cityListView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(CityListActivity.this,R.layout.city_list_item,dataList);
        cityListView.setAdapter(adapter);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView cityNameView = (TextView) view.findViewById(R.id.list_item_content);
                String cityName = cityNameView.getText().toString();
                if (!cityName.matches("[A-Z]")) {
                    Intent intent = new Intent();
                    intent.putExtra("cityName",cityName);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        queryCities();
    }

    //根据s去找到在dataList中位置的索引
    private int findIndex(String s) {
        for (int i = 0; i<dataList.size(); i++) {
            if (s.equals(dataList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    //查询城市信息，并显示在列表上
    private void queryCities() {
        cities = DataSupport.findAll(City.class);
        if (cities.size() > 0) {
            dataList.clear();
            String alpha = "Z";
            for (City city : cities) {
                String citySortKey = city.getSortKey();
                if (!alpha.equals(citySortKey)) {
                    dataList.add(citySortKey);
                    alpha = citySortKey;
                }
                dataList.add(city.getCityName());
            }
        } else {
            queryFromServer(Constants.CITY_DATA_URL);
        }
    }

    //从服务器上查询城市信息
    private void queryFromServer(String address) {
        HttpUtil.SendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = Utility.HandleCityResponse(responseText);
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryCities();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),"数据获取失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}

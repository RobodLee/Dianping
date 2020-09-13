package com.dianping.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dianping.android.adapter.AllCategoryItemAdapter;
import com.dianping.android.constants.Constants;
import com.dianping.android.enity.CategoryItem;
import com.dianping.android.utils.MyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AllCategoryActivity extends AppCompatActivity {

    Button backButton;

    List<CategoryItem> categoryItemList = new ArrayList<>();

    ListView listView;
    AllCategoryItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_all_category);

        backButton = (Button) findViewById(R.id.home_all_category_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ListView的相关设置
        initList();
        listView = (ListView) findViewById(R.id.home_all_category_list);
        //执行异步任务，从服务器中获取条目数据
        new CategoryTask().execute();
    }

    class CategoryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(Constants.CATEGORY_DATA_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                parseCategoryData(builder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AllCategoryItemAdapter(MyApplication.getContext(),R.layout.home_all_category_item,categoryItemList);
            listView.setAdapter(adapter);
        }
    }

    //处理和解析从服务器中传回的数据
    private void parseCategoryData(String numberData) {
        if (!TextUtils.isEmpty(numberData)) {
            try {
                JSONObject object = new JSONObject(numberData);
                JSONArray array = object.getJSONArray("datas");
                for (int i=0; i<array.length(); i++) {
                    JSONObject category = array.getJSONObject(i);
                    MyUtils.allCategoryNumber[i] = Integer.parseInt(category.getString("categoryNumber"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //list的初始化
    private void initList() {
        for (int i=0; i< MyUtils.allCategory.length; i++) {
            CategoryItem item = new CategoryItem(R.drawable.all_category_item_image,MyUtils.allCategory[i]);
            categoryItemList.add(item);
        }
    }
}

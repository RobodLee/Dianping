package com.dianping.android.utils;

import android.text.TextUtils;

import com.dianping.android.enity.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    //处理和解析服务器传回的城市数据
    public static boolean HandleCityResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("datas");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        City city = new City();
                        city.setCityName(object.getString("name"));
                        city.setSortKey(object.getString("sortKey"));
                        city.save();
                    }
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

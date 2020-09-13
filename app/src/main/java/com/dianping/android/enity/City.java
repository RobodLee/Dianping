package com.dianping.android.enity;

import org.litepal.crud.DataSupport;

public class City extends DataSupport{

    private int id;
    private String cityName;    //城市的名字
    private String sortKey;     //城市名的首字母

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }
}

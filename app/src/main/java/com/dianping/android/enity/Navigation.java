package com.dianping.android.enity;

//首页导航的子项，由图片和名字组成
public class Navigation {

    private int imageId;

    private String name;

    public Navigation(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }


    public String getName() {
        return name;
    }

}

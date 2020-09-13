package com.dianping.android.enity;

public class CategoryItem {

    private int imageId;

    private String text;

    public CategoryItem(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public String getText() {
        return text;
    }

}

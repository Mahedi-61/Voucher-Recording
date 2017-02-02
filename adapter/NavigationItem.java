package com.mahediapps.adapter;


public class NavigationItem {
    private String mText;
    private int mDrawable;

    public NavigationItem(String text, int drawable) {
        mText = text;
        mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public void setDrawable(int drawable) {
        mDrawable = drawable;
    }
}

package com.dianping.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SiderBar extends View {

    private OnTouchingLetterChangedListener letterChangedListener;

    //26个字母
    private static String[] siderBarAlpha = {"热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                                             "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint paint = new Paint();  //画笔对象

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.rgb(68, 104, 137));     //颜色
        paint.setTypeface(Typeface.DEFAULT_BOLD);   //字体样式
        paint.setTextSize(35);  //字体大小
        //获取自定义View的宽高
        int width = getWidth();
        int height = getHeight();
        //设定每一个字母所在控件的高度
        float each_alpha_height = height/siderBarAlpha.length;
        for (int i = 0; i <siderBarAlpha.length; i++) {
            //字体所在区域X轴的偏移量, siderBar最左边到字体最左边的距离
            float x =  width/2 - paint.measureText(siderBarAlpha[i])/2;
            //字体所在区域X轴的偏移量，siderBar顶部到字体中轴线的距离
            float y = (1+i) * each_alpha_height;
            canvas.drawText(siderBarAlpha[i], x, y, paint);
        }
    }

    //定义监听事件的接口
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    //
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener letterChangedListener) {
        this.letterChangedListener = letterChangedListener;
    }
    //事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int c = (int) (y/getHeight()*siderBarAlpha.length);
        switch (action) {
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            default:
                if (c>0 && c<siderBarAlpha.length) {
                    if (letterChangedListener != null) {
                        letterChangedListener.onTouchingLetterChanged(siderBarAlpha[c]);
                    }
                }
                invalidate();
        }
        return true;
    }

    //new对象时调用
    public SiderBar(Context context) {
        super(context);
    }

    //XML文件创建控件对象时调用
    public SiderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}

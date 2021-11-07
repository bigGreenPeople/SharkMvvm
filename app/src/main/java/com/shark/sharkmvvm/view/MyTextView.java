package com.shark.sharkmvvm.view;

/**
 * Created by Administrator on 2019/8/13.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/5/11.
 */

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    /**
     * 默认显示颜色
     */
    private int MyTextView_Color;
    /**
     * 点击显示颜色
     */
    private int MyTextView_onColor;
    /**
     * 选择显示颜色
     */
//	private int MyTextView_selectColor;
    /**
     * 默认显示文字
     */
    private String MyTextView_Text;
    /**
     * 点击显示文字
     */
    private String MyTextView_onText;

    /**
     * 选择显示文字
     */
//	private String MyTextView_selectText;
//    @SuppressLint("NewApi")
//    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        // TODO Auto-generated constructor stub
//        init(context);
////		initAttributeSet(context, attrs);
//    }
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init(context);
//		initAttributeSet(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
//		initAttributeSet(context, attrs);
    }

    public MyTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        // TODO Auto-generated method stub
//        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
    }
}

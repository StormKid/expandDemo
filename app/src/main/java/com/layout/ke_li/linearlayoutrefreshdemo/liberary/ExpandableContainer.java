package com.layout.ke_li.linearlayoutrefreshdemo.liberary;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.layout.ke_li.linearlayoutrefreshdemo.R;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ke_li on 2017/1/13.
 * 自定义显示更多文本
 */

public class ExpandableContainer extends LinearLayout {
    //默认的点击图标
    private static final int IMAGE_RES = R.mipmap.explain;
    // 默认图标的高度
    private static final int IMAGE_HEIGHT = 0;
    // 默认图标的宽度
    private static final int IMAGE_WIDTH = 0;
    // 默认显示文本的行数
    private static final int EXPAND_LINE = 2;
    // 控制默认显示文本的行数
    private int lines;
    // 判断是否展开
    private boolean isExpanded;
    // 变化的TextView
    private TextView textView;
    // 点击扩展的图标
    private ImageView imageView;
    // 显示文本
    private String text ;
    // 控制多少position
    private int position;
    /**
     * 将状态记录给缓存，下次取的时候进行修改
     */
    private static Map<Integer,Boolean> map = new HashMap<>();

    // 建立缓存机制
    SoftReference<Map> mapSoftReference =  new SoftReference<Map>(map);
    public ExpandableContainer(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        //创建TextView
        textView = new TextView(context);

        //创建ImageView
        imageView = new ImageView(context);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableContainer);

        String value = array.getString(R.styleable.ExpandableContainer_setText);
        int resourse = array.getInteger(R.styleable.ExpandableContainer_setImageResource, IMAGE_RES);
        int width = array.getDimensionPixelOffset(R.styleable.ExpandableContainer_image_width, IMAGE_WIDTH);
        int height = array.getDimensionPixelOffset(R.styleable.ExpandableContainer_image_height, IMAGE_HEIGHT);
        lines = array.getInteger(R.styleable.ExpandableContainer_expandableLine, EXPAND_LINE);

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context, attrs);
        layoutParams.gravity = Gravity.BOTTOM;
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width, height);
        LinearLayout.LayoutParams TextParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        textView.setText(value);
        textView.setLayoutParams(TextParams);
        imageView.setImageResource(resourse);
        imageView.setLayoutParams(imageParams);
        textView.setMaxLines(lines);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        this.setLayoutParams(layoutParams);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    textView.setEllipsize(null);
                    isExpanded = true;
                    textView.setMaxLines(Integer.MAX_VALUE);
                    initView();
                } else {
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    isExpanded = false;
                    textView.setMaxLines(lines);
                    initView();
                }
                map.put(position,isExpanded);
            }
        });
    }

    /**
     * 普通填充控件
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        initView();
    }

    /**
     * 进行重绘view
     */
    private void onresfreshView() {
        if (isExpanded) {
            textView.setEllipsize(null);
            textView.setMaxLines(Integer.MAX_VALUE);
            initView();
        } else {
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(lines);
            initView();
        }
    }


    @Override
    public void requestLayout() {
        super.requestLayout();

    }

    private void initView() {
        textView.setText(text);
        this.removeAllViews();
        this.addView(textView);
        this.addView(imageView);
    }


    public ExpandableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 在listview , gridview, recyclerview的条目中使用此方法，防止重绘布局
     *
     * @param text 你所要填充的文本
     * @param position 当前控件所在的position
     */
    public synchronized void setText(String text, int position) {
        this.position = position;
        this.text = text;
        initView();
        if (null!=map&&map.size()>0&&map.keySet().contains(position)){
            isExpanded = map.get(position);
        }else isExpanded=false;
        map.put(position,isExpanded);
        onresfreshView();
    }

    /**
     * 用软引用避免内存泄露
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mapSoftReference.clear();
    }
}

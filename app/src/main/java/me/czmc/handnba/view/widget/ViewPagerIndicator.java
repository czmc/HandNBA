package me.czmc.handnba.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.czmc.handnba.R;

/**
 * Created by MZone on 3/27/2016.
 */
public class ViewPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private Paint mIndicatorPaint;

    private int mIndicatorColor;//指针颜色
    private float mTextSize;//索引字体大小
    private int mHighLightTextColor;//索引字体高亮颜色
    private int mDefaultTextColor;
    private float mIndicatorHeight;//指针高
    private float mIndicatorWidth;//指针宽
    private int mVisibleMenuCount;//显示的索引数
    private int mIndicatorLimit;//指针移动限制
    private int mFirstPosition;//指针第一次的位置

    private int mChildHeight;//子view的高
    private int mChildWidth;//子view的宽
    private float mStartX;//指针画笔第一位置横坐标
    private float mStartY;//指针画笔第一位置纵坐标
    private float moveDelta = 0;//移动的增量
    private int mScreenWidth;//屏幕宽
    private CustomViewpager mViewPager;
    private View lastChild;//记录上一个指针所指的位置，用于复原高亮
    private OnPageChangeListener mOnPageChangeListener;

    public ViewPagerIndicator(Context context) {
        super(context);
        init(null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * 初始化
     * 获取自定义值
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {

        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.ViewPagerIndicator);
        mIndicatorColor = ta.getColor(R.styleable.ViewPagerIndicator_indicatorColor, Color.parseColor("#ffffff"));
        mHighLightTextColor = ta.getColor(R.styleable.ViewPagerIndicator_highLightTextColor, Color.parseColor("#ffffff"));
        mDefaultTextColor = ta.getColor(R.styleable.ViewPagerIndicator_defaultTextColor, Color.parseColor("#ffffff"));
        mTextSize = ta.getDimensionPixelSize(R.styleable.ViewPagerIndicator_textSize, 16);
        mIndicatorHeight = ta.getDimension(R.styleable.ViewPagerIndicator_indicatorHeight, 30);
        mIndicatorWidth = ta.getDimension(R.styleable.ViewPagerIndicator_indicatorWidth, 30);
        mVisibleMenuCount = ta.getInt(R.styleable.ViewPagerIndicator_visibleMenuCount, 3);
        mIndicatorLimit = ta.getInt(R.styleable.ViewPagerIndicator_indicatorLimit, 2);
        mFirstPosition = ta.getInt(R.styleable.ViewPagerIndicator_firstPosition, 0);
        ta.recycle();

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorLimit = Math.min(mVisibleMenuCount - 1, mIndicatorLimit);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels; //得到屏幕宽度
    }

    /**
     * 传递viewpager
     *
     * @param viewpager
     */
    public void setViewPager(CustomViewpager viewpager) {
        this.mViewPager = viewpager;
        if (viewpager != null) {
            viewpager.setOnPageChangeListener(this);
        }
    }

    public void setMenuTitle(String... titles) {
        if (titles == null) return;
        for (String title : titles) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView textView = new TextView(getContext());
            textView.setTextColor(mDefaultTextColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setText(title);
            textView.setGravity(Gravity.CENTER);
            this.addView(textView, layoutParams);
        }
        invalidate();
    }

    public void doOnComplete() {
        if (mViewPager != null) {
            if (mViewPager.getCurrentItem() == mFirstPosition)
                onPageSelected(mFirstPosition);
            mViewPager.setCurrentItem(mFirstPosition);
        }
    }

    public void addMenuTitle(String title) {
        if (title == null) return;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(getContext());
        textView.setTextColor(mDefaultTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setText(title);
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        textView.setGravity(Gravity.CENTER);
        this.addView(textView, layoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicator(canvas);
        super.onDraw(canvas);
    }

    /**
     * 画指针
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        Path path = new Path();
        path.moveTo(mStartX + moveDelta, mStartY);
        path.lineTo(mStartX + moveDelta - mIndicatorWidth / 2, mStartY - mIndicatorHeight);
        path.lineTo(mStartX + moveDelta - mIndicatorWidth, mStartY);
        path.lineTo(mStartX + moveDelta, mStartY);
        canvas.drawPath(path, mIndicatorPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View child = getChildAt(0);
        if (child != null) {
            mChildHeight = child.getMeasuredHeight();
            mChildWidth = mScreenWidth / mVisibleMenuCount;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                child.getLayoutParams().width = mChildWidth;
                child.setClickable(true);
                child.setTag(i);
                child.setOnClickListener(this);
            }
        }
        setMeasuredDimension(getChildCount() * mChildWidth, getMeasuredHeight());
        mStartX = mChildWidth / 2 + mIndicatorWidth / 2 + getPaddingLeft();
        mStartY = getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        int maxTriAngerHeight = (h - mChildHeight) / 2;
//        int maxTriAngerWidth = (w / mVisibleMenuCount) / 2;
//        mIndicatorHeight = Math.min(mIndicatorHeight, maxTriAngerHeight);
//        mIndicatorWidth = Math.min(mIndicatorWidth, maxTriAngerWidth);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        if (mIndicatorLimit <= 0) return;
        float moveX = mChildWidth * (position + positionOffset);
        if (position + positionOffset >= mIndicatorLimit - 1 && position + positionOffset <= getChildCount() - 1 - (mVisibleMenuCount - mIndicatorLimit)) {
            scrollTo((int) moveX - (mIndicatorLimit - 1) * mChildWidth, 0);
        }
        moveDelta = moveX;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        View child = getChildAt(position);
        if (child == null) return;
        ((TextView) child).setTextColor(mHighLightTextColor);
        if (lastChild != null) {
            ((TextView) lastChild).setTextColor(mDefaultTextColor);
        }
        lastChild = child;
        mViewPager.resetHeight(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (mViewPager != null)
            mViewPager.setCurrentItem(index);
    }

    /**
     * 提供外部使用的OnPageChangeListener
     *
     * @param l
     */
    public void setOnPageChangeListener(OnPageChangeListener l) {
        this.mOnPageChangeListener = l;
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}



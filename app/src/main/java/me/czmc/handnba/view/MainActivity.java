package me.czmc.handnba.view;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.czmc.handnba.R;
import me.czmc.handnba.presenter.MainPresenter;
import me.czmc.handnba.utils.ToolUtils;
import me.czmc.handnba.view.widget.ViewPagerIndicator;
import me.czmc.handnba.viewImp.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView, ObservableScrollViewCallbacks, View.OnClickListener {
    @Bind(R.id.scroll)
    ObservableScrollView scrollView;
    @Bind(R.id.title)
    TextView mTitleView;
    @Bind(R.id.toolbar)
    Toolbar mToolbarView;
    @Bind(R.id.flexible_space)
    View mFlexibleSpaceView;
    @Bind(R.id.body)
    LinearLayout body;
    @Bind(R.id.header)
    RelativeLayout header;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.im_team_left)
    ImageView im_team_left;
    @Bind(R.id.im_team_right)
    ImageView im_team_right;
    @Bind(R.id.im_team_left_move)
    ImageView im_team_left_move;
    @Bind(R.id.subtitle)
    TextView subtitleView;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.team1)
    TextView team1;
    @Bind(R.id.team2)
    TextView team2;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.viewPagerIndicator)
    ViewPagerIndicator viewPagerIndicator;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private int mFlexibleSpaceHeight;
    private int movex;
    private int movey;
    private ArrayList<Fragment> mContents;
    private TagFragmentPagerAdapter fragmentPagerAdapter;
    private final String TAG = "handnbaLog";
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        setSupportActionBar(mToolbarView);
        mActionBar = getSupportActionBar();
        mTitleView.setText(getTitle());
        setTitle(null);
        scrollView.setScrollViewCallbacks(this);
        scrollView.setTouchInterceptionViewGroup((ViewGroup) viewPager.getChildAt(1));
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height) - getActionBarSize();
        ScrollUtils.addOnGlobalLayoutListener(mTitleView, new Runnable() {
            @Override
            public void run() {
                updateFlexibleSpaceText(scrollView.getCurrentScrollY());
            }
        });
        final MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.loadData();
        movex = getfullWidth() - im_team_left.getLayoutParams().width;
        movey = mFlexibleSpaceHeight - im_team_left.getLayoutParams().width + getActionBarSize();
        mContents = new ArrayList();
        viewPager.setAdapter(fragmentPagerAdapter = new TagFragmentPagerAdapter(getSupportFragmentManager(), mContents));
        viewPagerIndicator.setViewPager(viewPager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.loadData();
            }
        });
        //解决滑动冲突
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    public int getFlexibleSpaceHeight() {
        return mFlexibleSpaceHeight;
    }


    @Override
    public void setSubtitle(String subtitle,String liveurl) {
        subtitleView.setText(subtitle);
        subtitleView.setTag(liveurl);
        subtitleView.setOnClickListener(this);
    }

    @Override
    public void setScore(String score) {
        this.score.setText(score);
    }

    @Override
    public void setPlayer1(String url, String name,String player1url) {
        Picasso.with(this).load(url).into(im_team_left);
        team1.setText(name);
        team1.setTag(player1url);
        team1.setOnClickListener(this);
    }

    @Override
    public void setPlayer2(String url, String name,String player2url) {
        Picasso.with(this).load(url).into(im_team_left_move);
        Picasso.with(this).load(url).into(im_team_right);
        team2.setText(name);
        team2.setTag(player2url);
        team2.setOnClickListener(this);
    }

    @Override
    public void addMenuTitle(String title) {
        viewPagerIndicator.addMenuTitle(title);
    }

    @Override
    public void addFragment(Fragment fragment) {
        mContents.add(fragment);
        fragmentPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        if (viewPagerIndicator != null) {
            viewPagerIndicator.removeAllViewsInLayout();
        }
        if (mContents != null)
            mContents.clear();
    }

    @Override
    public void loadfinish() {
        viewPagerIndicator.doOnComplete();
        swipeRefreshLayout.setRefreshing(false);
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getfullWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateFlexibleSpaceText(scrollY);
        if (scrollY > 0) {
            swipeRefreshLayout.setEnabled(false);
        } else {
            swipeRefreshLayout.setEnabled(true);
        }
        Log.i("onScrollChanged", "scrollY=" + scrollY + "," + "firstScroll=" + firstScroll + "," + "dragging=" + dragging);
    }

    @Override
    public void onDownMotionEvent() {
        Log.i("onDownMotionEvent", "onDownMotionEvent");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        Log.i("onUpOrCancelMotionEvent", "scrollState=" + scrollState.name());
    }

    private void updateFlexibleSpaceText(final int scrollY) {
        ViewHelper.setTranslationY(mFlexibleSpaceView, -scrollY);
        int adjustedScrollY = (int) ScrollUtils.getFloat(scrollY, 0, mFlexibleSpaceHeight);
        float maxScale = (float) (mFlexibleSpaceHeight - mToolbarView.getHeight()) / mToolbarView.getHeight();
        float scale = maxScale * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight;
        //向上滑动
        float scale0to1 = Math.min(1, (float) scrollY / mFlexibleSpaceHeight);
        float scale1to0 = 1 - scale0to1;

        ViewHelper.setAlpha(mToolbarView, scale0to1);
        if(scale0to1==0){
            mActionBar.hide();
        }else {
            mActionBar.show();
        }
        int maxTitleTranslationY = mToolbarView.getHeight() + mFlexibleSpaceHeight - (int) (mTitleView.getHeight() * (1 + scale));
        int titleTranslationY = (int) (maxTitleTranslationY * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight);
        //indicatorbar
        ViewHelper.setTranslationY(viewPagerIndicator, -mFlexibleSpaceHeight * scale0to1);
        //bg
        ViewHelper.setAlpha(image, 0.5f * scale1to0 + 0.5f);
        ViewHelper.setTranslationY(image, (0.5f * scale1to0 + 0.5f) * scrollY);
        //title
        ViewHelper.setTranslationY(mTitleView, -titleTranslationY);
        ViewHelper.setScaleX(mTitleView, 1 + scale);
        ViewHelper.setScaleY(mTitleView, 1 + scale);
        ViewHelper.setAlpha(mTitleView, scale0to1);
        ViewHelper.setTranslationY(subtitleView, titleTranslationY);
        ViewHelper.setPivotX(subtitleView, 0);
        ViewHelper.setPivotY(subtitleView, 0);
        //other
        ViewHelper.setAlpha(score, scale1to0);
        ViewHelper.setAlpha(team1, scale1to0);
        ViewHelper.setAlpha(team2, scale1to0);

        ViewHelper.setPivotX(im_team_left_move, 0);
        ViewHelper.setPivotY(im_team_left_move, 0);
        ViewHelper.setAlpha(im_team_left_move, scale1to0);
        ViewHelper.setAlpha(im_team_right, scale1to0);
        ViewHelper.setTranslationX(im_team_left_move, movex * scale1to0);
        ViewHelper.setTranslationY(im_team_left_move, movey * scale1to0);
        if (scale1to0 > 0.5) {
            ViewHelper.setScaleX(im_team_left, scale1to0);
            ViewHelper.setScaleY(im_team_left, scale1to0);
            ViewHelper.setScaleX(im_team_left_move, scale1to0);
            ViewHelper.setScaleY(im_team_left_move, scale1to0);
        }
        if (scrollY >= 10) {
            im_team_left_move.setVisibility(View.VISIBLE);
            im_team_right.setVisibility(View.GONE);
        } else {
            im_team_left_move.setVisibility(View.GONE);
            im_team_right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflate = getMenuInflater();
        menuInflate.inflate(R.menu.menu, menu);
        menu.add(0, 102, 102, "当前版本:" + ToolUtils.getAppVersionName(this)).setCheckable(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_update:
                UmengUpdateAgent.forceUpdate(MainActivity.this);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

                    @Override
                    public void onUpdateReturned(int arg0, UpdateResponse arg1) {
                        // TODO 自动生成的方法存根
                        switch (arg0) {
                            case UpdateStatus.Yes:
                                Log.i(TAG, "need update");
                                break;
                            case UpdateStatus.No:
                                Log.i(TAG, "don't need update");
                                Toast.makeText(MainActivity.this, "已经是最新版本了！", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case R.id.menu_exit:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        String name = ((TextView)v).getText().toString();
        String url = (String)v.getTag();
        Intent intent = new Intent(MainActivity.this,WebActivity.class);
        intent.putExtra("text",name);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    private class TagFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> content;

        public TagFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> mContents) {
            super(fm);
            this.content = mContents;
        }

        @Override
        public int getCount() {
            return content.size();
        }

        @Override
        public Fragment getItem(int position) {
            return content.get(position);
        }
    }
}

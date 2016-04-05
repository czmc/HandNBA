package me.czmc.handnba.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.czmc.handnba.R;
import me.czmc.handnba.data.entity.Combat;
import me.czmc.handnba.presenter.TeamPresenter;
import me.czmc.handnba.viewImp.ITeamView;

public class ITeamActivity extends AppCompatActivity implements ITeamView, View.OnClickListener, ObservableScrollViewCallbacks {
    @Bind(R.id.rootView)
    LinearLayout rootView;
    @Bind(R.id.scrollview)
    ObservableScrollView scrollView;
    private ImageView team1_logo;
    private ImageView team2_logo;
    private TextView team1_name;
    private TextView team2_name;
    private TextView score;
    private TextView time;
    private TextView status;
    private TextView link1;
    private TextView link2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        scrollView.setScrollViewCallbacks(this);
        String name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name + "赛程情况");
        TeamPresenter teamViewImp = new TeamPresenter(this);
        teamViewImp.loadData(name);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addTeamGame(Combat combat) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_match, null);
        team1_logo = (ImageView) view.findViewById(R.id.team1_logo);
        team2_logo = (ImageView) view.findViewById(R.id.team2_logo);
        team1_name = (TextView) view.findViewById(R.id.team1_name);
        team2_name = (TextView) view.findViewById(R.id.team2_name);
        score = (TextView) view.findViewById(R.id.score);
        time = (TextView) view.findViewById(R.id.time);
        link1 = (TextView) view.findViewById(R.id.link1);
        link2 = (TextView) view.findViewById(R.id.link2);
        status = (TextView) view.findViewById(R.id.status);
        Picasso.with(this).load(combat.player1logo).into(team1_logo);
        Picasso.with(this).load(combat.player2logo).into(team2_logo);
        team1_name.setText(combat.player1);
        team1_name.setTag(combat.player1url);
        team1_name.setOnClickListener(this);
        team2_name.setText(combat.player2);
        team2_name.setTag(combat.player2url);
        team2_name.setOnClickListener(this);
        score.setText(combat.score);
        time.setText(combat.time);
        link1.setText(combat.link1text);
        link2.setText(combat.link2text);
        link1.setTag(combat.link1url);
        link1.setOnClickListener(this);
        link2.setTag(combat.link2url);
        link2.setOnClickListener(this);
        switch (combat.status) {
            case 0:
                status.setText("未开赛");
                break;
            case 1:
                status.setText("进行中");
                break;
            case 2:
                status.setText("已结束");
                break;
            default:
                break;
        }
        rootView.addView(view);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("text", ((TextView)v).getText());
        intent.putExtra("url", (String)v.getTag());
        startActivity(intent);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}

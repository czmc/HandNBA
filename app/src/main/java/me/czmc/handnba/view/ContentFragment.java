package me.czmc.handnba.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.czmc.handnba.R;
import me.czmc.handnba.data.entity.Bottomlink;
import me.czmc.handnba.data.entity.Livelink;
import me.czmc.handnba.data.entity.Combat;

/**
 * Created by MZone on 3/27/2016.
 */
public class ContentFragment extends Fragment implements View.OnClickListener {
    public static final String BUNDLE_COMBAT = "BUNDLE_COMBAT";
    public static final String BUNDLE_BOTTOMLINK = "BUNDLE_BOTTOMLINK";
    public static final String BUNDLE_LIVELINK = "BUNDLE_LIVELINK";
    private View rootView;

    @Bind(R.id.team1_logo)
    ImageView team1_logo;
    @Bind(R.id.team2_logo)
    ImageView team2_logo;
    @Bind(R.id.team1_name)
    TextView team1_name;
    @Bind(R.id.team2_name)
    TextView team2_name;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.link1)
    TextView link1;
    @Bind(R.id.link2)
    TextView link2;


    public static ContentFragment newInstance(ArrayList<Combat> combats, ArrayList<Bottomlink> bottomlinks, ArrayList<Livelink> livelinks) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_COMBAT, combats);
        args.putParcelableArrayList(BUNDLE_BOTTOMLINK, bottomlinks);
        args.putParcelableArrayList(BUNDLE_LIVELINK, livelinks);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_content, container, false);
        ArrayList<Combat> combats = (ArrayList) getArguments().getParcelableArrayList(BUNDLE_COMBAT);
        ArrayList<Bottomlink> bottomlinks = (ArrayList) getArguments().getParcelableArrayList(BUNDLE_BOTTOMLINK);
        ArrayList<Livelink> livelinks = (ArrayList) getArguments().getParcelableArrayList(BUNDLE_LIVELINK);
        if (bottomlinks != null) {
            View view =inflater.inflate(R.layout.item_bottomlink, container,false);
            TextView live_link1 = (TextView) view.findViewById(R.id.live_link1);
            TextView live_link2 = (TextView) view.findViewById(R.id.live_link2);
            TextView live_link3 = (TextView) view.findViewById(R.id.live_link3);
            TextView live_link4 = (TextView) view.findViewById(R.id.live_link4);
            ArrayList<TextView> links = new ArrayList<>();
            links.add(live_link1);
            links.add(live_link2);
            links.add(live_link3);
            links.add(live_link4);
            if (livelinks != null) {
                view.findViewById(R.id.live).setVisibility(View.VISIBLE);
                for(int i=0;i<livelinks.size();i++){
                    links.get(i).setText(livelinks.get(i).text);
                    links.get(i).setTag(livelinks.get(i).url);
                    links.get(i).setOnClickListener(this);
                }
                for(int i=4-livelinks.size();i<4;i++){
                    links.get(i).setVisibility(View.GONE);
                }
            }
            links.clear();
            TextView bottom_link1 = (TextView) view.findViewById(R.id.bottom_link1);
            TextView bottom_link2 = (TextView) view.findViewById(R.id.bottom_link2);
            TextView bottom_link3 = (TextView) view.findViewById(R.id.bottom_link3);
            TextView bottom_link4 = (TextView) view.findViewById(R.id.bottom_link4);
            links.add(bottom_link1);
            links.add(bottom_link2);
            links.add(bottom_link3);
            links.add(bottom_link4);
            for(int i=0;i<bottomlinks.size();i++){
                links.get(i).setText(bottomlinks.get(i).text);
                links.get(i).setTag(bottomlinks.get(i).url);
                links.get(i).setOnClickListener(this);
            }
            links.clear();
            links=null;
            ((LinearLayout) rootView).addView(view);
        }
        OnTeamNameClick mOnTeamNameClick = new OnTeamNameClick();
        for (Combat combat : combats) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_match, null);
            ButterKnife.bind(this, view);
            Picasso.with(getContext()).load(combat.player1logobig).into(team1_logo);
            Picasso.with(getContext()).load(combat.player2logobig).into(team2_logo);
            team1_name.setText(combat.player1);
            team1_name.setTag(combat.player1);
            team1_name.setOnClickListener(mOnTeamNameClick);
            team2_name.setText(combat.player2);
            team2_name.setTag(combat.player2);
            team2_name.setOnClickListener(mOnTeamNameClick);
            score.setText(combat.score);
            time.setText(combat.time);
            link1.setTag(combat.link1url);
            link1.setText(combat.link1text);
            link1.setOnClickListener(this);

            link2.setTag(combat.link2url);
            link2.setText(combat.link2text);
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
            }
            ((LinearLayout) rootView).addView(view);
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("text", ((TextView)v).getText());
        intent.putExtra("url", (String)v.getTag());
        getActivity().startActivity(intent);
    }
    private class OnTeamNameClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ITeamActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("name",(String)v.getTag());
            startActivity(intent);
        }
    }
}

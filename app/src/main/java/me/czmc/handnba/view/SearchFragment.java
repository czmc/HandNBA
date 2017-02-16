package me.czmc.handnba.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import me.czmc.handnba.R;
import me.czmc.handnba.data.entity.Teammatch;
import me.czmc.handnba.presenter.SearchPresenter;
import me.czmc.handnba.view.widget.NiceSpinner;
import me.czmc.handnba.viewImp.ISearchView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements ISearchView {
    private View rootView;
    private SearchPresenter searchPresenter;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.hteam)
    NiceSpinner hteam;
    @BindView(R.id.vteam)
    NiceSpinner vteam;

    @OnFocusChange({R.id.hteam, R.id.vteam})
    public void focusChange(boolean focus) {
        if (focus) {
            ((MainActivity) getActivity()).scrollView.scrollTo(0, ((MainActivity) getActivity()).getFlexibleSpaceHeight());
        }
    }

    @OnClick(R.id.search)
    void search() {
        String hteamName = hteam.getText().toString();
        String vteamName = vteam.getText().toString();
        if (hteamName == null) return;
        if (vteamName == null) return;
        searchPresenter.search(hteamName, vteamName);
        ((MainActivity)getActivity()).viewPager.resetHeight(3);
    }

    public SearchFragment() {
    }

    public static SearchFragment newInstance(ArrayList<Teammatch> teammatches) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("teams",teammatches);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        searchPresenter = new SearchPresenter(this);
        ArrayList<Teammatch> teammatches=getArguments().getParcelableArrayList("teams");
        ArrayList<String> teamnames = new ArrayList<>();
       if(teammatches==null || teammatches.isEmpty()){

       }else {
           for (Teammatch team : teammatches) {
               teamnames.add(team.name);
           }
           hteam.attachDataSource(teamnames);
           vteam.attachDataSource(teamnames);
       }
        return rootView;
    }

    @Override
    public void addFragment(Fragment fragment) {
        Log.i("handnbaLog", "addFragment");
        getChildFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            focusChange(true);
        }
    }
}

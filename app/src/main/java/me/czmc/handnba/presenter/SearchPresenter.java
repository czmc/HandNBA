package me.czmc.handnba.presenter;

import java.util.ArrayList;

import me.czmc.handnba.data.NBAApi;
import me.czmc.handnba.data.entity.Combat;
import me.czmc.handnba.data.entity.CombatData;
import me.czmc.handnba.data.entity.CombatResult;
import me.czmc.handnba.view.ContentFragment;
import me.czmc.handnba.viewImp.ISearchView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MZone on 3/30/2016.
 */
public class SearchPresenter {
    private ISearchView mSearchViewImp;

    public SearchPresenter(ISearchView mSearchViewImp) {
        this.mSearchViewImp = mSearchViewImp;
    }

    public void search(String hteam,String vteam) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NBAApi.url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create()).build();
        NBAApi mNBAApi = retrofit.create(NBAApi.class);
        mNBAApi.getCombatInfo(NBAApi.token, hteam,vteam).map(new Func1<CombatData, CombatResult>() {
            @Override
            public CombatResult call(CombatData teamData) {
                return teamData.result;
            }
        }).map(new Func1<CombatResult, ArrayList<Combat>>() {
            @Override
            public ArrayList<Combat> call(CombatResult combatResult) {
                return combatResult.list;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<Combat>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList<Combat> combats) {
                mSearchViewImp.addFragment(ContentFragment.newInstance(combats,null,null));
            }
        });
    }
}

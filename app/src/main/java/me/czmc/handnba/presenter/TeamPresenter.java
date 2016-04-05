package me.czmc.handnba.presenter;

import android.util.Log;

import me.czmc.handnba.data.NBAApi;
import me.czmc.handnba.data.entity.Combat;
import me.czmc.handnba.data.entity.CombatData;
import me.czmc.handnba.data.entity.CombatResult;
import me.czmc.handnba.viewImp.ITeamView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MZone on 3/30/2016.
 */
public class TeamPresenter {
    private ITeamView mTeamViewImp;

    public TeamPresenter(ITeamView mTeamViewImp) {
        this.mTeamViewImp = mTeamViewImp;
    }

    public void loadData(String name) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NBAApi.url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create()).build();
        NBAApi mNBAApi = retrofit.create(NBAApi.class);
        mNBAApi.getTeamInfo(NBAApi.token, name).map(new Func1<CombatData, CombatResult>() {
            @Override
            public CombatResult call(CombatData teamData) {
                Log.i("handnbaLog",teamData.reason);
                return teamData.result;
            }
        }).flatMap(new Func1<CombatResult, Observable<Combat>>() {
            @Override
            public Observable<Combat> call(CombatResult teamResult) {
                return Observable.from(teamResult.list);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Combat>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Combat combat) {
                Log.i("handnbaLog",combat.player1);
                mTeamViewImp.addTeamGame(combat);
            }
        });
    }
}

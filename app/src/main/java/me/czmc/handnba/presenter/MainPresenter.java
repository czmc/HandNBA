package me.czmc.handnba.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

import me.czmc.handnba.data.NBAApi;
import me.czmc.handnba.data.entity.GameData;
import me.czmc.handnba.data.entity.ListObj;
import me.czmc.handnba.data.entity.Live;
import me.czmc.handnba.data.entity.Result;
import me.czmc.handnba.data.entity.Teammatch;
import me.czmc.handnba.view.ContentFragment;
import me.czmc.handnba.view.SearchFragment;
import me.czmc.handnba.viewImp.IMainView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by MZone on 3/25/2016.
 */
public class MainPresenter {
    private IMainView mViewImp;

    public MainPresenter(IMainView mViewImp) {
        this.mViewImp = mViewImp;
    }

    public void loadData() {
        mViewImp.showProgress("卖力加载中..");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NBAApi.url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create()).build();
        NBAApi mNBAApi = retrofit.create(NBAApi.class);
        Observable<GameData> mDataObserable = mNBAApi.getNBAInfo(NBAApi.token);
        loadLive(mDataObserable);
        loadTr(mDataObserable);
    }
    private ArrayList<Teammatch> teammatchs;
    private void loadTr(Observable<GameData> mDataObserable) {
        mViewImp.clearData();
        mDataObserable.map(new Func1<GameData, Result>() {
            @Override
            public Result call(GameData gameData) {
                return gameData.result;
            }
        }).map(new Func1<Result, ArrayList<ListObj>>() {
            @Override
            public ArrayList<ListObj> call(Result result) {
                teammatchs= result.teammatch;
                return result.list;
            }
        }).flatMap(new Func1<ArrayList<ListObj>, Observable<ListObj>>() {
            @Override
            public Observable<ListObj> call(ArrayList<ListObj> listObjs) {
                return Observable.from(listObjs);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ListObj>() {
            @Override
            public void onCompleted() {
                mViewImp.dismissProgress();
                mViewImp.addMenuTitle("查询");
                mViewImp.addFragment(SearchFragment.newInstance(teammatchs));
                mViewImp.loadfinish();
            }

            @Override
            public void onError(Throwable e) {
                mViewImp.dismissProgress();
            }

            @Override
            public void onNext(ListObj listObj) {
                Fragment fragment = ContentFragment.newInstance(listObj.tr, listObj.bottomlink, listObj.livelink);
                mViewImp.addFragment(fragment);
                mViewImp.addMenuTitle(listObj.title);
            }
        });
    }

    private void loadLive(Observable<GameData> mDataObserable) {
        mDataObserable.map(new Func1<GameData, Result>() {
            @Override
            public Result call(GameData gameData) {
                return gameData.result;
            }
        }).map(new Func1<Result, ArrayList<ListObj>>() {
            @Override
            public ArrayList<ListObj> call(Result result) {
                return result.list;
            }
        }).map(new Func1<ArrayList<ListObj>, ListObj>() {
            @Override
            public ListObj call(ArrayList<ListObj> listObjs) {
                return listObjs.get(1);
            }
        }).flatMap(new Func1<ListObj, Observable<Live>>() {
            @Override
            public Observable<Live> call(ListObj listObj) {
                return Observable.from(listObj.live);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Live>() {
            @Override
            public void onCompleted() {
                mViewImp.dismissProgress();
            }

            @Override
            public void onError(Throwable e) {
                mViewImp.dismissProgress();
                Log.i("mydata", e.toString() + " | " + e.getMessage() + " |" + e.getCause());
            }

            @Override
            public void onNext(Live live) {
                Log.i("mydata", live.toString());
                mViewImp.setPlayer1(live.player1logobig, live.player1location + "" + live.player1,live.player1url);
                mViewImp.setPlayer2(live.player2logobig, live.player2location + "" + live.player2,live.player2url);
                mViewImp.setSubtitle(live.title,live.liveurl);
                mViewImp.setScore(live.score);
            }
        });
    }
}

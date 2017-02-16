package me.czmc.handnba.viewImp;

import android.support.v4.app.Fragment;

public interface IMainView {
    void setSubtitle(String subtitle,String liveurl);
    void setScore(String score);
    void setPlayer1(String url,String name,String player1url);
    void setPlayer2(String url,String name,String player1url);
    void addMenuTitle(String title);
    void addFragment(Fragment fragment);
    void clearData();
    void loadfinish();
    void showProgress(String msg);
    void dismissProgress();
}
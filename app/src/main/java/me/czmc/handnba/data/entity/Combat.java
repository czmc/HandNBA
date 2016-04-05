package me.czmc.handnba.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MZone on 3/25/2016.
 */
public class Combat implements Parcelable{
    public String link1text;
    public String link2text;
    public String link1url;
    public String link2url;
    public String player1;
    public String player1logo;
    public String player1logobig;
    public String player1url;
    public String player2;
    public String player2logo;
    public String player2logobig;
    public String player2url;
    public String score;
    public int status;
    public String time;


    protected Combat(Parcel in) {
        link1text = in.readString();
        link2text = in.readString();
        link1url = in.readString();
        link2url = in.readString();
        player1 = in.readString();
        player1logo = in.readString();
        player1logobig = in.readString();
        player1url = in.readString();
        player2 = in.readString();
        player2logo = in.readString();
        player2logobig = in.readString();
        player2url = in.readString();
        score = in.readString();
        status = in.readInt();
        time = in.readString();
    }

    public static final Creator<Combat> CREATOR = new Creator<Combat>() {
        @Override
        public Combat createFromParcel(Parcel in) {
            return new Combat(in);
        }

        @Override
        public Combat[] newArray(int size) {
            return new Combat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link1text);
        dest.writeString(link2text);
        dest.writeString(link1url);
        dest.writeString(link2url);
        dest.writeString(player1);
        dest.writeString(player1logo);
        dest.writeString(player1logobig);
        dest.writeString(player1url);
        dest.writeString(player2);
        dest.writeString(player2logo);
        dest.writeString(player2logobig);
        dest.writeString(player2url);
        dest.writeString(score);
        dest.writeInt(status);
        dest.writeString(time);
    }
}

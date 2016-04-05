package me.czmc.handnba.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MZone on 3/25/2016.
 */
public class Livelink implements Parcelable{
    public String text;
    public String url;
    public String videoicon;

    protected Livelink(Parcel in) {
        text = in.readString();
        url = in.readString();
        videoicon = in.readString();
    }

    public static final Creator<Livelink> CREATOR = new Creator<Livelink>() {
        @Override
        public Livelink createFromParcel(Parcel in) {
            return new Livelink(in);
        }

        @Override
        public Livelink[] newArray(int size) {
            return new Livelink[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(url);
        dest.writeString(videoicon);
    }
}
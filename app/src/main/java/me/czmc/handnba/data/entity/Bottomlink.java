package me.czmc.handnba.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MZone on 3/25/2016.
 */
public class Bottomlink implements Parcelable{
    public String text;
    public String url;

    protected Bottomlink(Parcel in) {
        text = in.readString();
        url = in.readString();
    }

    public static final Creator<Bottomlink> CREATOR = new Creator<Bottomlink>() {
        @Override
        public Bottomlink createFromParcel(Parcel in) {
            return new Bottomlink(in);
        }

        @Override
        public Bottomlink[] newArray(int size) {
            return new Bottomlink[size];
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
    }
}

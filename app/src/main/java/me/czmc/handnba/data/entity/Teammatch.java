package me.czmc.handnba.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MZone on 3/25/2016.
 */
public class Teammatch implements Parcelable{
    public String name;
    public String url;

    protected Teammatch(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<Teammatch> CREATOR = new Creator<Teammatch>() {
        @Override
        public Teammatch createFromParcel(Parcel in) {
            return new Teammatch(in);
        }

        @Override
        public Teammatch[] newArray(int size) {
            return new Teammatch[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }
}

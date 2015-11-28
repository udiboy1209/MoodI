package org.iitb.moodi.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by udiboy on 26/10/15.
 */

public class Event implements Parcelable {
    @SerializedName("name")
    public String name;

    @SerializedName("oneline")
    public String intro_short;

    @SerializedName("genre")
    public String genre;

    @SerializedName("genrebaap")
    public String genrebaap;

    @SerializedName("event_id")
    public String id;

    @SerializedName("intro")
    public String intro;

    @SerializedName("rules")
    public String rules;

    @SerializedName("prizes")
    public String prizes;

    @SerializedName("registration")
    public String registration;

    public boolean fav = false;

    public Event(){

    }

    private Event(Parcel in) {
        name = in.readString();
        intro_short = in.readString();
        genre = in.readString();
        genrebaap = in.readString();
        id = in.readString();
        intro = in.readString();
        rules = in.readString();
        prizes = in.readString();
        registration = in.readString();
        fav= in.readInt()>0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(intro_short);
        dest.writeString(genre);
        dest.writeString(genrebaap);
        dest.writeString(id);
        dest.writeString(intro);
        dest.writeString(rules);
        dest.writeString(prizes);
        dest.writeString(registration);
        dest.writeInt(fav?1:0);
    }
}

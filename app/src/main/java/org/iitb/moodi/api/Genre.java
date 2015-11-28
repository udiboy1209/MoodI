package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by udiboy1209 on 30/6/15.
 */
public class Genre{
    @SerializedName("name")
    public String name;

    @SerializedName("shortname")
    public String name_short;

    @SerializedName("id")
    public String  id;

    @SerializedName("intro")
    public String intro;

    @SerializedName("details")
    public Event[] events;
}

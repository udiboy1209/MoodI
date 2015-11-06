package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by udiboy on 5/11/15.
 */
public class EventsResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("genre")
    public Genre[] genres;

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

        public class Event{
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
        }
    }
}

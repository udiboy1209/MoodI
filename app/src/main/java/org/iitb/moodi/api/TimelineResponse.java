package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by udiboy on 30/11/15.
 */
public class TimelineResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("data")
    public EventTime[] events;

    public class EventTime{
        final DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

        @SerializedName("ID")
        public String id;

        @SerializedName("Name")
        public String name;

        @SerializedName("Intro")
        public String intro;

        @SerializedName("Department")
        public String dept;

        @SerializedName("starttime")
        public String start;

        @SerializedName("endtime")
        public String end;

        @SerializedName("Venue")
        public String venue_id;

        @SerializedName("day")
        public String day;

        public Date getStart() {
            try {
                return format.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Date getEnd() {
            try {
                return format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

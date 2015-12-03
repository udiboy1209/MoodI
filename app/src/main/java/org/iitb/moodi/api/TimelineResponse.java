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

        @SerializedName("venue_name")
        public String venue_name;

        public Date getStart() {
            try {
                DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                if(start!=null) return format.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public int getStartHrs(){
            String[] times = start.split(":");
            return Integer.valueOf(times[0]);
        }

        public Date getEnd() {
            try {
                DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                if(end!=null) return format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public int compareTo(EventTime eventTime) {
            return getStart().compareTo(eventTime.getStart());
        }
    }
}

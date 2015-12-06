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
public class ResultResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("data")
    public Result[] results;

    public class Result {
        @SerializedName("event_id")
        public String id;

        @SerializedName("event_name")
        public String name;

        @SerializedName("first")
        public String first;

        @SerializedName("second")
        public String second;

        @SerializedName("third")
        public String third;
    }
}

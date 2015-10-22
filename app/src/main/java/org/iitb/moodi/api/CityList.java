package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalpesh on 22/10/15.
 */
public class CityList {


    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private City[] cities;

    public boolean getStatus() {
        return status;
    }

    public City[] getCityList() {
        return cities;
    }

    @Override
    public String toString() {

        /* TODO :- Return appropriate String  */
        return "";
    }

    public class City {
        @SerializedName("city_id")
        private int id;
        @SerializedName("city_name")
        private String name;
        @SerializedName("count_clgs")
        private int college_count;
        @SerializedName("count_allusers")
        private int user_count;

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public int getCollegeCount() {
            return college_count;
        }
        public int getUserCount() {
            return user_count;
        }
    }
}

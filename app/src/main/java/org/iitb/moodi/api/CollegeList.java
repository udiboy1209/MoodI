package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalpesh on 22/10/15.
 */
public class CollegeList {

    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private College[] colleges;

    public boolean getStatus() {
        return status;
    }

    public College[] getCollegeList() {
        return colleges;
    }

    @Override
    public String toString() {

        /* TODO :- Return appropriate String  */
        return "";
    }

    public class College {
        @SerializedName("clg_id")
        private int id;
        @SerializedName("city_id")
        private int city_id;
        @SerializedName("clg_name")
        private String name;
        @SerializedName("count_users")
        private int user_count;
        @SerializedName("total_points")
        private int points;
        @SerializedName("cl_bool")
        private int cl_bool;

        public int getId() {
            return id;
        }
        public int getCityId() {
            return city_id;
        }
        public String getName() {
            return name;
        }
        public int getCl_bool() {
            return cl_bool;
        }
        public int getUserCount() {
            return user_count;
        }
        public int getPoints() {
            return points;
        }
    }

}

package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

import org.iitb.moodi.BaseResponse;

/**
 * Created by kalpesh on 22/10/15.
 */
public class CityList extends BaseResponse {


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

        if (super.getError() != null) {
            return "Profile{error='" + super.getError() + "'}";
        }

        /* TODO :- Return appropriate String  */
        return "";
    }
}

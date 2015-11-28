package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalpesh on 29/11/15.
 */
public class EventRegisterResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private String result;

    public boolean getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
}

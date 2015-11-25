package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by kalpesh on 24/11/15.
 */
public class CheckUserResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private String user;

    public boolean getStatus() {
        return status;
    }

    public String getUser() {
        return user;
    }
}
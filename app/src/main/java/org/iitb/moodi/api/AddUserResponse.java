package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by kalpesh on 25/11/15.
 */
public class AddUserResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private String mi_no;

    public boolean getStatus() {
        return status;
    }

    public String getMINumber() {
        return mi_no;
    }
}

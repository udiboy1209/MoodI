package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by kalpesh on 22/10/15.
 */
public class User {
    @SerializedName("mi_no")
    public String mi_no;
    @SerializedName("city_id")
    public int city_id;
    @SerializedName("clg_id")
    public int clg_id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("fbid")
    public String fbid;
    @SerializedName("gender")
    public String gender;
    @SerializedName("year_study")
    public String year_study;
    @SerializedName("dob")
    public String dob;
    @SerializedName("phone")
    public String phone;

    public User() {

    }

    public User(JSONObject user) throws IOException {
        loadJSONData(user);
    }

    public void loadJSONData(JSONObject user) throws IOException {
        fbid=user.optString("fbid");
        mi_no=user.optString("mi_no");
        name=user.optString("name");
        email=user.optString("email");
        gender=user.optString("gender");
        clg_id=user.optInt("clg_id");
        phone =user.optString("phone");
        city_id=user.optInt("city_id");
        year_study=user.optString("year_study");
        dob=user.optString("dob");
    }

    public String getJSON() {

        String json="{";
        json+="\"mi_no\":\""+mi_no+"\"";
        json+=",\"fbid\":\""+fbid+"\"";
        json+=",\"name\":\""+name+"\"";
        json+=",\"email\":\""+email+"\"";
        json+=",\"gender\":\""+gender+"\"";
        json+=",\"clg_id\":"+clg_id+"";
        json+=",\"phone\":\""+ phone +"\"";
        json+=",\"city_id\":"+city_id+"";
        json+=",\"year_study\":\""+year_study+"\"";
        json+=",\"dob\":\""+dob+"\"";
        json+="}";
        return json;
    }
}

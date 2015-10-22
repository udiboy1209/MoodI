package org.iitb.moodi.api;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by kalpesh on 22/10/15.
 */
public class User {
    public String fbid, id, name, email, gender, dob,
            phone, year_study, city_id, college_id;

    public User() {

    }

    public User(JSONObject user) throws IOException {
        loadJSONData(user);
    }

    public void loadJSONData(JSONObject user) throws IOException {
        fbid=user.optString("fbid");
        id=user.optString("id");
        name=user.optString("name");
        email=user.optString("email");
        gender=user.optString("gender");
        college_id=user.optString("college_id");
        phone =user.optString("phone");
        city_id=user.optString("city_id");
        year_study=user.optString("year_study");
        dob=user.optString("dob");
    }

    public String getJSON() {

        String json="{";
        json+="\"id\":\""+id+"\"";
        json+=",\"name\":\""+name+"\"";
        json+=",\"email\":\""+email+"\"";
        json+=",\"gender\":\""+gender+"\"";
        json+=",\"college_id\":\""+college_id+"\"";
        json+=",\"phone\":\""+ phone +"\"";
        json+=",\"city_id\":\""+city_id+"\"";
        json+=",\"year_study\":\""+year_study+"\"";
        json+=",\"dob\":\""+dob+"\"";
        return json;
    }
}

package org.iitb.moodi.api;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by kalpesh on 22/10/15.
 */
public class User {
    public String id, name, email, gender, date_of_birth, phone, year, city, college;

    public User() {

    }

    public User(JSONObject user) throws IOException {
        loadJSONData(user);
    }

    public void loadJSONData(JSONObject user) throws IOException {
        id=user.optString("id");
        name=user.optString("name");
        email=user.optString("email");
        gender=user.optString("gender");
        college=user.optString("college");
        phone =user.optString("phone");
        city=user.optString("city");
        year=user.optString("year");
        date_of_birth=user.optString("date_of_birth");
    }

    public String getJSON() {

        String json="{";
        json+="\"id\":\""+id+"\"";
        json+=",\"name\":\""+name+"\"";
        json+=",\"email\":\""+email+"\"";
        json+=",\"gender\":\""+gender+"\"";
        json+=",\"college\":\""+college+"\"";
        json+=",\"phone\":\""+ phone +"\"";
        json+=",\"city\":\""+city+"\"";
        json+=",\"year\":\""+year+"\"";
        json+=",\"date_of_birth\":\""+date_of_birth+"\"";
        return json;
    }
}

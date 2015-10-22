package org.iitb.moodi;

import org.iitb.moodi.api.CityList;
import org.iitb.moodi.api.College;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by kalpesh on 22/10/15.
 */
public interface MoodIndigoClient {

    @GET("/api/city")
    void getCities(Callback<CityList> cb);

    @POST("/api/college")
    void getColleges(@Query("city") String city_id, Callback<College> cb);
}

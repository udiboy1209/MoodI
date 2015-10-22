package org.iitb.moodi;

import org.iitb.moodi.api.CityList;
import org.iitb.moodi.api.CollegeList;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by kalpesh on 22/10/15.
 */
public interface MoodIndigoClient {

    @GET("/api/city")
    void getCities(Callback<CityList> cb);

    @FormUrlEncoded
    @POST("/api/college")
    void getColleges(@Field("city") String city_id, Callback<CollegeList> cb);
}

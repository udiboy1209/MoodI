package org.iitb.moodi;

import org.iitb.moodi.api.CityResponse;
import org.iitb.moodi.api.CollegeResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by kalpesh on 22/10/15.
 */
public interface MoodIndigoClient {

    @GET("/api/city")
    void getCities(Callback<CityResponse> cb);

    @FormUrlEncoded
    @POST("/api/college")
    void getColleges(@Field("city") String city_id, Callback<CollegeResponse> cb);
}

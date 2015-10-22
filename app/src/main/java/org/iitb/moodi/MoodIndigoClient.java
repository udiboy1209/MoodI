package org.iitb.moodi;

import org.iitb.moodi.api.CityList;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by kalpesh on 22/10/15.
 */
public interface MoodIndigoClient {
    @GET("/api/city")
    void getCities(Callback<CityList> cb);
}

package org.iitb.moodi;

import org.iitb.moodi.api.AddUserResponse;
import org.iitb.moodi.api.CheckUserResponse;
import org.iitb.moodi.api.CityResponse;
import org.iitb.moodi.api.CollegeResponse;
import org.iitb.moodi.api.EventDetailsResponse;
import org.iitb.moodi.api.EventRegisterResponse;
import org.iitb.moodi.api.EventsResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by kalpesh on 22/10/15.
 */
public interface MoodIndigoClient {

    @GET("/api/city")
    void getCities(Callback<CityResponse> cb);

    @FormUrlEncoded
    @POST("/api/events")
    void getEventDetails(@Field("id") String event_id, Callback<EventDetailsResponse> cb);

    @FormUrlEncoded
    @POST("/api/college")
    void getColleges(@Field("city") String city_id, Callback<CollegeResponse> cb);

    @FormUrlEncoded
    @POST("/api/eventsdata")
    void getEvents(@Field("id") int id, Callback<EventsResponse> cb);

    @FormUrlEncoded
    @POST("/api/checkuserindatabase")
    void checkUser(@Field("fbid") String fbid, @Field("access_token") String access_token, Callback<CheckUserResponse> cb);

    @FormUrlEncoded
    @POST("/api/eventsreg")
    void eventRegister(@Field("event_id") int event_id, @Field("eventname") String name,
                       @Field("reglist") String reglist, Callback<EventRegisterResponse> cb);

    @FormUrlEncoded
    @POST("/api/register")
    void addUser(@Field("fbid") String fbid, @Field("city_id") int city_id,
                 @Field("college_id") int clg_id, @Field("name") String name,
                 @Field("email") String email, @Field("mobile") String mobile,
                 @Field("dob") String dob, @Field("gender") String gender,
                 @Field("year_study") String year_study, @Field("access_token") String access_token,
                 Callback<AddUserResponse> cb);
}
